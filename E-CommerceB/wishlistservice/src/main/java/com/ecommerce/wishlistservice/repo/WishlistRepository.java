package com.ecommerce.wishlistservice.repo;

import com.ecommerce.wishlistservice.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {

    List<WishlistItem> findAllByUserId( Long userId);

    Optional<WishlistItem> findByUserIdAndProductId(Long userId, String productId);

    boolean existsByUserIdAndProductId(Long userId, String productId);

    void deleteAllByProductId(String productId);

    void deleteAllByUserId(Long userId);
}
