package gao.spring.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import gao.spring.custom.CustomResource;
import gao.spring.dao.PersonDao;
import gao.spring.service.PersonService;

public class PersonServiceImpl implements PersonService {
//	@CustomResource // �Ȱ�����ע�룬�Ҳ������ְ�����ע�룬Ҳ����@Resource(name="")����ʽָ��name
	private PersonDao personDao; // @Autowired�ǰ�����ע�룬@Autowired @Qualifier("name") ������ע�룬@Autowired(require=true)Ĭ��Ϊtrue����ע��ֵ����˼
	private String name;
	private Integer id;
	private Set<String> sets = new HashSet<>();
	private List<String> lists = new ArrayList<>();
	private Properties properties = new Properties();
	private Map<String, String> map = new HashMap<>();
	
	// ���췽����������ע��
	public PersonServiceImpl(PersonDao personDao, String name){
		this.personDao = personDao;
		this.name = name;
	}
	
	public PersonServiceImpl() {
		System.out.println("PersonServiceImpl��ʼ��");
	}
	
	// ����bean�б�����
	public void init(){
		System.out.println("��ʼ��������");
	}
	
	@Override
	public void save() throws Exception {
//		System.out.println("����save()����");
//		System.out.println("name: "+name);
//		System.out.println("id: "+id);
//		System.out.println(lists);
//		for(String key : map.keySet()){
//			System.out.println(key+"="+map.get(key));
//		}
		personDao.add();
	}
	
	// ����bean�����õ���
	public void destory(){
		System.out.println("�ر���Դ��");
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

//	@Resource // Ҳ��������set����ע��
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

	public Set<String> getSets() {
		return sets;
	}

	public void setSets(Set<String> sets) {
		this.sets = sets;
	}

	public List<String> getLists() {
		return lists;
	}

	public void setLists(List<String> lists) {
		this.lists = lists;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
}
