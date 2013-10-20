package polar.obsessive.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DataField {

	public static List<DataItem> ITEMS = new ArrayList<DataItem>();

	public static Map<String, DataItem> ITEM_MAP = new HashMap<String, DataItem>();

	public static void addItem(String content) {
		DataItem item = new DataItem(content);
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	public static class DataItem {
		public String id;
		public String content;
		
		private static int nextId = 0;

		public DataItem(String content) {
			this.id = Integer.toString(nextId++);
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
