package com.bank.msdebt.controller;

import com.bank.msdebt.dto.DebtStatusRequest;
import com.bank.msdebt.dto.DebtStatusResponse;
import com.bank.msdebt.model.DebtStatus;
import com.bank.msdebt.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controlador REST reactivo para la deuda vencida.
 */
@RestController
@RequestMapping("/debts")
@RequiredArgsConstructor
public class DebtController {

    private final DebtService debtService;

    /**
     * Marca una deuda como vencida.
     *
     * @param request datos de la deuda
     * @return Mono con estado actualizado
     */
    @PostMapping("/overdue")
    public Mono<ResponseEntity<DebtStatusResponse>> markOverdue(@RequestBody DebtStatusRequest request) {
        return debtService.markOverdue(request.getCustomerId(), request.getCreditProductId())
                .map(this::toResponse)
                .map(debt -> ResponseEntity.status(HttpStatus.CREATED).body(debt));
    }

    /**
     * Registra la liquidacion de una deuda.
     *
     * @param request datos de la deuda
     * @return Mono con estado actualizado
     */
    @PostMapping("/settle")
    public Mono<ResponseEntity<DebtStatusResponse>> settleDebt(@RequestBody DebtStatusRequest request) {
        return debtService.settleDebt(request.getCustomerId(), request.getCreditProductId())
                .map(this::toResponse)
                .map(ResponseEntity::ok);
    }

    /**
     * Consulta el estado de una deuda concreta.
     *
     * @param customerId cliente
     * @param creditProductId producto
     * @return Mono con estado o 404
     */
    @GetMapping("/customers/{customerId}/products/{creditProductId}")
    public Mono<ResponseEntity<DebtStatusResponse>> getByCustomerAndProduct(
            @PathVariable String customerId,
            @PathVariable String creditProductId) {
        return debtService.findByCustomerAndProduct(customerId, creditProductId)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private DebtStatusResponse toResponse(DebtStatus debtStatus) {
        DebtStatusResponse response = new DebtStatusResponse();
        response.setId(debtStatus.getId());
        response.setCustomerId(debtStatus.getCustomerId());
        response.setCreditProductId(debtStatus.getCreditProductId());
        response.setStatus(debtStatus.getStatus());
        return response;
    }
}
