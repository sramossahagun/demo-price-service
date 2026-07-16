package com.demo.prices.adapters.in.rest.adapter;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.demo.prices.adapters.in.rest.mapper.PriceRestMapper;
import com.demo.prices.adapters.in.rest.model.PriceResponseDto;
import com.demo.prices.api.rest.service.PricesApi;
import com.demo.prices.domain.model.Price;
import com.demo.prices.ports.in.PriceUseCase;

import lombok.RequiredArgsConstructor;

/**
 * Controlador REST gestion de precios.
 */
@RestController
@RequiredArgsConstructor
public class PriceApiAdapter implements PricesApi {

    private final PriceUseCase priceUseCase;

    private final PriceRestMapper priceRestMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<PriceResponseDto> getApplicablePrice(
            final LocalDateTime pricingDate,
            final Long productId,
            final Long brandId) {

        final Price price = this.priceUseCase.getApplicablePrice(
                brandId,
                productId,
                pricingDate);

        return ResponseEntity.ok(this.priceRestMapper.toResponse(price));
    }

}
