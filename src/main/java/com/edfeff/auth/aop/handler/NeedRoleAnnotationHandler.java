package com.edfeff.auth.aop.handler;

import com.edfeff.auth.aop.NeedRole;
import com.edfeff.auth.aop.handler.AnnotationHandler;
import com.edfeff.auth.exception.NeedRoleException;
import com.edfeff.auth.user.CurrentUserHolder;
import com.edfeff.auth.user.User;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

public class NeedRoleAnnotationHandler extends AnnotationHandler {
    public NeedRoleAnnotationHandler() {
        setAnnotationClass(NeedRole.class);
    }

    @Override
    public void check(MethodInvocation invocation) {
        Annotation annotation = getAnnotation(invocation);
        if (annotation instanceof NeedRole) {
            NeedRole needRole = (NeedRole) annotation;
            String[] value = needRole.value();
            if (value == null || value.length == 0) {
                return;
            }
            User user = CurrentUserHolder.getUser();
            if (user == null) {
                throw new NeedRoleException("NeedRole");
            }
            if (needRole.all()) {
                for (String role : value) {
                    if (!user.getRoles().contains(role)) {
                        throw new NeedRoleException("NeedRole[" + role + "]");
                    }
                }
            } else {
                boolean hasOne = false;
                for (String role : value) {
                    if (user.getRoles().contains(role)) {
                        hasOne = true;
                        break;
                    }
                }
                if (!hasOne) {
                    throw new NeedRoleException("NeedRole");
                }
            }
        }
    }
}
