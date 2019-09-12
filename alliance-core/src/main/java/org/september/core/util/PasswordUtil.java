package org.september.core.util;

import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class PasswordUtil {
	public final static String[] word = {
            "a", "b", "c", "d", "e", "f", "g",
            "h", "j", "k", "m", "n",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G",
            "H", "J", "K", "M", "N",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    public final static String[] num = {
            "2", "3", "4", "5", "6", "7", "8", "9"
    };

    public static String randomPassword() {
    	Random random = new Random(new Date().getTime());
    	int length = random.nextInt(3) + 8;
    	return randomPassword(length);
    }
    
    public static String randomPassword(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random(new Date().getTime());
        boolean flag = false;
        for (int i = 0; i < length; i++) {
            if (flag) {
                stringBuffer.append(num[random.nextInt(num.length)]);
            } else {
                stringBuffer.append(word[random.nextInt(word.length)]);
            }
            flag = !flag;
        }
        return stringBuffer.toString();
    }
    
    /**
	 * 复杂的密码符合下面规则
	 * 长度>=8
	 * 包含数字，大写字母，小写字母，特殊字符中任意两种组合
	 * @param pwd
	 * @return
	 */
	public static boolean isComplexPwd(String pwd){
		if(StringUtils.isEmpty(pwd)){
			return false;
		}
		if(pwd.length()<8){
			return false;
		}
		int parts = 0;
		if(isContainNumber(pwd)){
			parts++;
		}
		
		if(isContainLowCaseLetter(pwd)){
			parts++;
		}
		
		if(isContainUpCaseLetter(pwd)){
			parts++;
		}
		String tmp = pwd.replaceAll("[0-9]", "");
		tmp = tmp.replaceAll("[a-z]", "");
		tmp = tmp.replaceAll("[A-Z]", "");
		if(!StringUtils.isEmpty(tmp)){
			parts++;
		}
		if(parts>=2){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean isContainNumber(String text) {
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(text);
        if (m.find()) {
            return true;
        }
        return false;
    }
	
	private static boolean isContainLowCaseLetter(String text) {
        Pattern p = Pattern.compile("[a-z]");
        Matcher m = p.matcher(text);
        if (m.find()) {
            return true;
        }
        return false;
    }
	
	
	private static boolean isContainUpCaseLetter(String text) {
        Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(text);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(randomPassword());
        Thread.sleep(100);
        System.out.println(randomPassword());
        Thread.sleep(100);
        System.out.println(randomPassword());
    }
    
    
}
