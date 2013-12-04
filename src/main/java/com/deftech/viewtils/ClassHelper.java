package com.deftech.viewtils;

class ClassHelper extends Helper {
    private final Class<?> instanceClass;
    
    public ClassHelper(Class<?> instanceClass){
        this.instanceClass = instanceClass;
    }
    
    @Override
    public MethodRunner executeOnUiThread(String methodName){
        return new MethodRunner(methodName, instanceClass);
    }
}