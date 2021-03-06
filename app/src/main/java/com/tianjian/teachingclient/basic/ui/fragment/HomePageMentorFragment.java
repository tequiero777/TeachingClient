/**
 * Copyright (c) 2013 Tianjian, Inc. All rights reserved.
 * This software is the confidential and proprietary information of 
 * Tianjian, Inc. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the 
 * license agreement you entered into with Tianjian.
 */
package com.tianjian.teachingclient.basic.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tianjian.teachingclient.R;
import com.tianjian.teachingclient.application.SystemApplcation;
import com.tianjian.teachingclient.basic.bean.InLoginSrv.InLoginSrvOutputItem;
import com.tianjian.teachingclient.basic.bean.InQueryQuestionSrv.InQueryQuestionSrvOutputCollection;
import com.tianjian.teachingclient.basic.bean.InQueryQuestionSrv.InQueryQuestionSrvOutputItem;
import com.tianjian.teachingclient.basic.bean.InQueryQuestionSrv.InQueryQuestionSrvResponse;
import com.tianjian.teachingclient.basic.ui.activity.QuestionOverViewActivity;
import com.tianjian.teachingclient.basic.ui.activity.SendTaskActivity;
import com.tianjian.teachingclient.util.ToastUtil;
import com.tianjian.teachingclient.util.network.callback.INetWorkCallBack;
import com.tianjian.teachingclient.util.network.helper.NetWorkHepler;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 导师端主页
 * <p>Title: HomePageMentor.java</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Tianjian</p>
 * <p>team: TianjianTeam</p>
 * @author: Yehao
 * @date 2016年8月17日上午8:56:52
 * @version 1.0
 * 
 */
public class HomePageMentorFragment extends BaseFragment{
	private View rootView;
	private Button send_button;
	private InLoginSrvOutputItem userDict;
	private SystemApplcation systemApplcation;
	private TextView taskText,taskTime,question_name,question_content,question_time,question_flag;
	private LinearLayout imagelayout,question_layout;
	private ImageView imageView;
	private InQueryQuestionSrvOutputItem question_listitem;
	private static final int UPDATE_CHECKCOMPLETED = 1;//已经完成检测新版本
	private static final int UPDATE_DOWNLOADING = 2; //下载中
	private static final int UPDATE_DOWNLOAD_ERROR = 3; //下载出错
	private static final int UPDATE_DOWNLOAD_COMPLETED = 4; //下载完成
	private static final int UPDATE_DOWNLOAD_CANCELED = 5;//取消下载
	private SwipeRefreshLayout refresh_layout = null;//刷新控件
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(rootView == null){
			rootView = inflater.inflate(R.layout.homepage_mentor_layout, null);
			systemApplcation = (SystemApplcation) getActivity().getApplication();
			userDict = systemApplcation.getUserDict();
			initView();	
		}
		systemApplcation = (SystemApplcation) getActivity().getApplication();
		userDict = systemApplcation.getUserDict();
		return rootView;
	}
	
	private void initView() {
		send_button = (Button) rootView.findViewById(R.id.home_mentor_sendtask);
		send_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SendTaskActivity.class);
				startActivity(intent);	
			}
		});
		
		question_name = (TextView) rootView.findViewById(R.id.home_men_question_name);
		question_content = (TextView) rootView.findViewById(R.id.home_men_question_content);
		question_time = (TextView) rootView.findViewById(R.id.home_men_question_time);
		question_flag = (TextView) rootView.findViewById(R.id.home_men_question_flag);
		imageView = (ImageView) rootView.findViewById(R.id.home_men_question_imageview);
		imagelayout = (LinearLayout) rootView.findViewById(R.id.home_men_question_imagelayout);
		imagelayout.removeAllViews();
		question_layout = (LinearLayout) rootView.findViewById(R.id.home_men_layout);
		question_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null!=question_listitem){
					Gson gson = new Gson();
					Intent intent = new Intent(getActivity(), QuestionOverViewActivity.class);
//					intent.putExtra("list", gson.toJson(question_listitem));
					intent.putExtra("id", question_listitem.getQUESTIONID());
					startActivity(intent);
				}else{
					ToastUtil.showToast(getActivity(), "暂无最新问题！");
				}
			}
		});
		
		HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
		hashMap2.put("USERNAME", userDict.getUSERNAME());
		hashMap2.put("OPERATE_TYPE", "5");
		queryNewQuestion(hashMap2);
		
		refresh_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_men_layout);
	    refresh_layout.setColorSchemeResources(R.color.green, R.color.gray, R.color.blue, R.color.white);//设置跑动的颜色值
	    refresh_layout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				initView();
			}
		});
	}
	
	private void queryNewQuestion(HashMap<String, Object> hashMap2) {
		NetWorkHepler.postWsData("queryquestionWs", "process", hashMap2, new INetWorkCallBack() {
			SoapObject objectResult;
			@Override
			public void onResult(Object result) {
				if(result == null){
					ToastUtil.showToast(getActivity(), "查询数据失败！");
				}else if(result instanceof SoapObject) {
					objectResult = (SoapObject) result;
				}else{
					ToastUtil.showToast(getActivity(), "服务器连接失败！");
					if(refresh_layout.isRefreshing()){
						refresh_layout.setRefreshing(false);
					}
					return;
				}
				InQueryQuestionSrvResponse response = new InQueryQuestionSrvResponse();
				try {
					for(int i=0;i<objectResult.getPropertyCount();i++){
						response.setProperty(i, objectResult.getProperty(i));
					}
				} catch (Exception e) {
					ToastUtil.showToast(getActivity(), "数据出错了，请重试！");
				}
				
				if("Y".equals(response.getErrorFlag())){
					if(refresh_layout.isRefreshing()){
						refresh_layout.setRefreshing(false);
						ToastUtil.showToast(getActivity(), "刷新完成！");
					}
					
					if(response.getErrorMessage().equals("暂时没有提问")){
						question_layout.setVisibility(View.GONE);
					}else{
						question_layout.setVisibility(View.VISIBLE);
						List<InQueryQuestionSrvOutputItem> list = new ArrayList<InQueryQuestionSrvOutputItem>();
						Gson gson = new Gson();
						InQueryQuestionSrvOutputCollection collection = gson.fromJson(response.getJSON(), new TypeToken<InQueryQuestionSrvOutputCollection>() {}.getType());
						list = collection.getInQueryQuestionSrvOutputItem();
						question_listitem = list.get(0);
						question_name.setText(list.get(0).getNAME());
						question_content.setText(list.get(0).getCONTENT().split("！@#-")[0]+"，"+list.get(0).getCONTENT().split("！@#-")[1]+"，"+list.get(0).getCONTENT().split("！@#-")[2]+"岁。"+list.get(0).getCONTENT().split("！@#-")[3]);
						question_time.setText(list.get(0).getTIME().replace(".0", ""));
						if(list.get(0).getCLASSTYPE().equals("1")){
							question_flag.setText("常规");
						}else{
							question_flag.setText("会诊");
						}
						if(null!=list.get(0).getIMAGE()){
							imageView.setImageBitmap(BitmapFactory.decodeByteArray(list.get(0).getIMAGE(), 0, list.get(0).getIMAGE().length));
						}
						
						if(null!=list.get(0).getRESOURCESDETAILLINE() && list.get(0).getRESOURCESDETAILLINE().getInQueryQuestionResourcesDetailSrvOutputItem().size()>0){
							for (int i = 0; i < list.get(0).getRESOURCESDETAILLINE().getInQueryQuestionResourcesDetailSrvOutputItem().size(); i++) {
								String currentFileUrl = list.get(0).getRESOURCESDETAILLINE().getInQueryQuestionResourcesDetailSrvOutputItem().get(i).getRESOURCESURL();
								String currentFileName = list.get(0).getRESOURCESDETAILLINE().getInQueryQuestionResourcesDetailSrvOutputItem().get(i).getRESOURCESNAME();
								String currentFileType = list.get(0).getRESOURCESDETAILLINE().getInQueryQuestionResourcesDetailSrvOutputItem().get(i).getRESOURCESURL().substring(list.get(0).getRESOURCESDETAILLINE().getInQueryQuestionResourcesDetailSrvOutputItem().get(i).getRESOURCESURL().lastIndexOf("."),list.get(0).getRESOURCESDETAILLINE().getInQueryQuestionResourcesDetailSrvOutputItem().get(i).getRESOURCESURL().length()).toLowerCase();
								
								File file = new File(Environment.getExternalStorageDirectory()+"/分级诊疗下载文件/"+currentFileName+currentFileType);
								if(!file.exists()){
									download(currentFileUrl,currentFileName,currentFileType);
								}else{
									ImageView iv = new ImageView(getActivity());
									String path=Environment.getExternalStorageDirectory()+"/分级诊疗下载文件/"+currentFileName+currentFileType;
									iv.setImageBitmap(loadBitmap(path,200,200));
									iv.setLayoutParams(new LayoutParams(200, 200));
									iv.setPadding(0, 0, 20, 0);
									imagelayout.addView(iv);
								}
							}
						}
					}
				}else{
					ToastUtil.showToast(getActivity(), "查询数据失败！");
				}
				
			}
			
			@Override
			public void onProgressUpdate(Integer[] values) {
			}
			
			@Override
			public void onPreExecute() {
			}
		});
	}
	
	// 下载
	public void download(final String filePath, final String fileName, final String fileType){
		new Thread() {			
			@Override  
		        public void run() {  
		            try {  
		                URL url = new URL(filePath);  
		                //新建连接并获取资源
		                HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
		                conn.setConnectTimeout(5000);
		                conn.setRequestMethod("POST");
		                conn.setRequestProperty("Accept-Encoding", "identity"); 
		                conn.connect();  
		                int length = conn.getContentLength();
		                
		                InputStream is = conn.getInputStream();  
		                
		                File file = new File(Environment.getExternalStorageDirectory()+"/分级诊疗下载文件/");
		                if(!file.exists()){
		                	file.mkdirs();
		                }
		                String saveDir = Environment.getExternalStorageDirectory() + "/分级诊疗下载文件/";
		                
		                //如果文件存在，就先删除
		                File sdfile = new File(saveDir,fileName+fileType);  		                
		                if(sdfile.exists())
		                {
		                	sdfile.delete();
		                }
		                
		                FileOutputStream fos = new FileOutputStream(sdfile);  
		                 
		                int count = 0;  
		                byte buf[] = new byte[512];  
		                Boolean canceled = false;
		                do{  
		                    int numread = is.read(buf);  
		                    count += numread;  
		                   //把进度传给updateHandler更新ProgressDialog,从消息池中拿来一个msg 不需要另开辟空间
		                    updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOADING)); 
		                    if(numread <= 0){    
		                    	Message message=new Message();  
		                    	Bundle bundle=new Bundle();  
		                        bundle.putString("currentFileName", fileName); 
		                        bundle.putString("currentFileType", fileType);
		                        message.setData(bundle);
		                        message.what=UPDATE_DOWNLOAD_COMPLETED;
		                        updateHandler.sendMessage(message);
//			                    	updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_COMPLETED);
		                        break;  
		                    }  
		                    fos.write(buf,0,numread);  
		                }while(!canceled);  
			                fos.close();  
			                is.close();  
		            } catch (MalformedURLException e) {  
		                e.printStackTrace(); 
		                updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOAD_ERROR,e.getMessage()));
		            } catch(IOException e){  
		                e.printStackTrace();  
		                updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOAD_ERROR,e.getMessage()));
		            }  
		              
		        } 
		}.start();
	}
		
	Handler updateHandler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case UPDATE_DOWNLOADING:
				//更新ProgressDialog的进度
				break;
			case UPDATE_DOWNLOAD_ERROR:
				//下载失败
				downloadCompleted(false, msg.obj!=null?msg.obj.toString():"",null,null);
				break;
			case UPDATE_DOWNLOAD_COMPLETED:
				//下载完成
				String name = msg.getData().getString("currentFileName");
				String type = msg.getData().getString("currentFileType");
				downloadCompleted(true, "",name,type);
				break;
			default:
				break;
			}
		}
	};
	
	//更新ProgressDialog的进度
	public void downloadProgressChanged(int progress) {

	}
	
	//下载完成
	public void downloadCompleted(Boolean sucess, String errorMsg, String name, String type) {
		if (sucess) {
			ImageView iv = new ImageView(getActivity());
			String path=Environment.getExternalStorageDirectory()+"/分级诊疗下载文件/"+name+type;
			iv.setImageBitmap(loadBitmap(path,100,200));
			iv.setLayoutParams(new LayoutParams(100, 200));
			iv.setPadding(0, 0, 20, 0);
			imagelayout.addView(iv);
		} else {
			ToastUtil.showToast(getActivity(), "下载失败！");
		}
	}
	
	public static Bitmap loadBitmap(String url, int width, int height)
	{
	    Bitmap bitmap = null;
	    if (url == null || !new File(url).exists()) return bitmap;
	    try
	    {
	        BitmapFactory.Options opts = new BitmapFactory.Options();
	        opts.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(url, opts);
	        opts.inSampleSize = calculateSampleSize(opts, width, height);
	        opts.inJustDecodeBounds = false;
	        opts.inPreferredConfig = Bitmap.Config.RGB_565;
	        opts.inPurgeable = true;
	        opts.inInputShareable = true;
	        bitmap = BitmapFactory.decodeStream(new FileInputStream(url), null, opts);
	        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);
	        return bitmap;
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    return bitmap;
	}

	private static int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	    if (height > reqHeight || width > reqWidth)
	    {
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
		while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth)
		{
			inSampleSize *= 2;
		}
	    }
	    return inSampleSize;
	}
}
