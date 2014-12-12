package com.example.vozac.AsyncTaskKlase;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vozac.Drugi;
import com.example.vozac.Postavke;
import com.example.vozac.Klase.Voznja;

public class PostVoznja extends AsyncTask<Voznja, Void, String> {

	private Voznja v;
	private Context activity;

	private String username = null;
	private String password = null;
	private String lat = null;
	private String lon = null;
	private String idKorisnika = null;
	private String idVozila = null;
	private String idLinije = null;
	
	public PostVoznja(Postavke a) {
		this.activity = a;
	}

	@Override
	protected String doInBackground(Voznja... params) {
		Log.d("info", "Usao u doInBackground u voznji");
		v = params[0];
		username = v.getUsername();
		password = v.getPassword();
		lat = String.valueOf(34);
		lon = String.valueOf(12);
		idKorisnika = String.valueOf(v.getIdKorisnika());
		idVozila = String.valueOf(v.getIdVozila());
		idLinije = String.valueOf(v.getIdLinije());
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://farisc.comlu.com/Voznje.php");
		
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("lat", lat));
			nameValuePairs.add(new BasicNameValuePair("lon", lon));
			nameValuePairs.add(new BasicNameValuePair("idKorisnika", idKorisnika));
			nameValuePairs.add(new BasicNameValuePair("idLinije", idLinije));
			nameValuePairs.add(new BasicNameValuePair("idVozila ", idVozila ));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			for (int i=0; i<nameValuePairs.size(); i++) {
				NameValuePair a = nameValuePairs.get(i);
				String b = a.getValue();
				Log.d("idKorisnika iz for-a", b);
			}
			
			//HttpResponse response = httpclient.execute(httppost);
			//Log.d("response", EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
			Log.d("ovdje", "ovdje");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String response) {
		Log.d("info", "Usao u onPostExecute voznje");
		Log.d("ovdje1", "ovdje1");
		//Log.d("info", response);
		String id;
		
		

		try {
			final JSONArray jsonObj = new JSONObject(response).getJSONArray("voznje");
			id = jsonObj.getJSONObject(0).getString("idVoznje");

			Log.d("ovo je id voznje", id);
			id = id.replace("<!-- Hosting24 Analytics Code -->", "");
			id = id.replace("<script type=\"text/javascript\" src=\"http://stats.hosting24.com/count.php\"></script>", "");
			id = id.replace("<!-- End Of Analytics Code -->", "");
			Log.d("info2", response);

			v.setIdVoznje(Integer.valueOf(id));

			Intent in = new Intent(activity, Drugi.class);
			in.putExtra("username", username);
			in.putExtra("password", password);
			in.putExtra("idVoznje", id);
			in.putExtra("idVozila", idVozila);
			in.putExtra("idLinije", idLinije);

			in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.startActivity(in);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}