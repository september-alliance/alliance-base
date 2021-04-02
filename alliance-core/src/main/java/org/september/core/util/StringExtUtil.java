package org.september.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;

public class StringExtUtil {

	/**
     * 将下划线风格替换为驼峰风格
     *
     * @param inputString
     * @return
     */
    public static String underlineToCamelhump(String inputString) {
        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (c == '_') {
                if (sb.length() > 0) {
                    nextUpperCase = true;
                }
            } else {
                if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
    


	public static boolean isEmpty(Object str) {
		if (null == str) {
			return true;
		}
		return "".equals(str);
	}

	public static boolean isNotEmpty(Object str) {
		return !isEmpty(str);
	}

	// 寻找以start开头，end结尾的内容
	public static List<String> findMatch(String text, String start, String end) {
		List<String> result = new ArrayList<String>();
		if (isEmpty(text) || isEmpty(end) || isEmpty(end)) {
			return result;
		}
		int fromIndex = 0;
		while (fromIndex < text.length()) {
			int x = text.indexOf(start, fromIndex);
			if (x == -1) {
				return result;
			} else {
				x += start.length();
			}
			int y = text.indexOf(end, x);
			if (y == -1) {
				return result;
			}
			fromIndex = y + end.length();
			result.add(text.substring(x, y));
		}

		return result;
	}

	public static String findFirstMatch(String text, String start, String end) {
		List<String> result = findMatch(text, start, end);
		if (result.isEmpty()) {
			return "";
		}
		return result.get(0);
	}

	public static String findLastMatch(String text, String start, String end) {
		List<String> result = findMatch(text, start, end);
		if (result.isEmpty()) {
			return "";
		}
		return result.get(result.size() - 1);
	}

	public static String subString(String source, int start, int length) {
		if (isEmpty(source)) {
			return "";
		}
		if (start < 0) {
			start = 0;
		}
		if (length < 0) {
			length = 0;
		}
		int endPos = start + length;
		if (endPos > source.length()) {
			endPos = source.length();
		}
		return source.substring(start, endPos);
	}

	// 转成半角字符
	public static void tohalfAngle(String source) {

	}

	public static boolean isNumber(Object str) {
		if (isEmpty(str)) {
			return false;
		}
		if(!(str instanceof String)){
			return false;
		}
		
		try {
			Double.valueOf(str.toString());
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public static boolean isDecimal(Object str) {
		if (isEmpty(str)) {
			return false;
		}
		if(!(str instanceof String)){
			return false;
		}
		try {
			Double dvalue = Double.valueOf(str.toString());
			if (dvalue - dvalue.longValue() == 0) {
				if (str.toString().contains(".")) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	public static final boolean isLetter(char ch) {
		if( (ch>='A' && ch<='Z') || (ch>='a' && ch<='z') ) {
			return true;
		}
		return false;
	}
	
	public static final boolean isAllChinese(String strName) {
		if(isEmpty(strName)){
			return false;
		}
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!isChinese(c)) {
				return false;
			}
		}
		return true;
	}

	public static boolean hasChinese(String str) {
		if (isEmpty(str)) {
			return false;
		}
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (isChinese(charArray[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmail(String str) {
		if(isEmpty(str)){
			return false;
		}
		boolean tag = true;
		if (!str.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
			tag = false;
		}
		return tag;
	}
	
	public static InputStream toInputStream(String text){
		if(isEmpty(text)){
			return null;
		}
		return IOUtils.toInputStream(text);
	}
	
	public static String toString(InputStream is) throws IOException{
	     return IOUtils.toString(is);
	}

	public static void main(String[] args) {
		System.out.println(findMatch("${name} , ${text}", "${", "}"));
		System.out.println(findFirstMatch("${name} , ${text}", "${", "}"));
		System.out.println(findLastMatch("${name} , ${text}", "${", "}"));

		System.out.println(subString("abcdefg", 5, -1));
		System.out.println(isDecimal("056.0"));
		System.out.println(isNumber("056.0"));

		System.out.println(hasChinese("056.0！"));
		System.out.println(isAllChinese("件了！￥（"));
		System.out.println(isEmail("253187898"));
		System.out.println(isEmail("253187898@qq.sd.com"));
	}



	public static boolean safeEquals(String secret, String secretCode) {
		if(secret==null || secretCode==null) {
			return false;
		}
		return secret.equals(secretCode);
	}

	public static boolean safeEqualsIgnoreCase(String secret, String secretCode) {
		if(secret==null || secretCode==null) {
			return false;
		}
		return secret.equalsIgnoreCase(secretCode);
	}

	public static List<Long> sperateToLongList(String data , String seprator){
		if(data==null) {
			return null;
		}
		List<Long> list = new ArrayList<>();
		for(String str: data.split(seprator)) {
			if(!StringUtils.isEmpty(str)) {
				list.add(Long.valueOf(str));
			}
		}
		return list;
	}
}
