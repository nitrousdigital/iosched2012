package com.nitrous.iosched.client.widget;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * A card layout implementation
 * @author nick
 *
 */
public class CardPanel implements IsWidget {
	private DockLayoutPanel layout;
	
	private Map<String, Widget> cards;
	private String activeId = null;
	private Widget activeWidget = null;
	
	public CardPanel() {
		cards = new HashMap<String, Widget>();
		layout = new DockLayoutPanel(Unit.PX);
	}
	
	public void addCard(String id, IsWidget card) {
		addCard(id, card.asWidget());
	}
	
	public void addCard(String id, Widget card) {
		cards.put(id, card);
		if (activeId != null && activeId.equals(id)) {
			setActiveWidget(card);
		}
	}

	public void showCard(String id) {
		Widget w = cards.get(id);
		setActiveWidget(w);
	}
	
	public Widget getActiveCard() {
		return activeWidget;
	}
	
	private void setActiveWidget(Widget w) {
		if (activeWidget != null) {
			layout.remove(activeWidget);
		}
		
		activeWidget = w;
		
		if (activeWidget != null) {
			layout.add(activeWidget);
			activeWidget.setSize("100%", "100%");
		}
		Scheduler.get().scheduleDeferred(new ScheduledCommand(){
			@Override
			public void execute() {
				layout.forceLayout();
			}
		});
		layout.forceLayout();
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}
	
}
