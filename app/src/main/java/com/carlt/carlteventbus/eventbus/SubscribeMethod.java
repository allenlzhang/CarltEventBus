package com.carlt.carlteventbus.eventbus;

import java.lang.reflect.Method;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/2/28 16:19
 */
class SubscribeMethod {
    public Method     mMethod;
    public ThreadMode mMode;
    public Class<?>   mClass;

    public SubscribeMethod(Method method, ThreadMode mode, Class<?> aClass) {
        mMethod = method;
        mMode = mode;
        mClass = aClass;
    }
}
