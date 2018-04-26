package com.brewer.validation.validator.exception;

public class ConfirmacaoValidatorException extends RuntimeException {

    public ConfirmacaoValidatorException(String msg, Exception exception) {
        super(msg, exception);
    }
}
