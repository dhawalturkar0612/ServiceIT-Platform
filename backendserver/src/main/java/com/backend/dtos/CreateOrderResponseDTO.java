package com.backend.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponseDTO {

    private String orderId;

    private String currency;

    private BigDecimal amount;

    private String razorpayKey;
}