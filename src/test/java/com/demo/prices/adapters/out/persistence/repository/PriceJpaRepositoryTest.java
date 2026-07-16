package com.demo.prices.adapters.out.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.demo.prices.adapters.out.persistence.entity.PriceEntity;

@DataJpaTest
@ActiveProfiles("test")
class PriceJpaRepositoryTest {

    @Autowired
    private PriceJpaRepository repository;

    
    @Test
    @DisplayName("Consulta sin resultados")
    void shouldReturnNightPriceAt21PM() {

        List<PriceEntity> result = repository.findApplicablePrices(
                1L,
                35455L,
                LocalDateTime.of(2026, 7, 15, 21, 0));

        assertThat(result)
                .hasSize(0);
    }
    
    @Test
    @DisplayName("Consulta con resultados")
    void shouldReturnApplicablePricesOrderedByPriority() {

        List<PriceEntity> result = 
        		repository.findApplicablePrices(
	                1L,
	                35455L,
	                LocalDateTime.of(2020, 6, 14, 16, 0));

        assertThat(result)
                .hasSize(2);

        assertThat(result)
                .extracting(
                        PriceEntity::getPriceList,
                        PriceEntity::getPriority,
                        PriceEntity::getPrice,
                        PriceEntity::getCurrency)
                .containsExactly(
                        tuple(2L, 1, new BigDecimal("25.45"), "EUR"),
                        tuple(1L, 0, new BigDecimal("35.50"), "EUR"));
    }

}