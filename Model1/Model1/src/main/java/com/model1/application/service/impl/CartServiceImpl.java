package com.model1.application.service.impl;

import com.model1.application.entity.*;
import com.model1.application.repository.CartProductRepository;
import com.model1.application.repository.CartRepository;
import com.model1.application.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private  CartProductRepository cartProductRepository;

    public void addToCart(User user, Product product, ProductColor color, int quantity){
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(()->{
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        CartProduct item = new CartProduct();
        item.setCart(cart);
        item.setProduct(product);
        item.setProductColor(color);
        item.setQuantity(quantity);

        cart.getCartProducts().add(item);
        cartRepository.save(cart);
    }

    public List<CartProduct> getItems(User user){
        return cartRepository.findByUserId(user.getId())
                .map(Cart::getCartProducts)
                .orElse(List.of());
    }
}
