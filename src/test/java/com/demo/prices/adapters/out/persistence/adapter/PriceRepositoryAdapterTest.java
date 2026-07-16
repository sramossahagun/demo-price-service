package com.demo.prices.adapters.out.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.demo.prices.adapters.out.persistence.entity.BrandEntity;
import com.demo.prices.adapters.out.persistence.entity.PriceEntity;
import com.demo.prices.adapters.out.persistence.mapper.PriceEntityMapper;
import com.demo.prices.adapters.out.persistence.repository.PriceJpaRepository;
import com.demo.prices.domain.model.Price;

@ExtendWith(MockitoExtension.class)
class PriceRepositoryAdapterTest {

    @Mock
    private PriceJpaRepository priceJpaRepository;

    private PriceRepositoryAdapter repository;
    
    private final PriceEntityMapper priceEntityMapper = Mappers.getMapper(PriceEntityMapper.class);

    @BeforeEach
    void setUp() {
        repository = new PriceRepositoryAdapter(priceJpaRepository, priceEntityMapper);
    }

    @Test
    @DisplayName("Devuelve lista de precios mapeada al dominio")
    void shouldReturnMappedPrices() {

        LocalDateTime pricingDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        BrandEntity brand = BrandEntity.builder()
                .id(1L)
                .name("ZARA")
                .build();

        PriceEntity entity = PriceEntity.builder()
                .id(1L)
                .brand(brand)
                .productId(35455L)
                .priority(0)
                .price(BigDecimal.valueOf(35.50))
                .currency("EUR")
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .build();

        when(priceJpaRepository.findApplicablePrices(
                1L,
                35455L,
                pricingDate))
                .thenReturn(List.of(entity));

        // Act
        List<Price> result = repository.findApplicablePrices(
                1L,
                35455L,
                pricingDate);

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(1);

        Price price = result.getFirst();

        assertThat(price.getId()).isEqualTo(1L);
        assertThat(price.getBrand().getId()).isEqualTo(1L);
        assertThat(price.getProductId()).isEqualTo(35455L);
        assertThat(price.getPriority()).isEqualTo(0);
        assertThat(price.getPrice()).isEqualByComparingTo("35.50");
        assertThat(price.getCurrency()).isEqualTo("EUR");
        assertThat(price.getStartDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 0, 0));
        assertThat(price.getEndDate()).isEqualTo(LocalDateTime.of(2020, 12, 31, 23, 59, 59));

        verify(priceJpaRepository)
                .findApplicablePrices(1L, 35455L, pricingDate);
    }

    @Test
    @DisplayName("Devolver una lista vacía cuando no hay resultados")
    void shouldReturnEmptyList() {

        // Arrange
        LocalDateTime pricingDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        when(priceJpaRepository.findApplicablePrices(
                1L,
                35455L,
                pricingDate))
                .thenReturn(Collections.emptyList());

        // Act
        List<Price> result = repository.findApplicablePrices(
                1L,
                35455L,
                pricingDate);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEmpty();

        verify(priceJpaRepository)
                .findApplicablePrices(1L, 35455L, pricingDate);
    }
}