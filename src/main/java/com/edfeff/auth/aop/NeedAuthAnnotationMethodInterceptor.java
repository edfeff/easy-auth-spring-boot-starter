package com.edfeff.auth.aop;

import com.edfeff.auth.aop.handler.AnnotationHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.List;

public class NeedAuthAnnotationMethodInterceptor implements MethodInterceptor {
    private final List<AnnotationHandler> annotationHandlers;

    public NeedAuthAnnotationMethodInterceptor(List<AnnotationHandler> annotationHandlers) {
        this.annotationHandlers = annotationHandlers;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        check(invocation);
        return doInvoke(invocation);
    }

    private Object doInvoke(MethodInvocation invocation) throws Throwable {
        return invocation.proceed();
    }

    private void check(MethodInvocation invocation) {
        for (AnnotationHandler annotationHandler : annotationHandlers) {
            if (annotationHandler.support(invocation)) {
                annotationHandler.check(invocation);
            }
        }
    }
}
