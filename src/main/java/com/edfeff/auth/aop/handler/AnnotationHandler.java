package com.edfeff.auth.aop.handler;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

public abstract class AnnotationHandler {
    protected Class<? extends Annotation> annotationClass;

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    public abstract void check(MethodInvocation invocation);

    public boolean support(MethodInvocation mi) {
        return null != getAnnotation(mi);
    }

    public Annotation getAnnotation(MethodInvocation mi) {
        return mi.getMethod().getAnnotation(getAnnotationClass());
    }

}
