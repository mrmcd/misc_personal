package com.awmcdaniel.scratch_pad;

public class SAMRunTest {

	public static void main(String[] args) {
		testSimple();
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
}
