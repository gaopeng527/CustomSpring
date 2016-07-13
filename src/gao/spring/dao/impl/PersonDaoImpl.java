package gao.spring.dao.impl;

import gao.spring.dao.PersonDao;

public class PersonDaoImpl implements PersonDao {

	@Override
	public void add() {
		System.out.println("执行PersonDaoImpl中的add()方法！");
	}

}
