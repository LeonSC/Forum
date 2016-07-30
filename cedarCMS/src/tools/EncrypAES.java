package tools;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class EncrypAES {

	private final static String AESKEY="|z_c-";
	
	public static String encrypt(String content, String key) 
			throws NoSuchAlgorithmException, 
			NoSuchPaddingException, UnsupportedEncodingException, 
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(new StringBuffer(key).append(EncrypAES.AESKEY).toString().getBytes()));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec keyin = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		byte[] byteContent = content.getBytes("utf-8");
		cipher.init(Cipher.ENCRYPT_MODE, keyin);
		byte[] result = cipher.doFinal(byteContent);
		return EncrypAES.bytes2Hex(result);
	}
	
	public static String decrypt(String content, String key) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, 
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, 
			UnsupportedEncodingException
	{
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(new StringBuffer(key).append(EncrypAES.AESKEY).toString().getBytes()));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec keyin = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, keyin);
		byte[] result = cipher.doFinal(EncrypAES.hex2bytes(content));
		return new String(result,"utf-8");
	}
	
	
	private static String bytes2Hex(byte[] src)
	{         
		char[] res = new char[src.length*2];    
		final char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};    
		for(int i=0,j=0; i<src.length; i++){    
			res[j++] = hexDigits[src[i] >>>4 & 0x0f];    
			res[j++] = hexDigits[src[i] & 0x0f];    
		}    
            
		return new String(res);    
    }
	
	private static byte[] hex2bytes(String hexStr)
	{ 
        if (hexStr.length() < 1)
        {
        	return null;
        }
        byte[] result = new byte[hexStr.length()/2]; 
        for (int i = 0;i< hexStr.length()/2; i++) 
        {
        	int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16); 
        	int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16); 
        	result[i] = (byte) (high * 16 + low); 
        } 
        return result; 
	}
}