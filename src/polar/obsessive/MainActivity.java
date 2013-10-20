package polar.obsessive;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import polar.obsessive.R;
import android.os.Bundle;
import android.os.StrictMode;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class MainActivity extends FragmentActivity {
	
	private NotificationHelper notifyMan;
	private UiLifecycleHelper uiHelper;
	private Cron cronTab;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
/*
 * REMOVE ME LATER	
 */

	public void startCron(View v){
		Log.i("alarm", "STARTING!!!");
		cronTab.SetCron(this, 5);
	}
	
	public void stopCron(View v){
		Log.i("alarm", "ENDING!!!");
		cronTab.CancelCron(this);
	}
	
	public void testImage(View v){
		Bitmap mBit = notifyMan.convertURLtoBitmap("http://www.eminemlab.com/images/wallpapers/Eminem-01-1024x768b.jpg");
		notifyMan.notify("New Album!!", "Eminem is releasing a new Album on 11/25/2013!", mBit, null);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		cronTab = new Cron();
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.splash);

		//setContentView(R.layout.activity_main);
		notifyMan = new NotificationHelper(this);

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
