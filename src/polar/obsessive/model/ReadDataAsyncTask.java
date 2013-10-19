package polar.obsessive.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;


public class ReadDataAsyncTask extends AsyncTask<String, Integer, ArrayList<String[]>> { 

	private static String remoteHost = "http://people.rit.edu/~rwl3564/obession/data.txt";
	
	public ArrayList<String[]> doInBackground(String... host) {
		try {
			ArrayList<String[]> result = new ArrayList<String[]>();
			
			BufferedReader bf = new BufferedReader(new InputStreamReader((new URL(remoteHost)).openStream()));
			String line = bf.readLine();
			while(line != null)
			{
				result.add(line.split(","));
				line = bf.readLine();
			}
			return result;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
