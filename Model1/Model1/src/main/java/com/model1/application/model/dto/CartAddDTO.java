package com.model1.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartAddDTO {
    private Long userId;

    private String productId;

    private Long colorId;

    private Long quantity;
}
