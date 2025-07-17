package com.vivatech.ums_api_gateway.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BeanInspector {

    @Autowired
    private ApplicationContext applicationContext;

    public void inspectBeans() {
        Map<String, Object> beans = applicationContext.getBeansOfType(Object.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            String beanName = entry.getKey();
            Object bean = entry.getValue();
            System.out.println("Bean name: " + beanName + ", class: " + bean.getClass());
        }
    }

}
