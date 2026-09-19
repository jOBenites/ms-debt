package com.bank.msdebt.service;

import com.bank.msdebt.event.DebtEventProducer;
import com.bank.msdebt.model.DebtStatus;
import com.bank.msdebt.repository.DebtStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Servicio reactivo para la gestion de deuda vencida.
 */
@Service
@RequiredArgsConstructor
public class DebtService {

    private final DebtStatusRepository debtStatusRepository;
    private final DebtEventProducer debtEventProducer;

    /**
     * Marca una deuda como vencida.
     *
     * @param customerId identificador del cliente
     * @param creditProductId identificador del producto
     * @return Mono con el estado actualizado
     */
    public Mono<DebtStatus> markOverdue(String customerId, String creditProductId) {
        if (customerId == null || customerId.isBlank()) {
            return Mono.error(new IllegalArgumentException("El cliente es obligatorio"));
        }
        if (creditProductId == null || creditProductId.isBlank()) {
            return Mono.error(new IllegalArgumentException("El producto es obligatorio"));
        }
        return debtStatusRepository.findByCustomerIdAndCreditProductId(customerId, creditProductId)
                .switchIfEmpty(Mono.defer(() -> {
                    DebtStatus status = new DebtStatus(customerId, creditProductId, DebtStatus.STATUS_OVERDUE);
                    return debtStatusRepository.save(status);
                }))
                .flatMap(current -> {
                    current.setStatus(DebtStatus.STATUS_OVERDUE);
                    return debtStatusRepository.save(current)
                            .doOnNext(saved -> debtEventProducer.publishOverdueDetected(customerId, creditProductId));
                });
    }

    /**
     * Registra la liquidacion de una deuda.
     *
     * @param customerId identificador del cliente
     * @param creditProductId identificador del producto
     * @return Mono con el estado actualizado
     */
    public Mono<DebtStatus> settleDebt(String customerId, String creditProductId) {
        if (customerId == null || customerId.isBlank()) {
            return Mono.error(new IllegalArgumentException("El cliente es obligatorio"));
        }
        if (creditProductId == null || creditProductId.isBlank()) {
            return Mono.error(new IllegalArgumentException("El producto es obligatorio"));
        }
        return debtStatusRepository.findByCustomerIdAndCreditProductId(customerId, creditProductId)
                .switchIfEmpty(Mono.just(new DebtStatus(customerId, creditProductId, DebtStatus.STATUS_SETTLED)))
                .flatMap(current -> {
                    current.setStatus(DebtStatus.STATUS_SETTLED);
                    return debtStatusRepository.save(current)
                            .doOnNext(saved -> debtEventProducer.publishSettled(customerId, creditProductId));
                });
    }

    /**
     * Busca una deuda por cliente y producto.
     *
     * @param customerId identificador del cliente
     * @param creditProductId identificador del producto
     * @return Mono con la deuda o vacio
     */
    public Mono<DebtStatus> findByCustomerAndProduct(String customerId, String creditProductId) {
        return debtStatusRepository.findByCustomerIdAndCreditProductId(customerId, creditProductId);
    }
}
