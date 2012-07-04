package com.nitrous.iosched.client.view.home.agenda.selection;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.data.EventType;
import com.nitrous.iosched.client.event.HomeTabSelectionEvent;
import com.nitrous.iosched.client.event.HomeTabSelectionEvent.Tab;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent.View;
import com.nitrous.iosched.client.event.ShowFilteredSessionSelectionViewEvent;
import com.nitrous.iosched.client.event.SimpleEventHandler;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.util.Util;

public class SessionSelectionToolbar implements IsWidget {
	private static final DateTimeFormat TIME_FORMAT = DateTimeFormat.getFormat("hh:mmaa");
	
	private static final int PADDING = 5;	
	private static final float LOGO_WIDTH = 96f;
	private static final float LOGO_HEIGHT = 96f;
	
	private static final int SCALED_LOGO_HEIGHT = 32;
	
	public static final int HEIGHT = PADDING * 2 + SCALED_LOGO_HEIGHT;
	
	private HorizontalPanel layout;
	private Label titleLabel;
	public SessionSelectionToolbar() {
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

		titleLabel = new Label();
		titleLabel.setStyleName("map-toolbar-text");
		backButtons.add(titleLabel);
		
		// right-aligned buttons
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		layout.add(buttons);
		
		Registry.get().getEventBus().addHandler(ShowFilteredSessionSelectionViewEvent.TYPE, new SimpleEventHandler<ShowFilteredSessionSelectionViewEvent>(){
			@Override
			public void handleEvent(ShowFilteredSessionSelectionViewEvent event) {
				showTitle(event);
			}
		});
	}
	
	private void showTitle(ShowFilteredSessionSelectionViewEvent event) {
		EventType type = event.getEventType();
		Date startTime = event.getStartTime();
		StringBuffer buf = new StringBuffer();
		buf.append(TIME_FORMAT.format(startTime, Util.TIMEZONE));
		switch (type) {
		case CODELAB:
			buf.append(" - Code Labs");
			break;
		case SESSION:
			buf.append(" - Sessions");
			break;			
		}
		titleLabel.setText(buf.toString());
	}
	
	private void onNavBack() {
		Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.HOME));
		Registry.get().getEventBus().fireEvent(new HomeTabSelectionEvent(Tab.MY_AGENDA));
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
