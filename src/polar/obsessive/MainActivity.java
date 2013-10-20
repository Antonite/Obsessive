package polar.obsessive;


import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

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
	private Cron cronTab;
	private UiLifecycleHelper uiHelper;
	
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
		cronTab = new Cron();
		notifyMan = new NotificationHelper(this);
		
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.splash);

		//setContentView(R.layout.activity_main);
		

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		 LoginButton auth = (LoginButton)findViewById(R.id.login_button);
		 auth.setReadPermissions(Arrays.asList("user_likes"));

		 /*
		 
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
	    if (state.isOpened() && !ArFieldListActivity.open) {
	    	ArFieldListActivity.open = true;
	    	startActivity(new Intent(MainActivity.this, ArFieldListActivity.class)); 
	    	makeMeRequest(session);
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
	
	private void makeMeRequest(final Session session) {
	    // Make an API call to get user data and define a 
	    // new callback to handle the response.
	    Request request = Request.newGraphPathRequest(session, "me/og.likes", new Request.Callback() {
	        @Override
	        public void onCompleted(Response response) {
	            // If the response is successful
	            if (session == Session.getActiveSession()) {

                    // Set the id for the ProfilePictureView
                    // view that in turn displays the profile picture.

	            	JSONObject a = response.getGraphObject().getInnerJSONObject();
					try {
						getValue(a);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

	            }
	            if (response.getError() != null) {
	                // Handle errors, will do so later.
	            }
	        }

	    });
	    request.executeAsync();


	    // handle the response
	}
	public void getValue(JSONObject a) throws JSONException{
		Iterator<?> keys = a.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            if(a.optJSONArray(key) != null){
            	getValue(a.optJSONArray (key));
			}
			else if (a.optJSONObject(key) != null){
				getValue(a.optJSONObject(key));
			}
			else if (key.equals("id") || key.equals("title")){
 
				System.out.println(key + ": " + a.get(key));

			}
        }
	}
	public void getValue(JSONArray a){
		for (int i = 0; i < a.length(); i++) {
			  try {
				if(a.getJSONObject(i) != null){
					getValue(a.getJSONObject(i));
				  }
				  else if (a.getJSONArray(i) != null){
					  getValue(a.getJSONArray(i));
				  }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
