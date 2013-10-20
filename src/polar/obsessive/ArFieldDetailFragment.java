package polar.obsessive;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import polar.obsessive.ArFieldListFragment.LazyAdapter;
import polar.obsessive.data.DataField;
import polar.obsessive.data.DataField.DataItem;

/**
 * A fragment representing a single ArField detail screen. This fragment is
 * either contained in a {@link ArFieldListActivity} in two-pane mode (on
 * tablets) or a {@link ArFieldDetailActivity} on handsets.
 */
public class ArFieldDetailFragment extends ListFragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	private LazyAdapter content;
	private DataField.DataItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ArFieldDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the data content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DataField.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
		
		ArrayList<String> allartists = new ArrayList<String>();
		allartists.add("Eminem");
		content = new LazyAdapter(allartists);

		setListAdapter(content);
		

	}

	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
//		if (savedInstanceState != null
//				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
//			setActivatedPosition(savedInstanceState
//					.getInt(STATE_ACTIVATED_POSITION));
//		}
		
//		ListView lv = (ListView) getListView().findViewById(R.id.list);
		//registerForContextMenu(lv);
	}
	
	
	public class LazyAdapter extends BaseAdapter {
		 
	    private ArrayList<String> data;
	    private LayoutInflater inflater=null;
//	    public ImageLoader imageLoader;
	 
	    public LazyAdapter(ArrayList<String> artists) {
	        data=artists;
	        inflater = (LayoutInflater)ArFieldDetailFragment.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	        imageLoader=new ImageLoader(activity.getApplicationContext());
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
	            vi = inflater.inflate(R.layout.detail_row, null);
	 
//	        TextView month = (TextView)vi.findViewById(R.id.month); 
//	        TextView day = (TextView)vi.findViewById(R.id.day); 
	        TextView artist = (TextView)vi.findViewById(R.id.artist);
//	        TextView album = (TextView)vi.findViewById(R.id.album);
	        ImageView thumb_image = (ImageView)vi.findViewById(R.id.album_image);
	 
//	        String date = data.get(position).date;
//	        String mon = date.substring(5,7);
//	        String dai = date.substring(8);
//	        String monStr = "";
	        
//	        switch (Integer.parseInt(mon)) {
//            case 1:  monStr = "JAN";
//                     break;
//            case 2:  monStr = "FEB";
//                     break;
//            case 3:  monStr = "MAR";
//                     break;
//            case 4:  monStr = "APR";
//                     break;
//            case 5:  monStr = "MAY";
//                     break;
//            case 6:  monStr = "JUN";
//                     break;
//            case 7:  monStr = "JUL";
//                     break;
//            case 8:  monStr = "AUG";
//                     break;
//            case 9:  monStr = "SEP";
//                     break;
//            case 10: monStr = "OCT";
//                     break;
//            case 11: monStr = "NOV";
//                     break;
//            case 12: monStr = "DEC";
//                     break;
//            default: monStr = "UNF";
//                     break;
//	        }
	        
	        
	        // Setting all values in listview
//	        month.setText(monStr);
//	        day.setText(dai);
	        artist.setText(data.get(position));
//	        album.setText(data.get(position).album);
	        
//	        NotificationHelper.convertURLtoDisplayBitmap(src)
	        
//	        imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
	        thumb_image.setImageBitmap(NotificationHelper.convertURLtoDisplayBitmap("http://upload.wikimedia.org/wikipedia/en/6/64/John_Legend_Love_in_the_Future.jpg"));
	        return vi;
	    }
	}
	
	
}
