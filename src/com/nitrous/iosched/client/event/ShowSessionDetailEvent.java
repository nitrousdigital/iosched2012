package com.nitrous.iosched.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.nitrous.iosched.client.data.EventDataWrapper;

/**
 * Display the details of the specified session in the session details view
 * 
 * @author nick
 * 
 */
public class ShowSessionDetailEvent extends GwtEvent<SimpleEventHandler<ShowSessionDetailEvent>> {
	private EventDataWrapper event;
	private RootViewChangeEvent.View parentView;

	/**
	 * 
	 * @param event
	 *            The event whose details are to be displayed
	 * @param parentView
	 *            The view that should be displayed if the user clicks the
	 *            nav-back button in the toolbar on the session detail view
	 */
	public ShowSessionDetailEvent(EventDataWrapper event, RootViewChangeEvent.View parentView) {
		this.event = event;
		this.parentView = parentView;
	}

	/**
	 * 
	 * @return The view that should be displayed if the user clicks the nav-back
	 *         button in the toolbar on the session detail view
	 */
	public RootViewChangeEvent.View getParentView() {
		return this.parentView;
	}

	public EventDataWrapper getEvent() {
		return event;
	}

	// /// boiler plate
	public static final Type<SimpleEventHandler<ShowSessionDetailEvent>> TYPE = new Type<SimpleEventHandler<ShowSessionDetailEvent>>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SimpleEventHandler<ShowSessionDetailEvent>> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SimpleEventHandler<ShowSessionDetailEvent> handler) {
		handler.handleEvent(this);
	}

}
