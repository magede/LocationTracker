package se.edevag.locationtracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.widget.TextView;
import android.widget.Toast;

public class TrackerService {

	//private static String DB_FILE = "trackerStorage";
	private Context ctx;
	private Activity activity;
	private int historyEntriesLoaded = 0;
	
	private TrackerDbAdapter dbAdapter;
	
	public TrackerService (Activity activity, Context ctx) {
		this.ctx = ctx;
		this.activity = activity;
		
		dbAdapter = new TrackerDbAdapter(ctx).open();
	}
	
	public void perist(Location location) {
		/*
		try {
			FileOutputStream fos = ctx.openFileOutput(DB_FILE, Context.MODE_APPEND);
			
			fos.write((System.currentTimeMillis() + "," + location.getLatitude() + "," + location.getLongitude()).getBytes());
			fos.write('|');
			fos.close();
			
			//Toast.makeText( ctx, "Location persisted, now sending notif", Toast.LENGTH_SHORT ).show();
			//sendNotification("NOTIF!");
		} catch (IOException e) {
			Toast.makeText( ctx, "Failed saving tracker cordinates: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
			sendNotification("Failed saving tracker cordinates: " + e.getMessage());
		}
		*/
		dbAdapter.insert(location);
		
		SimpleDateFormat sdf = new SimpleDateFormat("hh:MM:ss");
		
		TextView headerText = (TextView) activity.findViewById(R.id.header);
		TextView lastLocation = (TextView) activity.findViewById(R.id.lastLocation);
		
		headerText.setText("Updated: " + sdf.format(new Date(System.currentTimeMillis())) + ", accur: " + location.getAccuracy() + " m");
		lastLocation.setText("   " + location.getLatitude() + " / " + location.getLongitude());
	}
	
	public List<TrackerEntry> load(int i, boolean fromBeginning) {
		List<String> history = new ArrayList<String>();
		/*
		if (fromBeginning)historyEntriesLoaded=0;
		
		try {
			StringBuffer content = new StringBuffer();
			FileInputStream fis = ctx.openFileInput(DB_FILE);
			
			int ch;
			int counter = 0;
			while( ((ch = fis.read()) != -1) && counter < i) {
				if (ch == '|') {
					if (i > historyEntriesLoaded) {
						history.add(content.toString());
						content = new StringBuffer();
						counter++;
					}
				} else {
					content.append((char)ch);
				}
			}
			
			fis.close();
			
			historyEntriesLoaded +=i;
			
		} catch (IOException e) {
			Toast.makeText( ctx, "Unable to load history: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
		}
		*/
		
		return dbAdapter.fetch(50, true);
	}
	
	/*
	public boolean clearHistory() {
		boolean deleted = ctx.deleteFile(DB_FILE);
		
		if (deleted) {
			Toast.makeText( ctx, "History removed", Toast.LENGTH_SHORT ).show();
		} else {
			Toast.makeText( ctx, "Failed to remove history", Toast.LENGTH_SHORT ).show();
		}
		
		historyEntriesLoaded = 0;
		
		return deleted;
	}
	*/
	
	/*
	public void sendNotification(String message) {

		Intent contentIntent = new Intent();
		PendingIntent appIntent = PendingIntent.getActivity(ctx, 0, contentIntent, 0);
		
		NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(
				R.drawable.ic_launcher,
				message,
				System.currentTimeMillis());
		
		notification.setLatestEventInfo(ctx, message, message, appIntent);
		
		mNotificationManager.notify(1, notification);
		
	}
	*/
}
