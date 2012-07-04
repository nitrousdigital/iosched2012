package com.nitrous.iosched.client.widget;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class TabPanel implements IsWidget {
	private static final int TAB_HEIGHT = 34;
	
	private DockLayoutPanel layout;
	private TabHeader header;
	private CardPanel cards;
	private TabChangeListener listener;
	private String visibleTabId = null;
	
	public interface TabChangeListener {
		public void onTabSelected(String id);
	}
	
	public TabPanel() {
		layout = new DockLayoutPanel(Unit.PX);
		header = new TabHeader();
		layout.addNorth(header, TabHeader.HEIGHT);
		
		cards = new CardPanel();
		cards.asWidget().setSize("100%", "100%");
		layout.add(cards);
	}
	
	public void add(IsWidget widget, String id, String label) {
		add(widget.asWidget(), id, label);
	}
	
	public void add(Widget widget, String id, String label) {
		cards.addCard(id, widget);
		header.addButton(label, id);
	}
	
	public String getSelectedTabId() {
		return visibleTabId;
	}
	
	public void setTabChangeListener(TabChangeListener l) {
		this.listener = l;
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

	public void showTab(String id) {
		showTab(id, true);
	}
	public void showTab(String id, boolean notify) {
		if ((visibleTabId == null && id != null) 
				|| (visibleTabId != null && !visibleTabId.equals(id))) {
			visibleTabId = id;
			cards.showCard(id);
			header.selectButton(id);
			if (notify && listener != null) {
				listener.onTabSelected(id);
			}
		}
	}
	
	private class TabHeader implements IsWidget {
		private HorizontalPanel layout;
		private static final int TAB_PANEL_PADDING = 1;
		public static final int HEIGHT = TAB_HEIGHT + TAB_PANEL_PADDING;
		private ArrayList<TabButton> buttons;
		
		public TabHeader() {
			layout = new HorizontalPanel();
			layout.setStyleName("tab-panel");
			layout.setWidth("100%");
			layout.setHeight(TAB_HEIGHT + "px");
			layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			buttons = new ArrayList<TabButton>();
		}
				
		public void selectButton(String id) {
			for (TabButton button : buttons) {
				if (id.equals(button.getId())) {
					button.setSelected(true);
				} else {
					button.setSelected(false);
				}
			}
		}
		
		public void addButton(String label, final String id) {
			TabButton button = new TabButton(label, id);
			if (buttons.size() > 0) {
				buttons.get(buttons.size()-1).setHasRightBorder(true);
			}
			buttons.add(button);
			button.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					showTab(id);
				}
			});
			layout.add(button);
		}
		
		@Override
		public Widget asWidget() {
			return layout;
		}
	}
	
	private static class TabButton implements IsWidget {
		private Button button;
		private String activeStyle;
		private String inactiveStyle;
		private boolean selected = false;
		private String id;
		
		public TabButton(String text, String id) {
			this.id = id;
			button = new Button(text);
			activeStyle = "tab-button-active-no-right-border";
			inactiveStyle = "tab-button-inactive-no-right-border";
			button.setWidth("100%");
			button.setHeight(TAB_HEIGHT+"px");
			setSelected(false);
		}
		
		public String getId() {
			return id;
		}
		
		public void setHasRightBorder(boolean hasRightBorder) {
			activeStyle = hasRightBorder ? "tab-button-active" : "tab-button-active-no-right-border";
			inactiveStyle = hasRightBorder ? "tab-button-inactive" : "tab-button-inactive-no-right-border";
			setSelected(selected);
		}
		
		public void setSelected(boolean selected) {
			this.selected = selected;
			button.setStyleName(selected ? activeStyle : inactiveStyle);
		}
		
		@Override
		public Widget asWidget() {			
			return button;
		}

		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return button.addClickHandler(handler);
		}
		
	}}
