package com.demo.prices.adapters.in.rest.adapter;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.prices.adapters.in.rest.mapper.PriceRestMapperImpl;
import com.demo.prices.domain.consts.ErrorCode;
import com.demo.prices.domain.exception.DemoPriceException;
import com.demo.prices.domain.exception.NotFoundPriceException;
import com.demo.prices.domain.model.Brand;
import com.demo.prices.domain.model.Price;
import com.demo.prices.ports.in.PriceUseCase;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(PriceApiAdapter.class)
@Import(PriceRestMapperImpl.class)
class PriceApiAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PriceUseCase priceUseCase;

    @Test
    @DisplayName("Resultado OK - 200: Datos precio aplicable")
    void shouldReturnApplicablePrice() throws Exception {

        Price price = Price.builder()
                .brand(Brand.builder().id(1L).build())
                .productId(35455L)
                .priceList(1L)
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .startDate(LocalDateTime.of(2020,6,14,0,0))
                .endDate(LocalDateTime.of(2020,12,31,23,59,59))
                .build();

        when(priceUseCase.getApplicablePrice(
                eq(1L),
                eq(35455L),
                eq(LocalDateTime.of(2020,6,14,10,0))))
                .thenReturn(price);

        mockMvc.perform(get("/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("pricingDate", "2020-06-14T10:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"));

        verify(priceUseCase)
                .getApplicablePrice(
                        1L,
                        35455L,
                        LocalDateTime.of(2020,6,14,10,0));
    }
    
    @Test
    @DisplayName("Resultado error - 404: No existen resultados para parametros indicados")
    void shouldReturn404WhenPriceNotFound() throws Exception {

    	when(priceUseCase.getApplicablePrice(
                eq(1L),
                eq(35455L),
                eq(LocalDateTime.of(2020, 6, 14, 10, 0))))
            .thenThrow(new NotFoundPriceException(
                    "No existe ningún precio aplicable para los criterios indicados."));

    	mockMvc.perform(get("/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("pricingDate", "2020-06-14T10:00:00"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PRICE_NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value("Precio no encontrado"))
                .andExpect(jsonPath("$.description")
                        .value("No existe ningún precio aplicable para los criterios indicados."))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(priceUseCase)
                .getApplicablePrice(
                        1L,
                        35455L,
                        LocalDateTime.of(2020, 6, 14, 10, 0));
    }
    
    @Test
    @DisplayName("Resultado error - 500: Existen múltiples resultados para parametros indicados")
    void shouldReturn500WhenMultipleApplicablePricesExist() throws Exception {

        when(priceUseCase.getApplicablePrice(
                eq(1L),
                eq(35455L),
                eq(LocalDateTime.of(2020, 6, 14, 10, 0))))
            .thenThrow(new DemoPriceException(
                    ErrorCode.MULTIPLE_APPLICABLE_PRICES,
                    "Existen multiples precios aplicables para los criterios indicados"));

        mockMvc.perform(get("/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("pricingDate", "2020-06-14T10:00:00"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code")
                        .value("MULTIPLE_APPLICABLE_PRICES"))
                .andExpect(jsonPath("$.message")
                        .value("Multiples precios encontrados"))
                .andExpect(jsonPath("$.description")
                        .value("Existen multiples precios aplicables para los criterios indicados"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(priceUseCase)
                .getApplicablePrice(
                        1L,
                        35455L,
                        LocalDateTime.of(2020, 6, 14, 10, 0));
    }
    
    @Test
    @DisplayName("Resultado error - 400: Parametro obligatorio: brandId")
    void shouldReturn400WhenBrandIdIsMissing() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("productId", "35455")
                        .param("pricingDate", "2020-06-14T10:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT_PARAMETER"))
                .andExpect(jsonPath("$.description")
                        .value("El parámetro 'brandId' es obligatorio."))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Resultado error - 400: Parametro obligatorio: productId")
    void shouldReturn400WhenProductIdIsMissing() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("brandId", "1")
                        .param("pricingDate", "2020-06-14T10:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT_PARAMETER"))
                .andExpect(jsonPath("$.description")
                        .value("El parámetro 'productId' es obligatorio."))
                .andExpect(jsonPath("$.timestamp").exists());
    }
    
    @Test
    @DisplayName("Resultado error - 400: Parametro obligatorio: pricingDate")
    void shouldReturn400WhenPricingDateIsMissing() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("brandId", "1")
                        .param("productId", "35455"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT_PARAMETER"))
                .andExpect(jsonPath("$.description")
                        .value("El parámetro 'pricingDate' es obligatorio."))
                .andExpect(jsonPath("$.timestamp").exists());
    }
    
    @Test
    @DisplayName("Resultado error - 400: Formato parametro no valido: brandId")
    void shouldReturn400WhenBrandIdHasInvalidFormat() throws Exception {

        mockMvc.perform(get("/prices")
                        .param("brandId", "ABC")
                        .param("productId", "35455")
                        .param("pricingDate", "2020-06-14T10:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT_PARAMETER"))
                .andExpect(jsonPath("$.description")
                        .value("El parámetro 'brandId' contiene un valor no válido."))
                .andExpect(jsonPath("$.timestamp").exists());
    }
    
    @Test
    @DisplayName("Resultado error - 400: Formato paramrtro no valido: productId")
    void shouldReturn400WhenProductIdHasInvalidFormat() throws Exception {

        mockMvc.perform(get("/prices")
                        .param("brandId", "1")
                        .param("productId", "ABC")
                        .param("pricingDate", "2020-06-14T10:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT_PARAMETER"))
                .andExpect(jsonPath("$.description")
                        .value("El parámetro 'productId' contiene un valor no válido."))
                .andExpect(jsonPath("$.timestamp").exists());
    }
    
    @Test
    @DisplayName("Resultado error - 400: Formato parametro no valido: pricingDate")
    void shouldReturn400WhenPricingDateHasInvalidFormat() throws Exception {

        mockMvc.perform(get("/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("pricingDate", "14/06/2020"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT_PARAMETER"))
                .andExpect(jsonPath("$.description")
                        .value("El parámetro 'pricingDate' contiene un valor no válido."))
                .andExpect(jsonPath("$.timestamp").exists());
    }
    
    @Test
    @DisplayName("Resultado error - 400: Valor no valido: pricingDate")
    void shouldReturn400WhenPricingDateHasInvalidValue() throws Exception {

        mockMvc.perform(get("/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("pricingDate", "2020-02-31T10:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT_PARAMETER"))
                .andExpect(jsonPath("$.description")
                        .value("El parámetro 'pricingDate' contiene un valor no válido."))
                .andExpect(jsonPath("$.timestamp").exists());
    }
    
    @Test
    @DisplayName("Resultado error - 500: Error no controlado")
    void shouldReturn500WhenUnexpectedException() throws Exception {
    	when(priceUseCase.getApplicablePrice(
                eq(1L),
                eq(35455L),
                eq(LocalDateTime.of(2020,6,14,10,0))))
    	.thenThrow(new EntityNotFoundException("Unexpected Exception Error"));
    	
        mockMvc.perform(get("/prices")
                .param("brandId", "1")
                .param("productId", "35455")
                .param("pricingDate", "2020-06-14T10:00:00"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.code").value(ErrorCode.INTERNAL_SERVER_ERROR.getCode()))
        .andExpect(jsonPath("$.message")
        		.value("Se ha producido un error inesperado"))
        .andExpect(jsonPath("$.description")
        		.value("Se ha producido un error inesperado durante el procesamiento de la petición."))
        .andExpect(jsonPath("$.timestamp").exists());
    }
}