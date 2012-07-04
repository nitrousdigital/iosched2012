package com.nitrous.iosched.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public final class ConsoleLogger {
	private static Boolean debugEnabled = null;
	
	private ConsoleLogger() {
	}
	
	public static boolean isDebugEnabled() {
		if (debugEnabled == null) {
			debugEnabled = "1".equals(Window.Location.getParameter("debug"));
		}
		return debugEnabled;
	}
	
	public static final void debug(String message) {
		GWT.log(message);
		if (isDebugEnabled()) {
			debugNative(message);
		}
	}
	
	public static final void error(String message) {
		GWT.log(message);
		errorNative(message);
	}
	
	public static final void error(String message, Throwable cause) {
		StringBuffer buf = new StringBuffer();
		if (message != null) {
			buf.append(message);
		}
		if (cause != null && cause.getMessage() != null) {
			buf.append(" - Caused by: "+cause.getMessage());
		}
		
		if (cause != null) {
			GWT.log(message, cause);
		} else {
			GWT.log(message);
		}
		errorNative(buf.toString());
	}
	
	public static final void debug(String message, Throwable cause) {
		StringBuffer buf = new StringBuffer();
		if (message != null) {
			buf.append(message);
		}
		if (cause != null && cause.getMessage() != null) {
			buf.append(" - Caused by: "+cause.getMessage());
		}
		
		if (cause != null) {
			GWT.log(message, cause);
		} else {
			GWT.log(message);
		}
		debugNative(buf.toString());
	}
	
	private static final native void debugNative(String message) /*-{
		if ($wnd.console && $wnd.console.debug) {
			$wnd.console.debug(message);
		}
	}-*/;
	private static final native void errorNative(String message) /*-{
		if ($wnd.console && $wnd.console.error) {
			$wnd.console.error(message);
		}
	}-*/;
}
