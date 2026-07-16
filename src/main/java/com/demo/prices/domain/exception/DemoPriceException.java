package com.demo.prices.domain.exception;

import java.util.Optional;

import com.demo.prices.domain.consts.ErrorCode;

import lombok.Getter;

/**
 * Excepción base para los errores controlados
 */
@Getter
public class DemoPriceException extends DemoPriceAbstractException {

	private static final long serialVersionUID = 1L;
	
	private String code = "INTERNAL_SERVER_ERROR";
	
	private Integer codeStatus;

	public DemoPriceException(final String message) {
		super(message);
	}

	public DemoPriceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public DemoPriceException(String message, String description, Throwable cause) {
		super(message, description, cause);
	}

	public DemoPriceException(String message, String description) {
		super(message, description);
	}
	
	public DemoPriceException(String code, String message, String description) {
		super(message, description);
		this.code = code;
	}
	
	public DemoPriceException(ErrorCode error, String description) {
		this(error.getCode(), error.getMessage(), description);
		this.codeStatus = error.getCodeStatus();
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public int getCodeStatus() {
		return Optional.ofNullable(this.codeStatus).orElse(super.getCodeStatus());
	}

}