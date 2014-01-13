package com.deepak.forexconverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.os.AsyncTask;
import android.util.Log;


public class HttpTask extends AsyncTask<HttpUriRequest, Void, JSONObject> {

	@Override
	protected JSONObject doInBackground(HttpUriRequest... params) {
		HttpUriRequest request = params[0];
		HttpClient client = new DefaultHttpClient();
		JSONObject json = null;

		try {
			HttpResponse response = client.execute(request);
			BufferedReader buffRead = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();

			for (String line = null; (line = buffRead.readLine()) != null;) {
				sb.append(line).append("\n");
			}

			JSONTokener tokener = new JSONTokener(sb.toString());
			json = new JSONObject(tokener);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			Log.d("JSON EXCEPTION", e.getMessage().toString());
			e.printStackTrace();
		}
		return json;
	}

	@Override
	protected void onPostExecute(JSONObject json) {
		if (json != null) {
			taskHandler.taskSuccess(json);
		} else {
			taskHandler.taskFailed();
		}
	}

	public static interface HttpTaskHandler {
		void taskSuccess(JSONObject json);

		void taskFailed();
	}

	HttpTaskHandler taskHandler;

	public void setTaskHandler(HttpTaskHandler taskHandler) {
		this.taskHandler = taskHandler;
	}
}
