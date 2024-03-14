package com.edfeff.auth.aop.handler;

import com.edfeff.auth.aop.NeedAuth;
import com.edfeff.auth.aop.handler.AnnotationHandler;
import com.edfeff.auth.exception.NeedAuthException;
import com.edfeff.auth.user.CurrentUserHolder;
import com.edfeff.auth.user.User;
import org.aopalliance.intercept.MethodInvocation;

public class NeedAuthAnnotationHandler extends AnnotationHandler {
    public NeedAuthAnnotationHandler() {
        setAnnotationClass(NeedAuth.class);
    }

    @Override
    public void check(MethodInvocation invocation) {
        User user = CurrentUserHolder.getUser();
        if (user == null || User.Anon == user) {
            throw new NeedAuthException("NeedAuth");
        }
    }
}
