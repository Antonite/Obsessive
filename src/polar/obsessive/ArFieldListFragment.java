package polar.obsessive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import polar.obsessive.data.DataField;
import polar.obsessive.data.DataField.DataItem;

public class ArFieldListFragment extends ListFragment {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	private Callbacks mCallbacks = sDummyCallbacks;
	
	private int mActivatedPosition = ListView.INVALID_POSITION;

	private LazyAdapter content;
//	private ArrayAdapter<DataField.DataItem> content;
	
	private ProgressDialog progressDialog;
	
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

		//content = new ArrayAdapter<DataField.DataItem>(getActivity(),
		//		android.R.layout.simple_list_item_activated_1,
		//		android.R.id.text1, DataField.ITEMS);
		
		content = new LazyAdapter(DataField.ITEMS);
		
		
		setListAdapter(content);
		
		// Fire async task
		ReadDataAsyncTask task = new ReadDataAsyncTask();
		task.execute();
		
		// Start InProgress dialog
		progressDialog = ProgressDialog.show(ArFieldListFragment.this.getActivity(), "", "Loading. Please wait...", true);
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
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(DataField.ITEMS.get(position).id);
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
	
	public void onCompleteTask(ArrayList<String[]> data) {
		DataField.ITEMS.clear();
		for(String[] arr : data) {
			DataField.addItem(arr[0],arr[1],arr[2]);
		}
		
		content.notifyDataSetChanged();
		
		progressDialog.dismiss();
		
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
			ArFieldListFragment.this.onCompleteTask(result);
		}
	}
	
	public class LazyAdapter extends BaseAdapter {
		 
	    private List<DataItem> data;
	    private LayoutInflater inflater=null;
//	    public ImageLoader imageLoader;
	 
	    public LazyAdapter(List<DataItem> iTEMS) {
	        data=iTEMS;
	        inflater = (LayoutInflater)ArFieldListFragment.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      //  imageLoader=new ImageLoader(activity.getApplicationContext());
	    }
	 
	    public int getCount() {
	        return data.size();
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
	 
	        TextView month = (TextView)vi.findViewById(R.id.month); 
	        TextView day = (TextView)vi.findViewById(R.id.day); 
	        TextView artist = (TextView)vi.findViewById(R.id.artist);
	        TextView album = (TextView)vi.findViewById(R.id.album);
//	        ImageView thumb_image = (ImageView)vi.findViewById(R.id.list_image);
	 
	        String date = data.get(position).date;
	        String mon = date.substring(5,7);
	        String dai = date.substring(8);
	        String monStr = "";
	        
	        switch (Integer.parseInt(mon)) {
            case 1:  monStr = "JAN";
                     break;
            case 2:  monStr = "FEB";
                     break;
            case 3:  monStr = "MAR";
                     break;
            case 4:  monStr = "APR";
                     break;
            case 5:  monStr = "MAY";
                     break;
            case 6:  monStr = "JUN";
                     break;
            case 7:  monStr = "JUL";
                     break;
            case 8:  monStr = "AUG";
                     break;
            case 9:  monStr = "SEP";
                     break;
            case 10: monStr = "OCT";
                     break;
            case 11: monStr = "NOV";
                     break;
            case 12: monStr = "DEC";
                     break;
            default: monStr = "UNF";
                     break;
	        }
	        
	        
	        // Setting all values in listview
	        month.setText(monStr);
	        day.setText(dai);
	        artist.setText(data.get(position).artist);
	        album.setText(data.get(position).album);
	        
//	        NotificationHelper.convertURLtoDisplayBitmap(src)
	        
	      //  imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
	        return vi;
	    }
	}
	
}

