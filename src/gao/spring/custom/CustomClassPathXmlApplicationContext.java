package gao.spring.custom;

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
}
