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
//	@CustomResource // 先按名字注入，找不到名字按类型注入，也可以@Resource(name="")来显式指明name
	private PersonDao personDao; // @Autowired是按类型注入，@Autowired @Qualifier("name") 按名称注入，@Autowired(require=true)默认为true必须注入值的意思
	private String name;
	private Integer id;
	private Set<String> sets = new HashSet<>();
	private List<String> lists = new ArrayList<>();
	private Properties properties = new Properties();
	private Map<String, String> map = new HashMap<>();
	
	// 构造方法，构造器注入
	public PersonServiceImpl(PersonDao personDao, String name){
		this.personDao = personDao;
		this.name = name;
	}
	
	public PersonServiceImpl() {
		System.out.println("PersonServiceImpl初始化");
	}
	
	// 可在bean中被调用
	public void init(){
		System.out.println("初始化方法！");
	}
	
	@Override
	public void save() throws Exception {
//		System.out.println("这是save()方法");
//		System.out.println("name: "+name);
//		System.out.println("id: "+id);
//		System.out.println(lists);
//		for(String key : map.keySet()){
//			System.out.println(key+"="+map.get(key));
//		}
		personDao.add();
	}
	
	// 可在bean中配置调用
	public void destory(){
		System.out.println("关闭资源！");
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

//	@Resource // 也可以用于set方法注入
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
