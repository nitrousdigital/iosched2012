package com.nitrous.iosched.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class RefreshViewEvent  extends GwtEvent<SimpleEventHandler<RefreshViewEvent>> {
	public static enum View {
		HOME,
		SESSIONS_LIST,
		SESSION_DETAIL,
		MAP,
		AGENDA_SESSION_SELECTION
	}
	private View view;
	public RefreshViewEvent(View view) {
		this.view = view;
	}
	
	public View getView() {
		return view;
	}
	
	///// boiler plate
	public static final Type<SimpleEventHandler<RefreshViewEvent>> TYPE = new Type<SimpleEventHandler<RefreshViewEvent>>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SimpleEventHandler<RefreshViewEvent>> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SimpleEventHandler<RefreshViewEvent> handler) {
		handler.handleEvent(this);
	}

}
