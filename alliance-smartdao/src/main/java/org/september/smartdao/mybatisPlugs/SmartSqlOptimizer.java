package org.september.smartdao.mybatisPlugs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.september.smartdao.model.ParamMap;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.util.JdbcConstants;

public class SmartSqlOptimizer {

	public static String optimize(String sql , Map map){
		ParamMap pm2 = fullfillParamMap(map);
		StringBuilder out = new StringBuilder();
		SmartMySqlOutputVisitor visitor = new SmartMySqlOutputVisitor(out , pm2);
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
		SQLSelectStatement selectStmt = (SQLSelectStatement) stmtList.get(0);
		selectStmt.accept(visitor);
		return removeEndOper(out.toString());
	}
	

	private static String removeEndOper(String sql){
		sql = sql.trim();
		sql = sql.replace("()", "");
		if(sql.endsWith(SQLBinaryOperator.BooleanAnd.name)){
			sql = sql.substring(0, sql.length()-3);
		}else if(sql.endsWith(SQLBinaryOperator.BooleanOr.name)){
			sql = sql.substring(0, sql.length()-2);
		}else if(sql.endsWith("WHERE")){
			sql = sql.substring(0, sql.length()-5);
		}
		return sql;
	}
	
	private static ParamMap fullfillParamMap(Map pm) {
		ParamMap pm2 = new ParamMap();
		for(Object key : pm.keySet()){
			pm2.put("#{"+key+"}", pm.get(key));
			pm2.put("${"+key+"}", pm.get(key));
		}
		return pm2;
	}


	public static void main(String[] args){
		ParamMap pm = new ParamMap();
		pm.put("dd", "ee");
		pm.put("userId", 12);
//		pm.put("name", "xzye");
//		pm.put("aa", "xzye");
		pm.put("cc", "xzye");
		pm.put("buildId", "xzye");
		pm.put("version", "xzye");
		pm.put("buildSetId", "xzye");
		
		for(String sql : getSqlList()){
			System.out.println(sql+"-->"+optimize(sql , pm));
			System.out.println("--------------------------------------------------------");
		}
	}
	
	private static List<String> getSqlList(){
		List<String> list = new ArrayList<String>();
//		list.add("select aa , bb , cc from table_x where aa = #{aa} and name LIKE CONCAT('%',#{name},'%' )");
//		list.add("select aa , bb , cc from table_x where aa = #{aa} and bb = #{bb} and dd in (${dd})");
//		list.add("select aa , bb , cc from table_x where aa = 1 and bb = #{bb} and cc = ${cc}");
//		list.add("select * from table_x where aa = (select id from table_y where name= #{name} and zz= ${zz} order by xx desc)");
//		list.add("select id from table_y where name like #{name}");
//		list.add("select id from table_y where name != #{name}");
//		list.add("select name from table_y where name != #{name} limit 1,10");
//		list.add("select * from table_x xx , table_y yy where xx.a1 = 1 and yy.c = #{cc}");
//		list.add("select * from table_x xx left join table_y yy on xx.a1 = yy.b1 where yy.c = #{cc}");
//		list.add("select * from table_x xx left join table_y yy on xx.a1 = yy.b1 where yy.c is not null ");
//		list.add("select * from table_x xx , table_y yy where xx.a1 = yy.b1 and xx.a2 = yy.b2 and yy.build = #{build}");
//		list.add("select * from table_x xx , table_y yy where xx.a1 = yy.b1 and xx.a2 = yy.b2 and (yy.c3 = 2 or yy.c3 = ${build})");
//		list.add("select * from table_x where 1 = 1 and ( plat.owner_uid = #{userId} or plat.member_uids like CONCAT('%,',#{userId},',%') )");
//		String sql1 = "select build.id as id , build.version as version ,build.build_result as buildResult, build.commit_message as commitMessage,"
//				+"commit_number as commitNumber,build.create_time as createTime , project.name as projectName from easyops_building build , easyops_project project,"
//				+"easyops_plat plat"
//				+" 	where "
//				+"	build.project_id = project.id and plat.id = project.plat_id"
//				+"	and build.id = #{buildId}"
//				+"	and build.building_set_id = #{buildSetId}"
//				+"	and project.name LIKE CONCAT('%',#{projectName},'%' )"
//				+"	and ( plat.owner_uid = #{userId} or plat.member_uids like CONCAT('%,',#{userId},',%') )"
//				+"	and build.version LIKE CONCAT('%',#{version},'%' )"
//				+"	and build.build_result IN (${buildResult})"
//				+"	order by build.create_time desc";
//		list.add(sql1);
//		
//		String sql2 = "select 	plat.*,user.fullname as ownerFullName from easyops_plat plat , easyops_ops_user user"
//				+"	where 	plat.owner_uid=user.id"
//				+"	and ( plat.owner_uid = #{userId} or plat.member_uids like CONCAT('%,',#{userId},',%') )"
//				+"	AND name LIKE CONCAT('%',#{name},'%' )";
//		list.add(sql2);
		
//		list.add("select id , username, fullname from easyops_ops_user where id in (${uids})");
		list.add("select id , username, fullname from easyops_ops_user where name = ${dd} and id > #{we}");
		
//		String sql3 = "select project.id, project.name , project.remark,"
//				+" project.code_repo as codeRepo , project.code_server_type as codeServerType , category.name as projectCategory,"
//				+" plat.name as platName from easyops_project project , easyops_project_category category , easyops_plat plat"
//				+" where project.category_id = category.id and project.plat_id = plat.id"
//				+" AND project.name LIKE CONCAT('%',#{name},'%' )"
//				+" and ( plat.owner_uid = #{userId} or plat.member_uids like CONCAT('%,',#{userId},',%') )"
//				+" and plat_id = #{platId}"
//				+" AND category.name = #{type}"
//				+" ORDER BY project.id DESC";
//		list.add(sql3);
		return list;
	}
}
