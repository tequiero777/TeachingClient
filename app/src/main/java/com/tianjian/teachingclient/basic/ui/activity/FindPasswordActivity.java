/**
 * Copyright (c) 2013 Tianjian, Inc. All rights reserved.
 * This software is the confidential and proprietary information of 
 * Tianjian, Inc. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the 
 * license agreement you entered into with Tianjian.
 */
package com.tianjian.teachingclient.basic.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tianjian.teachingclient.R;

/**
 * 找回密码
 * <p>
 * Title: ResetPasswordActivity.java
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: Tianjian
 * </p>
 * <p>
 * team: TianjianTeam
 * </p>
 * <p>
 * 
 * @author: cheng
 *          </p>
 * @date 2014-8-5上午9:38:47
 * @version 1.0
 * 
 */
public class FindPasswordActivity extends BaseActivity implements OnClickListener,Handler.Callback{
	
	/** 输入的手机号*/
	private EditText cellNumber,username,identify_code;
	/**输入框:输入发送到手机的验证码*/
	private Button btn_getIdentifyCode,btn_sure;
	private Handler handler;
	private int backWards = 120;
	private String verifyCode = "";
	private String phone;
	private ImageButton button_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_findpassword_activity);
		handler = new Handler(this);
		initCellPhone();
	}
	/**
	 * 
	*布局文件初始化
	* @Title: initCellPhone
	* @return void
	* @throws
	* @author cheng
	 */
	private void initCellPhone(){
		//回退按钮
		button_back = (ImageButton) findViewById(R.id.findpassword_button_back);
		button_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		cellNumber = (EditText) findViewById(R.id.findpassword_cell_number);
		btn_getIdentifyCode = (Button) findViewById(R.id.findpassword_get_identifying_code);
		btn_getIdentifyCode.setOnClickListener(this);
		btn_sure = (Button) findViewById(R.id.findpassword_sure);
		btn_sure.setOnClickListener(this);
		username = (EditText) findViewById(R.id.username);
		identify_code = (EditText) findViewById(R.id.findpassword_identify_code);
	}
	
		/**
		 * 
		*倒计时开始
		* @Title: startBackwards
		* @return void
		* @throws
		* @author cheng
		 */
	private void startBackwards(){
		
		backWards = 120;
		//获取验证码
		/**倒计时线程*/
		new Thread() {
			boolean flag = true;
			@Override
			public void run() {
				while(flag){
					try {
						Thread.sleep(1000);
						backWards--;
						Message msg = handler.obtainMessage();
						msg.what = 1;
						msg.obj = backWards;
						handler.sendMessage(msg);
						if(backWards<=0){
							flag = false;
							handler.sendEmptyMessage(2);
							backWards = 120;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
//		getBackwards(username.getText().toString(), ID_number.getText().toString(), cellNumber.getText().toString());
	}
	
	/**
	 * 
	*向服务器请求重置密码
	* @Title: getBackwards
	* @param name
	* @param idNo
	* @param mobileTel
	* @return void
	* @throws
	* @author cheng
	 */
//	private void getBackwards(String name,String idNo,final String mobileTel){
//		HashMap<String, String> json = new HashMap<String, String>();
//		json.put("phone", mobileTel);
//		json.put("name", name);
//		json.put("idNO", idNo);
//		
//		NetWorkHepler.postDataSslOneSide(Constant.HTTP_SERVER_URL, JsonUtils.toJsonStr(json), "findMyPassword", new INetWorkCallBack() {
//			
//			@Override
//			public void onResult(String result) {
//				if(StringUtil.isEmpty(result)){
//					Utils.show(getApplicationContext(), "请求失败，请重试");
//					backWards = 0;
//					return;
//				}
//				Map<String,Object> map = JsonUtils.fromJson(result, Map.class);
//				if(map==null){
//					Utils.show(getApplicationContext(), "数据解释失败，请重试");
//					backWards = 0;
//					return ;
//				}
//				if(map.get("flag") != null && "1".equalsIgnoreCase(map.get("flag")+"")){
//					Utils.show(systemApplcation, map.get("err")+"");
//					backWards = 0;
//					return;
//				}
//				// result = {"data":[{"ISEXIST":"3248"}],"err":"成功找到该用户","flag":"0"}
//				String verifyValue = JsonUtils.getValue(result, "data");
//				List<Map<String,Object>> list = JsonUtils.fromJson(verifyValue, new TypeToken<List<Map<String,Object>>>(){});
//				if(list!=null&&list.size()>0){
//					verifyCode = list.get(0).get("ISEXIST")+"";
//					phone = mobileTel;
//				}else{
//					Utils.show(getApplicationContext(), "解析验证码失败");
//				}
//				
//			}
//			
//			@Override
//			public void onPreExecute() {
//				// TODO Auto-generated method stub
//				
//			}
//		}, this);
//		
//	}
	
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.findpassword_get_identifying_code:
			
			if(!btn_getIdentifyCode.isSelected()){
				if(cellNumber.getText().toString()!=null&&!cellNumber.getText().toString().trim().equalsIgnoreCase("")){
					if(cellNumber.getText().toString().trim().length()==11){
						btn_getIdentifyCode.setSelected(true);
						startBackwards();
					}else{
						Toast.makeText(FindPasswordActivity.this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(FindPasswordActivity.this, "输入的手机号不能为空", Toast.LENGTH_LONG).show();
				}
				
			}else{
				Toast.makeText(FindPasswordActivity.this, "验证码已发送，请稍等", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.findpassword_sure:
			if(backWards<=0){
				Toast.makeText(FindPasswordActivity.this, "验证码超时，请重新获取", Toast.LENGTH_LONG).show();
				return;
			}
			if(verifyCode!=null&&!verifyCode.equals("")){
				if(verifyCode.equals(identify_code.getText().toString())){
//					Intent in = new Intent();
//					in.setClass(this, ResetPasswordActivity.class);
//					in.putExtra("phone", phone);
//					startActivity(in);
//					finish();
					//重置密码
				}else{
					Toast.makeText(FindPasswordActivity.this, "验证码输入错误", Toast.LENGTH_LONG).show();
					return;
				}
			}else{
				Toast.makeText(this, "验证码不能为空", Toast.LENGTH_LONG).show();
				return ;
				
			}
			break;
		default:
			break;
		}
	}
	@SuppressLint("ResourceAsColor")
	@Override
	public boolean handleMessage(Message msg) {
		if(msg.what==1){
			int temp = (Integer) msg.obj;
			btn_getIdentifyCode.setBackgroundResource(R.drawable.button_selected_shap);
			btn_getIdentifyCode.setTextColor(Color.parseColor("#000000"));
			btn_getIdentifyCode.setText("("+temp+")重发");
			btn_getIdentifyCode.invalidate();
		}else if(msg.what==2){
			btn_getIdentifyCode.setBackgroundResource(R.drawable.button_selector);
			btn_getIdentifyCode.setText("获取验证码");
			btn_getIdentifyCode.setSelected(false);
			btn_getIdentifyCode.setTextColor(Color.parseColor("#FFFFFF"));
		}
		return false;
	}
//	@Override
//	public void onBackPressed() {
//		startActivity(new Intent(FindPasswordActivity.this,LoginActivity.class));
//		finish();
//		super.onBackPressed();
//	}
	
	
}
