package gao.spring.service.impl;

import gao.spring.service.PersonService;

public class PersonServiceImpl implements PersonService {

	public PersonServiceImpl() {
		System.out.println("PersonServiceImpl初始化");
	}
	
	// 可在bean中被调用
	public void init(){
		System.out.println("初始化方法！");
	}
	
	@Override
	public void save() throws Exception {
		System.out.println("这是save()方法");
		
	}
	
	// 可在bean中配置调用
	public void destory(){
		System.out.println("关闭资源！");
	}
	
}
