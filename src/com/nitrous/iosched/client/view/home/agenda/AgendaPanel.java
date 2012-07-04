package com.nitrous.iosched.client.view.home.agenda;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.data.ConferenceSchedule;
import com.nitrous.iosched.client.data.EventSlotDateComparator;
import com.nitrous.iosched.client.data.EventType;
import com.nitrous.iosched.client.data.SessionStore;
import com.nitrous.iosched.client.data.TimeSlotPanelComparator;
import com.nitrous.iosched.client.data.json.EventSlotListWrapper;
import com.nitrous.iosched.client.data.json.EventSlotWrapper;
import com.nitrous.iosched.client.data.json.SessionDataWrapper;
import com.nitrous.iosched.client.event.HomeTabSelectionEvent;
import com.nitrous.iosched.client.event.HomeTabSelectionEvent.Tab;
import com.nitrous.iosched.client.event.ReloadAndDisplayAgendaEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent.View;
import com.nitrous.iosched.client.event.SimpleEventHandler;
import com.nitrous.iosched.client.util.ConsoleLogger;

public class AgendaPanel implements IsWidget {
	private VerticalPanel layout;
	private ArrayList<TimeSlotPanel> slotPanels;
	
	public AgendaPanel() {
		this.slotPanels = new ArrayList<TimeSlotPanel>();
		layout = new VerticalPanel();
		layout.setWidth("100%");
		
		Registry.get().getEventBus().addHandler(ReloadAndDisplayAgendaEvent.TYPE, new SimpleEventHandler<ReloadAndDisplayAgendaEvent>(){
			@Override
			public void handleEvent(ReloadAndDisplayAgendaEvent event) {
				Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.HOME));
				Registry.get().getEventBus().fireEvent(new HomeTabSelectionEvent(Tab.MY_AGENDA));				
				onRefresh(false);
			}
		});
		
		Timer timer = new Timer(){
			@Override
			public void run() {
				onRefreshExpired();
			}
		};
		timer.scheduleRepeating(15000);
		onRefresh(false);		
	}
	
	private void onRefreshExpired() {
		if (slotPanels == null) {
			return;
		}
		for (TimeSlotPanel slot : slotPanels) {
			slot.refreshExpired();
		}
	}

	public void onRefresh(boolean reload) {
		SessionStore.get().getSessions(new AsyncCallback<ConferenceSchedule>(){
			@Override
			public void onFailure(Throwable caught) {
				ConsoleLogger.error("Agenda Panel - Failed to load session data", caught);
				onNoData();
			}

			@Override
			public void onSuccess(ConferenceSchedule result) {
				onRefresh(result);
			}
			
		}, reload);
	}
	
	private void onClear() {
		slotPanels.clear();
		layout.clear();
	}
	
	private void onNoData() {
		onClear();
		layout.add(new Label("No Session Data"));
	}
	
	/**
	 * Rebuild the UI with the updated conference schedule data
	 * @param result
	 */
	private void onRefresh(ConferenceSchedule result) {
		if (result == null) {
			onNoData();
			return;
		}
		
		ArrayList<SessionDataWrapper> sessionData = result.getSessionData();
		if (sessionData == null || sessionData.size() == 0) {
			onNoData();
			return;
		}
		
		// build a sorted map of time slot panels
		int slotCount = 0;
		TreeMap<Date, TreeSet<TimeSlotPanel>> slotPanels = new TreeMap<Date, TreeSet<TimeSlotPanel>>(new EventSlotDateComparator());
		for (SessionDataWrapper sessionDataWrapper : sessionData) {
			// keynote, session or codelab
			String eventType = sessionDataWrapper.getData().getEventType();
			EventType type = EventType.parse(eventType);
			if (type == null) {
				continue;
			}
			
			// the event slots for a particular event type (i.e. keynote, session or codelab)
			EventSlotListWrapper eventSlots = sessionDataWrapper.getEventSlots();
			if (eventSlots == null) {
				continue;				
			}
			TreeMap<Date, TreeSet<EventSlotWrapper>> slots = eventSlots.getSlots();
			if (slots == null) {
				continue;
			}
			for (Map.Entry<Date, TreeSet<EventSlotWrapper>> slotDef : slots.entrySet()) {
				Date date = slotDef.getKey();
				TreeSet<TimeSlotPanel> panels = slotPanels.get(date);
				if (panels == null) {
					panels = new TreeSet<TimeSlotPanel>(new TimeSlotPanelComparator());
					slotPanels.put(date, panels);
				}
				
				// create a slot panel for each time slot on the current date
				for (EventSlotWrapper slot : slotDef.getValue()) {
					TimeSlotPanel slotPanel = new TimeSlotPanel(type, slot);
					panels.add(slotPanel);
					slotCount++;
				}
			}			
		}
		
		if (slotCount == 0) {
			onNoData();
			return;
		}
		
		// add slots to the UI
		onClear();
		for (Map.Entry<Date, TreeSet<TimeSlotPanel>> entry : slotPanels.entrySet()) {
			Date date = entry.getKey();				
			DateSectionRow dateRow = new DateSectionRow(date);
			this.layout.add(dateRow);
			for (TimeSlotPanel slot : entry.getValue()) {
				this.layout.add(slot);
				this.slotPanels.add(slot);
			}
		}
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}		

}
