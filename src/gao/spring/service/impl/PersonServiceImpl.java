package gao.spring.service.impl;

import gao.spring.service.PersonService;

public class PersonServiceImpl implements PersonService {

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
		
	}
	
	// ����bean�����õ���
	public void destory(){
		System.out.println("�ر���Դ��");
	}
	
}
