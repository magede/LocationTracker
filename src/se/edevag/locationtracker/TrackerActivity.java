package se.edevag.locationtracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class TrackerActivity extends Activity {
	private static final int ACTIVITY_CREATE=0;
	
	LocationManager	locationManager;
	TrackerLocationListener listener;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        loadPreferences();
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final TrackerService service = new TrackerService(this, this.getApplicationContext());
        listener = new TrackerLocationListener(service);
        
        // Start with tracking the GPS
        setupGPS(service);
        
        // Setup button listeners
        setupUploadButtonListener(service);
        setupUpdateButtonListener(service);
        setupHistoryButton(service);
        setupSeekBar();
        
        // Now set up the user interface
        TextView headerText = (TextView) findViewById(R.id.header);
        String textToDisplay = "Welcome to LocationTrackar Magnus :)";
        headerText.setText(textToDisplay);
        
        
    }
    
    private void loadPreferences() {
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		
		prefs.getInt("UpdateSpeed", 30);
		
	}

	private void setupGPS(TrackerService service) {
    	
     	Criteria criteria = new Criteria();
     	String provider = locationManager.getBestProvider(criteria, true);
     	Location location = locationManager.getLastKnownLocation(provider);
        
 		if (location != null) {
 			service.perist(location);
 		}
 		
 		int minTimeInMiliz = 1 * 60 * 1000;
 		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeInMiliz, 0, listener);
    }
    
    private void setupUploadButtonListener(final TrackerService service) {
    	Button button = (Button) findViewById(R.id.uploadButton);
    	
    	button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Toast.makeText(TrackerActivity.this , 
            			"One day, this will upload", 
            			Toast.LENGTH_SHORT ).show();
            }
        });
    }
    
    private void setupUpdateButtonListener(final TrackerService service) {
    	Button button = (Button) findViewById(R.id.updateButton);
    	
    	button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            	Toast.makeText(TrackerActivity.this , "Requesting update", Toast.LENGTH_SHORT ).show();
            	
             	Criteria criteria = new Criteria();
             	
             	String provider = locationManager.getBestProvider(criteria, true);
             	locationManager.requestSingleUpdate(criteria, 
             			new LocationListener() {
							public void onStatusChanged(String provider, int status, Bundle extras) {
								Toast.makeText(TrackerActivity.this , "onStatusChanged", Toast.LENGTH_SHORT ).show();
							}
							public void onProviderEnabled(String provider) {
								Toast.makeText(TrackerActivity.this , "onProviderEnabled", Toast.LENGTH_SHORT ).show();
							}
							public void onProviderDisabled(String provider) {
								Toast.makeText(TrackerActivity.this , "onProviderDisabled", Toast.LENGTH_SHORT ).show();
							}
							public void onLocationChanged(Location location) {
								service.perist(location);
							}
						},null);
             	
            	/*if (service.clearHistory()) {
	            	TextView tv = (TextView) findViewById(R.id.textView1);
	            	tv.setText("<Nothing>");
            	}
            	*/

            }
        });
    }
    
    private void setupHistoryButton(final TrackerService service) {
    	final Button history = (Button) findViewById(R.id.historyButton);
    	
    	history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(TrackerActivity.this, HistoryActivity.class);
                startActivity(i);
            }
        });
    }
    
    private void setupSeekBar() {
    	
    	final SeekBar speed = (SeekBar) findViewById(R.id.seekBar1);
    	
    	speed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar seekBar) {
				
				locationManager.removeUpdates(listener);
				
				int minutes = getMinutes(seekBar.getProgress()) + getHours(seekBar.getProgress());
				
				long minTimeInMiliz = minutes * 60 * 1000;
				
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeInMiliz, 0, listener);
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				
		        TextView updateSpeedText = (TextView) findViewById(R.id.textViewUpdateSpeed);
		        
		        int minutes = getMinutes(progress);
		        int hours = getHours(progress);
		        
		        updateSpeedText.setText(hours + "h " + minutes + "min");
			}
			
			private int getHours(int progress) {
				if (progress > 50) {
					return 7 + (progress - 50);
				} else {
					return 0;
				}
			}
			private int getMinutes(int progress) {
				if (progress <= 10) {
		        	return progress;

		        } else if (progress <= 50) {
		        	progress = (progress -9) * 10;
		        	return progress;
		        } else {
		        	return 0;
		        }
			}
		});
    	
    }
}