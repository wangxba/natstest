/**
 *  (c) Copyright 北京并行科技有限公司 
 *  http://www.paratera.com
 *  ApplicationContextUtils.java
 *  add by gaoqi
 */
package natstest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

/**
 * 获得Spring上下文单例工具类
 * @author gaoqi
 */
public class ApplicationContextUtils implements BeanFactoryAware,ApplicationContextAware {
 
    private static BeanFactory beanFactory;
    
    private static ApplicationContext applicationContext;
 
    @Override
	@SuppressWarnings("static-access")
	public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
 
    public static Object getBean(String name) {
        return beanFactory.getBean(name);
    }
 
    @SuppressWarnings("unchecked")
	public static <T> T getBean(String name, Class<T> clazz) {
        return (T)beanFactory.getBean(name);
    }
 
    public static boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		applicationContext = context;
	}
	
	public static void publish(ApplicationEvent event) {
	    applicationContext.publishEvent(event);
	}
	
	/**
	 * 通过Class获取Bean，例如通过接口的名称来获取Bean
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}
}

