package com.lateensoft.pathfinder.toolkit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class EntryUtils {

	public static <K, V> List<V> valueList(List<Entry<K, V>> entryList) {
		List<V> values = new ArrayList<V>(entryList.size());
		for (Entry<K, V> entry : entryList) {
			values.add(entry.getValue());
		}
		return values;
	}
	
	public static <K> String[] valueArray(List<Entry<K, String>> entryList) {
		String[] values = new String[entryList.size()];
		for (int i = 0; i < entryList.size(); i++) {
			values[i] = entryList.get(i).getValue();
		}
		return values;
	}
}
