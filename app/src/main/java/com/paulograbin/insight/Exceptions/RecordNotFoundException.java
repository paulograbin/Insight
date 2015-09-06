package com.paulograbin.insight.Exceptions;

/**
 * Created by paulograbin on 05/09/15.
 */
public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String message) {
        super(message);
    }
}
