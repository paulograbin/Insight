package com.paulograbin.insight.Model;

/**
 * Created by paulograbin on 08/08/15.
 */
public interface ModelInterface<T> {

    long id = 0;
    long getId();

    boolean isEqualTo(T object);
}
