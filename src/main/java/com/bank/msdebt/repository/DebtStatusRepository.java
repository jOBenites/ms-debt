package com.bank.msdebt.repository;

import com.bank.msdebt.model.DebtStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo para deuda vencida.
 */
public interface DebtStatusRepository extends ReactiveMongoRepository<DebtStatus, String> {

    /**
     * Busca la deuda por cliente y producto.
     *
     * @param customerId identificador del cliente
     * @param creditProductId identificador del producto
     * @return Mono con la deuda existente o vacio
     */
    Mono<DebtStatus> findByCustomerIdAndCreditProductId(String customerId, String creditProductId);
}
