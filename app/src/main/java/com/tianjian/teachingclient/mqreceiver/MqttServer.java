package com.tianjian.teachingclient.mqreceiver;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

import com.tianjian.teachingclient.common.Constant;
import com.tianjian.teachingclient.util.JsonUtils;
import com.tianjian.teachingclient.util.ToastUtil;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.tianjian.teachingclient.R;
/**
 * TODO
 * <p>Title: MqttServer.java</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Tianjian</p>
 * <p>team: TianjianTeam</p>
 * @author: Leipeijie
 * @date 2015年11月10日下午1:44:24
 * @version 1.0
 * 
 */
public class MqttServer extends Service implements Callback{

    private String userName = "admin";
    private String passWord = "admin";
    private String clientId = "android-client";
    private String TAG = "MQTTSERVER";
    private Handler handler;
 
    private MqttClient client;
 
    private String myTopic = "android";
 
    private MqttConnectOptions options;
 
    private SharedPreferences sf;
    private NotificationManager notificationManager;
    //wake cpu  confirm socke is runing
    private WakeLock wakeLock;
    private AlarmManager alarmManager;
    private PendingIntent pi;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		handler = new Handler(this);
		sf = getSharedPreferences("TEACH", MODE_PRIVATE);
		notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		myTopic = ""+sf.getString("userName", "da");
		clientId =""+sf.getString("userName", "da");
		init();
	}

	/**
	*TODO
	* @Title: keepcpuwaklock
	* @return void
	* @throws
	* @author Leipeijie
	*/
	private void keepcpuwaklock() {
		Intent i = new Intent();
		i.setClass(this, MqttServer.class);
		pi = PendingIntent.getService(this, 0, i, 0);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 60*1000,60*1000, pi);
	}

	@Override
	public boolean handleMessage(Message msg) {
		 if(msg.what == 1) {
         } else if(msg.what == 2) {
//        	 keepcpuwaklock();
             try {
                 client.subscribe(myTopic, 2);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         } else if(msg.what == 3) {
             Toast.makeText(this, "推送服务器连接失败，系统正在重连", Toast.LENGTH_SHORT).show();
             if(client!=null && !client.isConnected())
             reconnected();
         }
		return false;
	}
	
	private void init() {
        try {
        	acquireWakeLock();
        	client = new MqttClient(Constant.MQTT_ADDRESS, clientId,new MemoryPersistence());
                       //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
                       //MQTT的连接设置
            options = new MqttConnectOptions();
                       //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(Constant.MQTT_CLEAN_SESSION);
                       //设置连接的用户名
            options.setUserName(userName);
                       //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
                        //设置回调
            client.setCallback(new MqttCallback(){

				@Override
				public void connectionLost(Throwable cause) {
					//连接丢失后，一般在这里面进行重连
                	ToastUtil.showToast(getBaseContext(), "连接丢失");
                	 connect();
				}

				@Override
				public void deliveryComplete(MqttDeliveryToken arg0) {
					 //publish后会执行到这里
//					Log.e("MQTT", "deliveryComplete。。。。。。。。。。。。。deliveryComplete---------"
//							+ token.isComplete());
//					pushNotify();  
				}

				@Override
				public void messageArrived(MqttTopic topicName, MqttMessage token)
						throws Exception {
					 //subscribe后得到的消息会执行到这里面
//                  Log.e("MQTT", "messageArrived:"+new String(token.getPayload()));
                   Message msg = handler.obtainMessage();
                   msg.what = 1;
                   msg.obj = topicName+"---"+msg.toString();
                   handler.sendMessage(msg);
                   pushNotify(new String(token.getPayload()));    
					
				}
            	
            });
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    private void connect() {
        new Thread(new Runnable() {
 
            @Override
            public void run() {
                try {
                    client.connect(options);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
	   

	/**
	*TODO
	* @Title: reconnected
	* @return void
	* @throws
	* @author Leipeijie
	*/
	private void reconnected() {
	Thread thread =	new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(50000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				connect();
				
			}
		});
		thread.setDaemon(true);
		thread.start();
	}
    //删除通知    
    private void clearNotification(){
        // 启动后删除之前我们定义的通知   
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);   
        notificationManager.cancel(0);  
  
    }
    
    /**
	*TODO
	* @Title: pushNotify
	* @return void
	* @throws
	* @author Leipeijie
	*/
	@SuppressWarnings({ "deprecation", "unchecked" })
	@SuppressLint("NewApi")
	private void pushNotify(String text) {
		int uuid = UUID.randomUUID().hashCode();
		HashMap<String, String> dataMap = (HashMap<String, String>) JsonUtils.formJsonStr(text, Map.class);
		String text2 = dataMap.get("name")+"："+dataMap.get("itemName")+":"+dataMap.get("itemValue");
		int currentapiVersion=android.os.Build.VERSION.SDK_INT ;
		Notification notification;
		Intent	notificationIntent= null;
		notificationIntent = new Intent();
		notificationIntent.setAction("com.tianjian.mda");
//		notificationIntent.setAction(Intent.ACTION_MAIN);
//		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		Bundle bundle = new Bundle();
		bundle.putInt("uuid", uuid);
		bundle.putSerializable("data", dataMap);
		notificationIntent.putExtras(bundle);
//		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		 Set the action and category so it appears that the app is being launched
		if(currentapiVersion>=16){
			PendingIntent contentItent = PendingIntent.getBroadcast(this, uuid, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);   
			notification = new Notification.Builder(this)
		     .setContentTitle(getString(R.string.push_title))
		     .setTicker(getString(R.string.push_title))
		     .setContentText(text2)
		     .setSmallIcon(R.drawable.ic_launcher)
		     .setWhen(System.currentTimeMillis())	
		     .setContentIntent(contentItent)
		     .build();
//		 	notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中   
//	        notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用   
	        notification.flags |= Notification.FLAG_SHOW_LIGHTS;   
	        notification.defaults = Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE; 
	        notification.ledARGB = Color.BLUE;   
	        notification.ledOnMS =5000; //闪光时间，毫秒
		}else{
			notification =new Notification(R.drawable.ic_launcher,   
					getString(R.string.push_title), System.currentTimeMillis()); 
			 //FLAG_AUTO_CANCEL   该通知能被状态栏的清除按钮给清除掉
	        //FLAG_NO_CLEAR      该通知不能被状态栏的清除按钮给清除掉
	        //FLAG_ONGOING_EVENT 通知放置在正在运行
	        //FLAG_INSISTENT     是否一直进行，比如音乐一直播放，知道用户响应
//	        notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中   
//	        notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用   
	        notification.flags |= Notification.FLAG_SHOW_LIGHTS;   
	        //DEFAULT_ALL     使用所有默认值，比如声音，震动，闪屏等等
	        //DEFAULT_LIGHTS  使用默认闪光提示
	        //DEFAULT_SOUNDS  使用默认提示声音
	        //DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission android:name="android.permission.VIBRATE" />权限
	        notification.defaults = Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;  
	        //叠加效果常量
	        //notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
	        notification.ledARGB = Color.BLUE;   
	        notification.ledOnMS =5000; //闪光时间，毫秒
	          
	        // 设置通知的事件消息   
	        CharSequence contentTitle =getString(R.string.push_title); // 通知栏标题   
	        CharSequence contentText =text2; // 通知栏内容   
	        PendingIntent contentItent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);   
		}
		 notificationManager.notify(uuid, notification);
		 wakeUpAndUnlock(this);
		 
	}
	
	
	/** 
     * 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行 
     */  
    private void acquireWakeLock() {  
        if (null == wakeLock) {  
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);  
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK  
                    | PowerManager.ON_AFTER_RELEASE, MqttServer.this.getClass().getCanonicalName());  
            if (null != wakeLock) {  
                Log.i(TAG, "call acquireWakeLock");  
                wakeLock.acquire();  
            }  
        }  
    }  
  
    // 释放设备电源锁  
    private void releaseWakeLock() {  
        if (null != wakeLock && wakeLock.isHeld()) {  
            Log.i(TAG, "call releaseWakeLock");  
            wakeLock.release();  
            wakeLock = null;  
        }  
    }
    
    public  void wakeUpAndUnlock(Context context){  
    	PowerManager pm =(PowerManager) getSystemService(Context.POWER_SERVICE);
    	WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
        //点亮屏幕  
    	if(!pm.isScreenOn())
        wl.acquire(); 
    }  
    
    @Override
    public void onDestroy() {
    	releaseWakeLock();
    	clearNotification();
    	try {
    		if(client.isConnected())
			client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}finally{
			if(pi!=null && alarmManager!=null)
			alarmManager.cancel(pi);
			client = null;
	    	System.gc();
	    	super.onDestroy();
		}
    	
    }
}
