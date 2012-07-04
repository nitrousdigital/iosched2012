package com.nitrous.iosched.client.view.session.detail;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.data.EventDataWrapper;
import com.nitrous.iosched.client.event.EnrollSessionEvent;
import com.nitrous.iosched.client.event.RefreshViewEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent.View;
import com.nitrous.iosched.client.event.SessionEnrolledStateChangeEvent;
import com.nitrous.iosched.client.event.ShowSessionDetailEvent;
import com.nitrous.iosched.client.event.SimpleEventHandler;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.util.ConsoleLogger;
import com.nitrous.iosched.client.view.home.agenda.AgendaManager;
import com.nitrous.iosched.client.view.session.AbstractSessionToolbar;

public class SessionDetailTopToolbar extends AbstractSessionToolbar {
	private RootViewChangeEvent.View parentView;
	private EventDataWrapper eventDataWrapper;
	private static final int SCALED_SIZE = 32;
	
	private Image addIcon;
	private Image removeIcon;
	public SessionDetailTopToolbar() {
		super(false);
		
		addButtons();
		Registry.get().getEventBus().addHandler(SessionEnrolledStateChangeEvent.TYPE, new SimpleEventHandler<SessionEnrolledStateChangeEvent>(){
			@Override
			public void handleEvent(SessionEnrolledStateChangeEvent event) {
				if (eventDataWrapper != null && eventDataWrapper.getData().getId().equals(event.getEventId())) {
					enableAddSchedule(!event.isEnrolled());
				}
			}
			
		});
		Registry.get().getEventBus().addHandler(ShowSessionDetailEvent.TYPE, new SimpleEventHandler<ShowSessionDetailEvent>(){
			@Override
			public void handleEvent(ShowSessionDetailEvent event) {
				RootViewChangeEvent.View  view = event.getParentView(); 
				if (view != null) {
					parentView = view;
				}
				eventDataWrapper = event.getEvent(); 
				toggleButtons();
			}
		});
	}
	
	private void hideButtons() {
		addIcon.setVisible(false);
		removeIcon.setVisible(false);
	}
	private void enableAddSchedule(boolean enable) {
		addIcon.setVisible(enable);
		removeIcon.setVisible(!enable);
	}
	
	private void toggleButtons() {
		if (eventDataWrapper == null) {
			hideButtons();
			return;
		}
		
		final String eventId = eventDataWrapper.getData().getId();
		AgendaManager.get().isEnrolled(eventId, new AsyncCallback<Boolean>(){
			@Override
			public void onFailure(Throwable caught) {
				ConsoleLogger.error("Failed to determine whether enrolled in event id "+eventId, caught);
				hideButtons();
			}

			@Override
			public void onSuccess(Boolean result) {
				enableAddSchedule(Boolean.FALSE.equals(result));
			}
		});
	}
	
	private void addButtons() {
		// add schedule
		ImageResource addImg = Images.INSTANCE.addSchedule();
		addIcon = new Image(addImg.getSafeUri());
		addIcon.setPixelSize(SCALED_SIZE, SCALED_SIZE);
		rightButtons.add(addIcon);
		addIcon.setStyleName("clickable");
		addIcon.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onAddSchedule();
			}
		});
		addIcon.setTitle("Add to agenda");
		addIcon.setVisible(false);
		
		// remove schedule
		ImageResource removeImg = Images.INSTANCE.removeSchedule();
		removeIcon = new Image(removeImg.getSafeUri());
		removeIcon.setPixelSize(SCALED_SIZE, SCALED_SIZE);
		rightButtons.add(removeIcon);
		removeIcon.setStyleName("clickable");
		removeIcon.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onRemoveSchedule();
			}
		});
		removeIcon.setTitle("Remove from agenda");
		removeIcon.setVisible(false);
	}
	
	private void onAddSchedule() {
		Registry.get().getEventBus().fireEvent(new EnrollSessionEvent(eventDataWrapper, true));
	}
	private void onRemoveSchedule() {
		Registry.get().getEventBus().fireEvent(new EnrollSessionEvent(eventDataWrapper, false));
	}
	
	protected void onNavBack() {
		Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(parentView == null ? View.SESSION_LIST : parentView));
	}
	
	protected void onRefreshClick() {
		ConsoleLogger.debug("SessionDetailTopToolbar::onRefreshClick()");
		Registry.get().getEventBus().fireEvent(new RefreshViewEvent(RefreshViewEvent.View.SESSION_DETAIL));
	}
}
