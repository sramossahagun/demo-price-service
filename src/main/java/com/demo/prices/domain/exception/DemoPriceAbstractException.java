package com.demo.prices.domain.exception;

import lombok.Getter;

/**
 * Abtraccion para gestion de excepciones
 */
@Getter
public abstract class DemoPriceAbstractException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private String description;

    /**
     * Crea una excepción de negocio con un mensaje.
     *
     * @param message mensaje descriptivo del error
     */
    protected DemoPriceAbstractException(final String message) {
        super(message);
    }
    
    /**
     * Crea una excepción de negocio con un mensaje.
     *
     * @param message mensaje descriptivo del error
     */
    protected DemoPriceAbstractException(final String message, final String description) {
        super(message);
        this.description = description;
    }

    /**
     * Crea una excepción de negocio con un mensaje y una causa.
     *
     * @param message mensaje descriptivo del error
     * @param cause excepción origen
     */
    protected DemoPriceAbstractException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Crea una excepción de negocio con un mensaje y una causa.
     *
     * @param message mensaje descriptivo del error
     * @param cause excepción origen
     */
    protected DemoPriceAbstractException(final String message, final String description, final Throwable cause) {
        super(message, cause);
        this.description = description;
    }

    /**
     * Devuelve el código funcional del error.
     *
     * @return código funcional
     */
    public abstract String getCode();
    
    
    public String getDescription () {
    	return this.description;
    }

    /**
     * Devuelve el codigo de estado asociado al error.
     *
     * @return codigo de estado de error
     */
	public int getCodeStatus() {
		return 500;
	}

}