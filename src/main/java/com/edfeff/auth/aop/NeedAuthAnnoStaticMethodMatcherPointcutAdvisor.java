package com.edfeff.auth.aop;

import com.edfeff.auth.aop.handler.AnnotationHandler;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NeedAuthAnnoStaticMethodMatcherPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {
    private final List<Class<? extends Annotation>> ANNOTATIONS = new ArrayList<>();

    public NeedAuthAnnoStaticMethodMatcherPointcutAdvisor(List<AnnotationHandler> annotationHandlers) {
        for (AnnotationHandler annotationHandler : annotationHandlers) {
            Class<? extends Annotation> annotationClass = annotationHandler.getAnnotationClass();
            ANNOTATIONS.add(annotationClass);
        }
        setAdvice(new NeedAuthAnnotationMethodInterceptor(annotationHandlers));
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        for (Class<? extends Annotation> annotation : ANNOTATIONS) {
            if (method.getAnnotation(annotation) != null) {
                return true;
            }
        }
        return false;
    }
}
