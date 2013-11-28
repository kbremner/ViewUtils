package com.deftech.viewtils;

public class GenericHelper<T> extends Helper<T,GenericHelper> {
    GenericHelper(T instance, Class<T> instanceClass) {
        super(instance, instanceClass, GenericHelper.class);
    }
}
