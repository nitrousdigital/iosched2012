package com.nitrous.iosched.client.view.home.countdown;

import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CountDownPanel implements IsWidget, CountDown.View, ClickHandler {
	private VerticalPanel layout;
	public static final int HEIGHT = 42;
	
	private CountDown countdown;
	private Label timeRemaining;
	private CountDownContainer container;
	public CountDownPanel(CountDownContainer container) {
		this.container = container;

		timeRemaining = new Label();
		timeRemaining.addClickHandler(this);
		timeRemaining.setStyleName("countdown-text");
		HorizontalPanel topRow = new HorizontalPanel();
		topRow.setWidth("100%");
		topRow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		topRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		topRow.add(timeRemaining);
		
		Label message = new Label("until Google I/O");
		message.setStyleName("countdown-text");
		message.addClickHandler(this);
		HorizontalPanel bottomRow = new HorizontalPanel();
		bottomRow.setWidth("100%");
		bottomRow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		bottomRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		bottomRow.add(message);
		
		layout = new VerticalPanel();
		layout.setWidth("100%");
		layout.setStyleName("countdown-panel");
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		layout.add(topRow);
		layout.add(bottomRow);
		
		countdown = new CountDown(this);
		countdown.start();
	}

	@Override
	public Widget asWidget() {
		return layout;
	}

	private void hide() {
		layout.getElement().getStyle().setVisibility(Visibility.HIDDEN);
	}
	
	public boolean isExpired() {
		return countdown.isExpired();
	}
		
	@Override
	public void onCountDownUpdate() {
		
		int days = countdown.getDays();
		days = Math.max(days, 0);
		
		int hours = countdown.getHours();
		hours = Math.max(hours, 0);
		
		int minutes = countdown.getMinutes();
		minutes = Math.max(minutes, 0);
		
		int seconds = countdown.getSeconds();
		seconds = Math.max(seconds, 0);
		
		StringBuffer text = new StringBuffer();
		
		// days
		if (days > 0) {
			text.append(days);
			if (days == 1) {
				text.append(" day, ");
			} else {
				text.append(" days, ");
			}
		}
		
		// hours
		if (hours > 0) {
			text.append(hours).append(":");
		}
		
		// minutes
		if (minutes < 10) {
			text.append("0");
		}
		text.append(minutes).append(":");
		
		// seconds
		if (seconds < 10) {
			text.append("0");
		}
		text.append(seconds);
		
		timeRemaining.setText(text.toString());
		
		if (countdown.isExpired()) {
			countdown.stop();
			hide();
			container.onCountDownExpired();
			return;
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		Window.open("https://input-output.appspot.com", "_new", "");				
	}
	
}
