package com.model1.application.service;

import com.model1.application.entity.Cart;
import com.model1.application.model.dto.CartAddDTO;
import com.model1.application.model.dto.CartUpdateDTO;
import com.model1.application.model.dto.ViewCartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    void addToCart(CartAddDTO cartAddDTO);
    List<ViewCartDTO> getCartItemsByUserId(Long userId);
    void removeFromCart(Long cartId);
    void updateFromCart(CartUpdateDTO cartUpdateDTO);
}
