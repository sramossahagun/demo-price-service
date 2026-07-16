package com.demo.prices.ports.in;

import java.time.LocalDateTime;

import com.demo.prices.domain.exception.DemoPriceException;
import com.demo.prices.domain.model.Price;

/**
 * Operaciones de dominio para gestion de precios
 */
public interface PriceUseCase {

    /**
     * Obtiene el precio aplicable para un producto y una cadena en una fecha determinada.
	 * @param brandId identificador de la cadena
	 * @param productId identificador del producto
	 * @param pricingDate fecha para la que se desea consultar el precio
	 * @return precio aplicable
	 * @throws DemoPriceException Devuelve excepcion si no tiene resultados o tiene mas de uno
	 */
    Price getApplicablePrice(Long brandId, Long productId, LocalDateTime pricingDate);
}
