package polar.obsessive;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import polar.obsessive.data.DataField;

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
	 
	        TextView date = (TextView)vi.findViewById(R.id.dt); 
	        TextView datetxt = (TextView)vi.findViewById(R.id.date_txt); 
	        TextView artist = (TextView)vi.findViewById(R.id.artist);
	        TextView artisttxt = (TextView)vi.findViewById(R.id.artist_txt);
//	        TextView album = (TextView)vi.findViewById(R.id.album);
	        ImageView thumb_image = (ImageView)vi.findViewById(R.id.album_image);
	 
	        String adate = "2013-10-07";
	        int mon = Integer.parseInt(adate.substring(5,7));
	        String dai = adate.substring(8);
	        
	        String[] months = new String[] {"January","February","March","April","May","June","July","August","September","October","November","December"};
	        
	        
	        // Setting all values in listview
	        date.setText(months[mon-1] + " " + dai);
	        datetxt.setText(datetxt.getText());
	        
	        artist.setText(data.get(position));
	        artisttxt.setText(artisttxt.getText());
//	        album.setText(data.get(position).album);
	        
//	        NotificationHelper.convertURLtoDisplayBitmap(src)
	        
//	        imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
	        thumb_image.setImageBitmap(NotificationHelper.convertURLtoDisplayBitmap("http://upload.wikimedia.org/wikipedia/en/6/64/John_Legend_Love_in_the_Future.jpg"));
	        return vi;
	    }
	}
	
	
}
