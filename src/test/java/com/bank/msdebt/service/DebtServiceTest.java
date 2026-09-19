package com.bank.msdebt.service;

import com.bank.msdebt.event.DebtEventProducer;
import com.bank.msdebt.model.DebtStatus;
import com.bank.msdebt.repository.DebtStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtServiceTest {

    @Mock
    private DebtStatusRepository debtStatusRepository;

    @Mock
    private DebtEventProducer debtEventProducer;

    @InjectMocks
    private DebtService debtService;

    private DebtStatus overdue;

    @BeforeEach
    void setUp() {
        overdue = new DebtStatus("cust-1", "credit-2", "OVERDUE");
        overdue.setId("debt-1");
    }

    @Test
    void markOverdue_success() {
        when(debtStatusRepository.findByCustomerIdAndCreditProductId("cust-1", "credit-2"))
                .thenReturn(Mono.empty());
        when(debtStatusRepository.save(any(DebtStatus.class))).thenReturn(Mono.just(overdue));

        StepVerifier.create(debtService.markOverdue("cust-1", "credit-2"))
                .assertNext(status -> org.junit.jupiter.api.Assertions.assertEquals("OVERDUE", status.getStatus()))
                .verifyComplete();

        verify(debtEventProducer).publishOverdueDetected("cust-1", "credit-2");
    }

    @Test
    void settleDebt_success() {
        when(debtStatusRepository.findByCustomerIdAndCreditProductId("cust-1", "credit-2"))
                .thenReturn(Mono.just(overdue));
        when(debtStatusRepository.save(any(DebtStatus.class))).thenReturn(Mono.just(overdue));

        StepVerifier.create(debtService.settleDebt("cust-1", "credit-2"))
                .assertNext(status -> org.junit.jupiter.api.Assertions.assertEquals("SETTLED", status.getStatus()))
                .verifyComplete();

        verify(debtEventProducer).publishSettled("cust-1", "credit-2");
    }
}
