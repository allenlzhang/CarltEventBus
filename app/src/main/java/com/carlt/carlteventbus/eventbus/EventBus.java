package com.carlt.carlteventbus.eventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/2/28 16:05
 */
public class EventBus {
    private static volatile EventBus                           sEventBus;
    private                 Map<Object, List<SubscribeMethod>> cacheMap;
    private                 Handler                            mHandler;
    private                 ExecutorService                    mExecutorService;

    public static EventBus getInstance() {
        if (sEventBus == null) {
            synchronized (EventBus.class) {
                if (sEventBus == null) {
                    sEventBus = new EventBus();
                }
            }
        }
        return sEventBus;
    }

    private EventBus() {
        cacheMap = new HashMap<>();
        mHandler = new Handler(Looper.getMainLooper());
        mExecutorService = Executors.newCachedThreadPool();
    }

    public void register(Object obj) {
        List<SubscribeMethod> managerList = cacheMap.get(obj);
        if (managerList == null) {
            managerList = findAnnotation(obj);
            cacheMap.put(obj, managerList);
        }

    }

    private List<SubscribeMethod> findAnnotation(Object obj) {
        List<SubscribeMethod> managerList = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            if (clazz.getName().startsWith("com.java.") || clazz.getName().startsWith("com.android.")) {
                break;
            }
            Method[] methods = clazz.getMethods();
            for (Method me : methods) {
                //获取带subscribe注解的方法，如果为null则获取下一个方法
                Subscribe subscribe = me.getAnnotation(Subscribe.class);
                if (subscribe == null) {
                    continue;
                }
                //方法返回值必须是void
                Type genericReturnType = me.getGenericReturnType();
                if (!genericReturnType.toString().equals("void")) {
                    throw new RuntimeException(me.getName() + "Method return must be void");
                }
                //方法中参数只能有一个
                Class<?>[] parameterTypes = me.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException(me.getName() + "Method can only have one parameter");
                }
                ThreadMode threadMode = subscribe.threadMode();
                SubscribeMethod manager = new SubscribeMethod(me, threadMode, parameterTypes[0]);
                managerList.add(manager);
            }
            clazz = clazz.getSuperclass();
        }


        return managerList;
    }

    public void post(final Object type) {
        Set<Object> objects = cacheMap.keySet();
        for (final Object obj : objects) {
            List<SubscribeMethod> managerList = cacheMap.get(obj);
            if (managerList != null) {
                for (final SubscribeMethod method : managerList) {
                    //判断class类型是否一致。
                    if (method.mClass.isAssignableFrom(type.getClass())) {
                        ThreadMode mode = method.mMode;
                        switch (mode) {
                            //无论子线程还是主线程最后结果都在主线程
                            case MAIN:
                                if (Looper.myLooper() == Looper.getMainLooper()) {
                                    invoke(method, obj, type);
                                } else {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            invoke(method, obj, type);
                                        }
                                    });
                                }

                                break;
                                
                                
                            //无论是子线程还是主线程，都在子线程中接收

                            case BACKGROUND:

                                if (Looper.myLooper() == Looper.getMainLooper()) {
                                    //主线程-子线程
                                    mExecutorService.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            invoke(method, obj, type);
                                        }
                                    });
                                } else {
                                    //子线程-子线程
                                    invoke(method, obj, type);
                                }
                                break;
                            default:
                                break;
                        }

                    }
                }
            }
        }

    }

    private void invoke(SubscribeMethod method, Object obj, Object type) {
        Method me = method.mMethod;
        try {
            me.invoke(obj, type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
