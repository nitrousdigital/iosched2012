package com.nitrous.iosched.client.data.json;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public final class EventData extends JavaScriptObject {
	protected EventData() {
	}
	
	/**
	 * 
	 * @return Number or empty string
	 */
	public native String getRoom() /*-{
		return this.room;
	}-*/;
	
	/**
	 * 
	 * @return Beginner, Intro, Intermediate, Advanced, Tech Talk, Fireside Chat
	 */
	public native String getLevel() /*-{
		return this.level;
	}-*/;
	
	/**
	 * 
	 * @return Android, Chrome, YouTube, Commerce, Cloud Platform, Google+,
	 *         Google Drive, Google Maps, Tech Talk, Google TV, Google APIs,
	 *         Entrepreneurship, Code Labs 1, Code Labs 2, Keynote
	 */
	public native JsArrayString getTracks() /*-{
		return this.track;
	}-*/;
	
	/**
	 * @return yyyy-MM-dd
	 */
	public native String getStartDate() /*-{
		return this.start_date;
	}-*/;
	
	/**
	 * @return HH:mm
	 */
	public native String getStartTime() /*-{
		return this.start_time;
	}-*/;
	
	/**
	 * @return yyyy-MM-dd
	 */
	public native String getEndDate() /*-{
		return this.end_date;
	}-*/;
	
	/**
	 * @return HH:mm
	 */
	public native String getEndTime() /*-{
		return this.end_time;
	}-*/;
	
	public native String getTitle() /*-{
		return this.title;
	}-*/;
	
	/**
	 * 
	 * @return Y or N
	 */
	public native String getAttending() /*-{
		return this.attending;
	}-*/;
	
	/**
	 * 
	 * @return Y or N
	 */
	public native String getHasStreaming() /*-{
		return this.has_streaming;
	}-*/;
	
	/**
	 * 
	 * @return TBD or URL e.g. "_PmU9mpdnqM"
	 */
	public native String getLiveStreamUrl() /*-{
		return this.livestream_url;
	}-*/;
	
	/**
	 * Append this ID to https://developers.google.com/events/io/session-details/ to view the session details
	 * 
	 * @return id e.g. gooio2012/114/
	 */
	public native String getId() /*-{
		return this.id;
	}-*/;
	
	/**
	 * 
	 * @return The HTML description of this event. May contain escaped unicode characters e.g. \u2019
	 */
	public native String getAbstract() /*-{
		return $wnd.getDescription(this);
	}-*/;
	
	/**
	 * @return The HTML describing the prerequisites for attending this event
	 */
	public native JsArrayString getPrerequisites() /*-{
		return this.prereq;
	}-*/;
	
	/**
	 * 
	 * @return the IDs of the speakers e.g: "gooio2012/104//2178", "gooio2012/104//2186"
	 */
	public native JsArrayString getSpeakerIds() /*-{
		return this.speaker_id;
	}-*/;
	
	/**
	 * 
	 * @return a comma separated list of tags or an empty string, e.g:  HTML5,performance,web apps,websockets,file,multimedia,graph,Android
	 */
	public native String getTags() /*-{
		return this.tags;
	}-*/;
	
	/**
	 * 
	 * @return the event type. e.g. "session" or "codelab"
	 */
	public native String getType() /*-{
		return this.type;
	}-*/;
	
    public static native EventData eval(String json) /*-{
		var ret = eval('(' + json + ')');
		return ret;
	}-*/;
	
}
