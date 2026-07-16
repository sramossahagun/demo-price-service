package com.demo.prices.adapters.out.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entidad de persistencia que representa la información de precios aplicables
 */
@Entity
@Table( name = "PRICES")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PriceEntity {

    /**
     * ID de la entidad.
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Cadena.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "BRAND_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_PRICES_BRANDS"))
    private BrandEntity brand;

    /**
     * Fecha de inicio de aplicación del precio.
     */
    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;

    /**
     * Fecha de fin de aplicación del precio.
     */
    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate;

    /**
     * Identificador de la tarifa aplicada.
     */
    @Column(name = "PRICE_LIST", nullable = false)
    private Long priceList;

    /**
     * Identificador del producto.
     */
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    /**
     * Prioridad de aplicación frente a otras tarifas coincidentes.
     */
    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    /**
     * Precio final aplicable.
     */
    @Column(name = "PRICE", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Código ISO de la moneda.
     */
    @Column(name = "CURRENCY", nullable = false, length = 3)
    private String currency;
}
