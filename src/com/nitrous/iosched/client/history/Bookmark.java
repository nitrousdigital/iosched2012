package com.nitrous.iosched.client.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Bookmark {
	private BookmarkCategory category;
	private ArrayList<String> stateTokens;
	public Bookmark(BookmarkCategory category) {
		this(category, null);
	}
	public Bookmark(BookmarkCategory category, ArrayList<String> stateTokens) {
		this.stateTokens = stateTokens;
		this.category = category;
	}
	
	public BookmarkCategory getCategory() {
		return category;
	}
	public void setCategory(BookmarkCategory category) {
		this.category = category;
	}
	public void clearStateTokens() {
		if (stateTokens != null) {
			stateTokens.clear();
		}
	}
	public boolean hasState() {
		return stateTokens != null && stateTokens.size() > 0;
	}
	public ArrayList<String> getState() {
		return stateTokens;
	}
	public void setStateToken(String token) {
		if (stateTokens != null) {
			stateTokens.clear();
		} else {
			stateTokens = new ArrayList<String>();
		}
		stateTokens.add(token);
	}
	public void addStateToken(String token) {
		if (stateTokens == null) {
			stateTokens = new ArrayList<String>();
		}
		stateTokens.add(token);
	}
	public void addStateToken(String key, String value) {
		addStateToken(toKeyValuePair(key, value));
	}
	public void addStateTokens(Map<String, String> tokens) {
		if (tokens == null) {
			return;
		}
		for (Map.Entry<String, String> token : tokens.entrySet()) {
			String key = token.getKey();
			String value = token.getValue();
			addStateToken(key, value);
		}
	}
	
	private static String toKeyValuePair(String key, String value) {
		StringBuffer token = new StringBuffer();
		token.append(key);
		token.append('=');
		token.append(value);
		return token.toString();
	}
	
	/**
	 * Attempts to parse the specified token into a key value pair
	 * @param token The token to parse
	 * @return An array with [0]=key [1]=value or null if unable to separate a pair
	 */
	private static String[] getKeyValuePair(String token) {
		int idx = -1;
		if (token != null 
				&& token.length() > 2 
				&& ((idx = token.indexOf('=')) != -1)
				&& idx < token.length() - 1) {
			String key = token.substring(0, idx);
			String value = token.substring(idx + 1);
			return new String[]{key, value};
		}
		return null;
	}	
	
	/**
	 * @return Returns the state tokens as a map or an empty map
	 */
	public HashMap<String, String> getStateMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		if (hasState()) {
			for (String token : stateTokens) {
				String[] pair = getKeyValuePair(token);
				if (pair != null) {
					map.put(pair[0], pair[1]);
				} else {
					map.put(token, null);
				}
			}
		}
		return map;
	}

	/**
	 * Returns the value associated with the specified key in the state tokens
	 * @param key The key
	 * @return The value associated with the specified key or null
	 */
	public String getStateValue(String key) {
		return getStateMap().get(key);
	}
	
	/**
	 * @Return The URL encoded bookmark
	 */
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(category.toString());
		if (stateTokens != null && stateTokens.size() > 0) {
			for (String s : stateTokens) {
				buf.append(";");
				buf.append(s);
			}
		}
		return buf.toString();
	}
	
	public static Bookmark parse(String token) {
		if (token != null) {
			token = token.trim();
			if (token.length() > 0) {
				String[] parts = token.split(";");
				if (parts.length > 0) {
					String categoryStr = parts[0];
					BookmarkCategory cat = null;
					for (BookmarkCategory b : BookmarkCategory.values()) {
						if (b.toString().equalsIgnoreCase(categoryStr)) {
							cat = b;
							break;
						}
					}
					if (cat != null) {
						ArrayList<String> state = new ArrayList<String>();
						if (parts.length > 1) {
							for (int i = 1; i < parts.length; i++) {
								state.add(parts[i]);
							}
						}
						return new Bookmark(cat, state);
					}
				}
			}			
		}
		return null;
	}
}
