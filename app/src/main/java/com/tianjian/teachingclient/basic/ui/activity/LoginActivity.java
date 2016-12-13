package com.tianjian.teachingclient.basic.ui.activity;

import java.io.File;
import java.util.HashMap;

import org.ksoap2.serialization.SoapObject;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tianjian.teachingclient.R;
import com.tianjian.teachingclient.application.SystemApplcation;
import com.tianjian.teachingclient.basic.bean.InLoginSrv.InLoginSrvOutputItem;
import com.tianjian.teachingclient.basic.bean.InLoginSrv.InLoginSrvResponse;
import com.tianjian.teachingclient.basic.ui.view.CustomDialog;
import com.tianjian.teachingclient.basic.ui.view.CustomerProgress;
import com.tianjian.teachingclient.mqreceiver.MqttServer;
import com.tianjian.teachingclient.util.CircleImageView;
import com.tianjian.teachingclient.util.StringUtil;
import com.tianjian.teachingclient.util.ToastUtil;
import com.tianjian.teachingclient.util.network.callback.INetWorkCallBack;
import com.tianjian.teachingclient.util.network.helper.NetWorkHepler;
/**
 * TODO
 * <p>Title: 登录页面</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Tianjian</p>
 * <p>team: TianjianTeam</p>
 * @author: Yehao
 * @date 2016年7月15日上午11:31:43
 * @version 1.0
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener{
	private TextView check_stu,check_mentor;
	private EditText username;//用户名
	private EditText pwd;//密码
	private CheckBox rePassword;//记住密码
	private Button subBut;//登录按钮
	private Button regBut;//注册按钮
	private ImageButton claer_but;
	private ImageButton toggleButton;
	private boolean toggleBut=false;
	private SystemApplcation applcation;
	String loginIp="";
	public static String FROM = null;
	public static String class_url=null;
	private Intent intent;
	private File file;
	private Bitmap mImage = null;
	private byte[] mImageData = null;
	// authid为6-18个字符长度，用于唯一标识用户
	private String mAuthid = null;
	private CustomDialog progressDialog;
	private ProgressDialog mProDialog;
	private SharedPreferences preferences;
	private int flag = 1;//用户类型标志   1为学生 2为导师
	private CircleImageView user_image;
	private TextView forgetPassword;
	
	public static void setClazz(String class_url) {
		LoginActivity.class_url = class_url;
	}

	public static void setFROM(String fROM) {
		FROM = fROM;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_login);
		
		applcation = (SystemApplcation)getApplication();
		applcation.addActivity(this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		initView();
	}
	
	private void initView(){
//		Intent service = new Intent(LoginActivity.this, MqttServer.class);
//		service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		this.startService(service);
		
		preferences = getSharedPreferences("TEACH", MODE_PRIVATE);
		
		user_image = (CircleImageView) findViewById(R.id.login_userimage);
		if(preferences!=null){
			if(preferences.getString("imagebitmap", null)!=null){
				byte[] bitmapArray;  
	            bitmapArray = Base64.decode(preferences.getString("imagebitmap", null), Base64.DEFAULT);  
				user_image.setImageBitmap(BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length));
			}
		}
		
		
		claer_but = (ImageButton)findViewById(R.id.claer_but);
		claer_but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				username.setText("");
			}
		});
		toggleButton = (ImageButton)findViewById(R.id.claer_but_1);
		toggleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(toggleBut==false){
					pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					toggleBut=true;
				}else{
					pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
					toggleBut=false;
				}
				
			}
		});
		
		username = (EditText)findViewById(R.id.username);
		pwd = (EditText)findViewById(R.id.pwd);
		String str = getIntent().getStringExtra("phone");
		if(StringUtil.isEmpty(str)){
			username.setText(preferences!=null?preferences.getString("username", null):"");
			pwd.setText(preferences!=null?preferences.getString("password", null):"");
		}else{
			username.setText(str);
		}
		rePassword = (CheckBox)findViewById(R.id.rePassword);
		rePassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor edit = preferences.edit();
				if (!isChecked) {
					edit.putString("username", "");
					edit.putString("password", "");
				}
				edit.commit();
			}
		});
		subBut = (Button)findViewById(R.id.subBut);
		username.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().equals("")){
					claer_but.setVisibility(View.GONE);
				}else{
					claer_but.setVisibility(View.VISIBLE);
				}
				
			}
		});
		pwd.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().equals("")){
					toggleButton.setVisibility(View.GONE);
				}else{
					toggleButton.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
       subBut.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(StringUtil.isBlank(username.getText()+"") || StringUtil.isBlank(pwd.getText()+"")){
				ToastUtil.showToast(LoginActivity.this, "用户名或密码不能为空");
				return;
			}
			Editor editor = preferences.edit();
			if(rePassword.isChecked()){
				editor.putString("userName", username.getText()+"");
				editor.putString("userPwd", pwd.getText()+"");
			}else{
				editor.putString("userName", username.getText()+"");
				editor.putString("userPwd", "");
				
			}
			editor.putBoolean("savePwd", rePassword.isChecked());
			
			editor.commit();
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("USERNAME", username.getText());
			hashMap.put("PASSWORD", pwd.getText());
			hashMap.put("OPERATE_TYPE", "1");
			intent = new Intent();
			intent.setClass(LoginActivity.this, MainActivity.class);
			queryLogin(hashMap,intent);
			}
       });
       
       
		if(preferences.getBoolean("savePwd", false)){
			username.setText(preferences.getString("userName", ""));
			pwd.setText(preferences.getString("userPwd", ""));
		}else{
			username.setText(preferences.getString("userName", ""));
		}
		rePassword.setChecked(preferences.getBoolean("savePwd", false));
		
		rePassword.setOnCheckedChangeListener(this);
		if(getIntent().getExtras()!=null&& getIntent().getExtras().getSerializable("data")!=null){
			username.setText(preferences.getString("userName", ""));
			pwd.setText(preferences.getString("userPwd", ""));
			subBut.performClick();
		}
		
		forgetPassword = (TextView) findViewById(R.id.forgetPassword);
		forgetPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  
		forgetPassword.setText("忘记密码");
		forgetPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 修改密码
				startActivity(new Intent(LoginActivity.this,FindPasswordActivity.class));
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//				finish();
			}
		});
	}
	
	private void queryLogin(HashMap<String, Object>  request,final Intent  intent) {
		final CustomerProgress customerProgress =  new CustomerProgress(this,com.tianjian.teachingclient.R.style.customer_dialog);
		NetWorkHepler.postWsData("loginWs", "process", request, new INetWorkCallBack() {
			SoapObject objectResult;
			@Override
			public void onResult(Object result) {
				customerProgress.dismissDialog(customerProgress);
				if(result == null){
					ToastUtil.showToast(getApplication(), "登陆出错了！");
				}else if(result instanceof SoapObject) {
					objectResult = (SoapObject) result;
				}else{
					ToastUtil.showToast(getApplication(), "服务器连接失败");
					return;
				}
				InLoginSrvResponse response = new InLoginSrvResponse();
				try {
					for(int i=0;i<objectResult.getPropertyCount();i++){
						response.setProperty(i, objectResult.getProperty(i));
					}
				} catch (Exception e) {
					ToastUtil.showToast(getApplication(), "数据出错了，请重试！");
				}
				
				if(response.getInLoginSrvOutputCollection()==null 
						||response.getInLoginSrvOutputCollection().getInLoginSrvOutputItem()==null
						||response.getInLoginSrvOutputCollection().getInLoginSrvOutputItem().get(0)==null){
					ToastUtil.showToast(getApplication(), "用户名或者密码错误！");
					return;
				}
				if("Y".equals(response.getErrorFlag())){
					//启动mq服务
					Intent service = new Intent(LoginActivity.this, MqttServer.class);
					service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startService(service);
					
					boolean isRuning = getIntent().getExtras()!=null&& getIntent().getExtras().getSerializable("data")!=null;
					if(!isRuning){
//						ToastUtil.showToast(getApplication(),"密码验证成功！");
					}
					SystemApplcation systemApplcation = (SystemApplcation) getApplication();
					systemApplcation.setUserDict(response.getInLoginSrvOutputCollection().getInLoginSrvOutputItem().get(0));
					InLoginSrvOutputItem userInfo = response.getInLoginSrvOutputCollection().getInLoginSrvOutputItem().get(0);
					
					if(isRuning){
						intent.putExtras(getIntent().getExtras());
					}
					if(userInfo.getTYPE().equals("1")){
						ToastUtil.showToast(getApplication(),"登录成功！");
						intent.putExtra("loginflag", 1);
						startActivity(intent);
						finish();
					}else if(userInfo.getTYPE().equals("2")){
						ToastUtil.showToast(getApplication(),"登录成功！");
						intent.putExtra("loginflag", 2);
						startActivity(intent);
						finish();
					}
				}else{
					ToastUtil.showToast(getApplication(), "登陆遇到问题，请重试!");
				}
				
			}
			
			@Override
			public void onProgressUpdate(Integer[] values) {
				
			}
			
			@Override
			public void onPreExecute() {
				customerProgress.show();
				
			}
		});
	}

	public void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}
	
	
	@Override
	public void onBackPressed() {
		Builder builder = new Builder(this);
		builder.setTitle("退出提示");
		builder.setMessage("确定要退出吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
				systemApplcation = null;
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
