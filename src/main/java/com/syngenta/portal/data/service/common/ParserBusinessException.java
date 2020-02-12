package com.syngenta.portal.data.service.common;


public class ParserBusinessException extends RuntimeException {

    public ParserBusinessException(String message) {
        super(message);
    }

    public ParserBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}