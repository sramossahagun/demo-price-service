package com.demo.prices.application.service;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.demo.prices.domain.consts.ErrorCode;
import com.demo.prices.domain.exception.DemoPriceException;
import com.demo.prices.domain.exception.NotFoundPriceException;
import com.demo.prices.domain.model.Price;
import com.demo.prices.ports.in.PriceUseCase;
import com.demo.prices.ports.out.PriceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación casos de uso de gestion de precios
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PriceService implements PriceUseCase {

    private final PriceRepository priceRepository;

    
    /**
     * {@inheritDoc}
     */
    @Override
	public Price getApplicablePrice(Long brandId, Long productId, LocalDateTime pricingDate) {

    	log.debug("Iniciando busqueda de precio aplicable [brandId:{}] - [productId:{}] - [pricingDate:{}]", brandId, productId, pricingDate);
    	
    	validateInputParameters(brandId, productId, pricingDate);

        final List<Price> applicablePrices = 
        		this.priceRepository.findApplicablePrices(brandId, productId, pricingDate);

        if (applicablePrices.isEmpty()) {
            throw new NotFoundPriceException ("No existe ningún precio aplicable para los criterios indicados");
        }

        if (applicablePrices.size() > 1) {
        	throw new DemoPriceException (
        			ErrorCode.MULTIPLE_APPLICABLE_PRICES, 
        			"Existen multiples precios aplicables para los criterios indicados");
        }
        
        log.info("Recuperado precio aplicable [brandId:{}] - [productId:{}] - [pricingDate:{}]: {}", brandId, productId, pricingDate, applicablePrices.getFirst().getPrice());

        return applicablePrices.getFirst();
    }
    
    private void validateInputParameters(final Long brandId, final Long productId, final LocalDateTime pricingDate) {
        requireNotNull(brandId, "brandId");
        requireNotNull(productId, "productId");
        requireNotNull(pricingDate, "pricingDate");
    }
    
    private static <T> T requireNotNull(final T value, final String parameterName) {
        return Optional.ofNullable(value)
                .orElseThrow(() ->
                        new InvalidParameterException(
                                String.format("El parámetro '%s' es obligatorio.", parameterName)));
    }
}

