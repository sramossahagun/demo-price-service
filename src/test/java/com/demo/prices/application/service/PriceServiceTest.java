package com.demo.prices.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.demo.prices.domain.consts.ErrorCode;
import com.demo.prices.domain.exception.DemoPriceException;
import com.demo.prices.domain.exception.NotFoundPriceException;
import com.demo.prices.domain.model.Brand;
import com.demo.prices.domain.model.Price;
import com.demo.prices.ports.out.PriceRepository;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;

    private PriceService priceService;

    private static final Long BRAND_ID = 1L;
    private static final Long PRODUCT_ID = 35455L;
    private static final LocalDateTime PRICING_DATE = LocalDateTime.of(2020, 6, 14, 10, 0);

    @BeforeEach
    void setUp() {
        priceService = new PriceService(priceRepository);
    }

    @Test
    @DisplayName("Resultado OK con precio aplicable")
    void shouldReturnApplicablePrice() {

        Price expectedPrice = Price.builder()
                .priceList(1L)
                .brand(Brand.builder().id(BRAND_ID).build())
                .productId(PRODUCT_ID)
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        when(priceRepository.findApplicablePrices(
                BRAND_ID,
                PRODUCT_ID,
                PRICING_DATE))
                .thenReturn(List.of(expectedPrice));

        Price result = priceService.getApplicablePrice(
                BRAND_ID,
                PRODUCT_ID,
                PRICING_DATE);

       
        assertEquals(expectedPrice, result);
        verify(priceRepository)
                .findApplicablePrices(BRAND_ID, PRODUCT_ID, PRICING_DATE);
    }

    @Test
    @DisplayName("Resultado error: No existen precios aplicables para parametros indicados")
    void shouldThrowNotFoundException() {
        when(priceRepository.findApplicablePrices(
                BRAND_ID,
                PRODUCT_ID,
                PRICING_DATE))
                .thenReturn(List.of());
        
        assertThrows(
                NotFoundPriceException.class,
                () -> priceService.getApplicablePrice(
                        BRAND_ID,
                        PRODUCT_ID,
                        PRICING_DATE));

        verify(priceRepository)
                .findApplicablePrices(BRAND_ID, PRODUCT_ID, PRICING_DATE);
    }

    @Test
    @DisplayName("Resultado error: Existen varios precios aplicables para parametros indicados")
    void shouldThrowBusinessExceptionWhenMultiplePricesExist() {
        Price price1 = Price.builder().priceList(1L).build();
        Price price2 = Price.builder().priceList(2L).build();

        when(priceRepository.findApplicablePrices(
                BRAND_ID,
                PRODUCT_ID,
                PRICING_DATE))
                .thenReturn(List.of(price1, price2));

        DemoPriceException exception = assertThrows(
                DemoPriceException.class,
                () -> priceService.getApplicablePrice(
                        BRAND_ID,
                        PRODUCT_ID,
                        PRICING_DATE));
        
        assertEquals(
                ErrorCode.MULTIPLE_APPLICABLE_PRICES.getCode(),
                exception.getCode());

        verify(priceRepository)
                .findApplicablePrices(BRAND_ID, PRODUCT_ID, PRICING_DATE);
    }
    
    @Test
    @DisplayName("Parametro nulo: brandId")
    void shouldThrowExceptionWhenBrandIdIsNull() {
        // Act & Assert
        assertThatThrownBy(() ->
                priceService.getApplicablePrice(
                        null,
                        35455L,
                        LocalDateTime.of(2020, 6, 14, 10, 0)))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("El parámetro 'brandId' es obligatorio.");
        
        verifyNoInteractions(priceRepository);
    }
    
    @Test
    @DisplayName("Parametro nulo: productId")
    void shouldThrowExceptionWhenProductIdIsNull() {
        assertThatThrownBy(() ->
                priceService.getApplicablePrice(
                        1L,
                        null,
                        LocalDateTime.of(2020, 6, 14, 10, 0)))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("El parámetro 'productId' es obligatorio.");

        verifyNoInteractions(priceRepository);
    }
    
    @Test
    @DisplayName("Parametro nulo: pricingDate")
    void shouldThrowExceptionWhenPricingDateIsNull() {

        assertThatThrownBy(() ->
                priceService.getApplicablePrice(
                        1L,
                        35455L,
                        null))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("El parámetro 'pricingDate' es obligatorio.");

        verifyNoInteractions(priceRepository);
    }

}