package gao.spring.custom;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.StandardEmitterMBean;

import org.apache.commons.beanutils.ConvertUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.support.ServletContextAttributeExporter;
/**
 * 自定义Spring容器
 * @author DELL
 *
 */
public class CustomClassPathXmlApplicationContext {
	// 存放XML中所有读取到的bean
	private List<BeanDefinition> beanDefinitions = new ArrayList<>();
	// 存放实例化的bean对象
	private Map<String, Object> singleInstance = new HashMap<>();
	
	public CustomClassPathXmlApplicationContext(String filename) {
		this.readXML(filename);
		this.instanceBeans();
		this.annotionInject(); // 注解的方式注入
		this.injectObject(); // 注入bean的属性
	}
	
	/**
	 * 注解方式注入的方法
	 */
	private void annotionInject() {
		for(String beanName : singleInstance.keySet()){
			Object bean = singleInstance.get(beanName);
			if(bean != null){
				try {
					PropertyDescriptor[] ps = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
					// 对属性（方法）进行处理
					for(PropertyDescriptor propertyDescriptor : ps){
						Method setter = propertyDescriptor.getWriteMethod(); // 获取属性的setter方法
						if(setter != null && setter.isAnnotationPresent(CustomResource.class)){
							CustomResource resource = setter.getAnnotation(CustomResource.class);
							Object value = null;
							if(resource.name() != null && !"".equals(resource.name())){
								value = singleInstance.get(resource.name());
							} else {
								value = singleInstance.get(propertyDescriptor.getName());
								if(value == null){
									for(String key : singleInstance.keySet()){
										if(propertyDescriptor.getPropertyType().isAssignableFrom(singleInstance.get(key).getClass())){
											value = singleInstance.get(key);
											break;
										}
									}
								}
							}
							setter.setAccessible(true); // 允许访问私有的方法
							setter.invoke(bean, value); // 把属性注入到对应的bean当中
						}
					}
					// 对字段进行处理
					Field[] fields = bean.getClass().getDeclaredFields();
					for(Field field : fields){
						if(field.isAnnotationPresent(CustomResource.class)){
							CustomResource resource = field.getAnnotation(CustomResource.class);
							Object value = null;
							if(resource.name() != null && !"".equals(resource.name())){
								value = singleInstance.get(resource.name());
							} else {
								value = singleInstance.get(field.getName());
								if(value == null){
									for(String key : singleInstance.keySet()){
										if(field.getType().isAssignableFrom(singleInstance.get(key).getClass())){
											value = singleInstance.get(key);
											break;
										}
									}
								}
							}
							field.setAccessible(true); // 允许访问私有字段
							field.set(bean, value); // 注入字段的值
						}
					}
					
				} catch (IntrospectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * 读取XML配置文件
	 * @param filename
	 */
	private void readXML(String filename) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			URL xmlpath = this.getClass().getClassLoader().getResource(filename);
			document = saxReader.read(xmlpath);
			Map<String, String> nsMap = new HashMap<String, String>();
			nsMap.put("ns", "http://www.springframework.org/schema/beans");// 加入命名空间
			XPath xsub = document.createXPath("//ns:beans/ns:bean");// 创建beans/bean查询路径
			xsub.setNamespaceURIs(nsMap);// 设置命名空间
			@SuppressWarnings("unchecked")
			List<Element> beans = xsub.selectNodes(document);// 获取文档下所有bean节点
			for (Element element : beans) {
				String id = element.attributeValue("id");// 获取id属性值
				String clazz = element.attributeValue("class"); // 获取class属性值
				BeanDefinition beanDefine = new BeanDefinition(id, clazz);
				XPath propertysub = element.createXPath("ns:property"); // property的相对路径
				propertysub.setNamespaceURIs(nsMap);// 设置命名空间
				List<Element> propertys = propertysub.selectNodes(element);
				for(Element property : propertys){
					String propertyName = property.attributeValue("name");
					String propertyRef = property.attributeValue("ref");
					String propertyValue = property.attributeValue("value");
//					System.out.println("name="+propertyName);
//					System.out.println("ref="+propertyRef);
					PropertyDefinition propertyDefinition = new PropertyDefinition(propertyName, propertyRef, propertyValue);
					beanDefine.getPropertys().add(propertyDefinition);
				}
				beanDefinitions.add(beanDefine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 实例化bean
	 */
	private void instanceBeans() {
		for(BeanDefinition beanDefinition:beanDefinitions){
			try {
				if(beanDefinition.getClassName() != null && !"".equals(beanDefinition.getClassName().trim())){
					Object object = Class.forName(beanDefinition.getClassName()).newInstance();
					singleInstance.put(beanDefinition.getId(), object);
				}				
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	// 根据id获取bean实例
	public Object getBean(String id){
		return singleInstance.get(id);
	}
	
	// 注入bean的属性
	private void injectObject() {
		for(BeanDefinition beanDefinition : beanDefinitions){
			Object bean = singleInstance.get(beanDefinition.getId());
			if(bean != null){
				try {
					PropertyDescriptor[] ps = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
					for(PropertyDefinition propertyDefinition : beanDefinition.getPropertys()){
						for(PropertyDescriptor propertyDescriptor : ps){
							if(propertyDefinition.getName().equals(propertyDescriptor.getName())){
								Method setter = propertyDescriptor.getWriteMethod(); // 获取属性的setter方法
								if(setter != null){
									Object value = null;
									if(propertyDefinition.getRef()!=null && !"".equals(propertyDefinition.getRef().trim())){
										value = singleInstance.get(propertyDefinition.getRef());
									} else {
										value = ConvertUtils.convert(propertyDefinition.getValue(), propertyDescriptor.getPropertyType());
									}		
									setter.setAccessible(true); // 允许访问私有的方法
									setter.invoke(bean, value); // 把属性注入到对应的bean当中
								}		
								break;
							}
						}
					}
				} catch (IntrospectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
