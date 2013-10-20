package polar.obsessive;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import polar.obsessive.R;
import android.os.Bundle;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class MainActivity extends FragmentActivity {
	
	private NotificationManager notificationManager;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	private UiLifecycleHelper uiHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.splash);

		//setContentView(R.layout.activity_main);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		 /*LoginButton auth = (LoginButton)findViewById(R.id.login_button);
		 
		 try {
		        PackageInfo info = getPackageManager().getPackageInfo("polar.obsessive", PackageManager.GET_SIGNATURES);
		        for (Signature signature : info.signatures) {
		            MessageDigest md = MessageDigest.getInstance("SHA");
		            md.update(signature.toByteArray());
		            Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
		            }
		    } catch (NameNotFoundException e) {

		    } catch (NoSuchAlgorithmException e) {

		    }*/
	}
	
	
	@Override
	protected void onResumeFragments() {
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
		super.onResumeFragments();
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
		
	    if (state.isOpened() && !ArFieldListActivity.open) {
	    	ArFieldListActivity.open = true;
	    	startActivity(new Intent(MainActivity.this, ArFieldListActivity.class)); 
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
