package com.nitrous.iosched.client.view.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.data.EventDataWrapper;
import com.nitrous.iosched.client.util.ConsoleLogger;
import com.nitrous.iosched.client.view.home.agenda.DateSectionRow;

/**
 * A re-usable component for displaying and allowing the user to select sessions
 * @author nick
 *
 */
public class SessionListComponent implements IsWidget, SessionSelectionListener {
	private VerticalPanel list;
	private ScrollPanel scroll;
	private ArrayList<SessionRow> rows;
	private SessionSelectionListener selectionListener = null;
	public SessionListComponent() {
		list = new VerticalPanel();
		rows = new ArrayList<SessionRow>();
		list.setWidth("100%");
		list.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		scroll = new ScrollPanel(list);
		scroll.setSize("100%", "100%");
		
		// refresh expiration periodically
		Timer expirationRefreshTimer = new Timer(){
			@Override
			public void run() {
				for (SessionRow row : rows) {
					row.refreshExpired();
				}
			}
		};
		expirationRefreshTimer.scheduleRepeating(15000);
	}
	
	public void setSessionSelectionListener(SessionSelectionListener listener) {
		this.selectionListener = listener;
	}
	
	public void showError(String error) {
		ConsoleLogger.error("SessionListComponent::showError("+error+")");
		onClear();
		list.add(new HTML("<span style='color=\"#ff0000\"'>" + error + "</span>"));
	}
	
	public void onClear() {
		list.clear();
		for (SessionRow row : rows) {
			row.dispose();
		}
		rows.clear();
	}
	
	public void showEvents(TreeSet<EventDataWrapper> sortedEvents) {
		ConsoleLogger.debug("SessionListComponent::showSchedule() - displaying "
				+(sortedEvents != null ? sortedEvents.size() : 0)
				+" events");
		onClear();
		if (sortedEvents == null || sortedEvents.size() == 0) {
			list.add(new HTML("<span style='color=\"#0000ff\"'>No event data.</span>"));
			return;
		}
		
		Date curDate = null;
		for (EventDataWrapper event : sortedEvents) {
			Date evtDate = event.getSlot().getStartDate();
			if (curDate == null || !curDate.equals(evtDate)) {
				curDate = evtDate;
				list.add(new DateSectionRow(curDate));
			}
			SessionRow row = new SessionRow(event);
			row.setSelectionListener(this);
			list.add(row);
			rows.add(row);
		}				
	}
	
	@Override
	public Widget asWidget() {
		return scroll;
	}

	@Override
	public void onSessionSelected(EventDataWrapper event) {
		if (selectionListener != null) {
			selectionListener.onSessionSelected(event);
		}		
	}

}
