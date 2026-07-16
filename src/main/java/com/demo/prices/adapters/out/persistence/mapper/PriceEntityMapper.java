package com.demo.prices.adapters.out.persistence.mapper;

import org.mapstruct.Mapper;

import com.demo.prices.adapters.out.persistence.entity.PriceEntity;
import com.demo.prices.domain.model.Price;


@Mapper(componentModel = "spring")
public interface PriceEntityMapper {

    /**
     * Convierte modelo entidad JPA a modelo de dominio
     *
     * @param entity entidad JPA
     * @return modelo de dominio
     */
    Price toDomain(PriceEntity entity);

}