package com.paulograbin.insight.Exceptions;

/**
 * Created by paulograbin on 04/09/15.
 */
public class NoWayException extends RuntimeException {

    public NoWayException() {
        super("Não há um caminho até o local selecionado");
    }
}
