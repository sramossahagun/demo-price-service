package com.demo.prices.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Representa informacion de precio aplicable
 */
@Getter
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode
public final class Price {

	private final Long id;
    private final Brand brand;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Long priceList;
    private final Long productId;
    private final Integer priority;
    private final BigDecimal price;
    private final String currency;
}

