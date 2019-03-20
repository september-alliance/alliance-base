package org.apache.ibatis.scripting.xmltags;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.scripting.ScriptingException;
import org.apache.ibatis.type.SimpleTypeRegistry;
import org.september.smartdao.model.ParamMap;
import org.september.smartdao.mybatisPlugs.SmartSqlOptimizer;
import org.springframework.util.StringUtils;

public class TextSqlNode implements SqlNode {

	  private final String text;
	  private final Pattern injectionFilter;

	  public TextSqlNode(String text) {
	    this(text, null);
	  }
	  
	  public TextSqlNode(String text, Pattern injectionFilter) {
	    this.text = text;
	    this.injectionFilter = injectionFilter;
	  }
	  
	  public boolean isDynamic() {
//	    DynamicCheckerTokenParser checker = new DynamicCheckerTokenParser();
//	    GenericTokenParser parser = createParser(checker);
//	    parser.parse(text);
//	    return checker.isDynamic();
		  // 使得所有的sql都动态优化
	    return true;
	  }

	  @Override
	  public boolean apply(DynamicContext context) {
	    GenericTokenParser parser = createParser(new BindingTokenParser(context, injectionFilter));
	    Object _parameter = context.getBindings().get("_parameter");
		if(_parameter instanceof Map){
			Map map = (Map)_parameter;
			if(map!=null && Boolean.TRUE.equals(map.get(ParamMap.Smart_Sql))){
				String sql = SmartSqlOptimizer.optimize(text, map);
				context.appendSql(parser.parse(sql));
			}else{
				context.appendSql(parser.parse(text));
			}
		}else{
			context.appendSql(parser.parse(text));
		}
		return true;
	  }
	  
	  private GenericTokenParser createParser(TokenHandler handler) {
	    return new GenericTokenParser("${", "}", handler);
	  }

	  private static class BindingTokenParser implements TokenHandler {

	    private DynamicContext context;
	    private Pattern injectionFilter;

	    public BindingTokenParser(DynamicContext context, Pattern injectionFilter) {
	      this.context = context;
	      this.injectionFilter = injectionFilter;
	    }

	    @Override
	    public String handleToken(String content) {
	      Object parameter = context.getBindings().get("_parameter");
	      if (parameter == null) {
	        context.getBindings().put("value", null);
	      } else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())) {
	        context.getBindings().put("value", parameter);
	      }
	      Object value = OgnlCache.getValue(content, context.getBindings());
	      String srtValue = (value == null ? "" : String.valueOf(value)); // issue #274 return "" instead of "null"
	      checkInjection(srtValue);
	      return srtValue;
	    }

	    private void checkInjection(String value) {
	      if (injectionFilter != null && !injectionFilter.matcher(value).matches()) {
	        throw new ScriptingException("Invalid input. Please conform to regex" + injectionFilter.pattern());
	      }
	    }
	  }
	  
	  private static class DynamicCheckerTokenParser implements TokenHandler {

	    private boolean isDynamic;

	    public DynamicCheckerTokenParser() {
	      // Prevent Synthetic Access
	    }

	    public boolean isDynamic() {
	      return isDynamic;
	    }

	    @Override
	    public String handleToken(String content) {
	      this.isDynamic = true;
	      return null;
	    }
	  }
	  

}
