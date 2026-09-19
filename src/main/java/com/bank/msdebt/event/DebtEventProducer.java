package com.bank.msdebt.event;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Productor de eventos para deuda vencida.
 */
@Component
@RequiredArgsConstructor
public class DebtEventProducer {

    private static final Logger log = LoggerFactory.getLogger(DebtEventProducer.class);
    private static final String DEBT_OVERDUE_TOPIC = "bank.debt.overdue-detected";
    private static final String DEBT_SETTLED_TOPIC = "bank.debt.settled";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publica evento de deuda vencida.
     *
     * @param customerId identificador del cliente
     * @param creditProductId identificador del producto
     */
    public void publishOverdueDetected(String customerId, String creditProductId) {
        Map<String, Object> payload = Map.of(
                "customerId", customerId,
                "creditProductId", creditProductId,
                "occurredAt", LocalDateTime.now()
        );
        kafkaTemplate.send(DEBT_OVERDUE_TOPIC, customerId, payload);
        log.info("Evento bank.debt.overdue-detected publicado para cliente {}", customerId);
    }

    /**
     * Publica evento de deuda liquidada.
     *
     * @param customerId identificador del cliente
     * @param creditProductId identificador del producto
     */
    public void publishSettled(String customerId, String creditProductId) {
        Map<String, Object> payload = Map.of(
                "customerId", customerId,
                "creditProductId", creditProductId,
                "occurredAt", LocalDateTime.now()
        );
        kafkaTemplate.send(DEBT_SETTLED_TOPIC, customerId, payload);
        log.info("Evento bank.debt.settled publicado para cliente {}", customerId);
    }
}
