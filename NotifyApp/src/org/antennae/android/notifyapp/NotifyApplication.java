package org.antennae.android.notifyapp;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class NotifyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Enable Local Datastore.
		Parse.enableLocalDatastore(this);
		Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
		ParseCrashReporting.enable(this);
		Parse.initialize(this, "N2aksV1jywajiRKOV92u42Scz3Q5RGFmOuW6Iwtx", "vfmxpQWtEIsweuTYGOC18dssmifgznEVTelzi8NS");
		
		ParsePush.subscribeInBackground("", new SaveCallback() {
			  @Override
			  public void done(ParseException e) {
			    if (e == null) {
			      Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
			    } else {
			      Log.e("com.parse.push", "failed to subscribe for push", e);
			    }
			  }
			});
	}
}
