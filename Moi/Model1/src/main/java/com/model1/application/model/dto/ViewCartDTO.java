package com.model1.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewCartDTO {
    private Long cartId;
    private String productId;
    private Long colorId;
    private String productName;
    private String color;
    private Long quantity;
    private String productImage; // nếu cần hiển thị ảnh
    private Long price;
}
