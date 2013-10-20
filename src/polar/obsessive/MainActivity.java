package polar.obsessive;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import polar.obsessive.R;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
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
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	
	private NotificationManager notificationManager;
	private UiLifecycleHelper uiHelper;
	private Cron cronTab;
	
	private TextView userInfoTextView;
	
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
	
	public void testImage(View v){
		Bitmap mBit = convertURLtoBitmap("http://www.eminemlab.com/images/wallpapers/Eminem-01-1024x768b.jpg");
		notify("New Album!!", "Eminem is releasing a new Album on 11/25/2013!", mBit, null);
	}
	
	public Bitmap convertURLtoBitmap(String src) {

        try {
                        URL url = new URL(src);  
                        HttpURLConnection connection = (HttpURLConnection) url
                                        .openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        myBitmap = Bitmap.createScaledBitmap(myBitmap, 96, 96, true);
                        return myBitmap;

        }

        catch (IOException e) {
                        e.printStackTrace();
                        return null;
        }
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
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		 uiHelper = new UiLifecycleHelper(this, callback);
		 uiHelper.onCreate(savedInstanceState);

		 LoginButton auth = (LoginButton)findViewById(R.id.login_button);
		 auth.setReadPermissions(Arrays.asList("user_likes"));
		 
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
	
	
	@Override
	protected void onResumeFragments() {
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
		super.onResumeFragments();
	}

	
	// creates a notification with default app-icon in case IMG-URL was not found/not provided
	public void notify(String contentTitle, String contentText, PendingIntent pIntent){
		Intent intent = new Intent(this, MainActivity.class);
		if(pIntent == null)
			pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.obsessive_icon);
		builder.setContentTitle(contentTitle);
		builder.setContentText(contentText);
		builder.setTicker(contentTitle);
		// this imposes the pendingIntent onto the notification, acting as an eventhandler
		builder.setContentIntent(pIntent);
        Notification n = builder.build();
		notificationManager.notify(0,n);
	}
	
	// creates a notification with provided bitmap picture (album)
	public void notify(String contentTitle, String contentText, Bitmap largeIcon, PendingIntent pIntent){
		Intent intent = new Intent(this, MainActivity.class);
		if(pIntent == null)
			pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setLargeIcon(largeIcon);
		builder.setSmallIcon(R.drawable.obsessive_icon);
		builder.setContentTitle(contentTitle);
		builder.setContentText(contentText);
		builder.setTicker(contentTitle);
		// this imposes the pendingIntent onto the notification, acting as an eventhandler
		builder.setContentIntent(pIntent);
        Notification n = builder.build();

		notificationManager.notify(0,n);
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		
		Log.e("STATE", state.toString());
		
	    if (state.isOpened()) {
	    	startActivity(new Intent(MainActivity.this, ArFieldListActivity.class));
	    	makeMeRequest(session);
	    	
	    } else if (state.isClosed()) {
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
