package org.september.smartdao.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.september.smartdao.util.ReflectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/** 
 * 说明：参数封装Map
 * 
 * 修改时间：2014年9月20日
 * @version
 */
public class ParamMap extends HashMap{

	public static final String Smart_Sql = "smartSql";
	
	private static final Logger logger = LoggerFactory.getLogger(ParamMap.class);
	private static final long serialVersionUID = 1L;
	
	/**
	 * (多个)排序条件
	 */
	private List<Order> orders = new ArrayList<Order>();
	
	private Map map = null;
	
	public ParamMap() {
		map = new HashMap();
	}
	
	public ParamMap(Object vo){
		if(vo==null){
			map = new HashMap();
		}
		map = ReflectHelper.transEmptyString2Null(vo);
	}
	
	@Override
	public Object get(Object key) {
		Object obj = null;
		if(map.get(key) instanceof Object[]) {
			Object[] arr = (Object[])map.get(key);
			obj = arr[0];
		} else {
			obj = map.get(key);
		}
		return obj;
	}
	
	public String getString(Object key) {
		return (String)get(key);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}
	
	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set entrySet() {
		return map.entrySet();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set keySet() {
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	public void putAll(Map t) {
		map.putAll(t);
	}

	public int size() {
		return map.size();
	}

	public Collection values() {
		return map.values();
	}
	
	/**
	 * 增加一个排序条件,注意<param>field</param>是要排序的数据库字段名称，而不是实体类中类字段名,业务代码需要将实体类的类字段名转成数据库字段名,
	 * 可以使用
	 * <code>ReflectHelper.getColumnName(User.class, "name");</code>
	 * 的是否获取实体类字段对应的数据库类字段
	 * @param field 
	 * @param direction
	 */
	public void addOrder(String field , String direction){
		
	}
	
	public List<Order> getOrders(){
		return orders;
	}
}
