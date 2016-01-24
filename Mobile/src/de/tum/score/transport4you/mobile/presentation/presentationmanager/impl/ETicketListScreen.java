package de.tum.score.transport4you.mobile.presentation.presentationmanager.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationSingleton;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.qrcode.GenerateQRCodeActivity;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;

public class ETicketListScreen extends Activity implements IPresentation, OnItemSelectedListener{
    private IMainApplication mainApplication;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eticketlist);
        
        mainApplication = ApplicationSingleton.getApplicationController();
        mainApplication.registerActivity(this);
        
		BlobEntry entry = mainApplication.getStoredBlobEntry();
        
		ArrayList<ETicket> tickets = new ArrayList<ETicket>();
		
		//get etickets from blob
        if (entry != null) {
        	tickets = entry.geteTicketList();
        }
        
        // Create the adapter to convert the array to views
        TicketAdapter adapter = new TicketAdapter(this, 0, tickets);
        
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.list_etickets);
        listView.setAdapter(adapter);
        listView.setOnItemSelectedListener(this);
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    	startActivity(new Intent(this, GenerateQRCodeActivity.class));
    }
    
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    	// TODO Auto-generated method stub
    	
    }
	
    public void onDestroy() {
    	super.onDestroy();
    }

	@Override
	public void shutdown() {
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.mn_exit:
	        mainApplication.shutdownSystem();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void updateProgessDialog(String title, String message, boolean visible, Integer increment) {
		// TODO Auto-generated method stub
		
	}
}