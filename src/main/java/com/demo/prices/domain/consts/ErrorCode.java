package com.demo.prices.domain.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Catalogo de errores
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    PRICE_NOT_FOUND(
            "PRICE_NOT_FOUND",
            "Precio no encontrado",
            404),

    MULTIPLE_APPLICABLE_PRICES(
            "MULTIPLE_APPLICABLE_PRICES",
            "Multiples precios encontrados",
            500),
    
    INVALID_INPUT_PARAMETER(
            "INVALID_INPUT_PARAMETER",
            "La petición contiene parámetros no válidos.",
            400),

    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "Se ha producido un error inesperado",
            500);

    /**
     * Código funcional del error.
     */
    private final String code;

    /**
     * Mensaje de error.
     */
    private final String message;

    /**
     * Codigo de estado asociado al error.
     */
    private final int codeStatus;

}
