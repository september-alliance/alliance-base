package org.september.smartdao.model;

public class SmartParamMap extends ParamMap{

	/**
	 * 说明:默认设置smartSql=true
	 */
	private static final long serialVersionUID = -6344559513317649142L;

	public SmartParamMap(){
		put(Smart_Sql , Boolean.TRUE);
	}

	public SmartParamMap(Object obj) {
		super(obj);
	}

}
