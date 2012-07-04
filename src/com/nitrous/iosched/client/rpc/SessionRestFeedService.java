package com.nitrous.iosched.client.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.nitrous.iosched.client.data.Configuration;
import com.nitrous.iosched.client.util.ConsoleLogger;

public class SessionRestFeedService {

	public static void loadSessions() {
		ConsoleLogger.debug("Fetching sessions from: "+Configuration.getSessionFeed());
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Configuration.getSessionFeed());
		try {
			builder.sendRequest(null,  new RequestCallback(){
				@Override
				public void onResponseReceived(Request request, Response response) {
					int code = response.getStatusCode();
					String text = response.getText();
					ConsoleLogger.debug("Code="+code+" Response: "+text);
				}

				@Override
				public void onError(Request request, Throwable exception) {
					ConsoleLogger.error("Failed to fetch sessions", exception);
				}
			});
		} catch (RequestException e) {
			ConsoleLogger.error("Failed to fetch sessions", e);
		}
	}
}
