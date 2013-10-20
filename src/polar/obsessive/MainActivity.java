package polar.obsessive;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import polar.obsessive.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class MainActivity extends FragmentActivity {
	
	private NotificationManager notificationManager;
	private UiLifecycleHelper uiHelper;
	private Cron cronTab;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	public void startCron(View v){
		Log.i("alarm", "STARTING!!!");
		cronTab.SetCron(this, 5);
	}
	
	public void stopCron(View v){
		Log.i("alarm", "ENDING!!!");
		cronTab.CancelCron(this);
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		cronTab = new Cron();
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.splash);

		//setContentView(R.layout.activity_main);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		 uiHelper = new UiLifecycleHelper(this, callback);
		 uiHelper.onCreate(savedInstanceState);

		 LoginButton auth = (LoginButton)findViewById(R.id.login_button);
		 
		 try {
			 Log.e("KeyHash:", "WHAT");
		        PackageInfo info = getPackageManager().getPackageInfo("polar.obsessive", PackageManager.GET_SIGNATURES);
		        for (Signature signature : info.signatures) {
		            MessageDigest md = MessageDigest.getInstance("SHA");
		            md.update(signature.toByteArray());
		            Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
		            }
		    } catch (NameNotFoundException e) {

		    } catch (NoSuchAlgorithmException e) {

		    }
	}
	
	public void createNotification(View v){
		notify("New Album!!", "Eminem is releasing a new Album on 11/25/2013!");
	}
	
	// creates a notification with default app-icon in case IMG-URL was not found/not provided
	public void notify(String contentTitle, String contentText){
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		// notification image subject to change
		long when = System.currentTimeMillis();
		Notification n  = new Notification(R.drawable.obsessive_icon, contentTitle, when);
		n.setLatestEventInfo(this, contentTitle, contentText, pIntent);
		notificationManager.notify(0,n);
	}
	
	// creates a notification with provided bitmap picture (album)
	public void notify(String contentTitle, String contentText, Bitmap largeIcon){
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		// notification image subject to change
		long when = System.currentTimeMillis();
		Notification n  = new Notification(R.drawable.obsessive_icon, contentTitle, when);
		n.largeIcon = largeIcon;
		n.setLatestEventInfo(this, contentTitle, contentText, pIntent);
		notificationManager.notify(0,n);
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		
		Log.e("STATE", state.toString());
		
	    if (state.isOpened()) {
	    	Log.i("INFO", "Stuck Here...");
	    	//startActivity(new Intent(MainActivity.this, ArFieldListActivity.class)); 
	    } else if (state.isClosed()) {
	        Log.i("INFO", "Logged out...");
	    }
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
}
