package com.model1.application.entity;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class ProductColorId implements Serializable {
    private String productId;
    private String color;
}
