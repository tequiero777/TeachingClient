package com.tianjian.teachingclient.util.network.internet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;

import com.tianjian.teachingclient.basic.bean.FormFile;

/**
 * 
 * @author luoqb
 *
 * @param <T> 
 */
public interface INetWorkCore  {
	
	/**
     * 提交post 提交的方式
     * @param url   请求的url地址
     * @param clazz 请求的对象
     * @return  返回json字符串
     */
	public String postDataBySsl(String url, String jsonStr, String authorization, String mehodName);
	
    /**
     * 提交post 提交表单的方式
     * @param url   请求的url地址
     * @param clazz 请求的对象
     * @return  返回json字符串
     */
	public String postDataFormBySsl(String url, Map<String, Object> params, String authorization);
	
	/***
	 * 
	* ssl 单向 验证的 数据请求 默认的参数名：是 attr 后台的参数名字必须是  attr
	* @Title: postDataOneSideSsl
	* @param url   地址
	* @param methodName 方法名字
	* @param jsonAttr 参数值
	* @param authorization 鉴权
	* @return
	* @return String json 字符串
	* @throws
	* @author Leipeijie
	 */
	public String postDataOneSideSsl(String url, String methodName, String jsonAttr, Activity baseActivity);
	
	
	/**
	 * post多文件上传
	 * @param path   请求的url地址
	 * @param params 参数
	 * @param files  文件集合
	 * @return
	 * @throws Exception
	 */
	public String postFile(String path, Map<String, String> params, List<FormFile> fileByte) throws Exception;
	
	
	/***
	 * 
	* ws数据请求
	* @Title: postWsData
	* 
	* @param methodName 方法名
	* @param request json字符串
	* @param paramsName 参数名
	* @param adrressName 地址名称
	* @return
	* @return String 返回结果
	* @throws
	* @author Leipeijie
	 */
	
	public Object postWsData(String adrressName, String methodName, HashMap<String, Object> request);
	
	
//	
//	public InputStream loadImage(String url);
}
