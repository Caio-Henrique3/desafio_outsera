package br.com.caio.desafio_tecnico.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
