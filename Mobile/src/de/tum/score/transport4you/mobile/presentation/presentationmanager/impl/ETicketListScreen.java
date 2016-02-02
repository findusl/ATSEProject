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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationSingleton;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.qrcode.GenerateQRCodeActivity;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketSingle;

public class ETicketListScreen extends Activity implements IPresentation, OnItemClickListener {
	// i know this should be persisted over app starts. but honestly that is to
	// much work for the noten bonus
	private static final ArrayList<ETicket> usedTickets = new ArrayList<ETicket>();
	private IMainApplication mainApplication;

	TicketAdapter mAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eticketlist);

		mainApplication = ApplicationSingleton.getApplicationController();
		mainApplication.registerActivity(this);
	}

	@Override
	protected void onStart() {
		// reload tickets every time.

		BlobEntry entry = mainApplication.getStoredBlobEntry();

		ArrayList<ETicket> tickets = new ArrayList<ETicket>();

		// get etickets from blob
		if (entry != null) {
			tickets = entry.geteTicketList();
		}

		tickets.removeAll(usedTickets);

		// Create the adapter to convert the array to views
		mAdapter = new TicketAdapter(this, 0, tickets);

		// Attach the adapter to a ListView
		ListView listView = (ListView) findViewById(R.id.list_etickets);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
		super.onStart();
	}

	@Override
	public void onItemClick(AdapterView<?> list, View view, int position, long id) {
		Intent intent = new Intent(this, GenerateQRCodeActivity.class);
		ETicket ticket = mAdapter.getItem(position);
		byte[] content = ticket.getEncryptedTicket();
		intent.putExtra(GenerateQRCodeActivity.ARGS_TEXT, content);
		startActivityForResult(intent, position);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == GenerateQRCodeActivity.RETURN_USED) {
			// ticket was used
			ETicket ticket = mAdapter.getItem(requestCode);
			if (ticket != null && ticket instanceof ETicketSingle) {
				// if it's a single ticket add it to the list of no longer
				// displayed tickets
				usedTickets.add(ticket);
				mAdapter.remove(ticket);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
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