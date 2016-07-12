package gao.spring.service.impl;
/**
 * 使用工厂方法来创建bean对象
 * @author DELL
 *
 */
public class PersonServiceFactory {
	/**
	 * 使用静态工厂方法来创建bean对象
	 * @return
	 */
	public static PersonServiceImpl createPersonServiceBeanStatic(){
		return new PersonServiceImpl();
	}
	
	/**
	 * 使用非静态工厂方法来创建bean对象
	 * @return
	 */
	public PersonServiceImpl createPersonServiceBeanNonStatic(){
		return new PersonServiceImpl();
	} 
}
