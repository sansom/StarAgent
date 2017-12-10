package com.core.jadlsoft.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.core.jadlsoft.utils.SpringBeanFactory;

public class SpringBeanFactoryInitListener implements ServletContextListener {
	
	public void contextDestroyed(ServletContextEvent event) {
	}
	public void contextInitialized(ServletContextEvent event) {
		//初始化springBeanFactory;
		SpringBeanFactory springBeanFactory = new SpringBeanFactory();
		springBeanFactory.setApplicationContext();		
	}
}
