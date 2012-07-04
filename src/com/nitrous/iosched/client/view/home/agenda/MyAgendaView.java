package com.nitrous.iosched.client.view.home.agenda;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class MyAgendaView implements IsWidget {
	private DockLayoutPanel layout;
	private AgendaPanel agendaPanel;
	
	public MyAgendaView() {
		layout = new DockLayoutPanel(Unit.PX);		
		agendaPanel = new AgendaPanel(); 
		ScrollPanel scroll = new ScrollPanel(agendaPanel.asWidget());
		layout.add(scroll);
	}
	
	public void onRefresh() {
		boolean reloadConferenceSchedule = false;
		agendaPanel.onRefresh(reloadConferenceSchedule);
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
