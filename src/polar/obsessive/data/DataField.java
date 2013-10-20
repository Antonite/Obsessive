package polar.obsessive.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample date for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DataField {

	public static List<DataItem> ITEMS = new ArrayList<DataItem>();

	public static Map<String, DataItem> ITEM_MAP = new HashMap<String, DataItem>();

	public static void addItem(String date, String artist, String album) {
		DataItem item = new DataItem(date, artist, album);
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	public static void clear() {
		for(DataItem item : ITEMS) {
			ITEM_MAP.remove(item.id);
		}
		ITEMS.clear();
	}
	
	public static class DataItem {
		public String id;
		public String date;
		public String artist;
		public String album;
		private static int nextId = 0;

		public DataItem(String date, String artist, String album) {
			this.id = Integer.toString(nextId++);
			this.date = date;
			this.artist = artist;
			this.album = album;
		}

		@Override
		public String toString() {
			return date;
		}
	}
}
