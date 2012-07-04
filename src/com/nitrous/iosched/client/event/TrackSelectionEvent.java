package com.nitrous.iosched.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.nitrous.iosched.client.model.SessionTrack;

public class TrackSelectionEvent extends GwtEvent<SimpleEventHandler<TrackSelectionEvent>> {
	private SessionTrack track;
	
	public TrackSelectionEvent(SessionTrack track) {
		this.track = track;
	}
	
	public SessionTrack getTrack() {
		return track;
	}
	
	///// boiler plate
	public static final Type<SimpleEventHandler<TrackSelectionEvent>> TYPE = new Type<SimpleEventHandler<TrackSelectionEvent>>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SimpleEventHandler<TrackSelectionEvent>> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SimpleEventHandler<TrackSelectionEvent> handler) {
		handler.handleEvent(this);
	}

}
