package polar.obsessive.gui;

import polar.obsessive.ArFieldListActivity;
import polar.obsessive.R;
import polar.obsessive.SettingsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void navigatePolarBear(View v){ 
			Log.i("clicks","You Clicked B1"); 
			Intent i=new Intent(MainActivity.this, ArFieldListActivity.class);
			startActivity(i); 
			System.out.println("asdf"); 
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(MainActivity.this, SettingsActivity.class));
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
