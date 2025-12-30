package br.com.caio.desafio_tecnico.exception;

public class CsvFileNotFoundException extends RuntimeException {

    public CsvFileNotFoundException(String message) {
        super(message);
    }

}
