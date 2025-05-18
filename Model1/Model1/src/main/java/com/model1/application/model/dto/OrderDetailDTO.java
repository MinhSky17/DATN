package com.model1.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private long id;

    private long totalPrice;

    private long productPrice;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private int status;

    private String statusText;

    private int sizeVn;

    private double sizeUs;

    private double sizeCm;

    private String productName;

    private String productImg;

}
