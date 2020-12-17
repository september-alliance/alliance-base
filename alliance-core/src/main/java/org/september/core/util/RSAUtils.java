package org.september.core.util;

import java.math.BigInteger;    
import java.security.KeyFactory;    
import java.security.KeyPair;    
import java.security.KeyPairGenerator;    
import java.security.NoSuchAlgorithmException;    
import java.security.Provider;
import java.security.interfaces.RSAPrivateKey;    
import java.security.interfaces.RSAPublicKey;    
import java.security.spec.RSAPrivateKeySpec;    
import java.security.spec.RSAPublicKeySpec;    
import java.util.HashMap;   

import javax.crypto.Cipher;

public class RSAUtils {
	
	public static final String RAS_Key_Private="RAS_Key_Private";
	
	public static final String RAS_Key_Public="RAS_Key_Public";
	
	public static final String RAS_Cache="RAS_Cache";
	
	public static final String RAS_Key="RAS_Key";
	
	private RSAUtils(){
		
	}
	
	/**  
     * 生成公钥和私钥  
     * @throws NoSuchAlgorithmException   
     *  
     */    
    public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException{    
        HashMap<String, Object> map = new HashMap<String, Object>();    
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", getProvider());
        keyPairGen.initialize(1024);    
        KeyPair keyPair = keyPairGen.generateKeyPair();    
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();    
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();    
        map.put(RAS_Key_Public, publicKey);    
        map.put(RAS_Key_Private, privateKey);    
        return map;    
    }    
    /**  
     * 使用模和指数生成RSA公钥  
     *   
     *   
     * @param modulus  
     *            模  
     * @param exponent  
     *            指数  
     * @return  
     */    
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {    
        try {    
            BigInteger b1 = new BigInteger(modulus);    
            BigInteger b2 = new BigInteger(exponent);    
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", getProvider());    
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);    
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);    
        } catch (Exception e) {    
            e.printStackTrace();    
            return null;    
        }    
    }    
    
    /**  
     * 使用模和指数生成RSA私钥  
      
     * /None/NoPadding】  
     *   
     * @param modulus  
     *            模  
     * @param exponent  
     *            指数  
     * @return  
     */    
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {    
        try {    
            BigInteger b1 = new BigInteger(modulus);    
            BigInteger b2 = new BigInteger(exponent);    
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", getProvider());    
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);    
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);    
        } catch (Exception e) {    
            e.printStackTrace();    
            return null;    
        }    
    }    
    
    /**  
     * 公钥加密  
     *   
     * @param data  
     * @param publicKey  
     * @return  
     * @throws Exception  
     */    
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)    
            throws Exception {    
        Cipher cipher = Cipher.getInstance("RSA", getProvider());    
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);    
        // 模长    
        int keyLen = publicKey.getModulus().bitLength() / 8;    
        // 加密数据长度 <= 模长-11    
        String[] datas = splitString(data, keyLen - 11);    
        String mi = "";    
        //如果明文长度大于模长-11则要分组加密    
        for (String s : datas) {    
            mi += bcd2Str(cipher.doFinal(s.getBytes()));    
        }    
        return mi;    
    }    
    
    /**  
     * 私钥解密  
     *   
     * @param data  
     * @param privateKey  
     * @return  
     * @throws Exception  
     */    
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)    
            throws Exception {    
        Cipher cipher = Cipher.getInstance("RSA", getProvider());    
        cipher.init(Cipher.DECRYPT_MODE, privateKey);    
        //模长    
        int keyLen = privateKey.getModulus().bitLength() / 8;    
        byte[] bytes = data.getBytes();    
        byte[] bcd = ASCIIToBCD(bytes, bytes.length);    
        //System.err.println(bcd.length);    
        //如果密文长度大于模长则要分组解密    
        String ming = "";    
        byte[][] arrays = splitArray(bcd, keyLen);    
        for(byte[] arr : arrays){    
            ming += new String(cipher.doFinal(arr));    
        }    
        return ming;    
    }    
    /**  
     * ASCII码转BCD码  
     *   
     */    
    public static byte[] ASCIIToBCD(byte[] ascii, int asc_len) {    
        byte[] bcd = new byte[asc_len / 2];    
        int j = 0;    
        for (int i = 0; i < (asc_len + 1) / 2; i++) {    
            bcd[i] = ascToBcd(ascii[j++]);    
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : ascToBcd(ascii[j++])) + (bcd[i] << 4));    
        }    
        return bcd;    
    }    
    public static byte ascToBcd(byte asc) {    
        byte bcd;    
    
        if ((asc >= '0') && (asc <= '9'))    
            bcd = (byte) (asc - '0');    
        else if ((asc >= 'A') && (asc <= 'F'))    
            bcd = (byte) (asc - 'A' + 10);    
        else if ((asc >= 'a') && (asc <= 'f'))    
            bcd = (byte) (asc - 'a' + 10);    
        else    
            bcd = (byte) (asc - 48);    
        return bcd;    
    }    
    /**  
     * BCD转字符串  
     */    
    public static String bcd2Str(byte[] bytes) {    
        char temp[] = new char[bytes.length * 2], val;    
    
        for (int i = 0; i < bytes.length; i++) {    
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);    
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');    
    
            val = (char) (bytes[i] & 0x0f);    
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');    
        }    
        return new String(temp);    
    }    
    /**  
     * 拆分字符串  
     */    
    public static String[] splitString(String string, int len) {    
        int x = string.length() / len;    
        int y = string.length() % len;    
        int z = 0;    
        if (y != 0) {    
            z = 1;    
        }    
        String[] strings = new String[x + z];    
        String str = "";    
        for (int i=0; i<x+z; i++) {    
            if (i==x+z-1 && y!=0) {    
                str = string.substring(i*len, i*len+y);    
            }else{    
                str = string.substring(i*len, i*len+len);    
            }    
            strings[i] = str;    
        }    
        return strings;    
    }    
    /**  
     *拆分数组   
     */    
    public static byte[][] splitArray(byte[] data,int len){    
        int x = data.length / len;    
        int y = data.length % len;    
        int z = 0;    
        if(y!=0){    
            z = 1;    
        }    
        byte[][] arrays = new byte[x+z][];    
        byte[] arr;    
        for(int i=0; i<x+z; i++){    
            arr = new byte[len];    
            if(i==x+z-1 && y!=0){    
                System.arraycopy(data, i*len, arr, 0, y);    
            }else{    
                System.arraycopy(data, i*len, arr, 0, len);    
            }    
            arrays[i] = arr;    
        }    
        return arrays;    
    }
    
    private static  Provider getProvider(){
    	return new org.bouncycastle.jce.provider.BouncyCastleProvider();
    }
    
    public static void main(String[] args) throws Exception{  
        HashMap<String, Object> map = getKeys();    
        //生成公钥和私钥    
        RSAPublicKey publicKey = (RSAPublicKey) map.get(RAS_Key_Public);    
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get(RAS_Key_Private);    
            
        //模    
        String modulus = publicKey.getModulus().toString();    
        System.out.println("pubkey modulus="+modulus);  
        //公钥指数    
        String public_exponent = publicKey.getPublicExponent().toString();  
        System.out.println("pubkey exponent="+public_exponent);  
        //私钥指数    
        String private_exponent = privateKey.getPrivateExponent().toString();    
        System.out.println("private exponent="+private_exponent);  
        //明文    
        String ming = "111";    
        //使用模和指数生成公钥和私钥    
        RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);    
        RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);    
        //加密后的密文    
        String mi = RSAUtils.encryptByPublicKey(ming, pubKey);    
        System.err.println("mi="+mi);    
        //解密后的明文    
        String ming2 = RSAUtils.decryptByPrivateKey(mi, priKey);    
        System.err.println("ming2="+ming2);    
    }
}
