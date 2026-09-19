package com.bank.msdebt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Estado de deuda vencida para un producto de credito concreto.
 */
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "debt_status")
public class DebtStatus {

    /** Estado de deuda vigente. */
    public static final String STATUS_OVERDUE = "OVERDUE";
    /** Estado resuelto. */
    public static final String STATUS_SETTLED = "SETTLED";

    @Id
    private String id;

    @Indexed
    private String customerId;

    @Indexed
    private String creditProductId;

    private String status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Constructor.
     *
     * @param customerId identificador del cliente
     * @param creditProductId identificador del producto de credito
     * @param status estado inicial
     */
    public DebtStatus(String customerId, String creditProductId, String status) {
        this.customerId = customerId;
        this.creditProductId = creditProductId;
        this.status = status;
    }
}
