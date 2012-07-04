package com.nitrous.iosched.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.nitrous.iosched.client.data.ConferenceSchedule;
import com.nitrous.iosched.client.data.SessionStore;
import com.nitrous.iosched.client.history.HistoryManager;
import com.nitrous.iosched.client.view.ApplicationPanel;
import com.nitrous.iosched.client.view.home.agenda.AgendaManager;

/**
 * Entry point for the Google IO 2012 Schedule application
 */
public class IOSched2012 implements EntryPoint {
	private Label loading;
	public void onModuleLoad() {
		loading = new Label("Loading Schedule, Please Wait...");
		RootLayoutPanel.get().add(loading);
		
		SessionStore.get().getSessions(new AsyncCallback<ConferenceSchedule>(){

			@Override
			public void onFailure(Throwable caught) {
				onDataReady();
			}

			@Override
			public void onSuccess(ConferenceSchedule result) {
				onDataReady();
			}}, false);
	}
	
	private void onDataReady() {
		loading.setText("Initializing...");
		AgendaManager.get().register();
		
		ApplicationPanel view = new ApplicationPanel();		
		RootLayoutPanel.get().remove(loading);		
		RootLayoutPanel.get().add(view);
		HistoryManager.get().init();
	}
}
