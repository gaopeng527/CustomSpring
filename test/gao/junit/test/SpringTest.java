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

	// 用构造方法实例化bean对象
	@Test
	public void instanceSpring() throws Exception {
		AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		PersonService personService = (PersonService) applicationContext.getBean("personService");
		personService.save();
		applicationContext.close();
	}
	
	// 测试自定义的Spring容器获取bean对象
	@Test
	public void testCustomClassPathXmlApplicationContext() throws Exception {
		CustomClassPathXmlApplicationContext applicationContext = new CustomClassPathXmlApplicationContext("applicationContext.xml");
//		PersonService personService = (PersonService) applicationContext.getBean("personService");
//		personService.save();
	}
	
	// 使用静态工厂实例化bean对象
	@Test
	public void instanceByStaticSpringFactory() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		PersonService personService2 = (PersonService) applicationContext.getBean("personService2");
		personService2.save();
	}

	// 使用非静态工厂实例化bean对象
	@Test
	public void instanceByNoStaticSpringFactory() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		PersonService personService3 = (PersonService) applicationContext.getBean("personService3");
		personService3.save();
	}
	
	// 测试是否是单例（默认为单例，如果在配置文件中指定scope="prototype"则不是单例模式，每次产生一个新的对象）
	@Test
	public void singletonTest() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		PersonService personService1 = (PersonService) applicationContext.getBean("personService");
		PersonService personService2 = (PersonService) applicationContext.getBean("personService");
		System.out.println(personService1==personService2);
	}
	
}
