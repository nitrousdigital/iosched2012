package com.nitrous.iosched.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle {
	public static final Images INSTANCE = GWT.create(Images.class);
	
	@Source("googleio12_logo.png")
	public ImageResource actionbarLogo();
	
	@Source("ic_action_search32.png")
	public ImageResource search();
	
	@Source("ic_action_refresh32.png")
	public ImageResource refresh();
	
	@Source("io_logo.png")
	public ImageResource ioLogo();
	
	@Source("empty.png")
	public ImageResource empty();
	
	@Source("abs__ic_ab_back_holo_dark.png")
	public ImageResource backArrow();
	
	@Source("ic_action_add_schedule.png")
	public ImageResource addSchedule();

	@Source("ic_action_remove_schedule.png")
	public ImageResource removeSchedule();
}
