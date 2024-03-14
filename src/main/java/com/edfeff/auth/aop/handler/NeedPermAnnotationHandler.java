package com.edfeff.auth.aop.handler;

import com.edfeff.auth.aop.NeedPerm;
import com.edfeff.auth.aop.handler.AnnotationHandler;
import com.edfeff.auth.exception.NeedPermException;
import com.edfeff.auth.user.CurrentUserHolder;
import com.edfeff.auth.user.User;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

public class NeedPermAnnotationHandler extends AnnotationHandler {
    public NeedPermAnnotationHandler() {
        setAnnotationClass(NeedPerm.class);
    }

    @Override
    public void check(MethodInvocation invocation) {
        Annotation annotation = getAnnotation(invocation);
        if (annotation instanceof NeedPerm) {
            NeedPerm needPerm = (NeedPerm) annotation;
            String[] value = needPerm.value();
            if (value == null || value.length == 0) {
                return;
            }
            User user = CurrentUserHolder.getUser();
            if (user == null) {
                throw new NeedPermException("NeedPerm");
            }
            if (needPerm.all()) {
                for (String perm : value) {
                    if (!user.perms().contains(perm)) {
                        throw new NeedPermException("NeedPerm[" + perm + "]");
                    }
                }
            } else {
                boolean hasOne = false;
                for (String perm : value) {
                    if (user.perms().contains(perm)) {
                        hasOne = true;
                        break;
                    }
                }
                if (!hasOne) {
                    throw new NeedPermException("NeedPerm");
                }
            }
        }
    }
}
