package com.model1.application.service;

import com.model1.application.entity.CartProduct;
import com.model1.application.entity.Product;
import com.model1.application.entity.ProductColor;
import com.model1.application.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    void addToCart(User user, Product product, ProductColor color, int quantity);
    List<CartProduct> getItems(User user);
}
