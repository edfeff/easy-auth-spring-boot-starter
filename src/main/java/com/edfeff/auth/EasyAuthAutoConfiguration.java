package com.edfeff.auth;

import com.edfeff.auth.aop.NeedAuthAnnoStaticMethodMatcherPointcutAdvisor;
import com.edfeff.auth.aop.handler.AnnotationHandler;
import com.edfeff.auth.aop.handler.NeedAuthAnnotationHandler;
import com.edfeff.auth.aop.handler.NeedPermAnnotationHandler;
import com.edfeff.auth.aop.handler.NeedRoleAnnotationHandler;
import com.edfeff.auth.exception.AuthException;
import com.edfeff.auth.exception.AuthExceptionHandler;
import com.edfeff.auth.exception.ExceptionControllerAdvice;
import com.edfeff.auth.filter.AuthFilter;
import com.edfeff.auth.user.InMemoryUserService;
import com.edfeff.auth.user.UserService;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "easy.auth.enabled", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class EasyAuthAutoConfiguration {

    /*------------------------------------------------*/
    /*------------！！必须重写用户服务替换业务逻辑！！-----*/
    /*------------------------------------------------*/
    @Bean
    @ConditionalOnMissingBean
    public UserService userService() {
        return new InMemoryUserService();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthExceptionHandler defaultAuthExceptionHandler() {
        return new AuthExceptionHandler() {
            @Override
            public ResponseEntity<?> handleAuthException(AuthException exception) {
                String message = exception.getMessage();
                ResponseEntity<String> res = ResponseEntity.status(403).body(message);
                return res;
            }
        };
    }

    @Bean
    public ExceptionControllerAdvice exceptionControllerAdvice(AuthExceptionHandler authExceptionHandler) {
        return new ExceptionControllerAdvice(authExceptionHandler);
    }


    /*--------------------------------------*/
    /*------------可以进行简单过滤器的配置-----*/
    /*--------------------------------------*/
    @Bean
    @ConditionalOnMissingBean
    public AuthFilter authFilter(UserService userService) {
        AuthFilter authFilter = new AuthFilter();
        authFilter.setUserService(userService);
        return authFilter;
    }

    /*--------------------------------------*/
    /*------------不需要重写下面的------------*/
    /*--------------------------------------*/
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    /*--------------------------------------*/
    /*------------不需要重写下面的------------*/
    /*--------------------------------------*/
    @Bean
    public NeedAuthAnnoStaticMethodMatcherPointcutAdvisor easyAuthStaticMethodMatcherPointcutAdvisor(List<AnnotationHandler> annotationHandlers) {
        return new NeedAuthAnnoStaticMethodMatcherPointcutAdvisor(annotationHandlers);
    }

    /*--------------------------------------*/
    /*------------不需要重写下面的------------*/
    /*--------------------------------------*/
    @Bean
    public FilterRegistrationBean<AuthFilter> easyAuthFilterFilterRegistrationBean(AuthFilter authFilter) {
        FilterRegistrationBean<AuthFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(authFilter);
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public NeedAuthAnnotationHandler needAuthAnnotationHandler() {
        return new NeedAuthAnnotationHandler();
    }

    @Bean
    public NeedPermAnnotationHandler needPermAnnotationHandler() {
        return new NeedPermAnnotationHandler();
    }

    @Bean
    public NeedRoleAnnotationHandler needRoleAnnotationHandler() {
        return new NeedRoleAnnotationHandler();
    }
}
