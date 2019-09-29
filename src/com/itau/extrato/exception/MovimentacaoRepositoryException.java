package com.itau.extrato.exception;

public class MovimentacaoRepositoryException extends RuntimeException{

    public MovimentacaoRepositoryException(String message) {
        super(message);
    }

    public MovimentacaoRepositoryException(Throwable t) {
        super(t);
    }
}
