package se.edevag.locationtracker;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        
        final TrackerService service = new TrackerService(this, this.getApplicationContext());
        
        List<TrackerEntry> history = service.load(30, true);
        
        // Setup button listeners
        setupViewMoreButtonListener(service);
        
        // Now set up the user interface
        TextView historicalEntries = (TextView) findViewById(R.id.historicEntries);
        historicalEntries.setVerticalScrollBarEnabled(true);
        
        String textToDisplay = "";
        int i=0;
        for (TrackerEntry entry : history) {i++;
        	textToDisplay += entry.getDisplayTime() + ": " + entry.latitude + " / " + entry.longitude + System.getProperty("line.separator");
        }
        
        Toast.makeText(this , 
    			"Loaded entries: " + i, 
    			Toast.LENGTH_SHORT ).show();
        
        historicalEntries.setText(textToDisplay);
    }

	private void setupViewMoreButtonListener(final TrackerService service) {
    	final Button history = (Button) findViewById(R.id.historyButton);
    	
    	history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String textToDisplay = "";
                for (TrackerEntry entry : service.load(30, true)) {
            		textToDisplay += entry.getDisplayTime() + ": " + entry.latitude + " / " + entry.longitude + System.getProperty("line.separator");
                }
                
                ((TextView) findViewById(R.id.historicEntries)).setText(textToDisplay);
            }
        });
	}

    
    
    
    
    
    
    
    
    
    /*
    
    private void setupUploadButtonListener(final TrackerService service) {
    	Button button = (Button) findViewById(R.id.button1);
    	
    	button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
            	List<String> history = service.load(10, true);
            	
            	TextView tv = (TextView) findViewById(R.id.textView1);
            	
            	tv.setText("Hej hopp");
            	
            	String text = "";
            	for (String line : history) {
            		//tv.append(line);
            		//tv.setText(line);
            		String [] parts = line.split(",");
            		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            		
            		text += sdf.format(new Date(new Long(parts[0])))
            				+ ":  " + parts[1] + " / " + parts[2]
            				+ System.getProperty("line.separator");
            	}
            	tv.setText(text);
            	
            }
        });
    }    
    */
    
    
    
    
    /*
    
    private void setupMoreHistoryListener(final TrackerService service) {
    	final RadioButton fetchMore = (RadioButton) findViewById(R.id.fetchMore);
    	
    	fetchMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            	List<String> history = service.load(10, false);
            	
            	TextView tv = (TextView) findViewById(R.id.textView1);
            	
            	for (String line : history) {
            		
            		
            		String [] parts = line.split(",");
            		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            		
            		String text = sdf.format(new Date(new Long(parts[0])))
            				+ ":  " + parts[1] + " / " + parts[2]
            				+ System.getProperty("line.separator");
            		
            		tv.append(text);
            	}
            	
            	fetchMore.setChecked(false);
            	
            }
        });
    	*/
    
}
