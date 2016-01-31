package com.awmcdaniel.scratch_pad;

import java.util.Arrays;

/**
 * 
 * Array-only implementation of a very simple hashmap datastructure. 
 * 
 * Very inefficient for non-sparse maps, but with the advantage of the internal data structure is a single array and has a very simple implementation
 * 
 *
 */
public class StringArrayMap {

	public static final int DEFAULT_SIZE = 1000;
	
	
	private static class Entry {
		private int key;
		private String value;
		
		public String toString(){
			return Integer.toString(key) + "->" + value;
		}
	}
	
	private Entry[] mapArray;
	
	public StringArrayMap(){
		this(DEFAULT_SIZE);
	}
	
	public StringArrayMap(int size){
		mapArray = new Entry[size];
	}
	
	private int putInternal(Entry e){
		int offset = (e.key % mapArray.length);
		for (int i = 0; i < mapArray.length; i++){
			int index = (offset+i) % mapArray.length;
			if (mapArray[index] == null){
				//found a spot
				mapArray[index] = e;
				return index;
			}else if (mapArray[index].key == e.key){
				//overwrite existing value
				mapArray[index] = e;
				return index;
			}
		}
		//went through whole array and it's full. Return -1 as error to signal it needs to be resized
		return -1;
	}
	
	private Entry getInternal(int key){
		int offset = (key % mapArray.length);
		for (int i = 0; i < mapArray.length; i++){
			int index = (offset+i) % mapArray.length;
			if ( (mapArray[index] != null) && (mapArray[index].key == key) ){
				//found the record
				return mapArray[index];
			}
		}
		return null; //no record found
	}
	
	private Entry deleteInternal(int key){
		int offset = (key % mapArray.length);
		for (int i = 0; i < mapArray.length; i++){
			int index = (offset+i) % mapArray.length;
			if ( (mapArray[index] != null) && (mapArray[index].key == key) ){
				//found the record
				Entry e = mapArray[index];
				mapArray[index] = null;
				return e;
			}
		}
		return null; //no record found
	}
	
	
	private void resizeStorage(){
		Entry[] oldStorage = mapArray;
		mapArray = new Entry[mapArray.length * 2];
		for (Entry e : oldStorage){
			int r = putInternal(e);
			if (r < 0){
				throw new IllegalStateException("Shouldn't fail puts on a resize operation!");
			}
		}
	}
	
	public void put(int key, String value){
		Entry e = new Entry();
		e.key = key;
		e.value = value;
		while (putInternal(e) < 0){
			resizeStorage();
		}
	}
	
	public String get(int key){
		Entry e = getInternal(key);
		return e != null ?  e.value : null;
	}
	
	public String delete(int key){
		Entry e = deleteInternal(key);
		return e != null ?  e.value : null;
	}
	
	public String toString(){
		return Arrays.toString(mapArray);
	}
}
