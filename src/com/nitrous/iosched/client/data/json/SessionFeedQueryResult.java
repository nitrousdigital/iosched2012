package com.nitrous.iosched.client.data.json;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * The session feed loaded from https://developers.google.com/events/io/conference/session
 * or https://developers.google.com/events/io/conference/codelab
 * 
 * @author nick
 *
 */
public final class SessionFeedQueryResult extends JavaScriptObject {
	protected SessionFeedQueryResult() {
	}

	public native JsArray<SessionData> getResult() /*-{
		return this.result;
	}-*/;
	
	public native String getError() /*-{
		return this.error;
	}-*/;
	
	public native String getEtag() /*-{
		return this.etag;
	}-*/;

	public static native SessionFeedQueryResult eval(String json) /*-{
		var ret = eval('(' + json + ')');
		return ret;
	}-*/;
}
