package com.demo.prices.ports.out;

import java.time.LocalDateTime;
import java.util.List;

import com.demo.prices.domain.model.Price;

/**
 * Operaciones persistencia de gestion de precios
 */
public interface PriceRepository {

	/**
	 * Busca los precios aplicables para un producto y una cadena en una fecha determinada.
	 * <p>
	 * Los resultados se devuelven ordenados de mayor a menor prioridad.
	 *
	 * @param brandId identificador de la cadena
	 * @param productId identificador del producto
	 * @param pricingDate fecha de consulta del precio
	 * @return lista de precios aplicables a un producto ordenada por prioridad descendente
	 */
	List<Price> findApplicablePrices (Long brandId, Long productId, LocalDateTime pricingDate);
}
