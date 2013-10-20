package polar.obsessive;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import polar.obsessive.data.LocalStore;
import polar.obsessive.data.LocalStore.Album;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class Cron extends BroadcastReceiver 
{    
	private static final String remoteHost = "http://people.rit.edu/~rwl3564/obsession/data.txt";
	private static HashSet<String> titles = new HashSet<String>();
	
	public static class DataCron extends AsyncTask<String, Integer, String> {

		Context c;
		NotificationHelper notif;
		
		DataCron(Context c) {
			this.c = c;
			notif = new NotificationHelper(c);
			
		}
		
		@Override
		protected String doInBackground(String... params) {
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
				
				LocalStore.ensure(c);
				for(String[] arr : result) {
					String date = arr[0];
					String artist = arr[1];
					String title = arr[2];
					String img = arr[3];
					
					if(LocalStore.subscribedArtists.contains(artist))
					{
						ArrayList<Album> albums = LocalStore.updates.get(artist);
						
						if(albums == null) {
							albums = new ArrayList<Album>();
							LocalStore.updates.put(artist, albums);
						}
						
						Album a = new Album();
						a.date = date;
						a.title = title;
						a.img = img;
						
						albums.add(a);
					}
					
					
					if(!titles.contains(title)) {
						notif.notify(title.hashCode(), title, artist, notif.convertURLtoBitmap(img), 
								PendingIntent.getActivity(c, 0, new Intent(c, ArFieldListActivity.class), 0));
						titles.add(title);
					}
					
				}
				LocalStore.save(c);
				
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	@Override
	public void onReceive(Context c, Intent intent) {
		
		DataCron task = new DataCron(c);
		task.execute();
	}
	
	 public static void SetCron(Context context, int seconds)
	 {
	     AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	     Intent i = new Intent(context, Cron.class);
	     PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
	     am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * seconds , pi); 
     }

     public static void CancelCron(Context context)
     {
         Intent intent = new Intent(context, Cron.class);
         PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
         AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
         alarmManager.cancel(sender);
     }
 }