package com.demo.prices.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PriceApiIT {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "{0} - productId: {2} - brandId: {1}")
    @MethodSource("priceCasesOK")
    @DisplayName("Casos de prueba especificados: Resultado OK")
    void shouldReturnApplicablePrice(
            String description,
            String brandId,
    		String productId, 
            String pricingDate,
            Long expectedPriceList,
            Integer expectedPriority,
            Double expectedPrice,
            String expectedStartDate,
            String expectedEndDate) throws Exception {

        mockMvc.perform(get("/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("pricingDate", pricingDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.priceList").value(expectedPriceList))
                .andExpect(jsonPath("$.price").value(expectedPrice))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.startDate").value(expectedStartDate))
                .andExpect(jsonPath("$.endDate").value(expectedEndDate));
    }

    static Stream<Arguments> priceCasesOK() {
        return Stream.of(
                Arguments.of(
                		"Caso 1 - 14/06 10:00", 
                		"1",
                		"35455", 
                		"2020-06-14T10:00:00",
                		1L, 
                		0, 
                		35.50, 
                		"2020-06-14T00:00:00", 
                		"2020-12-31T23:59:59"),
                Arguments.of(
                        "Caso 3 - 14/06 21:00", 
                		"1",
                		"35455",
                        "2020-06-14T21:00:00",
                        1L,
                        0,
                        35.50,
                        "2020-06-14T00:00:00",
                        "2020-12-31T23:59:59")
        );
    }
    
    @ParameterizedTest(name = "{0} - productId: {2} - brandId: {1}")
    @MethodSource("priceCasesMultiplesResults")
    @DisplayName("Casos de prueba especificados: Resultado error: existen multiples precios")
    void shouldReturnMultiplesApplicablePrice(
    		String description, 
    		String brandId,
    		String productId, 
    		String pricingDate) throws Exception {

    	mockMvc.perform(get("/prices")
	                .param("brandId", brandId)
	                .param("productId", productId)
	                .param("pricingDate", pricingDate))
	        .andExpect(status().isInternalServerError())
	        .andExpect(jsonPath("$.code")
	                .value("MULTIPLE_APPLICABLE_PRICES"))
	        .andExpect(jsonPath("$.message")
	                .value("Multiples precios encontrados"))
	        .andExpect(jsonPath("$.description")
	                .value("Existen multiples precios aplicables para los criterios indicados"))
	        .andExpect(jsonPath("$.timestamp").exists());
    }
    
    static Stream<Arguments> priceCasesMultiplesResults() {
        return Stream.of(
        		Arguments.of(
                        "Caso 2 - 14/06 16:00", "1", "35455",
                        "2020-06-14T16:00:00"),
                Arguments.of(
                        "Caso 4 - 15/06 10:00", "1", "35455",
                        "2020-06-15T10:00:00"),
                Arguments.of(
                        "Caso 5 - 16/06 21:00", "1", "35455",
                        "2020-06-16T21:00:00")
        );
    }


    @ParameterizedTest
    @MethodSource("priceCasesNoResults")
    @DisplayName("Casos de prueba: No existe ningun precio aplicable")
    void shouldReturn404WhenNoPriceFound(Long brandId, Long productId, String pricingDate) throws Exception {
        mockMvc.perform(get("/prices")
                        .param("brandId", brandId.toString())
                        .param("productId", productId.toString())
                        .param("pricingDate", pricingDate))
                .andExpect(status().isNotFound());
    }

    static Stream<Arguments> priceCasesNoResults() {
        return Stream.of(
                Arguments.of(1L, 35455L, "2019-01-01T10:00:00"),
                Arguments.of(99L, 35455L,"2020-06-14T10:00:00"),
                Arguments.of(1L, 99999L, "2020-06-14T10:00:00")
        );
    }
}