package polar.obsessive.gui;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import polar.obsessive.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
	private NotificationManager notificationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void createNotification(View v){
		notify("New Album!!", "Eminem is releasing a new Album on 11/25/2013!");
	}
	
	// creates a notification with default app-icon in case IMG-URL was not found/not provided
	public void notify(String contentTitle, String contentText){
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		// notification image subject to change
		long when = System.currentTimeMillis();
		Notification n  = new Notification(R.drawable.obsessive_icon, contentTitle, when);
		n.setLatestEventInfo(this, contentTitle, contentText, pIntent);
		notificationManager.notify(0,n);
	}
	
	// creates a notification with provided bitmap picture (album)
	public void notify(String contentTitle, String contentText, Bitmap largeIcon){
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		// notification image subject to change
		long when = System.currentTimeMillis();
		Notification n  = new Notification(R.drawable.obsessive_icon, contentTitle, when);
		n.largeIcon = largeIcon;
		n.setLatestEventInfo(this, contentTitle, contentText, pIntent);
		notificationManager.notify(0,n);
	}

}
