package com.deftech.viewtils.finders;

public abstract class Finder<T> {
    private T instance;

    Finder(T instance){
        this.instance = instance;
    }

    public T getInstance(){ return instance; }
}
