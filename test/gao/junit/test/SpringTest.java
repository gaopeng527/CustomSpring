package gao.junit.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import gao.spring.custom.CustomClassPathXmlApplicationContext;
import gao.spring.service.PersonService;

public class SpringTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void instanceSpring() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		PersonService personService = (PersonService) applicationContext.getBean("personService");
		personService.save();
	}
	
	@Test
	public void testCustomClassPathXmlApplicationContext() throws Exception {
		CustomClassPathXmlApplicationContext applicationContext = new CustomClassPathXmlApplicationContext("applicationContext.xml");
		PersonService personService = (PersonService) applicationContext.getBean("personService");
		personService.save();
	}

}
