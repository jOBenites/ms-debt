package com.bank.msdebt.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO de respuesta para el estado de una deuda.
 */
@Getter
@Setter
public class DebtStatusResponse {

    private String id;
    private String customerId;
    private String creditProductId;
    private String status;
}
