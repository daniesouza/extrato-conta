package com.itau.extrato.exception;

public class MovimentacaoServiceException extends RuntimeException{

    public MovimentacaoServiceException(String message) {
        super(message);
    }

    public MovimentacaoServiceException(Throwable t) {
        super(t);
    }
}
