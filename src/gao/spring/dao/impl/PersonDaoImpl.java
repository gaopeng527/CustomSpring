package gao.spring.dao.impl;

import org.springframework.stereotype.Repository;

import gao.spring.dao.PersonDao;

@Repository
public class PersonDaoImpl implements PersonDao {

	@Override
	public void add() {
		System.out.println("ִ��PersonDaoImpl�е�add()������");
	}

}
