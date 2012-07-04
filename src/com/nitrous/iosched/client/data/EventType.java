package com.nitrous.iosched.client.data;

public enum EventType {
	KEYNOTE("keynote"),
	SESSION("session"),
	CODELAB("codelab");
	private String id;
	private EventType(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public static EventType parse(String eventType) {
		if (eventType == null) {
			return null;
		}
		
		String et = eventType.trim();
		if (et.length() == 0) {
			return null;
		}
		
		for (EventType type : values()) {
			if (type.id.equalsIgnoreCase(et)) {
				return type;
			}
		}
		return null;
	}
}
