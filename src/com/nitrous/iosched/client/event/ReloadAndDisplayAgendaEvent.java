package com.nitrous.iosched.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Cause the agenda panel to display and refresh
 * @author nick
 *
 */
public class ReloadAndDisplayAgendaEvent extends GwtEvent<SimpleEventHandler<ReloadAndDisplayAgendaEvent>> {
	public ReloadAndDisplayAgendaEvent() {
	}
	
	///// boiler plate
	public static final Type<SimpleEventHandler<ReloadAndDisplayAgendaEvent>> TYPE = new Type<SimpleEventHandler<ReloadAndDisplayAgendaEvent>>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SimpleEventHandler<ReloadAndDisplayAgendaEvent>> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SimpleEventHandler<ReloadAndDisplayAgendaEvent> handler) {
		handler.handleEvent(this);
	}

}
