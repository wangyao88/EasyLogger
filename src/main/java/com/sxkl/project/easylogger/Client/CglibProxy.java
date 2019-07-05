package com.sxkl.project.easylogger.Client;

import com.sxkl.project.easylogger.common.LoggerLevelEnum;
import com.sxkl.project.easylogger.config.Configer;
import com.sxkl.project.easylogger.core.EasyLogger;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxy implements MethodInterceptor {

    public Object creatProxyedObject(Class<?> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        try {
            long start = System.currentTimeMillis();
            Object result = methodProxy.invokeSuper(target, args);
            long end = System.currentTimeMillis();
            EasyLogger.doProxy(target, methodProxy, Configer.getInstance().needWriteInfoToConsole(), LoggerLevelEnum.INFO, null, "操作成功");
            return result;
        } catch (Throwable throwable) {
            EasyLogger.doProxy(target, methodProxy, Configer.getInstance().needWriteErrorToConsole(), LoggerLevelEnum.ERROR, throwable, "操作失败");
            throw throwable;
        }
    }
}
