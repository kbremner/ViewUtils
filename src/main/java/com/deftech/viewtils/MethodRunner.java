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
import java.io.StringWriter;
import java.io.PrintWriter;


/***
 * Helps in executing methods on the main (UI) thread
 * of an application.
 * @see Handler#post(Runnable)
 */
public class MethodRunner {
    private static final Logger logger = Logger.getLogger(MethodRunner.class.getSimpleName());
    /* Staticly load the robolectric classes */
    private static Class<?> SHADOW_LOOPER_CLASS;
    private static Class<?> ROBOLECTRIC_CLASS;
    private static Method SHADOWOF_LOOPER_METHOD;
    private static Method RUNTOENDOFTASKS_METHOD;
    static {
        try {
            SHADOW_LOOPER_CLASS = Class.forName("org.robolectric.shadows.ShadowLooper");
            ROBOLECTRIC_CLASS = Class.forName("org.robolectric.Robolectric");
            SHADOWOF_LOOPER_METHOD = ROBOLECTRIC_CLASS.getMethod("shadowOf", Looper.class);
            RUNTOENDOFTASKS_METHOD = SHADOW_LOOPER_CLASS.getMethod("runToEndOfTasks");
        } catch(ReflectiveOperationException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.warning("Not using robolectric or unsupported version of robolectric used: " + sw.toString());
        }
    }
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


    private final String methodName;
    private List<Object> args = new ArrayList<Object>();
    private List<Class<?>> paramTypes = new ArrayList<Class<?>>();
    private final Object instance;
    private Class<?> instanceClass;
    private Handler handler;
    private long time;


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
            handler.postDelayed(runnable, time);

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

    /***
     * Advances the looper if using robolectric
     */
    private void runRobolectricLooper(){
        if(ROBOLECTRIC_CLASS != null) {
            logger.fine("Using robolectric, advancing looper");
            try {
                // Get the appropriate looper
                Looper looper = (handler != null) ? handler.getLooper() : Looper.getMainLooper();
    
                // Get the ShadowLooper using Robolectric.shadowOf(Looper)
                Object shadowLooper = SHADOWOF_LOOPER_METHOD.invoke(null, looper);
                
                // Run all the tasks posted to the looper
                RUNTOENDOFTASKS_METHOD.invoke(shadowLooper);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to execute method", e);
            }
        } else {
            logger.fine("Not using robolectric");
        }
    }
}
