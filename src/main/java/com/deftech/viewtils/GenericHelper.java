package com.deftech.viewtils;

public class GenericHelper<T> extends Helper<T,Helper> {
    GenericHelper(T instance, Class<T> instanceClass) {
        super(instance, instanceClass);
    }

    @Override
    public GenericHelper<T> usingRobolectric(){
        super.usingRobolectric();
        return this;
    }
}
