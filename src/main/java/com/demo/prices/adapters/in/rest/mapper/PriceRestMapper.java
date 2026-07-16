package com.demo.prices.adapters.in.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.demo.prices.adapters.in.rest.model.PriceResponseDto;
import com.demo.prices.domain.model.Price;


@Mapper(componentModel = "spring")
public interface PriceRestMapper {

    /**
     * Convierte modelo de dominio a modelo de respuesta REST.
     *
     * @param price modelo de dominio
     * @return modelo de respuesta de la API
     */
	@Mapping(source="brand.id", target= "brandId")
    PriceResponseDto toResponse(Price price);

}
