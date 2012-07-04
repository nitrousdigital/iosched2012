package com.nitrous.iosched.client.event;

import com.nitrous.iosched.client.history.Bookmark;


public class RootViewChangeEvent extends AbstractNavigationEvent<RootViewChangeEvent> {
	public static enum View {
		HOME,
		MAP,
		SESSION_LIST,
		SESSION_DETAIL,
		AGENDA_SESSION_SELECTION
	}
	
	private View view;
	public RootViewChangeEvent(View view) {
		this(view, null);
	}
	public RootViewChangeEvent(View view, Bookmark bookmark) {
		super(bookmark);
		this.view = view;
	}
	
	public View getView() {
		return view;
	}
	
	///// boiler plate
	public static final Type<SimpleEventHandler<RootViewChangeEvent>> TYPE = new Type<SimpleEventHandler<RootViewChangeEvent>>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SimpleEventHandler<RootViewChangeEvent>> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SimpleEventHandler<RootViewChangeEvent> handler) {
		handler.handleEvent(this);
	}

}
