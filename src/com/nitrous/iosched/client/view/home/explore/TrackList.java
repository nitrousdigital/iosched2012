package com.nitrous.iosched.client.view.home.explore;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent.View;
import com.nitrous.iosched.client.event.TrackSelectionEvent;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.util.ConsoleLogger;

/**
 * The scrollable list of session tracks
 * @author nick
 *
 */
public class TrackList implements IsWidget {
	private VerticalPanel list;
	public TrackList() {
		
		list = new VerticalPanel();
		list.setWidth("100%");
		list.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		for (SessionTrack track : SessionTrack.values()) {
			Label label = new Label(track.getLabel());
			label.setStyleName("session-track-text");
			label.setWidth("100%");
			label.setHeight("32px");
			
			HorizontalPanel row = new HorizontalPanel();
			row.setStyleName("session-track-row");
			row.setWidth("100%");
			row.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			row.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			row.add(label);
			
			ImageResource resource = Images.INSTANCE.empty();
			Image swatch = new Image(resource.getSafeUri());
			swatch.setStyleName("session-track-swatch");
			swatch.setPixelSize(16, 16);
			if (track.getColor() != null) {
			    swatch.getElement().getStyle().setBackgroundColor(track.getColor());
			}
			
			final SessionTrack t = track;
			label.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					onSelectTrack(t);
				}
			});
			swatch.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					onSelectTrack(t);
				}
			});
			
			
			row.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			row.add(swatch);
			row.setCellWidth(swatch, "16px");
			
			list.add(row);
		}
	}
	
	private void onSelectTrack(SessionTrack track) {
		ConsoleLogger.debug("TrackList::onSelectTrack("+track+")");		
		Registry.get().getEventBus().fireEvent(new TrackSelectionEvent(track));
		Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.SESSION_LIST));
	}
	
	@Override
	public Widget asWidget() {
		return list;
	}

}
