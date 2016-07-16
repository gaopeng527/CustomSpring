package gao.spring.dao.impl;

import org.springframework.stereotype.Repository;

import gao.spring.dao.PersonDao;

@Repository
public class PersonDaoImpl implements PersonDao {

	@Override
	public void add() {
		System.out.println("执行PersonDaoImpl中的add()方法！");
	}

}
