package gao.spring.service.impl;
/**
 * ʹ�ù�������������bean����
 * @author DELL
 *
 */
public class PersonServiceFactory {
	/**
	 * ʹ�þ�̬��������������bean����
	 * @return
	 */
	public static PersonServiceImpl createPersonServiceBeanStatic(){
		return new PersonServiceImpl();
	}
	
	/**
	 * ʹ�÷Ǿ�̬��������������bean����
	 * @return
	 */
	public PersonServiceImpl createPersonServiceBeanNonStatic(){
		return new PersonServiceImpl();
	} 
}
