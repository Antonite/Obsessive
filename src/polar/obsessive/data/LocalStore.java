package polar.obsessive.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;

public class LocalStore {
	
	private final static long CACHE_TIME_DELAY = 10*60*1000; 
	
	private static boolean loaded = false;
	public static long lastUpdate;
	public static ArrayList<String> subscribedArtists;
	
	public static ArrayList<String[]> cachedPage;
	
	public static void ensure(Context c) {
		if(!loaded) {
			load(c);
		}
	}
	
	public static void init() {
		subscribedArtists = new ArrayList<String>();
		lastUpdate = -1;
		loaded = true;
	}
	
	public static void load(Context c) {
		DataInputStream fis;
		try {
			fis = new DataInputStream(c.openFileInput("artists.txt"));
			
			lastUpdate = fis.readLong();
			
			int count = fis.readInt();
			for(int i=0; i < count; ++i) {
				byte[] buf = new byte[fis.readInt()];
				fis.read(buf);
				subscribedArtists.add(new String(buf));
			}
			
			if(lastUpdate + CACHE_TIME_DELAY < System.currentTimeMillis()) {
				cachedPage = new ArrayList<String[]>();
				count = fis.readInt();
				for(int i=0; i < count; ++i) {
					String[] arr = new String[fis.readInt()];
					for(int j=0; j < arr.length; ++j) {
						byte[] buf = new byte[fis.readInt()];
						fis.read(buf);
						arr[j] = new String(buf);
					}
					cachedPage.add(arr);
				}
			}
			else
			{
				cachedPage = null;
			}
			
			fis.close();
			loaded = true;
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		init();
	}
	
	public static void save(Context c) {
		if(!loaded)
			return;
		
		DataOutputStream fos;
		try {
			fos = new DataOutputStream(c.openFileOutput("artists.txt", Context.MODE_PRIVATE));
			fos.writeLong(lastUpdate);
			if(subscribedArtists == null) {
				fos.writeInt(0);
			} else {
				fos.writeInt(subscribedArtists.size());
				for(String artist : subscribedArtists) {
					byte[] buf = artist.getBytes();
					fos.writeInt(buf.length);
					fos.write(buf);
				}
			}
			
			if(cachedPage == null) {
				fos.writeInt(0);
			} else {
				fos.writeInt(cachedPage.size());
				for(String[] arr : cachedPage) {
					fos.writeInt(arr.length);
					for(String str : arr) {
						byte[] buf = str.getBytes();
						fos.writeInt(buf.length);
						fos.write(buf);
					}
				}
			}
			
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
