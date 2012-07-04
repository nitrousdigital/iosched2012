package com.nitrous.iosched.client.view.session;

import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.event.RefreshViewEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent.View;
import com.nitrous.iosched.client.util.ConsoleLogger;

public class SessionToolbar extends AbstractSessionToolbar {
	public SessionToolbar() {
		super(true);
	}
	
	@Override
	protected void onNavBack() {
		Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.HOME));
	}
	
	@Override
	protected void onRefreshClick() {
		ConsoleLogger.debug("SessionToolbar::onRefreshClick()");
		Registry.get().getEventBus().fireEvent(new RefreshViewEvent(RefreshViewEvent.View.SESSIONS_LIST));
	}

}
