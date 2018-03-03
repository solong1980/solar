package com.lszyhb.common;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.regex.Pattern;

public class UtilHelper {

	public UtilHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean isInteger(String str) {    
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
		return pattern.matcher(str).matches();    
	}  

	public static String getFromAssets(Context context,String fileName){ 
	    try { 
	         InputStreamReader inputReader = new InputStreamReader( context.getResources().getAssets().open(fileName) ); 
	         BufferedReader bufReader = new BufferedReader(inputReader);
	         String line="";
	         String Result="";
	         while((line = bufReader.readLine()) != null)
	            Result += line;
	         return Result;
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    }
	    return null;
	} 

	public static boolean isPhone(String inputText) {
		if( inputText == null || inputText.trim().length() == 0 ){//输入手机号码为空
			return false;
		}
		inputText = inputText.trim();
        if( inputText.startsWith("+86") ){
        	inputText = inputText.substring(3);
        }
        //是否全部是数字
    	Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
    	boolean ret = pattern.matcher(inputText).matches();
    	if( ret == false ) return false;//不全部是数字，直接返回false
        //全部是数字，判断是否是11位数字
        if( inputText.length() == 11 ){
        	return true;
        }
        return false;
	}
	
	public static String getRandomString(int length) { //length表示生成字符串的长度
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }  
	
	public static boolean checkPhoneValid(String phone,Context context){
		if( phone == null || phone.trim().length() == 0 ){//电话号码不可以为空，必须有效
			Toast.makeText(context, "手机号不能为空！",Toast.LENGTH_LONG).show();
			return false;
		}
		if( isPhone(phone) == false ){//电话号码格式错误
			Toast.makeText(context, "手机号格式错误！", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

}
