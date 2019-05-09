package org.september.core.util;

import java.security.MessageDigest;
import java.util.UUID;
/** 
 * 说明：MD5处理
 * 修改时间：2014年9月20日
 * @version
 */
public class MD5 {

	private static final String Salt="938029d6-11ba-4966-a8b7-c8789098545b";
    /**
     * 加密字符串
     * @author yexinzhou
     * @date 2017年6月22日 上午10:27:04
     * @param str
     * @return
     */
	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] b = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
                    i += 256;
                }
				if (i < 16) {
                    buf.append("0");
                }
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
		return str;
	}
	
	public static String md5WithDefaultSalt(String str) {
		str += Salt;
		return md5(str);
	}
	
	public static String md5WithSalt(String str , String salt) {
		str += salt;
		return md5(str);
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID());
		System.out.println(md5("31119@qq.com"+"123456"));
		System.out.println(md5("1"));
	}
}
