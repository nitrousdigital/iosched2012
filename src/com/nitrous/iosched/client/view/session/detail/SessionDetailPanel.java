package com.nitrous.iosched.client.view.session.detail;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.data.Configuration;
import com.nitrous.iosched.client.data.EventDataWrapper;
import com.nitrous.iosched.client.data.json.EventData;

public class SessionDetailPanel implements IsWidget {
	private VerticalPanel layout;
	private HTML description;
//	private HTML speakers;
	private VerticalPanel requirements;
	private HTML links;
	
	public SessionDetailPanel() {
		description = new HTML();
		description.setWidth("100%");
		description.setStyleName("session-detail-first-section-text");
//		speakers = new HTML();
//		speakers.setWidth("100%");
//		speakers.setStyleName("session-detail-section-text");
		
		requirements = new VerticalPanel();
		requirements.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		requirements.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		requirements.setWidth("100%");
		requirements.setStyleName("session-detail-section-text");
		
		links = new HTML();
		links.setWidth("100%");
		
		layout = new VerticalPanel();
		layout.setStyleName("session-detail");
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		layout.setWidth("100%");
		layout.add(description);
//		layout.add(new SectionHeader("Speakers"));
//		layout.add(speakers);
		layout.add(new SectionHeader("Requirements"));
		layout.add(requirements);
		layout.add(new SectionHeader("Links"));
		links.setStyleName("session-detail-section-text");
		layout.add(links);
	}
	
	private void setDescription(String description) {
		this.description.setHTML(description);
	}
	
	private void setRequirements(JsArrayString req) {
		this.requirements.clear();
		if (req == null || req.length() == 0) {
			this.requirements.add(new HTML("none"));
		} else {
			for (int i = 0, len = req.length(); i < len; i++) {
				String requirement = req.get(i);
				this.requirements.add(new HTML(requirement));
			}
		}
	}
	
	private void setLink(String sessionId) {
		String url = Configuration.getSessionWebLink(sessionId);
		this.links.setHTML("<a href='"+url+"' target='_new'>Web Link</a>");		
	}
	
	public void showEvent(EventDataWrapper event) {
		EventData data = event.getData();
		setDescription(data.getAbstract());
	    setRequirements(data.getPrerequisites());	
	    setLink(data.getId());
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
