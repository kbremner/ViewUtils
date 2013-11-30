package com.deftech.viewtils;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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


    public <I> MethodRunner(String methodName, I instance, Class<I> instanceClass){
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

            handler.post(new Runnable() {
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
            });


            if(withRobolectric){
                try {
                    Class<?> robolectricClass = Class.forName("org.robolectric.Robolectric");
                    robolectricClass.getMethod("runUiThreadTasksIncludingDelayedTasks").invoke(null);
                } catch(InvocationTargetException e){
                    throw e.getCause();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }

            while(!finished.get()){
                System.out.println("Waiting...");
                Thread.sleep(100);
            }

            if(throwable.get() != null) throw throwable.get();
            return result.get();

        } catch(Throwable t){
            throw new RuntimeException("Failed to invoke method", t);
        }
    }
}
