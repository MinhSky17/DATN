package com.model1.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDTO {
    private long id;

    private long totalPrice;

    private int ColorVn;

    private double ColorUs;

    private double ColorCm;

    private String productName;

    private String productImg;

    public OrderInfoDTO(long id, long totalPrice, int ColorVn, String productName, String productImg) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.ColorVn = ColorVn;
        this.productName = productName;
        this.productImg = productImg;
    }
}