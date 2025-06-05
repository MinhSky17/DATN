package com.model1.application.service;

import com.model1.application.model.request.OrderDetailRequest;
import org.springframework.stereotype.Service;

@Service
public interface OrderDetailService {
    void addToOrderDetail(OrderDetailRequest orderDetailRequest);
}
