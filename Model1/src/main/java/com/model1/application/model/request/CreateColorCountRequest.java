package com.model1.application.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateColorCountRequest {
    @NotEmpty(message = "Mã màu trống")
    @JsonProperty("code")
    private String code;

    @NotEmpty(message = "Tên màu trống")
    private String name;

    @Min(0)
    private int count;

    @NotEmpty(message = "Mã sản phẩm trống")
    @JsonProperty("product_id")
    private String productId;
}
