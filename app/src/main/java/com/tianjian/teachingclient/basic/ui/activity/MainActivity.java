package com.tianjian.teachingclient.basic.ui.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tianjian.teachingclient.R;
import com.tianjian.teachingclient.application.SystemApplcation;
import com.tianjian.teachingclient.basic.ui.fragment.HomePageMentorFragment;
import com.tianjian.teachingclient.basic.ui.fragment.HomePageStudentFragment;
import com.tianjian.teachingclient.basic.ui.fragment.QuestionsMentorFragment;
import com.tianjian.teachingclient.basic.ui.fragment.QuestionsStudentFragment;
import com.tianjian.teachingclient.basic.ui.fragment.ResourcesFragment;
import com.tianjian.teachingclient.basic.ui.fragment.TasksMentorFragment;
import com.tianjian.teachingclient.basic.ui.fragment.TasksStudentFragment;
import com.tianjian.teachingclient.basic.ui.fragment.UserInfoMentorFragment;
import com.tianjian.teachingclient.basic.ui.fragment.UserInfoStudentFragment;

/**
 * TODO
 * <p>Title: 主界面</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Tianjian</p>
 * <p>team: TianjianTeam</p>
 * @author: Yehao
 * @date 2016年8月16日上午11:31:43
 * @version 1.0
 * 
 */

public class MainActivity extends FragmentActivity{
	private RadioGroup radioGroup;
	private RadioButton radio_home;
	private FragmentManager fragmentManager;
	public static final int GET_CONTACT = 10004;
	private String securityUserBaseinfoId,userId;
	private Handler handler;
	private Dialog dialog;
	private SystemApplcation systemApplcation;
	private SharedPreferences share;
	public static String FROM = null;
	public static MainActivity mainActivity ;
	private HomePageStudentFragment homepageFragment_stu;
	private HomePageMentorFragment homepageFragment_men;
	private ResourcesFragment resourcesFragment;
	private QuestionsStudentFragment questionsFragment_stu;
	private QuestionsMentorFragment questionsFragment_men;
	private TasksStudentFragment tasksFragment_stu;
	private TasksMentorFragment tasksFragment_men;
	private UserInfoStudentFragment userInfoFragment_stu;
	private UserInfoMentorFragment userInfoFragment_men;
	private int loginflag;
	
	public static void setFROM(String fROM) {
		FROM = fROM;
	}

	/**线程永久执行标识位*/
	private boolean threadFlag = false;
	
	
	public Handler getHandler() {
		return handler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_main);
		systemApplcation = (SystemApplcation) getApplication();
		systemApplcation.addActivity(this);
		mainActivity = this;
		
		Intent intent = getIntent();
		loginflag = intent.getIntExtra("loginflag", 0);
		initView();
	}

	private void initView(){
		radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		radio_home = (RadioButton) findViewById(R.id.home);
		
		radioGroup.check(R.id.home);
		if(loginflag == 1){
			showFragment(new HomePageStudentFragment());
		}else if(loginflag == 2){
			showFragment(new HomePageMentorFragment());
		}
		
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.home://主页
					if(loginflag == 1){
						if(null!=systemApplcation.getHomePageStudentFragment()){
							showFragment(systemApplcation.getHomePageStudentFragment());
						}else{
							homepageFragment_stu = new HomePageStudentFragment();
							systemApplcation.setHomePageStudentFragment(homepageFragment_stu);
							showFragment(homepageFragment_stu);
						}
					}else if(loginflag == 2){
						if(null!=systemApplcation.getHomePageMentorFragment()){
							showFragment(systemApplcation.getHomePageMentorFragment());
						}else{
							homepageFragment_men = new HomePageMentorFragment();
							systemApplcation.setHomePageMentorFragment(homepageFragment_men);
							showFragment(homepageFragment_men);
						}
					}
					break;
				case R.id.resources://课件
					if(null!=systemApplcation.getResourcesFragment()){
						showFragment(systemApplcation.getResourcesFragment());
					}else{
						resourcesFragment = new ResourcesFragment();
						systemApplcation.setResourcesFragment(resourcesFragment);
						showFragment(resourcesFragment);
					}
					break;
				case R.id.questions://提问
					if(loginflag == 1){
						if(null!=systemApplcation.getQuestionStudentFragment()){
							showFragment(systemApplcation.getQuestionStudentFragment());
						}else{
							questionsFragment_stu = new QuestionsStudentFragment();
							systemApplcation.setQuestionStudentFragment(questionsFragment_stu);
							showFragment(questionsFragment_stu);
						}
					}else if(loginflag == 2){
						if(null!=systemApplcation.getQuestionMentorFragment()){
							showFragment(systemApplcation.getQuestionMentorFragment());
						}else{
							questionsFragment_men = new QuestionsMentorFragment();
							systemApplcation.setQuestionMentorFragment(questionsFragment_men);
							showFragment(questionsFragment_men);
						}
					}
					break;
				case R.id.tasks://任务
					if(loginflag == 1){
						if(null!=systemApplcation.getTasksStudentFragment()){
							showFragment(systemApplcation.getTasksStudentFragment());
						}else{
							tasksFragment_stu = new TasksStudentFragment();
							systemApplcation.setTasksStudentFragment(tasksFragment_stu);
							showFragment(tasksFragment_stu);
						}
					}else if(loginflag == 2){
						if(null!=systemApplcation.getTasksMentorFragment()){
							showFragment(systemApplcation.getTasksMentorFragment());
						}else{
							tasksFragment_men = new TasksMentorFragment();
							systemApplcation.setTasksMentorFragment(tasksFragment_men);
							showFragment(tasksFragment_men);
						}
					}
					break;
				case R.id.my://个人中心
					if(loginflag == 1){
						if(null!=systemApplcation.getUserInfoStudentFragment()){
							showFragment(systemApplcation.getUserInfoStudentFragment());
						}else{
							userInfoFragment_stu = new UserInfoStudentFragment();
							systemApplcation.setUserInfoStudentFragment(userInfoFragment_stu);
							showFragment(userInfoFragment_stu);
						}
					}else if(loginflag == 2){
						if(null!=systemApplcation.getUserInfoMentorFragment()){
							showFragment(systemApplcation.getUserInfoMentorFragment());
						}else{
							userInfoFragment_men = new UserInfoMentorFragment();
							systemApplcation.setUserInfoMentorFragment(userInfoFragment_men);
							showFragment(userInfoFragment_men);
						}
					}
					break;
				}
			}
		});
		
	}
		

	public void showFragment(Fragment f) {
		if (fragmentManager == null) {
			fragmentManager = getSupportFragmentManager();
		}
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.mainFrameLayout, f);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	
	@Override
	public void onBackPressed() {
		Builder builder = new Builder(this);
		builder.setTitle("退出提示");
		builder.setMessage("确定要退出吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SystemApplcation sys = (SystemApplcation)getMainActivity().getApplication();
				sys.exit();
				sys = null;
				
				android.os.Process.killProcess(android.os.Process.myPid());
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
	protected void onDestroy() {
		threadFlag = false;
		((SystemApplcation)getApplication()).setCallBack(null);
		super.onDestroy();
	}


	public static Activity getMainActivity(){
		return mainActivity;
		
	}

	public SystemApplcation getSystemApplcation() {
		return systemApplcation;
	}

	public void setSystemApplcation(SystemApplcation systemApplcation) {
		this.systemApplcation = systemApplcation;
	}
	
}
