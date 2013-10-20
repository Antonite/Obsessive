package polar.obsessive;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import polar.obsessive.data.LocalStore;


public class ArFieldListFragment extends ListFragment {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private final static int MENU_DELETE = 1;
	
	private Callbacks mCallbacks = sDummyCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;
	private LazyAdapter content;
	
	public interface Callbacks {
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ArFieldListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		boolean firstLoad = !LocalStore.exists(getActivity());
	
		if(firstLoad) {
			LocalStore.init();
		} else {
			LocalStore.ensure(getActivity());
		}
		
		content = new LazyAdapter();
		setListAdapter(content);
		
		if(firstLoad) {
    		GetFBDataTask task = new GetFBDataTask();
    		task.execute();
    	}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}
	
	@Override
	public void onPause() {
		LocalStore.save(getActivity());
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		LocalStore.save(getActivity());
		super.onDestroy();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
		
	}
	
	@Override
	public void onCreateContextMenu(android.view.ContextMenu menu, View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE, MENU_DELETE , Menu.NONE, "Delete");
	};
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()) {
		case MENU_DELETE:
			LocalStore.subscribedArtists.remove(info.position);
			content.notifyDataSetChanged();
			break;
		}
		return false;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		mCallbacks.onItemSelected(LocalStore.subscribedArtists.get(position));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	
	public void updateList() {
		content.notifyDataSetChanged();
	}
	
	public class LazyAdapter extends BaseAdapter {
		 
	    private LayoutInflater inflater=null;
//	    public ImageLoader imageLoader;
	 
	    public LazyAdapter() {
	        inflater = (LayoutInflater)ArFieldListFragment.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	        imageLoader=new ImageLoader(activity.getApplicationContext());
	    }
	    
	    private class URLtoBitmap extends AsyncTask<String, Integer, Bitmap>{ 

			private ImageView imgView;
			
			public URLtoBitmap(ImageView v){
				imgView = v;
			}
			
			public Bitmap doInBackground(String... src) {
				 try {
                     URL url = new URL(src[0]);  
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
			protected void onPostExecute(Bitmap bm) {
				super.onPostExecute(bm);
				imgView.setImageBitmap(bm);
			}
		}
	 
	    public int getCount() {
	        return LocalStore.subscribedArtists.size();
	    }
	 
	    public Object getItem(int position) {
	        return position;
	    }
	 
	    public long getItemId(int position) {
	        return position;
	    }
	 
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View vi=convertView;
	        if(convertView==null)
	            vi = inflater.inflate(R.layout.list_row, null);
	 
	        TextView artist = (TextView)vi.findViewById(R.id.artist);
	        ImageView thumb_image = (ImageView)vi.findViewById(R.id.list_image);
	 
	        String artistName = LocalStore.subscribedArtists.get(position);
	        artist.setText(artistName);
	        new URLtoBitmap(thumb_image).execute(LocalStore.imgs.get(artistName));
	        
	        return vi;
	    }
	}
	
	class GetFBDataTask extends AsyncTask<String, Integer, ArrayList<String[]>> {
		
		private ArrayList<String[]> result = new ArrayList<String[]>();
		
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
		    request.executeAndWait();
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
		            		
		            		String[] vals = new String[2];
		            		vals[0] = username_title;
		            		vals[1] = profile_pic;
		            		result.add(vals);
							
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
		    request.executeAndWait();
		}
		@Override
		protected ArrayList<String[]> doInBackground(String... arg0) {
			makeMeRequest(Session.getActiveSession());
			return result;
		}	
		
		@Override
		protected void onPostExecute(ArrayList<String[]> result) {
			super.onPostExecute(result);
			for(String[] pair : result) {
				LocalStore.subscribedArtists.add(pair[0]);
				LocalStore.imgs.put(pair[0], pair[1]);
			}
			content.notifyDataSetChanged();
		}
	}
}

