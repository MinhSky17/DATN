package com.model1.application.repository;

import com.model1.application.entity.Cart;
import com.model1.application.model.dto.CartItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long userId);

    @Query("SELECT c FROM Cart c WHERE c.userId = :userId AND c.productId IN :productIds AND c.colorId IN :colorIds")
    List<Cart> findByUserIdAndCartItems(@Param("userId") Long userId,
                                        @Param("productIds") List<String> productIds,
                                        @Param("colorIds") List<Long> colorIds);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.userId = :userId AND c.productId IN :productIds AND c.colorId IN :colorIds")
    void deleteByUserIdAndCartItems(@Param("userId") Long userId,
                                    @Param("productIds") List<String> productIds,
                                    @Param("colorIds") List<Long> colorIds);

    // Overload để dùng trực tiếp List<CartItemDTO>
    default List<Cart> findByUserIdAndCartItems(Long userId, List<CartItemDTO> cartItems) {
        List<String> productIds = cartItems.stream().map(CartItemDTO::getProductId).collect(Collectors.toList());
        List<Long> colorIds = cartItems.stream().map(CartItemDTO::getColorId).collect(Collectors.toList());
        return findByUserIdAndCartItems(userId, productIds, colorIds);
    }

    default void deleteByUserIdAndCartItems(Long userId, List<CartItemDTO> cartItems) {
        List<String> productIds = cartItems.stream().map(CartItemDTO::getProductId).collect(Collectors.toList());
        List<Long> colorIds = cartItems.stream().map(CartItemDTO::getColorId).collect(Collectors.toList());
        deleteByUserIdAndCartItems(userId, productIds, colorIds);
    }

}
