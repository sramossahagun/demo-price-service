package com.demo.prices.adapters.out.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.prices.adapters.out.persistence.entity.PriceEntity;

/**
 * Repositorio JPA para precios.
 */
public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {

	@Query("""
		    SELECT p
		    FROM PriceEntity p
		    JOIN FETCH p.brand
		    WHERE p.brand.id = :brandId
		      AND p.productId = :productId
		      AND p.startDate <= :pricingDate
		      AND p.endDate >= :pricingDate
		    ORDER BY p.priority DESC
		""")
	List<PriceEntity> findApplicablePrices(
	        @Param("brandId") final Long brandId,
	        @Param("productId") final Long productId,
	        @Param("pricingDate") final LocalDateTime pricingDate);
}
