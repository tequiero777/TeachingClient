package com.tianjian.teachingclient.util;

import java.util.Date;

public class NaNN {
	public static boolean isNotNull(String s){
		return s != null && s.trim().length() > 0;
	}
	public static boolean isNull(String s){
		return s == null || s.trim().length() == 0;
	}
	public static boolean isNull(Date s){
		return s == null ;
	}
	
}
