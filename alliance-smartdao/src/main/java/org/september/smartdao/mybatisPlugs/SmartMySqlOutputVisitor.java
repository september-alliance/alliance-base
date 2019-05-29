package org.september.smartdao.mybatisPlugs;

import java.util.ArrayList;
import java.util.List;

import org.september.smartdao.model.Order;
import org.september.smartdao.model.Order.Direction;
import org.september.smartdao.model.ParamMap;
import org.springframework.util.StringUtils;

import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.SQLSetQuantifier;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;

public class SmartMySqlOutputVisitor extends MySqlOutputVisitor {

	private ParamMap pm;

	public SmartMySqlOutputVisitor(Appendable appender, ParamMap pm) {
		super(appender);
		this.pm = pm;
	}

	public boolean checkNode(SQLExpr x) {
		if (x instanceof SQLBinaryOpExpr) {
			return checkRight((SQLBinaryOpExpr) x, pm);
		}

		if (x instanceof SQLInListExpr) {
			return checkRight((SQLInListExpr) x, pm);
		}
		
		if (x instanceof SQLVariantRefExpr) {
			return checkRight((SQLVariantRefExpr) x, pm);
		} else if (x instanceof SQLMethodInvokeExpr) {
			SQLMethodInvokeExpr methodExpr = (SQLMethodInvokeExpr) x;
			return checkRight(methodExpr, pm);
		}
		return true;
	}

	public boolean visit(SQLBinaryOpExpr x) {

        SQLObject parent = x.getParent();
        boolean isRoot = parent instanceof SQLSelectQueryBlock;
        boolean relational = x.getOperator() == SQLBinaryOperator.BooleanAnd
                             || x.getOperator() == SQLBinaryOperator.BooleanOr;

        if(isLeaf(x.getLeft()) && checkNode(x.getRight())==false ){
        	return false;
        }
//        
        if (isRoot && relational) {
            incrementIndent();
        }

        List<SQLExpr> groupList = new ArrayList<SQLExpr>();
        SQLExpr left = x.getLeft();
        for (;;) {
            if (left instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr) left).getOperator() == x.getOperator()) {
                SQLBinaryOpExpr binaryLeft = (SQLBinaryOpExpr) left;
                groupList.add(binaryLeft.getRight());
                left = binaryLeft.getLeft();
            } else {
            	groupList.add(left);
                break;
            }
        }
        Appendable xx = this.appender;
        for (int i = groupList.size() - 1; i >= 0; --i) {
            SQLExpr item = groupList.get(i);
            if(isNotEmpty(item)==false){
            	continue;
            }
            visitBinaryLeft(item, x.getOperator());

            String sql = this.appender.toString().trim();
            if(sql.endsWith(SQLBinaryOperator.BooleanAnd.name)
            		||sql.endsWith(SQLBinaryOperator.BooleanOr.name)
            		|| "".equals(sql)){
            	continue;
            }
            
            if (relational) {
                println();
            	print(" ");
            } else {
                print(" ");
            }
            
            print(x.getOperator().name);
            print(" ");
            
//            if(i>0){
//            	if(isNotEmpty(groupList.get(i-1))==false){
//            		i--;
//            		continue;
//            	}else{
//            		print(x.getOperator().name);
//                    print(" ");
//            	}
//            }else{
//            	//判断right
//        		if(isNotEmpty(x.getRight())){
//        			print(x.getOperator().name);
//                    print(" ");
//        		}else{
//        			
//        		}
//            }
            
//            if(i>0){
//            	if(isNotEmpty(groupList.get(i-1))){
//            		print(leftOper.name);
//                    print(" ");
//            	}else{
//            		//判断right
//            		if(isNotEmpty(x.getRight())){
//            			print(x.getOperator().name);
//                        print(" ");
//            		}
//            		break;
//            	}
//            }else{
//            	//判断right
//        		if(isNotEmpty(x.getRight())){
//        			print(x.getOperator().name);
//                    print(" ");
//        		}else{
//        			
//        		}
//            }
        }
		
        visitorBinaryRight(x);

        if (isRoot && relational) {
            decrementIndent();
        }

        return false;
    
	}
	
	private boolean isNotEmpty(SQLExpr left){
		
		// 测试 left 是否有内容
		StringBuilder out = new StringBuilder();
		SmartMySqlOutputVisitor tmpVisitor = new SmartMySqlOutputVisitor(out , pm);
		left.accept(tmpVisitor);
		if(StringUtils.isEmpty(out.toString().trim()) || "()".equals(out.toString().trim())){
			return false;
		}
		return true;
	}
	
	public boolean visit(MySqlSelectQueryBlock x) {

        if (x.getOrderBy() != null) {
            x.getOrderBy().setParent(x);
        }

        print("SELECT ");

        for (int i = 0, size = x.getHintsSize(); i < size; ++i) {
            SQLCommentHint hint = x.getHints().get(i);
            hint.accept(this);
            print(' ');
        }

        if (SQLSetQuantifier.ALL == x.getDistionOption()) {
            print("ALL ");
        } else if (SQLSetQuantifier.DISTINCT == x.getDistionOption()) {
            print("DISTINCT ");
        } else if (SQLSetQuantifier.DISTINCTROW == x.getDistionOption()) {
            print("DISTINCTROW ");
        }

        if (x.isHignPriority()) {
            print("HIGH_PRIORITY ");
        }

        if (x.isStraightJoin()) {
            print("STRAIGHT_JOIN ");
        }

        if (x.isSmallResult()) {
            print("SQL_SMALL_RESULT ");
        }

        if (x.isBigResult()) {
            print("SQL_BIG_RESULT ");
        }

        if (x.isBufferResult()) {
            print("SQL_BUFFER_RESULT ");
        }

        if (x.getCache() != null) {
            if (x.getCache().booleanValue()) {
                print("SQL_CACHE ");
            } else {
                print("SQL_NO_CACHE ");
            }
        }

        if (x.isCalcFoundRows()) {
            print("SQL_CALC_FOUND_ROWS ");
        }

        printSelectList(x.getSelectList());

        if (x.getInto() != null) {
            println();
            print("INTO ");
            x.getInto().accept(this);
        }

        if (x.getFrom() != null) {
            println();
            print("FROM ");
            x.getFrom().accept(this);
        }

        if (x.getWhere() != null) {
            
            x.getWhere().setParent(x);
            
            StringBuilder out = new StringBuilder();
			SmartMySqlOutputVisitor tmpVisitor = new SmartMySqlOutputVisitor(out , pm);
			x.getWhere().accept(tmpVisitor);
			if(!StringUtils.isEmpty(out.toString().trim())){
				println();
	            print("WHERE ");
	            print(out.toString());
			}
//            x.getWhere().accept(this);
        }

        if (x.getGroupBy() != null) {
        	trimEndOper();
            println();
            x.getGroupBy().accept(this);
        }

        if (x.getOrderBy() != null) {
        	trimEndOper();
            println();
            x.getOrderBy().accept(this);
        }else {
            if(!pm.getOrders().isEmpty()) {
            	x.setOrderBy(new SQLOrderBy());
            	for(Order order : pm.getOrders()) {
            		if(Direction.ASC == order.getDirection()) {
            			x.getOrderBy().addItem(new SQLIdentifierExpr(order.getProperty()), SQLOrderingSpecification.ASC);
            		}else {
            			x.getOrderBy().addItem(new SQLIdentifierExpr(order.getProperty()), SQLOrderingSpecification.DESC);
            		}
            	}
            	trimEndOper();
                println();
                x.getOrderBy().accept(this);
            }
            
        }

        if (x.getLimit() != null) {
        	trimEndOper();
            println();
            x.getLimit().accept(this);
        }

        if (x.getProcedureName() != null) {
            print(" PROCEDURE ");
            x.getProcedureName().accept(this);
            if (x.getProcedureArgumentList().size() > 0) {
                print("(");
                printAndAccept(x.getProcedureArgumentList(), ", ");
                print(")");
            }
        }

        if (x.isForUpdate()) {
            println();
            print("FOR UPDATE");
        }

        if (x.isLockInShareMode()) {
            println();
            print("LOCK IN SHARE MODE");
        }

        return false;
    
	}

	public boolean visit(SQLInListExpr x) {
		if(checkNode(x)==false){
			return false;
		}
		return super.visit(x);
    }
	
	private static boolean checkRight(SQLBinaryOpExpr node, ParamMap pm) {
		if (node.getRight() instanceof SQLVariantRefExpr) {
			return checkRight((SQLVariantRefExpr) node.getRight(), pm);
		} else if (node.getRight() instanceof SQLMethodInvokeExpr) {
			SQLMethodInvokeExpr methodExpr = (SQLMethodInvokeExpr) node.getRight();
			return checkRight(methodExpr, pm);
		}
		return true;
	}
	
	private static boolean checkRight(SQLMethodInvokeExpr methodExpr, ParamMap pm) {
		for (SQLExpr expr : methodExpr.getParameters()) {
			if (expr instanceof SQLVariantRefExpr) {
				SQLVariantRefExpr refExpr = (SQLVariantRefExpr) expr;
				if (!pm.containsKey(refExpr.getName())) {
					return false;
				}else{
					Object val = pm.get(refExpr.getName());
					if(StringUtils.isEmpty(val)){
						// 默认空值排除
						return false;
					}
				}
			}
		}
		return true;
	}

	private static boolean checkRight(SQLInListExpr inExpr, ParamMap pm) {
		for (SQLExpr expr : inExpr.getTargetList()) {
			if (expr instanceof SQLVariantRefExpr) {
				SQLVariantRefExpr refExpr = (SQLVariantRefExpr) expr;
				if (!pm.containsKey(refExpr.getName())) {
					return false;
				}else{
					Object val = pm.get(refExpr.getName());
					if(StringUtils.isEmpty(val)){
						// 默认空值排除
						return false;
					}
				}
			}
		}
		return true;
	}

	private static boolean checkRight(SQLVariantRefExpr refExpr, ParamMap pm) {
		if (!pm.containsKey(refExpr.getName())) {
			return false;
		}else{
			Object val = pm.get(refExpr.getName());
			if(StringUtils.isEmpty(val)){
				// 默认空值排除
				return false;
			}
		}
		return true;
	}
	
	private void visitBinaryLeft(SQLExpr left, SQLBinaryOperator op) {
		
        if (left instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr binaryLeft = (SQLBinaryOpExpr) left;
            boolean leftRational = binaryLeft.getOperator() == SQLBinaryOperator.BooleanAnd
                                   || binaryLeft.getOperator() == SQLBinaryOperator.BooleanOr;

            if (binaryLeft.getOperator().priority > op.priority) {
                if (leftRational) {
                    incrementIndent();
                    println();
//                    print(op.name); 
//                    print(" ");
                }
                print('(');
                left.accept(this);
                print(')');

                if (leftRational) {
                    decrementIndent();
                }
            } else {
                left.accept(this);
            }
        } else {
            left.accept(this);
        }
    }
	
	private void visitorBinaryRight(SQLBinaryOpExpr x) {
		if(checkNode(x.getRight())==false){
			return;
		}
		
        if (x.getRight() instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr right = (SQLBinaryOpExpr) x.getRight();
            boolean rightRational = right.getOperator() == SQLBinaryOperator.BooleanAnd
                                    || right.getOperator() == SQLBinaryOperator.BooleanOr;

            if (right.getOperator().priority >= x.getOperator().priority) {
                if (rightRational) {
                    incrementIndent();
                }

                print('(');
                right.accept(this);
                print(')');

                if (rightRational) {
                    decrementIndent();
                }
            } else {
                right.accept(this);
            }
        } else {
            x.getRight().accept(this);
        }
    }
	
	private boolean isLeaf(SQLExpr expr){
		if(expr instanceof SQLPropertyExpr){
			return true;
		}
		if(expr instanceof SQLIdentifierExpr){
			return true;
		}
		return false;
	}
	
	private void trimEndOper(){
		StringBuilder sb = (StringBuilder)this.appender;
    	String sql = sb.toString().trim();
    	sql = sql.replace("()", "").trim();
    	if(sql.endsWith(SQLBinaryOperator.BooleanAnd.name)){
			sql = sql.substring(0, sql.length()-3);
		}else if(sql.endsWith(SQLBinaryOperator.BooleanOr.name)){
			sql = sql.substring(0, sql.length()-2);
		}else if(sql.endsWith("WHERE")){
			sql = sql.substring(0, sql.length()-5);
		}
    	sb.delete(0, sb.length());
    	sb.append(sql);
	}
}
