package com.tianjian.teachingclient.util;

import android.content.res.AssetManager;

import com.tianjian.teachingclient.application.SystemApplcation;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
 	private static Properties props;
	static{
		 if(props == null){
			 props = new Properties();
		 }
//		  try {
//		   /*InputStream in = PropertiesUtil
//				   				.class
//				   				.getResourceAsStream("/src/main/res/raw/config.properties");*/
////			  InputStream in = ClassLoader.getSystemResourceAsStream("config.properties");
//			  File f = new File("/config.properties");
//			  InputStream in =  new FileInputStream(f);
////			  props.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties"));
//			  props.load(in);
//		  } catch (Exception e1) {
//		   e1.printStackTrace();
//		  }
//		try {
//			FileInputStream is=new FileInputStream("/config.properties");
////			InputStream is=PropertiesUtil.class.getClassLoader().getResourceAsStream("/config.properties");
//			props.load(is);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	 public static String getProperty(String key){
		 return props.getProperty(key);
	 }

//	private static ResourceLoader loader = ResourceLoader.getInstance();
//	private static ConcurrentMap<String, String> configMap = new ConcurrentHashMap<String, String>();
//	private static final String DEFAULT_CONFIG_FILE = "config.properties";
//
//	private static Properties prop = null;
//
//	public static String getStringByKey(String key, String propName) {
//		try {
//			prop = loader.getPropFromProperties(propName);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//		key = key.trim();
//		if (!configMap.containsKey(key)) {
//			if (prop.getProperty(key) != null) {
//				configMap.put(key, prop.getProperty(key));
//			}
//		}
//		return configMap.get(key);
//	}
//
//	public static String getStringByKey(String key) {
//		return getStringByKey(key, DEFAULT_CONFIG_FILE);
//	}
//
//	public static Properties getProperties() {
//		try {
//			return loader.getPropFromProperties(DEFAULT_CONFIG_FILE);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
}

