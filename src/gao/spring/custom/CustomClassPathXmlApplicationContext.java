package gao.spring.custom;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
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
		this.injectObject(); // ע��bean������
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
//					System.out.println("name="+propertyName);
//					System.out.println("ref="+propertyRef);
					PropertyDefinition propertyDefinition = new PropertyDefinition(propertyName, propertyRef);
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
									Object value = singleInstance.get(propertyDefinition.getRef());
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
