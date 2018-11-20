package com.kawika.smart_survey.networkCheck;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("ALL")
public class NetworkCheck {

	private Context context;

	public NetworkCheck(Context context) {
		this.context = context;
	}

	public boolean isConnectingToInternet() {
		try {
			NetworkInfo aWIFI, aMobile;
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			aWIFI = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			aMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (aWIFI.isConnected() || aMobile.isConnected()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Boolean isOnline() {
		try {
			HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
			urlc.setRequestProperty("User-Agent", "Test");
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(2000);
			urlc.connect();
			return (urlc.getResponseCode()==200);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}