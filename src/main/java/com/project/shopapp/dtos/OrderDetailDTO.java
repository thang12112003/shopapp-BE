package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Builder//tự động tạo ra một builder pattern cho class.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order's ID must be > 0")
    private Long orderId;

    @Min(value = 1, message = "Order's ID must be > 0")
    @JsonProperty("product_id")
    private Long productId;

    @Min(value = 0, message = "Order's ID must be >= 0")
    private Long price;

    @Min(value = 1, message = "Order's ID must be >= 1")
    @JsonProperty("number_of_products")
    private Long numberOfProducts;

    @Min(value = 0, message = "Order's ID must be >= 0")
    @JsonProperty("total_money")
    private Long totalMoney;

    private String color;
}
