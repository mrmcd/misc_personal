package com.awmcdaniel.scratch_pad;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class SAMRunTest {

	public static void main(String[] args) {
		testSimple();
		stressTestVerify();
	}

	
	
	public static void testSimple(){
		StringArrayMap map = new StringArrayMap(25);
		
		map.put(10, "hello world");
		map.put(26, "lols");
		map.put(992, "need_coffee");
		map.put(982891, "we-see-you");
		
		System.out.println(map.toString());
		
		System.out.println("get 10: " + map.get(10));
		System.out.println("get 26: " + map.get(26));
		System.out.println("get 992: " + map.get(992));
		System.out.println("get 982891: " + map.get(982891));
		
		map.delete(982891);
		
		System.out.println("get 982891: " + map.get(982891));
		
		System.out.println(map.toString());
	}
	
	public static void stressTestVerify(){
		StringArrayMap testMap =  new StringArrayMap();
		Map<Integer, String> referenceMap = new HashMap<Integer, String>(1000);
		
		List<Integer> deleteList = new LinkedList<Integer>();  
		
		System.out.println("ST Loading...");
		//load with 2500 random records
		for (int i = 0; i < 2500; i++){
			int key = random.nextInt(1000000);
			String value = generateRandomString(random.nextInt(10)+5);
			testMap.put(key, value);
			referenceMap.put(key, value);
			if (i % 500 == 0){
				//every 500th record will be purged after insert
				deleteList.add(key);
			}
		}
		
		System.out.println("ST Delete...");
		//purge delete list
		for (Integer i : deleteList){
			testMap.delete(i);
			referenceMap.remove(i);
		}
		
		System.out.println("ST Verify...");
		//go through each entry in reference map and verify it's the same in test map
		for (Entry<Integer, String> e : referenceMap.entrySet()){
			String testVal = testMap.get(e.getKey());
			if (!e.getValue().equals(testVal)){
				System.out.println("MISMATCH! : " + e.getValue()+ " vs. " + testVal + " for key = " + e.getKey() );
			}
		}
		
		System.out.println("Outputting map internals:");
		System.out.println(testMap.toString());
	}
	
	private static final Random random = new Random();
	
	private static String[] source = { "a","b","c","d","e","f","g","h","i","j","k","l","m","o","p","q","r","s","t","u","v","w","x","y","z" };
	public static String generateRandomString(int length){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++){
			sb.append(source[random.nextInt(source.length)]);
		}
		return sb.toString();
	}
}
