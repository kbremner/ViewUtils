package com.deftech.viewtils;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


public class MethodRunner {
    private final String methodName;
    private List<Object> args = new ArrayList<Object>();
    private List<Class<?>> paramTypes = new ArrayList<Class<?>>();
    private Object instance;
    private Class<?> instanceClass;
    private boolean withRobolectric;
    private Handler handler;
    private Integer time;
    private TimeUnit unit;


    public MethodRunner(String methodName, Object instance, Class<?> instanceClass){
        this.methodName = methodName;
        this.instance = instance;
        this.instanceClass = instanceClass;
    }

    public <P> MethodRunner withParameter(P instance, Class<P> instanceClass){
        args.add(instance);
        paramTypes.add(instanceClass);
        return this;
    }

    public MethodRunner usingRobolectric(){
        withRobolectric = true;
        return this;
    }

    public MethodRunner withHandler(Handler handler){
        this.handler = handler;
        return this;
    }

    public MethodRunner in(int time, TimeUnit unit){
        this.time = time;
        this.unit = unit;
        return this;
    }

    public <T> T returning(Class<T> returnType) {
        return returnType.cast(invoke());
    }

    public void returningNothing() {
        invoke();
    }


    private Object invoke() {
        try {
            handler = (handler == null) ? new Handler(Looper.getMainLooper()) : handler;

            Class<?>[] pTypes = new Class<?>[paramTypes.size()];
            pTypes = paramTypes.toArray(pTypes);
            final Method method = instanceClass.getMethod(methodName, pTypes);

            final Object i = instance;
            final Object[] a = args.toArray();

            final AtomicReference<Object> result = new AtomicReference<Object>();
            final AtomicReference<Throwable> throwable = new AtomicReference<Throwable>();
            final AtomicBoolean finished = new AtomicBoolean();

            // Create the runnable to carry out the method invocation
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        result.set(method.invoke(i,a));
                    } catch(InvocationTargetException e){
                        throwable.set(e.getCause());
                    } catch(Throwable t){
                        throwable.set(t);
                    } finally {
                        finished.set(true);
                    }
                }
            };

            // If it's delayed, post it with the delay, else just post it
            if(time != null && unit != null) {
                handler.postDelayed(runnable, unit.toMillis(time));
            } else {
                handler.post(runnable);
            }

            // If using robolectric, advance the looper
            if(withRobolectric){
                try {
                    Class<?> robolectricClass = Class.forName("org.robolectric.Robolectric");
                    robolectricClass.getMethod("runUiThreadTasksIncludingDelayedTasks").invoke(null);
                } catch(InvocationTargetException e){
                    throw e.getCause();
                }
            }

            // Wait for the method call to finish
            while(!finished.get()){
                System.out.println("Waiting...");
                Thread.sleep(100);
            }

            // If the method encountered an exception, throw it
            if(throwable.get() != null) throw throwable.get();
            // Else return the result
            return result.get();

        } catch(Throwable t){
            // Wrap any throwables in a runtime exception
            throw new RuntimeException("Failed to invoke method", t);
        }
    }
}
