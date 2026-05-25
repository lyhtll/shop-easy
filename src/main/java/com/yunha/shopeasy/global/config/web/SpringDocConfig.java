package com.yunha.shopeasy.global.config.web;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig implements BeanDefinitionRegistryPostProcessor {
    
    private static final String QUERYDSL_CUSTOMIZER_BEAN = "queryDslQuerydslPredicateOperationCustomizer";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (registry.containsBeanDefinition(QUERYDSL_CUSTOMIZER_BEAN)) {
            registry.removeBeanDefinition(QUERYDSL_CUSTOMIZER_BEAN);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}