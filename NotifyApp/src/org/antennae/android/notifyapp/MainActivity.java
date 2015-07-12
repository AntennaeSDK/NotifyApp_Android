package org.antennae.android.notifyapp;

import java.util.ArrayList;
import java.util.List;

import org.antennea.android.notifyapp.model.Alert;
import org.antennea.android.notifyapp.model.AlertSeverityEnum;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.antennae.android.notifyapp.R;
import com.parse.ParseAnalytics;


public class MainActivity extends Activity {
	
	public static final String ALERT_PARAM = "alert";
	public static final String ALERT_NOTIFICATION_INTENT_FILTER = "ALERT_NOTIFICATION_INTENT_FILTER";
	
	ListView lvMessages;
	private List<Alert> alerts;
	
	@Override
	public void onResume() {
	    super.onResume();
	    registerReceiver(mMessageReceiver, new IntentFilter(ALERT_NOTIFICATION_INTENT_FILTER));
	}

	//Must unregister onPause()
	@Override
	protected void onPause() {
	    super.onPause();
	    unregisterReceiver(mMessageReceiver);
	}

	//This is the handler that will manager to process the broadcast intent
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
	    
		@Override
	    public void onReceive(Context context, Intent intent) {
	    	
	        // Extract data included in the Intent
	        Alert alert = (Alert) intent.getSerializableExtra(ALERT_PARAM);

	        if(alertsAdapter.getPosition(alert) == -1) {
		        //do other stuff here
		        alertsAdapter.insert(alert, 0);
	        }
	    }
		
	};
	
	private AlertAdapter alertsAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        
        
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        
        setContentView(R.layout.activity_main);
        
        lvMessages = (ListView) findViewById(R.id.lvMessages);
        
        populateAlerts();
        
        lvMessages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(MainActivity.this, MessageDetailActivity.class);
				i.putExtra(ALERT_PARAM, alerts.get(position));
				startActivity(i);
			}
		});
    }
    
    private void populateAlerts() {
		alerts = new ArrayList<Alert>();
		
//		for(int i=0;i<10;i++) {
//			Alert alert = new Alert("title" + i, "message " + i, AlertSeverityEnum.values()[i%5].toString(), "action" + i);
//			alerts.add(alert);
//		}
		
		Alert alert1 = new Alert("High I/O on ADP Apps", "ADP backend has abnormally high IO", AlertSeverityEnum.HIGH.toString(), "Join the bridge on 1-873-555-3846 #556");
		Alert alert2 = new Alert("MySQL index issue", "MySql index is slower on quote system", AlertSeverityEnum.SEVERE.toString(), "Join the bridge on 1-873-555-3846 #598");
		
		alerts.add( alert1 );
		alerts.add( alert2 );
		
		alertsAdapter = new AlertAdapter(this, alerts);
		
		// Attach the adapter to a ListView
		lvMessages.setAdapter(alertsAdapter);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
