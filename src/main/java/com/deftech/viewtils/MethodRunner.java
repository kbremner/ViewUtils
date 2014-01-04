package com.deftech.viewtils;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;


/***
 * Helps in executing methods on the main (UI) thread
 * of an application.
 * @see Handler#post(Runnable)
 */
public class MethodRunner {
    private final Logger logger = Logger.getLogger(MethodRunner.class.getSimpleName());
    private final String methodName;
    private List<Object> args = new ArrayList<Object>();
    private List<Class<?>> paramTypes = new ArrayList<Class<?>>();
    private final Object instance;
    private Class<?> instanceClass;
    private Handler handler;
    private Long time;

    /* Creates a map to allow primitive classes to be
     * wrapped in their corresponding wrapper class */
    private static final int NUM_PRIMITIVE_TYPES = 9;
    private static final Map<Class<?>, Class<?>> primMap = new HashMap<Class<?>, Class<?>>(NUM_PRIMITIVE_TYPES);
    static {
        primMap.put(boolean.class, Boolean.class);
        primMap.put(byte.class, Byte.class);
        primMap.put(char.class, Character.class);
        primMap.put(double.class, Double.class);
        primMap.put(float.class, Float.class);
        primMap.put(int.class, Integer.class);
        primMap.put(long.class, Long.class);
        primMap.put(short.class, Short.class);
        primMap.put(void.class, Void.class);
    }


    public static MethodRunner execute(Object instance, String methodName){
        return new MethodRunner(methodName, instance, instance.getClass());
    }

    public static MethodRunner execute(Class<?> instanceClass, String methodName){
        return new MethodRunner(methodName, null, instanceClass);
    }

    /***
     * Create a new MethodRunner that can be used to call
     * a method with the provided name on the provided instance. 
     * @param methodName name of the method to be invoked
     * @param instance Instance that implements the method. Can be null if calling a static method
     * @param instanceClass Class that defines the method
     */
    private MethodRunner(String methodName, Object instance, Class<?> instanceClass){
        this.methodName = methodName;
        this.instance = instance;
        this.instanceClass = instanceClass;
    }

    /***
     * Specifies a parameter to be passed in when invoking the method.
     * The type is required to help find the method that is to be invoked.
     * This method can be called as many times as is required to specify
     * all the parameters for a method
     * @return This instance to allow for method chaining
     */
    public <P> MethodRunner withParameter(P instance, Class<P> instanceClass){
        args.add(instance);
        paramTypes.add(instanceClass);
        return this;
    }

    public MethodRunner withHandler(Handler handler){
        this.handler = handler;
        return this;
    }

    public MethodRunner in(int time, TimeUnit unit){
        this.time = unit.toMillis(time);
        return this;
    }

    public <T> T returning(Class<T> returnType) {
        return invoke(returnType);
    }

    public void returningNothing() {
        returning(Void.class);
    }


    private <T> T invoke(Class<T> returnType) {
        try {
            // Create a handler if none specified
            handler = (handler == null) ? new Handler(Looper.getMainLooper()) : handler;

            // Try and find the method
            final Class<?>[] pTypes = paramTypes.toArray(new Class<?>[paramTypes.size()]);
            final Method method = instanceClass.getMethod(methodName, pTypes);

            // Wrap the return type, if required
            returnType = wrapPrimitiveClass(returnType);

            if(returnType == null){
                throw new NullPointerException("Return type cannot be null");
            } else if(returnType != wrapPrimitiveClass(method.getReturnType())){
                throw new NoSuchMethodException("No method returning " + returnType);
            }

            final Object[] argsArray = args.toArray();

            final AtomicReference<Object> result = new AtomicReference<Object>();
            final AtomicReference<Throwable> throwable = new AtomicReference<Throwable>();
            final AtomicBoolean finished = new AtomicBoolean();

            // Create the runnable to carry out the method invocation
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        result.set(method.invoke(instance,argsArray));
                    } catch(InvocationTargetException e){
                        throwable.set(e.getCause());
                    } catch(Throwable t){
                        throwable.set(t);
                    } finally {
                        finished.set(true);
                    }
                }
            };

            // If a delay was defined, use it, else post now
            handler.postDelayed(runnable, (time != null) ? time : 0);

            // if using Robolectric, need to advance the appropriate looper
            runRobolectricLooper();

            // Wait for the method call to finish
            while(!finished.get());

            // If the method encountered an exception, throw it
            if(throwable.get() != null) throw throwable.get();
            // Else return the result
            return returnType.cast(result.get());

        } catch(Throwable t){
            // Wrap any throwables in a runtime exception
            throw new RuntimeException("Failed to invoke method", t);
        }
    }

    /***
     * Wraps a class in it's wrapper class if it represents
     * a primitive type
     * @param primClass class for a primitive type to be wrapped
     * @return the wrapper class if {@code primClass} was for a primitive type,
     * else returns {@code primClass}
     */
    @SuppressWarnings("unchecked")
    private static <T> Class<T> wrapPrimitiveClass(Class<T> primClass){
        Class<?> mappedClass = primMap.get(primClass);
        if(mappedClass != null) return (Class<T>) mappedClass;
        return primClass;
    }

    private  void runRobolectricLooper(){
        try {
            // Get the required robolectric classes
            Class<?> shadowLooperClass = Class.forName("org.robolectric.shadows.ShadowLooper");
            Class<?> robolectricClass = Class.forName("org.robolectric.Robolectric");

            // Get the appropriate looper
            Looper looper = (handler != null) ? handler.getLooper() : Looper.getMainLooper();

            // Get the ShadowLooper using Robolectric.shadowOf(Looper)
            Object shadowLooper = robolectricClass.getMethod("shadowOf", Looper.class).invoke(null, looper);
            // Run all the tasks posted to the looper
            shadowLooperClass.getMethod("runToEndOfTasks").invoke(shadowLooper);
        } catch (ReflectiveOperationException e) {
            logger.fine("Not using Robolectric");
        }
    }
}
