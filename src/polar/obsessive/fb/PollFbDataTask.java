package polar.obsessive.fb;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import android.os.AsyncTask;

public class PollFbDataTask extends
		AsyncTask<Session, Integer, FbData> {

	@Override
	protected FbData doInBackground(Session... session) {
		FbData result = new FbData();
		
		Request request = new Request(
				session[0],
				"me/og.likes",
				null,
				HttpMethod.GET
			);
			
		Response response = request.executeAndWait();
		
		return result;
	}

}
