package org.september.core.component;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.september.core.exception.BusinessException;
import org.springframework.util.StringUtils;


public class AESService{

	private String securitySeed ="af5e357380bed52e7a09c217c41cf214";
	
	private KeyGenerator keyGenerator;
	
	private SecretKey secretKey;
	
	private byte[] keyBytes;
	
	private Key key;
	
	private Cipher cipher;
	
	 
	public AESService(String securitySeed) throws Exception{
		if(!StringUtils.isEmpty(securitySeed)){
			this.securitySeed = securitySeed;
		}
		keyGenerator = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(this.securitySeed.getBytes());
		keyGenerator.init(128, random); 
		secretKey = keyGenerator.generateKey();
		keyBytes = secretKey.getEncoded();
		//Key转换
		key = new SecretKeySpec(keyBytes, "AES");
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	}
	
	public String encrypt(String text) throws Exception{
		if(StringUtils.isEmpty(text)){
			return text;
		}
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encodeResult = cipher.doFinal(text.getBytes());
		return Hex.encodeHexString(encodeResult);
	}
	
	
	public String decrypt(String text) throws Exception{
		if(StringUtils.isEmpty(text)){
			return text;
		}
		try{
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decodeResult = cipher.doFinal(Hex.decodeHex(text.toCharArray()));
			return new String(decodeResult);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException("密码解密失败"+ex.getMessage());
		}
	}

}
