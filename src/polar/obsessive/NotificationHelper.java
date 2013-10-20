package polar.obsessive;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper {
	
	private NotificationManager notifyMan;
	private Context mainContext;
	
	public NotificationHelper(Context myContext){
		notifyMan = (NotificationManager) myContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mainContext = myContext;
	}
	
	// creates a notification with default app-icon in case IMG-URL was not found/not provided
		public void notify(String contentTitle, String contentText, PendingIntent pIntent){
			Intent intent = new Intent(mainContext, MainActivity.class);
			if(pIntent == null) {
				pIntent = PendingIntent.getActivity(mainContext, 0, intent, 0);
			}
			NotificationCompat.Builder builder = new NotificationCompat.Builder(mainContext);
			builder.setSmallIcon(R.drawable.obsessive_icon);
			builder.setContentTitle(contentTitle);
			builder.setContentText(contentText);
			builder.setTicker(contentTitle);
			// this imposes the pendingIntent onto the notification, acting as an eventhandler
			builder.setContentIntent(pIntent);
	        Notification n = builder.build();
	        notifyMan.notify(0,n);
		}
		
		// creates a notification with provided bitmap picture (album)
		public void notify(String contentTitle, String contentText, Bitmap largeIcon, PendingIntent pIntent){
			Intent intent = new Intent(mainContext, MainActivity.class);
			if(pIntent == null)
				pIntent = PendingIntent.getActivity(mainContext, 0, intent, 0);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(mainContext);
			builder.setLargeIcon(largeIcon);
			builder.setSmallIcon(R.drawable.obsessive_icon);
			builder.setContentTitle(contentTitle);
			builder.setContentText(contentText);
			builder.setTicker(contentTitle);
			// this imposes the pendingIntent onto the notification, acting as an eventhandler
			builder.setContentIntent(pIntent);
	        Notification n = builder.build();
	        notifyMan.notify(0,n);
		}
	
	public Bitmap convertURLtoBitmap(String src) {

        try {
                        URL url = new URL(src);  
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
	
	public static Bitmap convertURLtoDisplayBitmap(String src) {

        try {
                        URL url = new URL(src);  
                        HttpURLConnection connection = (HttpURLConnection) url
                                        .openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        myBitmap = Bitmap.createScaledBitmap(myBitmap, 256, 256, true);
                        return myBitmap;

        }

        catch (IOException e) {
                        e.printStackTrace();
                        return null;
        }
	}
	
	public static Bitmap resizeBitmap(Bitmap bit){
		Bitmap myBitmap = Bitmap.createScaledBitmap(bit, 32, 32, true);
        return myBitmap;
	}
	
}
