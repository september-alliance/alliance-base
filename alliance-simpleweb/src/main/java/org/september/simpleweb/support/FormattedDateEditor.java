package org.september.simpleweb.support;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.september.core.exception.BusinessException;
import org.springframework.util.StringUtils;

/**
 * 	支持各种类型格式的时间解析
 */
public class FormattedDateEditor extends PropertyEditorSupport{

	@Override
    public void setAsText(String text) throws IllegalArgumentException {
        Date date = null;
        SimpleDateFormat sdf = null;
    	
        try {
        	if (Pattern.compile("([GMT]|[gmt])").matcher(text).find()) { //Wed Nov 21 2018 08:00:00 GMT+0800(中国标准时间)
        		sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.ENGLISH);
        		try {
        			text = text.replace("GMT", "").replaceAll("\\(.*\\)", "");
        			date = sdf.parse(text);
    				setValue(date);
    				return;
    			} catch (ParseException e) {
    				throw new BusinessException("时间格式解析错误",e);
    			}
            }
        	
            //防止空数据出错
            if(!StringUtils.isEmpty(text)){
            	sdf = getSimpleDateFormat(text);
                date = sdf.parse(text);
            }
        } catch (ParseException e) {
           e.printStackTrace();
        }
        setValue(date);
    }
	
	private SimpleDateFormat getSimpleDateFormat(String source) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        if (Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", source)) { // yyyy-MM-dd
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        } else if (Pattern.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}$", source)) { // yyyy-MM-dd HH-mm-ss
            sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        } else if (Pattern.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", source)) { // yyyy-MM-dd HH:mm:ss
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else if (Pattern.matches("^\\d{4}/\\d{2}/\\d{2}$", source)) { // yyyy/MM/dd
            sdf = new SimpleDateFormat("yyyy/MM/dd");
        } else if (Pattern.matches("^\\d{4}/\\d{2}/\\d{2} \\d{2}/\\d{2}/\\d{2}$", source)) { // yyyy/MM/dd HH/mm/ss
            sdf = new SimpleDateFormat("yyyy/MM/dd HH/mm/ss");
        }  else if (Pattern.matches("^\\d{4}\\d{2}\\d{2}$", source)) { // yyyyMMdd
            sdf = new SimpleDateFormat("yyyyMMdd");
        }  else if (Pattern.matches("^\\d{4}\\d{2}\\d{2} \\d{2}\\d{2}\\d{2}$", source)) { // yyyyMMdd HHmmss
            sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
        } else if (Pattern.matches("^\\d{4}\\.\\d{2}\\.\\d{2}$", source)) { // yyyy.MM.dd
            sdf = new SimpleDateFormat("yyyy.MM.dd");
        }  else if (Pattern.matches("^\\d{4}\\.\\d{2}\\.\\d{2} \\d{2}\\.\\d{2}\\.\\d{2}$", source)) { // yyyy.MM.dd HH.mm.ss
            sdf = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
        }else if (Pattern.matches("^\\d{4}-\\d{2}$", source)) { // yyyy-MM-dd
            sdf = new SimpleDateFormat("yyyy-MM");
        }else{
            throw new BusinessException("无法识别的时间格式");
        }
        return sdf;
    }
}
