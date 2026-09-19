package com.bank.msdebt.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO de solicitud para registrar deuda vencida o liquidada.
 */
@Getter
@Setter
public class DebtStatusRequest {

    private String customerId;
    private String creditProductId;
}
