package com.deftech.viewtils;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


public class MethodRunner<I> {
    private final String methodName;
    private List<Object> args = new ArrayList<Object>();
    private List<Class<?>> paramTypes = new ArrayList<Class<?>>();
    private I instance;
    private Class<I> instanceClass;
    private boolean withRobolectric;


    MethodRunner(String methodName, I instance, Class<I> instanceClass, boolean withRobolectric){
        this.methodName = methodName;
        this.instance = instance;
        this.instanceClass = instanceClass;
        this.withRobolectric = withRobolectric;
    }

    public MethodRunner<I> withParameters(Param<?>... params){
        for (Param<?> param : params) {
            args.add(param.getInstance());
            paramTypes.add(param.getInstanceClass());
        }
        return this;
    }

    public <P> MethodRunner<I> withParameter(P instance, Class<P> instanceClass){
        args.add(instance);
        paramTypes.add(instanceClass);
        return this;
    }

    public static final class Param<P> {
        private final Class<P> instanceClass;
        private final P instance;

        private Param(P instance, Class<P> instanceClass){
            this.instance = instance;
            this.instanceClass = instanceClass;
        }

        public static <P> Param<P> makeParam(P instance, Class<P> instanceClass){
            return new Param<P>(instance, instanceClass);
        }

        public P getInstance(){ return instance; }
        public Class<P> getInstanceClass(){ return instanceClass; }
    }


    public <T> T returning(Class<T> returnType) {
        return returnType.cast(invoke());
    }

    public void returningNothing() {
        invoke();
    }


    private Object invoke() {
        try {
            Handler handler = new Handler(Looper.getMainLooper());

            Class<?>[] pTypes = new Class<?>[paramTypes.size()];
            pTypes = paramTypes.toArray(pTypes);
            final Method method = instanceClass.getMethod(methodName, pTypes);

            final I i = instance;
            final Object[] a = args.toArray();

            final AtomicReference<Object> result = new AtomicReference<Object>();
            final AtomicReference<Throwable> throwable = new AtomicReference<Throwable>();
            final AtomicBoolean finished = new AtomicBoolean();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        result.set(method.invoke(i,a));
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