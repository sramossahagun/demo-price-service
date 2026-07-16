package com.demo.prices.adapters.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entidad de persistencia que representa la información de una cadena.
 */
@Entity
@Table(name = "BRANDS")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BrandEntity {

	/**
     * ID de la cadena.
     */
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "ID")
    private Long id;

    /**
     * Nombre de la cadena.
     */
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
}
