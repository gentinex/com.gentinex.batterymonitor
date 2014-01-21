package com.example.myfirstapp;

import com.example.myfirstapp.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	public void setLimit(int limit, boolean high){
		String highLimitField = getResources().getString(R.string.high_level);
		String lowLimitField = getResources().getString(R.string.low_level);
		String limitFileName = getResources().getString(R.string.limit_file_name);
	    SharedPreferences settings = getSharedPreferences(limitFileName, 0);
	    
        String limitField;
        if(high){
        	limitField = highLimitField;
		}
		else{
        	limitField = lowLimitField;
		}

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(limitField, limit);
        editor.commit();
    }

	public void setSeekBarLevel(int bar, int text, int field){
        SeekBar seekBar = (SeekBar)findViewById(bar); 
        final TextView seekBarValue = (TextView)findViewById(text);
        final int barInt = bar;
		String limitFileName = getResources().getString(R.string.limit_file_name);
		String limitField = getResources().getString(field);
	    SharedPreferences settings = getSharedPreferences(limitFileName, 0);
	    int currentLimit = settings.getInt(limitField, 100);
	    seekBar.setProgress(currentLimit);
	    seekBarValue.setText(String.valueOf(currentLimit));
	    
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

        @Override 
        public void onProgressChanged(SeekBar seekBar, int progress, 
    		boolean fromUser) { 
        	seekBarValue.setText(String.valueOf(progress));
        	switch(barInt){
	          case R.id.high_bar:
	            setLimit(progress, true);
	          	break;
	          case R.id.low_bar:
	            setLimit(progress, false);
	          	break;
	          default:
	          	try {
					throw new Exception("Unsupported use case");
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
    	   } 

    	@Override 
    	public void onStartTrackingTouch(SeekBar seekBar) { 
    	    // TODO Auto-generated method stub 
    	} 

    	@Override 
    	public void onStopTrackingTouch(SeekBar seekBar) { 
    	    // TODO Auto-generated method stub 
    	} 
        	      
        }); 
		
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Context context = getBaseContext();
        ComponentName receiver = new ComponentName(context, BatteryBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        
        setSeekBarLevel(R.id.high_bar, R.id.high_level, R.string.high_level);
        setSeekBarLevel(R.id.low_bar, R.id.low_level, R.string.low_level);
    }
    
}
