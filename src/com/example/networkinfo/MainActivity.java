package com.example.networkinfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.widget.TextView;

@SuppressLint("ParserError")
public class MainActivity extends Activity {
	SignalChangedCallback callback;
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        int icon = R.drawable.ic_launcher;		        // icon from resources
        CharSequence tickerText = "Hello";              // ticker-text
        long when = System.currentTimeMillis();         // notification time
        Context context = getApplicationContext();      // application Context
        CharSequence contentTitle = "My notification";  // message title
        CharSequence contentText = "Hello World!";      // message text

        Intent notificationIntent = new Intent("Something");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        // the next two lines initialize the Notification, using the configurations above
        Notification notification = new Notification(icon, tickerText, when);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        
        callback = new SignalChangedCallback() {
			
			@Override
			public void getCdmaDbm(String value) {
				((TextView)findViewById(R.id.network_preference_label)).setText(value);
			}
		};
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        MyPhoneStateListener phoneStateListener = new MyPhoneStateListener(callback);
        TelephonyManager Tel = ( TelephonyManager )getSystemService(Context.TELEPHONY_SERVICE);
        Tel.listen(phoneStateListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        
        if(netInfo.isConnected()){
        
        	StringBuffer buffer = new StringBuffer();
        	buffer.append(netInfo.getSubtype());
        	buffer.append(" | ");
        	buffer.append(netInfo.getSubtypeName());
        	buffer.append(" | ");
        	buffer.append(netInfo.getType());
        	buffer.append(" | ");
        	
        	((TextView)findViewById(R.id.network_preference_label)).setText(buffer.toString());
        	
        }
        
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    class MyPhoneStateListener extends PhoneStateListener{
    	SignalChangedCallback callback;
    	
    	protected MyPhoneStateListener(SignalChangedCallback callback) {
    		this.callback = callback;
		}
    	
    	@Override
    	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
    		StringBuffer buffer = new StringBuffer();
        	buffer.append(signalStrength.getCdmaDbm());
        	buffer.append(" | ");
        	buffer.append(signalStrength.getCdmaEcio());
        	buffer.append(" | ");
        	buffer.append(signalStrength.getEvdoSnr());
        	buffer.append(" | ");
        	buffer.append(signalStrength.getGsmSignalStrength());
        	buffer.append(" | ");
        	
    		callback.getCdmaDbm(buffer.toString());
    		
    		super.onSignalStrengthsChanged(signalStrength);
    	}
    }
    
    public interface SignalChangedCallback{
    	public void getCdmaDbm(String signalInfo);
    }
}
