package gao.spring.service.impl;

import gao.spring.dao.PersonDao;
import gao.spring.service.PersonService;

public class PersonServiceImpl implements PersonService {
	private PersonDao personDao;
	private String name;
	private Integer id;
	
	public PersonServiceImpl() {
		System.out.println("PersonServiceImpl��ʼ��");
	}
	
	// ����bean�б�����
	public void init(){
		System.out.println("��ʼ��������");
	}
	
	@Override
	public void save() throws Exception {
		System.out.println("����save()����");
		System.out.println("name: "+name);
		System.out.println("id: "+id);
		personDao.add();
	}
	
	// ����bean�����õ���
	public void destory(){
		System.out.println("�ر���Դ��");
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
