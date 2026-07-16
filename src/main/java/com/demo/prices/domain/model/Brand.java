package com.demo.prices.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Representacion de informacion de una cadena
 */
@Getter
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode
public final class Brand {

    private final Long id;
    private final String name;
}

