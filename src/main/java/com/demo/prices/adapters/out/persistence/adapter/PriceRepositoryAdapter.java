package com.demo.prices.adapters.out.persistence.adapter;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.demo.prices.adapters.out.persistence.mapper.PriceEntityMapper;
import com.demo.prices.adapters.out.persistence.repository.PriceJpaRepository;
import com.demo.prices.domain.model.Price;
import com.demo.prices.ports.out.PriceRepository;

import lombok.RequiredArgsConstructor;

/**
 * Adaptador acceso a persistencia
 */
@Repository
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepository {

    private final PriceJpaRepository priceJpaRepository;

    private final PriceEntityMapper priceEntityMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Price> findApplicablePrices (Long brandId, Long productId, LocalDateTime pricingDate) {

        return this.priceJpaRepository
                .findApplicablePrices(brandId, productId, pricingDate)
                .stream()
                .map(this.priceEntityMapper::toDomain)
                .toList();
    }

}
