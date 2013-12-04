package com.deftech.viewtils;

/***
 * Helper that allows for {@link #executeOnUiThread(String}
 * to be called to execute a static method when only a class
 * can be provided.
 */
class ClassHelper extends Helper {
    private final Class<?> instanceClass;
    
    public ClassHelper(Class<?> instanceClass){
        super(null);
        this.instanceClass = instanceClass;
    }
    
    @Override
    public MethodRunner executeOnUiThread(String methodName){
        return new MethodRunner(methodName, instanceClass);
    }
}