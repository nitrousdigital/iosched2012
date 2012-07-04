package com.nitrous.iosched.client.data;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitrous.iosched.client.data.json.SessionData;
import com.nitrous.iosched.client.data.json.SessionDataWrapper;
import com.nitrous.iosched.client.data.json.SessionFeedQueryResult;
import com.nitrous.iosched.client.util.ConsoleLogger;

/**
 * The cache of most recently loaded session data
 * @author nick
 *
 */
public class SessionStore {
	private static SessionStore INSTANCE;

	private ConferenceSchedule conferenceSchedule;
	
	/** The set of callbacks waiting for the schedule to load */
	private HashSet<AsyncCallback<ConferenceSchedule>> requestors = new HashSet<AsyncCallback<ConferenceSchedule>>();
	private boolean isLoading = false;
	
	private SessionStore() {
	}
	
	public static SessionStore get() {
		if (INSTANCE == null) {
			INSTANCE = new SessionStore();
		}
		return INSTANCE;
	}
	
	/**
	 * Retrieve the conference schedule
	 * @param callback The callback
	 * @param reload True to force a reload from the server
	 */
	public void getSessions(final AsyncCallback<ConferenceSchedule> callback, boolean reload) {
		if (conferenceSchedule == null || reload) {
			requestors.add(callback);
			if (isLoading) {
				ConsoleLogger.debug("SessionStore::getSessions() - already loading. "+requestors.size()+" requestor(s) now waiting");
				return;
			} 
			isLoading = true;
			String url = Configuration.getSessionFeed();
			getSessions(url);
		} else {
			callback.onSuccess(conferenceSchedule);
		}		
	}
	
	private void notifyLoadSuccess() {
		isLoading = false;
		try {
			ConsoleLogger.debug("SessionStore::notifyLoadSuccess() - notifying "+requestors.size()+" requestors of schedule");
			for (AsyncCallback<ConferenceSchedule> callback : requestors) {
				callback.onSuccess(conferenceSchedule);
			}
		} finally {
			requestors.clear();
		}
	}
	private void notifyLoadFailure(Throwable cause) {
		isLoading = false;
		try {
			ConsoleLogger.debug("SessionStore::notifyLoadSuccess() - notifying "+requestors.size()+" requestors of schedule load failure");
			for (AsyncCallback<ConferenceSchedule> callback : requestors) {
				callback.onFailure(cause);
			}
		} finally {
			requestors.clear();
		}
	}
	
	private void getSessions(String url) {
		ConsoleLogger.debug("Loading session data from: "+url);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback(){
				public void onResponseReceived(Request request, Response response) {
					int code = response.getStatusCode();
					String body = response.getText();
					ConsoleLogger.debug("response code="+code);//+" body="+body);
					if (code != Response.SC_OK) {
						String status = null;
						try {
							status = response.getStatusText();
						} catch (Throwable t) {
							notifyLoadFailure(new Exception("Failed to load session data. Unexpected response from server."));
							return;
						}
						
						if (status != null && status.trim().length() > 0) {
							notifyLoadFailure(new Exception("Failed to load session data: "+status));
							return;
						} else {
							notifyLoadFailure(new Exception("Failed to load session data. Unexpected response from server."));
							return;
						}						
					} else {
						onParseSessionData(body);
					}
				}
				public void onError(Request request, Throwable exception) {
					ConsoleLogger.error("Failed to load session data", exception);
					String message = exception.getMessage();
					if (message != null && message.trim().length() > 0) {
						notifyLoadFailure(new Exception("Failed to load session data: "+message, exception));
					} else {
						notifyLoadFailure(new Exception("Failed to load session data", exception));
					}
				}
			});
		} catch (Exception ex) {
			ConsoleLogger.error("Failed to load session data", ex);
			String message = ex.getMessage();
			if (message != null && message.trim().length() > 0) {
				notifyLoadFailure(new Exception(message));
			} else {
				notifyLoadFailure(new Exception("Failed to load session data", ex));
			}
		}
	}
	
	private void onParseSessionData(String json) {
		ConsoleLogger.debug("Parsing session data: "+json);
		SessionFeedQueryResult result = SessionFeedQueryResult.eval(json);
		String err = result.getError();
		if (err != null && err.trim().length() > 0) {
			ConsoleLogger.debug("Result indicated error: "+err);
			//callback.onFailure(new Exception("Unexpected response from server"));
		}

		// wrap session data to expose time slot data 
		ArrayList<SessionDataWrapper> sessionData = new ArrayList<SessionDataWrapper>();
		JsArray<SessionData> arr = result.getResult();
		if (arr != null) {
			for (int i = 0, len = arr.length(); i < len; i++) {
				sessionData.add(new SessionDataWrapper(arr.get(i)));
			}
		}
		
		conferenceSchedule = new ConferenceSchedule(sessionData); 						
		notifyLoadSuccess();
	}
}
