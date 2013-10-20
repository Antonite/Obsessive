package polar.obsessive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import polar.obsessive.data.DataField;
import polar.obsessive.data.LocalStore;

import com.facebook.Session;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * An activity representing a list of ArFields. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ArFieldDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ArFieldListFragment} and the item details (if present) is a
 * {@link ArFieldDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ArFieldListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class ArFieldListActivity extends FragmentActivity implements
		ArFieldListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	public static boolean open = false;
	
	

	@Override
	public void onPause() {
		open = false;
		super.onPause();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		open = true;

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_arfield_list);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(false);
		
		if (findViewById(R.id.arfield_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ArFieldListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.arfield_list))
					.setActivateOnItemClick(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link ArFieldListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ArFieldDetailFragment.ARG_ITEM_ID, id);
			ArFieldDetailFragment fragment = new ArFieldDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.arfield_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ArFieldDetailActivity.class);
			detailIntent.putExtra(ArFieldDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(ArFieldListActivity.this, SettingsActivity.class));
			break;
		case R.id.action_logout:
			Session session = Session.getActiveSession();
		    if (session != null) {
		        if (!session.isClosed()) {
		            session.closeAndClearTokenInformation();
		        }
		    } else {
		        session = new Session(ArFieldListActivity.this);
		        Session.setActiveSession(session);
		        session.closeAndClearTokenInformation();
		    }
		    finish();
			break;
		case R.id.add_artist:
			final EditText input = new EditText(this);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("New Artist:");
			builder.setView(input);
	        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            	LocalStore.subscribedArtists.add(input.getText().toString());
	            	LocalStore.updates.put(input.getText().toString(), new ArrayList<LocalStore.Album>());
	            	
	            	ArFieldListFragment frag = ((ArFieldListFragment) getSupportFragmentManager().findFragmentById(R.id.arfield_list));
	            	if(frag != null) {
	            		frag.updateList();
	            	}
	            	
	            }
	        })
	        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            }
	        })
	        .create().show();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public void onBackPressed() {
	}
	
	public void onCompleteTask(ArrayList<String[]> data) {
		//Send Notifications!
	}
	
	private class ReadDataAsyncTask extends AsyncTask<String, Integer, ArrayList<String[]>> { 

		public static final String remoteHost = "http://people.rit.edu/~rwl3564/obsession/data.txt";
		
		public ArrayList<String[]> doInBackground(String... host) {
			try {	
				ArrayList<String[]> result = new ArrayList<String[]>();
				
				BufferedReader bf = new BufferedReader(new InputStreamReader((new URL(remoteHost)).openStream()));
				String line = bf.readLine();
				while(line != null)
				{
					result.add(line.split(","));
					line = bf.readLine();
				}
				bf.close();
				return result;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(ArrayList<String[]> result) {
			super.onPostExecute(result);
			LocalStore.cachedPage = result;
			ArFieldListActivity.this.onCompleteTask(result);
		}
	}
}
