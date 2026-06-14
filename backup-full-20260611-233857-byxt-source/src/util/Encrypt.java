package util;

import java.security.MessageDigest;

public class Encrypt {
	public static void main(String[] args){
		System.out.println(MD5("root"));
	}
	public static String MD5(String oldStr){
	 char[] hexDigits={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	 try{
	 	byte[] oldBytes=oldStr.getBytes();
	 	MessageDigest md=MessageDigest.getInstance("MD5");
	 	md.update(oldBytes);
	 	byte[] newBytes=md.digest();
	 	char[] newStr=new char[32];
	 	for(int i=0;i<16;i++){
	 		byte tmp=newBytes[i];
	 		newStr[2*i]=hexDigits[tmp>>>4 & 0xf];
	 		newStr[2*i+1]=hexDigits[tmp & 0xf];
	 	}
	 	return new String(newStr);
	 	
	 }catch(Exception e){
	 	System.out.println(e.toString());
	 	return null;
	 }
		
	}
}
