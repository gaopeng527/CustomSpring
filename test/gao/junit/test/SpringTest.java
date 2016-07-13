package gao.junit.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import gao.spring.custom.CustomClassPathXmlApplicationContext;
import gao.spring.service.PersonService;

public class SpringTest {

	@Before
	public void setUp() throws Exception {
	}

	// �ù��췽��ʵ����bean����
	@Test
	public void instanceSpring() throws Exception {
		AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		PersonService personService = (PersonService) applicationContext.getBean("personService");
		personService.save();
		applicationContext.close();
	}
	
	// �����Զ����Spring������ȡbean����
	@Test
	public void testCustomClassPathXmlApplicationContext() throws Exception {
		CustomClassPathXmlApplicationContext applicationContext = new CustomClassPathXmlApplicationContext("applicationContext.xml");
//		PersonService personService = (PersonService) applicationContext.getBean("personService");
//		personService.save();
	}
	
	// ʹ�þ�̬����ʵ����bean����
	@Test
	public void instanceByStaticSpringFactory() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		PersonService personService2 = (PersonService) applicationContext.getBean("personService2");
		personService2.save();
	}

	// ʹ�÷Ǿ�̬����ʵ����bean����
	@Test
	public void instanceByNoStaticSpringFactory() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		PersonService personService3 = (PersonService) applicationContext.getBean("personService3");
		personService3.save();
	}
	
	// �����Ƿ��ǵ�����Ĭ��Ϊ����������������ļ���ָ��scope="prototype"���ǵ���ģʽ��ÿ�β���һ���µĶ���
	@Test
	public void singletonTest() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		PersonService personService1 = (PersonService) applicationContext.getBean("personService");
		PersonService personService2 = (PersonService) applicationContext.getBean("personService");
		System.out.println(personService1==personService2);
	}
	
}
