package org.september.smartdao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.september.core.exception.BusinessException;
import org.september.smartdao.anno.AutoIncrease;
import org.september.smartdao.anno.OptimisticLock;
import org.september.smartdao.anno.Sequence;
import org.september.smartdao.datasource.SmartDatasourceHolder;
import org.september.smartdao.model.Order;
import org.september.smartdao.model.Page;
import org.september.smartdao.model.ParamMap;
import org.september.smartdao.model.QueryPair;
import org.september.smartdao.util.ReflectHelper;
import org.september.smartdao.util.SqlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Component
public class CommonDao {

	@Autowired
	SqlSessionTemplate sqlSessionTemplate;

	/**
	 * @author yexinzhou
	 * @Description:增加数据到数据库
	 * @date 2017/12/22
	 */
	public void save(Object entity) {
		try {
			SmartDatasourceHolder.switchToWrite();
			List<QueryPair> queryPairList = SqlHelper.getQueryPairs(entity);
			ParamMap pm = new ParamMap();
			pm.put("columnList", queryPairList);
			pm.put("tableName", SqlHelper.getTableName(entity.getClass()));
			String keyName = SqlHelper.getIdOfClass(entity.getClass()).getName();
			Sequence seq = SqlHelper.getIdOfClass(entity.getClass()).getAnnotation(Sequence.class);
			AutoIncrease auto = SqlHelper.getIdOfClass(entity.getClass()).getAnnotation(AutoIncrease.class);
			if (seq != null) {
				pm.put("selectKey", seq.selectKey());
				sqlSessionTemplate.insert("CommonEntityMapper.insertEntityWithSequence", pm);
				long id = (long) pm.get("id");
				BeanUtils.setProperty(entity, keyName, id);
			} else if (auto != null) {
				sqlSessionTemplate.insert("CommonEntityMapper.insertEntityAutoIncrease", pm);
				long id = (long) pm.get("id");
				BeanUtils.setProperty(entity, keyName, id);
			} else {
				sqlSessionTemplate.insert("CommonEntityMapper.insertEntityWithId", pm);
			}

		} catch (Exception e) {
			throw new BusinessException("保存数据失败", e);
		}
	}

	/**
	 * @author yexinzhou
	 * @Description:根据id(由注解 @Id 决定 ) 更新entity中不为null的值
	 * @date 2017/12/22
	 */
	public int update(Object entity) {
		try {
			SmartDatasourceHolder.switchToWrite();
			// 得到类中属性id
			Field id = SqlHelper.getIdOfEntity(entity);
			id.setAccessible(true);
			if (id != null) {
				Object val = id.get(entity);
				if (val == null) {
					throw new BusinessException("id can't be null when update");
				}
				return this.updateByField(entity.getClass(), id.getName(), val, entity, false);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new BusinessException("更新数据失败", e);
		}
		return 0;
	}

	public void updateWithNullFields(Object entity) {
		try {
			SmartDatasourceHolder.switchToWrite();
			// 得到类中属性id
			Field id = SqlHelper.getIdOfEntity(entity);
			id.setAccessible(true);
			if (id != null) {
				Object val = id.get(entity);
				if (val == null) {
					throw new RuntimeException("id can't be null when update");
				}
				this.updateByField(entity.getClass(), id.getName(), val, entity, true);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new BusinessException("更新数据失败", e);
		}
	}

	/**
	 * @author yexinzhou
	 * @Description:根据id批量删除表数据,适用于小数量级的批量删除，如一次删除50条以内的
	 * @date 2017/12/22
	 */
	public int deleteByIds(Class<?> clazz, List<Object> ids) {
		SmartDatasourceHolder.switchToWrite();
		ParamMap pm = new ParamMap();
		pm.put("idColumn", SqlHelper.getIdColumnOfClass(clazz));
		pm.put("tableName", SqlHelper.getTableName(clazz));
		pm.put("ids", ids);
		return sqlSessionTemplate.delete("CommonEntityMapper.deleteByIds", pm);
	}

	/**
	 * @author yexinzhou
	 * @Description:根据id(由注解 @Id 决定 ) 删除表数据
	 * @date 2017/12/22
	 */
	public void delete(Object entity) {
		SmartDatasourceHolder.switchToWrite();
		ParamMap pm = new ParamMap();
		Field id = SqlHelper.getIdOfEntity(entity);
		if (id != null) {
			Object val = null;
			try {
				id.setAccessible(true);
				val = id.get(entity);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (val == null) {
				throw new RuntimeException("id can't be null when delete");
			}
			pm.put("idColumn", SqlHelper.getIdColumnOfEntity(entity));
			pm.put("idValue", val);
		} else {
			throw new RuntimeException("id field not found for class " + entity.getClass());
		}
		pm.put("tableName", SqlHelper.getTableName(entity.getClass()));
		sqlSessionTemplate.delete("CommonEntityMapper.deleteById", pm);
	}

	public <T> T get(Class<T> clazz, Object id) {
		if (id == null) {
			return null;
		}
		SmartDatasourceHolder.switchToRead();
		String tableName = SqlHelper.getTableName(clazz);
		ParamMap pm = new ParamMap();
		pm.put("tableName", tableName);
		pm.put("idColumn", SqlHelper.getIdColumnOfClass(clazz));
		pm.put("idValue", id);
		Map<String, Object> map = sqlSessionTemplate.selectOne("CommonEntityMapper.getById", pm);
		T result = ReflectHelper.transformMapToEntity(clazz, map);
		return result;
	}

	/**
	 * 与listByExample方式一样，单返回单条数据
	 */
	public <T> T getByExample(T example) {
		if (example == null) {
			return null;
		}
		List<T> list = listByExample(example);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public <T> List<T> listByExample(T example) {
		return listByExample(example, null);
	}

	/**
	 * @author yexinzhou
	 * @Description:意义与 <code>listByExample(Object vo)</code>一样，增加了对排序的支持
	 * @date 2017/12/22
	 */
	public <T> List<T> listByExample(T vo, List<Order> orders) {
		SmartDatasourceHolder.switchToRead();
		String tableName = SqlHelper.getTableName(vo.getClass());
		List<QueryPair> queryPairs = SqlHelper.getQueryPairs(vo);
		ParamMap pm = new ParamMap();
		pm.put("tableName", tableName);
		pm.put("queryPairList", queryPairs);
		if (orders != null && !orders.isEmpty()) {
			pm.put("orders", orders);
		}
		List<Map<String, Object>> mapResult = sqlSessionTemplate.selectList("CommonEntityMapper.listByExample", pm);
		List<?> entityResult = ReflectHelper.transformMapToEntity(vo.getClass(), mapResult);
		return (List<T>) entityResult;
	}

	/**
	 * 根据查询条件查询结果,增加对结果的Xss解编码处理
	 * 
	 * @author yexinzhou
	 * @param statement
	 *            mybatis mapper文件中定义的查询语句id
	 * @param paramMap
	 *            参数，是一个map ,包含了排序条件
	 * @return 返回结果是map list.
	 */
	public List<Map<String, Object>> listByParams(String statementId, ParamMap pm) {
		SmartDatasourceHolder.switchToRead();
		List<Map<String, Object>> result = sqlSessionTemplate.selectList(statementId, pm);
		return result;
	}

	/**
	 * 返回符合条件的第一条数据
	 *
	 * @param statement
	 *            mybatis mapper文件中定义的查询语句id
	 * @param paramMap
	 *            参数，是一个map ,包含了排序条件
	 * @return 返回结果是map list.
	 */
	public Map<String, Object> findOne(String statement, ParamMap paramMap) {
		List<Map<String, Object>> list = this.listByParams(statement, paramMap);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据查询条件查询结果
	 *
	 * @param clazz
	 *            返回结果被封装成的java类，在联表查询时可以定义个类，包含所有查询语句需要返回的字段。
	 * @param statement
	 *            mybatis mapper文件中定义的查询语句id
	 * @param paramMap
	 *            参数，是一个map,,包含了排序条件
	 * @return 结果被封装成 参数clazz的实例集合
	 */
	public <T> List<T> listByParams(Class<T> clazz, String statementId, ParamMap paramMap) {
		List<Map<String, Object>> mapResult = listByParams(statementId, paramMap);
		List<T> entityResult = null;
		if (mapResult.isEmpty()) {
			entityResult = new ArrayList<>();
		} else {
			entityResult = ReflectHelper.transformMapToEntity(clazz, mapResult);
		}
		return entityResult;
	}

	/**
	 * @author yexinzhou
	 * @Description:给定查询对象进行分页查询
	 * @date 2017/12/22
	 */
	public <T> Page<T> findPageByExample(Class<T> clazz, Page<T> page, Object example) {
		SmartDatasourceHolder.switchToRead();
		String tableName = SqlHelper.getTableName(clazz);
		List<QueryPair> queryPairs = SqlHelper.getQueryPairs(example);
		ParamMap paramMap = new ParamMap();
		paramMap.put("tableName", tableName);
		paramMap.put("queryPairList", queryPairs);
		paramMap.put("page", page);
		com.github.pagehelper.Page<T> innerPage = PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<Map<String, Object>> mapResult = sqlSessionTemplate.selectList("CommonEntityMapper.findPage", paramMap);
		List<T> entityResult = ReflectHelper.transformMapToEntity(clazz, mapResult);
		page.setResult((List<T>) entityResult);
		PageInfo<T> pageInfo = innerPage.toPageInfo();
		page.setTotalResult((int) pageInfo.getTotal());
		page.setStartRow(pageInfo.getStartRow());
		page.setEndRow(pageInfo.getEndRow());
		return page;
	}

	/**
	 * @author yexinzhou
	 * @Description:分页查询，返回分页信息
	 * @date 2017/12/22
	 */
	public <T> Page<T> findPageByParams(Class<T> clazz, Page<T> page, String statement, ParamMap paramMap) {
		if (page == null) {
			throw new RuntimeException("page can not be null when findPageByParams");
		} else {
			com.github.pagehelper.Page<T> innerPage = PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
			List<T> resultLists = listByParams(clazz, statement, paramMap);
			PageInfo<T> pageInfo = innerPage.toPageInfo();
			page.setResult(resultLists);
			// setTotalResult 总的结果数
			page.setTotalResult((int) pageInfo.getTotal());
			page.setStartRow(pageInfo.getStartRow());
			page.setEndRow(pageInfo.getEndRow());
			return page;
		}
	}

	/**
	 * @author yexinzhou
	 * @Description:分页查询，返回分页信息,泛型是Map
	 * @date 2017/12/22
	 */
	public Page<Map<String, Object>> findPageByParams(Page<Map<String, Object>> page, String statement,
			ParamMap paramMap) {
		// pageHelper只对紧跟着的sql查询起作用
		if (page == null) {
			throw new RuntimeException("page can not be null when findPageByParams");
		} else {
			com.github.pagehelper.Page<Object> innerPage = PageHelper.startPage(page.getCurrentPage(),
					page.getPageSize());
			List<Map<String, Object>> resultsMap = listByParams(statement, paramMap);
			PageInfo<Object> pageInfo = innerPage.toPageInfo();
			page.setResult(resultsMap);
			// setTotalResult 总的结果数
			page.setTotalResult((int) pageInfo.getTotal());
			page.setStartRow(pageInfo.getStartRow());
			page.setEndRow(pageInfo.getEndRow());
			return page;
		}
	}

	/**
	 * 通常用来执行一个非查询的sql
	 */
	public int execute(String statement, ParamMap paramMap) {
		SmartDatasourceHolder.switchToWrite();
		return sqlSessionTemplate.update(statement, paramMap);
	}

	/**
	 * 根据给定的字段值更新数据，要更新的字段值在updateObj中所有不为null的字段
	 *
	 * @param updateObj
	 *            例子 Book book = new Book(); book.setWords(100);
	 *            book.setPrice(22f); CommonDao.updateByField(Book.class,
	 *            "authorId", 1L, book);//authorId为Book类的字段
	 * @author yexinzhou
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2017年12月15日 下午1:24:53
	 */
	public <T> int updateByField(Class<T> clazz, String fieldName, Object fieldValue, Object updateObj,
			boolean updateNull) {
		String tableName = SqlHelper.getTableName(clazz);
		Field[] fields = SqlHelper.getFieldsWithoutTransient(clazz);
		List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
		String whereColumnName = fieldName;
		String lockFieldName = null;
		Object lockFieldValue = null;
		for (Field f : fields) {
			if (f.getName().equals(fieldName)) {
				whereColumnName = SqlHelper.getColumnName(f);
				continue;
			}
			Map<String, Object> column = new HashMap<String, Object>();
			String columnName = SqlHelper.getColumnName(f);
			column.put("name", columnName);
			f.setAccessible(true);
			try {
				Object value = f.get(updateObj);
				if (value == null) {
					if (updateNull == false) {
						continue;
					}
				}
				if (f.getType().isEnum() && value != null) {
					column.put("value", ((Enum<?>) value).ordinal());
				} else {
					column.put("value", value);
				}
				// 判断是否乐观锁
	            OptimisticLock lockAnno = f.getAnnotation(OptimisticLock.class);
	            if(lockAnno!=null) {
	                lockFieldName = f.getName();
	                lockFieldValue = value;
	                continue;
	            }
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			columns.add(column);
		}
		ParamMap pm = new ParamMap();
		pm.put("tableName", tableName);
		pm.put("fieldName", whereColumnName);
		pm.put("fieldValue", fieldValue);
		pm.put("columnList", columns);
		pm.put("lockFieldName", lockFieldName);
		pm.put("lockFieldValue", lockFieldValue);
		
		int result = this.execute("CommonEntityMapper.updateByField", pm);
		return result;
	}

	/**
	 * 批量插入数据，一条sql语句
	 *
	 * @author yexinzhou
	 * @date 2017年12月18日 上午9:12:59
	 */
	public <T> int batchInsert(Class<T> clazz, List<T> list) throws IllegalArgumentException, IllegalAccessException {
		SmartDatasourceHolder.switchToWrite();
		ParamMap pm = new ParamMap();
		Field[] fields = SqlHelper.getFieldsWithoutTransient(clazz);
		List<String> columns = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			if (SqlHelper.isAutoInstreaseField(fields[i])) {
				continue;
			}
			columns.add(SqlHelper.getColumnName(fields[i]));
		}
		List<List<Object>> rows = new ArrayList<List<Object>>();
		for (T obj : list) {
			List<Object> values = getColumnValues(obj);
			rows.add(values);
		}
		pm.put("tableName", SqlHelper.getTableName(clazz));
		pm.put("columns", columns);
		pm.put("rows", rows);
		if (SqlHelper.isAutoInstreaseField(SqlHelper.getIdOfClass(clazz))) {
			return this.execute("CommonEntityMapper.batchInsertAutoIncrease", pm);
		} else {
			Sequence seq = SqlHelper.getIdOfClass(clazz).getAnnotation(Sequence.class);
			pm.put("selectKey", seq.selectKey());
			return this.execute("CommonEntityMapper.batchInsertBySequence", pm);
		}

	}

	private List<Object> getColumnValues(Object obj) {
		List<Object> values = new ArrayList<Object>();
		Field[] fields = SqlHelper.getFieldsWithoutTransient(obj.getClass());

		for (int i = 0; i < fields.length; i++) {
			if (SqlHelper.isAutoInstreaseField(fields[i])) {
				continue;
			}
			if (SqlHelper.isIdField(fields[i])) {
				// 根据序列获取id
				Field idField = fields[i];
				Sequence seqAno = idField.getAnnotation(Sequence.class);
				if (seqAno == null) {
					// 业务赋值，走下面try方法
				} else {
					String selectKey = seqAno.selectKey();
					ParamMap pm = new ParamMap();
					pm.put("selectKey", selectKey);
					Long id = sqlSessionTemplate.selectOne("CommonEntityMapper.selectId", pm);
					values.add(id);
					continue;
				}
			}
			try {
				fields[i].setAccessible(true);
				Object val = fields[i].get(obj);
				values.add(val);
			} catch (Exception e) {
				throw new BusinessException("批量插入数据失败", e);
			}
		}
		return values;
	}

	public int countByExample(Object vo) {
		SmartDatasourceHolder.switchToRead();
		String tableName = SqlHelper.getTableName(vo.getClass());
		Field[] fields = SqlHelper.getFieldsWithoutTransient(vo.getClass());
		List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
		for (Field f : fields) {
			Map<String, Object> column = new HashMap<String, Object>();
			String columnName = SqlHelper.getColumnName(f);
			column.put("name", columnName);
			f.setAccessible(true);
			try {
				Object value = f.get(vo);
				if (value == null) {
					continue;
				}
				if (f.getType().isEnum() && value != null) {
					column.put("value", ((Enum<?>) value).ordinal());
				} else {
					column.put("value", value);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			columns.add(column);
		}
		ParamMap pm = new ParamMap();
		pm.put("tableName", tableName);
		pm.put("columnList", columns);
		Long result = sqlSessionTemplate.selectOne("CommonEntityMapper.countByExample", pm);
		return result.intValue();
	}
}
