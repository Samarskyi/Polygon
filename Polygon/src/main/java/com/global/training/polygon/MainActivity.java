package com.global.training.polygon;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class MainActivity extends Activity {

	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		button = (Button) findViewById(R.id.butt);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						String url = "https://portal-ua.globallogic.com";
//						String url = "https://habrahabr.ru/";
//						 String url1 =  new Uri.Builder()
//								.scheme("https")
//								.authority("eugenii.samarskyi.com")
//								.path("someservlet")
//								.appendQueryParameter("param1", "")
//								.appendQueryParameter("param2", "")
//								.build().toString();
						String response = GET(url);
						Log.d(MainActivity.class.getSimpleName(),response);

					}
				}).start();
			}
		});

	}

	public  String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		try {

			HttpClient httpclient = new DefaultHttpClient();



			HttpGet httpGet = new HttpGet(url);

//			httpGet.setURI(url);
			httpGet.addHeader(BasicScheme
					.authenticate(new UsernamePasswordCredentials("eugenii.samarskyi", "[pi989898pi]!"),"UTF-8", false));
//			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("eugenii.samarskyi", "[pi989898pi]!");
//			BasicScheme scheme = new BasicScheme();
//			Header authorizationHeader = scheme.authenticate(credentials, httpGet);
//			httpGet.addHeader(authorizationHeader);

			HttpResponse httpResponse = httpclient.execute(httpGet);

			inputStream = httpResponse.getEntity().getContent();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

			String line = "";
			while ((line = bufferedReader.readLine()) != null){
				result += line;
				Log.d(MainActivity.class.getSimpleName(),line );
			}

			inputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
