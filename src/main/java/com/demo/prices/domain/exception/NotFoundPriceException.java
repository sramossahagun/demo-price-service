package com.demo.prices.domain.exception;

import com.demo.prices.domain.consts.ErrorCode;

import lombok.Getter;

@Getter
public class NotFoundPriceException extends DemoPriceException {
	private static final long serialVersionUID = 1L;
	
	private static final ErrorCode errorCode = ErrorCode.PRICE_NOT_FOUND;
	
	public NotFoundPriceException() {
		super(errorCode.getMessage());
	}
	
	public NotFoundPriceException(final Throwable cause) {
		super(errorCode.getMessage(), cause);
	}
	
	public NotFoundPriceException(String description) {
		super(errorCode.getMessage(), description);
	}
	
	public NotFoundPriceException(String description, final Throwable cause) {
		super(errorCode.getMessage(), description, cause);
	}

	@Override
	public final String getCode() {
		return errorCode.getCode();
	}

	@Override
	public final int getCodeStatus() {
		return errorCode.getCodeStatus();
	}

}