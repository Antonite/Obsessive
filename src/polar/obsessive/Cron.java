package polar.obsessive;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Cron extends BroadcastReceiver 
{    
     @Override
     public void onReceive(Context context, Intent intent) 
     {   
         Log.i("asdf","Still going!!!");
         Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
     }

	 public void SetCron(Context context, int seconds)
	 {
	     AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	     Intent i = new Intent(context, Cron.class);
	     PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
	     am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * seconds , pi); 
     }

     public void CancelCron(Context context)
     {
         Intent intent = new Intent(context, Cron.class);
         PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
         AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
         alarmManager.cancel(sender);
     }
     
 }