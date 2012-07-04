package com.nitrous.iosched.client.view.session;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.event.SimpleEventHandler;
import com.nitrous.iosched.client.event.TrackSelectionEvent;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.model.SessionTrack;

public abstract class AbstractSessionToolbar implements IsWidget {
	private static final int PADDING = 5;	
	private static final float LOGO_WIDTH = 96f;
	private static final float LOGO_HEIGHT = 96f;
	
	protected static final int SCALED_LOGO_HEIGHT = 32;
	
	public static final int HEIGHT = PADDING * 2 + SCALED_LOGO_HEIGHT;
	
	private HorizontalPanel layout;
	private Label trackLabel;
	protected HorizontalPanel leftButtons;
	protected HorizontalPanel rightButtons;
	
	protected abstract void onNavBack();
	
	protected void onRefreshClick() {
	}
	
	public AbstractSessionToolbar(boolean refreshable) {
		layout = new HorizontalPanel();
		layout.setStyleName("top-toolbar");
		layout.setWidth("100%");
		
		leftButtons = new HorizontalPanel();
		leftButtons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		layout.add(leftButtons);
		
		// back arrow
		Image backArrow = new Image(Images.INSTANCE.backArrow());
		leftButtons.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		leftButtons.add(backArrow);
		backArrow.setStyleName("clickable");
		backArrow.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onNavBack();
			}
		});
		
		// IO logo
		ImageResource resource = Images.INSTANCE.ioLogo();
		Image logo = new Image(resource.getSafeUri());
		logo.setPixelSize((int)(LOGO_WIDTH * (SCALED_LOGO_HEIGHT / LOGO_HEIGHT)), SCALED_LOGO_HEIGHT);
		leftButtons.add(logo);		
		logo.setStyleName("clickable");
		logo.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onNavBack();
			}
		});

		trackLabel = new Label("All tracks");
		trackLabel.setStyleName("map-toolbar-text");
		leftButtons.add(trackLabel);
		
		// right-aligned buttons
		rightButtons = new HorizontalPanel();
		rightButtons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		layout.add(rightButtons);
				
		Registry.get().getEventBus().addHandler(TrackSelectionEvent.TYPE, new SimpleEventHandler<TrackSelectionEvent>(){
			@Override
			public void handleEvent(TrackSelectionEvent event) {
				setTrack(event.getTrack());
			}
		});
	}
	
	private void setTrack(SessionTrack track) {
		if (track == null || SessionTrack.All.equals(track)) {
			trackLabel.setText("All sessions");
		} else {
			trackLabel.setText(track.getLabel());
		}
	}
	
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
