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
import polar.obsessive.data.LocalStore;
import android.os.Bundle;
import android.os.StrictMode;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

public class MainActivity extends FragmentActivity {
	
	
	private UiLifecycleHelper uiHelper;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
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

	    	if(!LocalStore.exists(this)) {
	    		LocalStore.init();
	    		makeMeRequest(session);
	    	}
	    	
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
	
	private void makeMeRequest(final Session session) {
	    // Make an API call to get user data and define a 
	    // new callback to handle the response.
	    Request request = Request.newGraphPathRequest(session, "me/likes", new Request.Callback() {
	        @Override
	        public void onCompleted(Response response) {
	            // If the response is successful
	            if (session == Session.getActiveSession()) {

                    // Set the id for the ProfilePictureView
                    // view that in turn displays the profile picture.

	            	JSONObject a = response.getGraphObject().getInnerJSONObject();
					getValue(a.optJSONArray("data"));

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
		if (a.get("category").equals("Musician/band")){
			queryArtist(a.get("id").toString(), Session.getActiveSession());
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
	public void queryArtist(String artist, final Session session){
		Request request = Request.newGraphPathRequest(session, artist, new Request.Callback() {
	        @Override
	        public void onCompleted(Response response) {
	            // If the response is successful
	            if (session == Session.getActiveSession()) {


	            	JSONObject a = response.getGraphObject().getInnerJSONObject();
	            	
	            	try {
	            		
	            		Log.i("JSON", a.toString());
	            		
	            		String username_title="", profile_pic="";
	            		if (a.has("name")) {
	            			username_title = a.get("name").toString();
	            		} else if(a.has("username")) {
	            			username_title = a.get("username").toString();
	            		}
	            		
	            		if(a.has("cover")) {
	            			profile_pic = a.optJSONObject("cover").get("source").toString();
	            		} else {
	            			profile_pic = "";
	            		}
				
	            		if(username_title == "") {
	            			return;
	            		}
						LocalStore.subscribedArtists.add(username_title);
						LocalStore.imgs.put(username_title, profile_pic);
						
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
	}
}
