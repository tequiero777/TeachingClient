package com.tianjian.teachingclient.util.network.callback;

public abstract class INetWorkCallBack {
	/**
	 * 加载之前处理的方法
	 */
	public abstract void onPreExecute();
	
	/**
	 * 回调成功方法
	 * @param json
	 * @return
	 */
	public abstract void onResult(Object result);

	/***
	 * 进度
	 * @param values
	 */
	public abstract void onProgressUpdate(Integer[] values);
	
	
	
}
