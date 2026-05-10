package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.clients.CartServiceClient;
import com.ecommerce.orderservice.dto.*;
import com.ecommerce.orderservice.exception.CartNotFoundException;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.model.OrderItem;
import com.ecommerce.orderservice.model.ShippingAddress;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.security.AuthenticatedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Pure business logic – no HTTP concerns, no SecurityContext reads.
 *
 * The controller resolves the authenticated user and raw token before calling
 * any method here; every piece of caller identity is passed in as a parameter.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository       orderRepository;
    private final CartServiceClient     cartServiceClient;
    private final OrderMapper           orderMapper;
    private final ProductSnapshotParser snapshotParser;
    private final StripeService         stripeService;
    private final ObjectMapper          objectMapper;

    // ─── 1. Create Cash Order ─────────────────────────────────────────────────
    @Override
    @Transactional
    public CreateOrderResponse createCashOrder(String cartId, AuthenticatedUser caller,
                                               ShippingAddress shippingAddress, String token) {

        log.info("Creating cash order: cartId={}, userId={}", cartId, caller.getId());

        CartDto cart = fetchAndValidateCart(cartId, token);

        List<OrderItem> items = buildOrderItems(cart);

        BigDecimal cartPrice      = computeCartPrice(items);
        BigDecimal taxPrice       = BigDecimal.ZERO;
        BigDecimal shippingPrice  = BigDecimal.ZERO;
        BigDecimal totalOrderPrice = cartPrice.add(taxPrice).add(shippingPrice);

        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .userId(caller.getId().toString())
                .userName(caller.getUsername())       
                .userEmail(caller.getEmail())         
                .userPhone(shippingAddress.getPhone()) 
                .shippingAddress(shippingAddress)
                .cartItems(items)
                .totalOrderPrice(totalOrderPrice)
                .taxPrice(taxPrice)
                .shippingPrice(shippingPrice)
                .paymentMethodType("cash")
                .isPaid(false)
                .isDelivered(false)
                .build();

        order = orderRepository.save(order);
        log.info("Order saved: id={}", order.getId());

        cartServiceClient.clearMyCart("Bearer " + token);

        return buildCreateOrderResponse(order, caller, shippingAddress,
                                        cartPrice, taxPrice, shippingPrice, totalOrderPrice);
    }

    // ─── 2. List All Orders (paginated) ──────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public ListOrdersResponse getAllOrders(int page, int limit) {

        Page<Order> orderPage = orderRepository.findAll(PageRequest.of(page - 1, limit));

        int     totalPages = orderPage.getTotalPages();
        Integer nextPage   = page < totalPages ? page + 1 : null;

        PaginationMetadata metadata = PaginationMetadata.builder()
                .currentPage(page)
                .numberOfPages(totalPages)
                .limit(limit)
                .nextPage(nextPage)
                .build();

        List<OrderData> data = orderPage.getContent().stream()
                .map(orderMapper::toOrderData)
                .collect(Collectors.toList());

        return ListOrdersResponse.builder()
                .results(orderPage.getTotalElements())
                .metadata(metadata)
                .data(data)
                .build();
    }

    // ─── 3. Get Orders for a Specific User ───────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<OrderData> getUserOrders(String targetUserId) {
        return orderRepository.findAllByUserId(targetUserId).stream()
                .map(orderMapper::toOrderData)
                .collect(Collectors.toList());
    }

    // // ─── 4. Create Checkout Session ───────────────────────────────────────────
    // @Override
    // @Transactional
    // public CheckoutSessionResponse createCheckoutSession(String cartId, AuthenticatedUser caller,
    //                                                      ShippingAddress shippingAddress,
    //                                                      String successUrl, String token) {

    //     log.info("Creating checkout session: cartId={}, userId={}", cartId, caller.getId());

    //     CartDto cart  = fetchAndValidateCart(cartId, token);
    //     BigDecimal total = computeCartPrice(buildOrderItems(cart));

    //     String sessionUrl = stripeService.createSession(
    //             cartId, caller.getId().toString(), shippingAddress, total, successUrl);

    //     return CheckoutSessionResponse.builder()
    //             .status("success")
    //             .sessionUrl(sessionUrl)
    //             .build();
    // }
        // ─── 4. Create Checkout Session ───────────────────────────────────────────
    @Override
    @Transactional
    public CheckoutSessionResponse createCheckoutSession(String cartId, AuthenticatedUser caller,
                                                        ShippingAddress shippingAddress,
                                                        String successUrl, String token) {

        log.info("Creating checkout session: cartId={}, userId={}", cartId, caller.getId());

        CartDto cart = fetchAndValidateCart(cartId, token);
        List<OrderItem> items = buildOrderItems(cart);

        BigDecimal cartPrice      = computeCartPrice(items);
        BigDecimal taxPrice       = BigDecimal.ZERO;
        BigDecimal shippingPrice  = BigDecimal.ZERO;
        BigDecimal totalOrderPrice = cartPrice.add(taxPrice).add(shippingPrice);

        // Persist a pending (unpaid) order so the webhook can find and mark it paid
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .userId(caller.getId().toString())
                .userName(caller.getUsername())
                .userEmail(caller.getEmail())
                .userPhone(shippingAddress.getPhone())
                .shippingAddress(shippingAddress)
                .cartItems(items)
                .totalOrderPrice(totalOrderPrice)
                .taxPrice(taxPrice)
                .shippingPrice(shippingPrice)
                .paymentMethodType("card")
                .isPaid(false)
                .isDelivered(false)
                .build();

        order = orderRepository.save(order);
        log.info("Pending order saved: id={}", order.getId());

        String sessionUrl = stripeService.createSession(
                order.getOrderId(),              // ← use orderId as the cartId metadata key
                caller.getId().toString(),
                shippingAddress,
                totalOrderPrice,
                successUrl);

        return CheckoutSessionResponse.builder()
                .status("success")
                .sessionUrl(sessionUrl)
                .build();
    }

    // ─── 5. Handle Stripe Webhook ─────────────────────────────────────────────
    @Override
    @Transactional
    public void handleStripeWebhook(String payload, String sigHeader) {
        String orderId = stripeService.extractVerifiedCartId(payload, sigHeader);

        if (orderId == null) {
            log.debug("Webhook received but no action needed");
            return;
        }

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found for orderId: " + orderId));

        if (order.isPaid()) {
            log.warn("Webhook received for already-paid order: orderId={}", orderId);
            return;
        }

        order.setPaid(true);
        order.setPaidAt(Instant.now());
        orderRepository.save(order);
        log.info("Order marked as paid via Stripe webhook: orderId={}", orderId);
    }

    // ─── Private: Cart Helpers ────────────────────────────────────────────────

    /**
     * Fetches the user's cart and validates the cartId matches.
     * Throws {@link CartNotFoundException} (→ 404) on mismatch or absence.
     */
    private CartDto fetchAndValidateCart(String cartId, String token) {
        GetCartResponse cartResponse = cartServiceClient.getMyCart("Bearer " + token);
        if (cartResponse == null || cartResponse.getData() == null || cartResponse.getCartId() == null) {
            throw new CartNotFoundException("Cart not found");
        }
        if (!cartId.equals(cartResponse.getCartId())) {
            throw new CartNotFoundException("Cart not found or does not belong to you");
        }
        return cartResponse.getData();
    }

    /**
     * Maps cart product entries to {@link OrderItem} entities,
     * storing a JSON snapshot of each product for future reference.
     */
    private List<OrderItem> buildOrderItems(CartDto cart) {
        return cart.getProducts().stream()
                .map(ci -> {
                    String snapshot   = serializeProduct(ci.getProductDetails());
                    String productId  = snapshotParser.extractProductId(ci.getProductDetails());

                    return OrderItem.builder()
                            .cartItemRef(ci.getId())
                            .productId(productId != null ? productId : "unknown")
                            .productSnapshot(snapshot)
                            .count(ci.getCount() != null ? ci.getCount() : 0)
                            .price(ci.getPrice())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private BigDecimal computeCartPrice(List<OrderItem> items) {
        return items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String serializeProduct(Object productDetails) {
        if (productDetails == null) return null;
        try {
            return objectMapper.writeValueAsString(productDetails);
        } catch (Exception ignored) {
            return null;
        }
    }

    // ─── Private: Response Builder ────────────────────────────────────────────

    /**
     * Builds the full {@link CreateOrderResponse} from the persisted order and
     * the caller's identity that was resolved by the controller.
     * No SecurityContext access here – caller is passed in explicitly.
     */
    private CreateOrderResponse buildCreateOrderResponse(Order order,
                                                         AuthenticatedUser caller,
                                                         ShippingAddress shippingAddress,
                                                         BigDecimal cartPrice,
                                                         BigDecimal taxPrice,
                                                         BigDecimal shippingPrice,
                                                         BigDecimal totalOrderPrice) {

        CreateOrderResponse.UserSummary userSummary = CreateOrderResponse.UserSummary.builder()
                .id(caller.getId().toString())
                .name(caller.getUsername())
                .email(caller.getEmail())
                .build();

        CreateOrderResponse.PricingSummary pricingSummary = CreateOrderResponse.PricingSummary.builder()
                .cartPrice(cartPrice)
                .taxPrice(taxPrice)
                .shippingPrice(shippingPrice)
                .totalOrderPrice(totalOrderPrice)
                .build();

        CreateOrderResponse.ShippingAddress responseAddress = CreateOrderResponse.ShippingAddress.builder()
                .details(shippingAddress.getDetails())
                .phone(shippingAddress.getPhone())
                .city(shippingAddress.getCity())
                .postalCode(shippingAddress.getPostalCode())
                .build();

        List<CreateOrderResponse.CartItem> cartItems = order.getCartItems().stream()
                .map(item -> CreateOrderResponse.CartItem.builder()
                        .id(item.getCartItemRef())
                        .count(item.getCount())
                        .price(item.getPrice())
                        .product(snapshotParser.parse(item.getProductSnapshot()))
                        .build())
                .collect(Collectors.toList());

        CreateOrderResponse.UserDetail userDetail = CreateOrderResponse.UserDetail.builder()
                .id(caller.getId().toString())
                .name(caller.getUsername())
                .email(caller.getEmail())
                .phone(shippingAddress.getPhone())
                .build();

        CreateOrderResponse.OrderData orderData = CreateOrderResponse.OrderData.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .user(userDetail)
                .shippingAddress(responseAddress)
                .taxPrice(taxPrice)
                .shippingPrice(shippingPrice)
                .totalOrderPrice(totalOrderPrice)
                .paymentMethodType(order.getPaymentMethodType())
                .isPaid(order.isPaid())
                .isDelivered(order.isDelivered())
                .cartItems(cartItems)
                .createdAt(order.getCreatedAt()  == null ? null : order.getCreatedAt().toString())
                .updatedAt(order.getUpdatedAt()  == null ? null : order.getUpdatedAt().toString())
                .version(0)
                .build();

        return CreateOrderResponse.builder()
                .status("success")
                .message("Order created")
                .user(userSummary)
                .pricing(pricingSummary)
                .data(orderData)
                .build();
    }
}