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
 * �Զ���Spring����
 * @author DELL
 *
 */
public class CustomClassPathXmlApplicationContext {
	// ���XML�����ж�ȡ����bean
	private List<BeanDefinition> beanDefinitions = new ArrayList<>();
	// ���ʵ������bean����
	private Map<String, Object> singleInstance = new HashMap<>();
	
	public CustomClassPathXmlApplicationContext(String filename) {
		this.readXML(filename);
		this.instanceBeans();
		this.annotionInject(); // ע��ķ�ʽע��
		this.injectObject(); // ע��bean������
	}
	
	/**
	 * ע�ⷽʽע��ķ���
	 */
	private void annotionInject() {
		for(String beanName : singleInstance.keySet()){
			Object bean = singleInstance.get(beanName);
			if(bean != null){
				try {
					PropertyDescriptor[] ps = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
					// �����ԣ����������д���
					for(PropertyDescriptor propertyDescriptor : ps){
						Method setter = propertyDescriptor.getWriteMethod(); // ��ȡ���Ե�setter����
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
							setter.setAccessible(true); // �������˽�еķ���
							setter.invoke(bean, value); // ������ע�뵽��Ӧ��bean����
						}
					}
					// ���ֶν��д���
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
							field.setAccessible(true); // �������˽���ֶ�
							field.set(bean, value); // ע���ֶε�ֵ
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
	 * ��ȡXML�����ļ�
	 * @param filename
	 */
	private void readXML(String filename) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			URL xmlpath = this.getClass().getClassLoader().getResource(filename);
			document = saxReader.read(xmlpath);
			Map<String, String> nsMap = new HashMap<String, String>();
			nsMap.put("ns", "http://www.springframework.org/schema/beans");// ���������ռ�
			XPath xsub = document.createXPath("//ns:beans/ns:bean");// ����beans/bean��ѯ·��
			xsub.setNamespaceURIs(nsMap);// ���������ռ�
			@SuppressWarnings("unchecked")
			List<Element> beans = xsub.selectNodes(document);// ��ȡ�ĵ�������bean�ڵ�
			for (Element element : beans) {
				String id = element.attributeValue("id");// ��ȡid����ֵ
				String clazz = element.attributeValue("class"); // ��ȡclass����ֵ
				BeanDefinition beanDefine = new BeanDefinition(id, clazz);
				XPath propertysub = element.createXPath("ns:property"); // property�����·��
				propertysub.setNamespaceURIs(nsMap);// ���������ռ�
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
	 * ʵ����bean
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
	
	// ����id��ȡbeanʵ��
	public Object getBean(String id){
		return singleInstance.get(id);
	}
	
	// ע��bean������
	private void injectObject() {
		for(BeanDefinition beanDefinition : beanDefinitions){
			Object bean = singleInstance.get(beanDefinition.getId());
			if(bean != null){
				try {
					PropertyDescriptor[] ps = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
					for(PropertyDefinition propertyDefinition : beanDefinition.getPropertys()){
						for(PropertyDescriptor propertyDescriptor : ps){
							if(propertyDefinition.getName().equals(propertyDescriptor.getName())){
								Method setter = propertyDescriptor.getWriteMethod(); // ��ȡ���Ե�setter����
								if(setter != null){
									Object value = null;
									if(propertyDefinition.getRef()!=null && !"".equals(propertyDefinition.getRef().trim())){
										value = singleInstance.get(propertyDefinition.getRef());
									} else {
										value = ConvertUtils.convert(propertyDefinition.getValue(), propertyDescriptor.getPropertyType());
									}		
									setter.setAccessible(true); // �������˽�еķ���
									setter.invoke(bean, value); // ������ע�뵽��Ӧ��bean����
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
