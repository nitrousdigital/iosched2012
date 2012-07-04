package com.nitrous.iosched.client.view.map;

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
import com.nitrous.iosched.client.event.RefreshViewEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent.View;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.util.ConsoleLogger;

public class MapToolbar implements IsWidget {
	private static final int PADDING = 5;	
	private static final float LOGO_WIDTH = 96f;
	private static final float LOGO_HEIGHT = 96f;
	
	private static final int SCALED_LOGO_HEIGHT = 32;
	
	public static final int HEIGHT = PADDING * 2 + SCALED_LOGO_HEIGHT;
	
	private HorizontalPanel layout;
	
	public MapToolbar() {
		layout = new HorizontalPanel();
		layout.setStyleName("top-toolbar");
		layout.setWidth("100%");
		
		HorizontalPanel backButtons = new HorizontalPanel();
		backButtons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		layout.add(backButtons);
		
		// back arrow
		Image backArrow = new Image(Images.INSTANCE.backArrow());
		backButtons.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		backButtons.add(backArrow);
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
		backButtons.add(logo);		
		logo.setStyleName("clickable");
		logo.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onNavBack();
			}
		});

		Label mapLabel = new Label("Map");
		mapLabel.setStyleName("map-toolbar-text");
		backButtons.add(mapLabel);
		
		// right-aligned buttons
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		layout.add(buttons);
		
		// refresh
		Image refresh = new Image(Images.INSTANCE.refresh());
		refresh.setStyleName("clickable");
		refresh.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onRefreshClick();
			}
		});
		buttons.add(refresh);
	}
	
	private void onNavBack() {
		Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.HOME));
	}
	
	private void onRefreshClick() {
		ConsoleLogger.debug("MapToolbar::onRefreshClick()");
		Registry.get().getEventBus().fireEvent(new RefreshViewEvent(RefreshViewEvent.View.MAP));
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
