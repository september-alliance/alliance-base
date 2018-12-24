package org.september.smartdao.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.september.smartdao.CommonDao;
import org.september.smartdao.model.Order;
import org.september.smartdao.model.Order.Direction;
import org.september.smartdao.model.Page;
import org.september.smartdao.model.ParamMap;
import org.september.smartdao.model.QueryPair;
import org.september.smartdao.test.entity.Book;
import org.september.smartdao.test.service.BookService;
import org.september.smartdao.util.SqlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test.properties")
public class SmartSqlTest {

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private BookService bookService;
	
	@Value("${spring.datasource.url}")
	private String jdbcUrl="";
	
	@Test
	public void contextLoads() {
		System.out.println("all system go");
		System.out.println("jdbcUrl="+jdbcUrl);
		System.out.println("datasource="+ dataSource.getClass());
		if(dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource){
			org.apache.tomcat.jdbc.pool.DataSource ds = (org.apache.tomcat.jdbc.pool.DataSource)dataSource;
			System.out.println("datasource="+ ds.getPoolProperties());
		}
		
	}

	@Test
	public void testListWithExampleExample() throws Exception {
		List<Book> list = commonDao.listByExample(new Book());
		System.out.println(list.size());
	}

	@Test
	public void testListWithNotNullFieldExample() throws Exception {
		Book book = new Book();
		book.setName("周易正读");
		book.setWords(100);
		List<Book> list = commonDao.listByExample(book);
		System.out.println(list.size());
	}

	/**
	 * @author Guojiangtao
	 * @Description:测试CommonDao类中的save（）方法
	 * @date 2017/12/25
	 */
	@Test
	public void testSaveInCommondao() {
		Book book = new Book();
		book.setName("js");
		book.setWords(199);
		book.setAuthorId(2L);
		commonDao.save(book);
		System.out.println("book id is "+book.getId());
		Assert.notNull(book.getId(), "id should not be null after saved");
	}

	@Test
	public void testTrancRollback(){
		bookService.saveBookError1();
	}
	
	@Test
	public void testNoTrancAnno(){
		bookService.saveBookError2();
	}
	
	/**
	 * @author Guojiangtao
	 * @Description:测试CommonDao类中的update（）方法
	 * @date 2017/12/25
	 */
	@Test
	public void testUpdateInCommondao() {
		Book book = new Book();
		book.setId(19L);
		book.setWords(200);
		book.setName("1231230");
		commonDao.update(book);
	}

	/**
	 * @author Guojiangtao
	 * @Description: 测试CommonDao类中的deleteByIds（）方法
	 * @date 2017/12/25
	 */
	@Test
	public void testdeleteByIdsInCommondao() {
		List<Object> ids = new ArrayList<>();
		ids.add(4477L);
		ids.add(4478L);
		ids.add(4479L);
		commonDao.deleteByIds(Book.class, ids);
	}

	/**
	 * @author Guojiangtao
	 * @Description: 测试CommonDao类中的delete（）方法
	 * @date 2017/12/25
	 */
	@Test
	public void testDeleteInCommondao() {
		Book book = new Book();
		book.setId(4483L);
		commonDao.delete(book);
	}

	/**
	 * @author Guojiangtao
	 * @Description: 测试CommonDao类中的get（）方法
	 * @date 2017/12/25
	 */
	@Test
	public void testGetInCommonDao() {
		Book book = new Book();
		book.setName("js");
		book.setWords(199);
		book.setAuthorId(2L);
		commonDao.save(book);
		
		Book po = commonDao.get(Book.class, book.getId());
		System.out.println(po.getName());
		Assert.state("js".equals(po.getName()), "根据id查询数据错误");
	}

	/**
	 * @author Guojiangtao
	 * @Description:测试CommonDao类中的getByExample（）方法
	 * @date 2017/12/25
	 */
	@Test
	public void testGetByExampleInCommonDao() {
		Book book = commonDao.getByExample(new Book());
		System.out.println("书名：" + book.getName());
	}

	/**
	 * @author Guojiangtao
	 * @Description:测试CommonDao类中的getByExample（）方法
	 * @date 2017/12/25
	 */
	@Test
	public void testListByExampleInCommonDao() {
		String idColumn = SqlHelper.getIdColumnOfClass(Book.class);
		Field[] declaredFields = Book.class.getDeclaredFields();
		String columnName = SqlHelper.getColumnName(declaredFields[2]);
		Direction direction1 = Direction.fromString("asc");
		Direction direction2 = Direction.fromString("desc");
		Order order1=new Order(idColumn,direction1);
		Order order2=new Order(columnName,direction2);
		List<Order> orders=new ArrayList<>();
		orders.add(order1);
		orders.add(order2);
		List<Book> list = commonDao.listByExample(new Book(), orders);
		for (Object object:list){
			System.out.println(object.toString());
		}
	}
	/**
	 * @author Guojiangtao
	 * @Description:测试CommonDao类中的listByParams（）方法 参数为String statementId, ParamMap paramMap
	 * @date 2017/12/25
	 */
	@Test
	public void testListByParamsInCommonDao() {
		// test 目录下架mapper文件
		String tableName = SqlHelper.getTableName(Book.class);
		ParamMap pm = new ParamMap();
		pm.put("tableName", tableName);
		pm.put("idColumn", SqlHelper.getIdColumnOfClass(Book.class));
		pm.put("idValue", 15);
		List<Map<String, Object>> results = commonDao.listByParams("CommonEntityMapper.getById", pm);
		System.out.println(results.get(0));
	}

	/**
	 * @author Guojiangtao
	 * @Description: 测试CommonDao类中的findOne（）方法
	 * @date 2017/12/25
	 */
	@Test
	public void testFindOneInCommonDao() {
		String tableName = SqlHelper.getTableName(Book.class);
		ParamMap pm = new ParamMap();
		pm.put("tableName", tableName);
		pm.put("idColumn", SqlHelper.getIdColumnOfClass(Book.class));
		pm.put("idValue", 15);
		Map<String, Object> result = commonDao.findOne("CommonEntityMapper.getById", pm);
		System.out.println(result);
	}


	/**
	 * @author Guojiangtao
	 * @Description: 测试CommonDao类中的findPageByExample（）方法 参数为Class<T> clazz,String statementId, ParamMap
	 * paramMap
	 * @date 2017/12/25
	 */
	@Test
	public void testFindPageByExampleInCommonDao() {
		Page<Book> page = new Page<Book>();
		page.setCurrentPage(2);
		page.setPageSize(15);
		page= commonDao.findPageByExample(Book.class, page, new Book());
		System.out.println("result:"+ page.getTotalResult());
	}

	/**
	 * @author Guojiangtao
	 * @Description: 测试CommonDao类中的findPageByParams（）方法
	 * @date 2017/12/25
	 */
	@Test
	public void testFindPageByParamsInCommonDao() {
		String tableName = SqlHelper.getTableName(Book.class);
		ParamMap pm = new ParamMap();
		List<QueryPair> queryPairs = SqlHelper.getQueryPairs(new Book());
		pm.put("tableName", tableName);
		pm.put("queryPairList", queryPairs);
		Page<Book> page = new Page<Book>();
		page.setCurrentPage(1);
		page.setPageSize(15);
		page = commonDao
				.findPageByParams(Book.class, page, "CommonEntityMapper.listByExample", pm);
		System.out.println("pageResult:" + page.getResult().size());
	}

	/**
	 * @author Guojiangtao
	 * @Description: 测试CommonDao类中的findPageByParams（）方法,返回类型是Map<String, Object>
	 * @date 2017/12/25
	 */
	@Test
	public void testFindPageByParamsWithMapResultInCommonDao() {
		List<QueryPair> queryPairs = SqlHelper.getQueryPairs(new Book());
		ParamMap pm = new ParamMap();
		pm.put("tableName", SqlHelper.getTableName(Book.class));
		pm.put("queryPairList", queryPairs);
		Page page = new Page();
		page.setCurrentPage(1);
		page.setPageSize(15);
		Page pageResult = commonDao.findPageByParams(page, "CommonEntityMapper.listByExample", pm);
		System.out.println("pageResult:" + pageResult.getResult().size());
	}

	/**
	 * @author Guojiangtao
	 * @Description: 测试CommonDao类中的 execute（）方法
	 * @date 2017/12/25
	 */
	@Test
	public void testExecuteInCommonDao() {
		List<Object> ids = new ArrayList<>();
		ids.add(204);
		ids.add(205);
		ids.add(206);
		ParamMap pm = new ParamMap();
		pm.put("idColumn", SqlHelper.getIdColumnOfClass(Book.class));
		pm.put("tableName", SqlHelper.getTableName(Book.class));
		pm.put("ids", ids);
		int result = commonDao.execute("CommonEntityMapper.deleteByIds", pm);
		System.out.println("result:" + result);
	}

	/**
	 * @author Guojiangtao
	 * @Description: 测试CommonDao类中的 updateByField（）方法 paramMap
	 * @date 2017/12/25
	 */
	@Test
	public void testupdateByFieldInCommonDao() {
		Book book = new Book();
		book.setWords(100);
		book.setPrice(22f);
		int result = commonDao.updateByField(Book.class, "authorId", 2L, book,false);
		System.out.println("result:" + result);
	}

	/**
	 * @author Guojiangtao
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @Description: 测试CommonDao类中的 batchInsert（）方法 paramMap
	 * @date 2017/12/25
	 */
	@Test
	public void testBatchInsertInCommonDao() throws IllegalArgumentException, IllegalAccessException {
		Book book = new Book();
		Book book1 = new Book();
		Book book2 = new Book();
		book.setName("jsp");
		book.setWords(299);
		book.setAuthorId(2L);
		book1.setName("jspjsp");
		book1.setWords(399);
		book1.setAuthorId(2L);
		book2.setName("jspjspjsp");
		book2.setWords(499);
		book2.setAuthorId(2L);
		List<Book> list = new ArrayList<>();
		list.add(book);
		list.add(book1);
		list.add(book2);
		int result = commonDao.batchInsert(Book.class, list);
		System.out.println("result:" + result);
	}

}
