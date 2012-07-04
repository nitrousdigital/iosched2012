package com.nitrous.iosched.client.view.home;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.images.Images;

public class RootHomeViewToolbar implements IsWidget {
	private static final int PADDING = 5;	
	private static final float LOGO_WIDTH = 340f;
	private static final float LOGO_HEIGHT = 72f;
	
	private static final int SCALED_LOGO_HEIGHT = 32;
	
	public static final int HEIGHT = PADDING * 2 + SCALED_LOGO_HEIGHT;
	
	private HorizontalPanel layout;
	
	public RootHomeViewToolbar() {
		layout = new HorizontalPanel();
		layout.setStyleName("top-toolbar");
		layout.setWidth("100%");
		
		// IO logo
		ImageResource resource = Images.INSTANCE.actionbarLogo();
		Image logo = new Image(resource.getSafeUri());
		logo.setPixelSize((int)(LOGO_WIDTH * (SCALED_LOGO_HEIGHT / LOGO_HEIGHT)), SCALED_LOGO_HEIGHT);
		layout.add(logo);		
		
		// right-aligned buttons
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		layout.add(buttons);		
	}
		
	@Override
	public Widget asWidget() {
		return layout;
	}

}
