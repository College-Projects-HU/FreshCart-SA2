package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Fetches all orders placed by a given user, sorted newest-first.
     */
    List<Order> findAllByUserIdOrderByCreatedAtDesc(String userId);

    /**
     * Alias used in the service layer for clarity.
     */
    default List<Order> findAllByUserId(String userId) {
        return findAllByUserIdOrderByCreatedAtDesc(userId);
    }
    Optional<Order> findByOrderId(String orderId);
}
