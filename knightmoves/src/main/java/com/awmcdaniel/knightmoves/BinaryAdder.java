package com.awmcdaniel.knightmoves;

public class BinaryAdder {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(" " + 1234 + " --> " + addOne(1234) );
		
		int num = 0;
		for (int i = 0; i < 256; i++){
			System.out.println(num);
			num = addOne(num);
		}
	}

	
	
	public static int addOne(int num){
		int result = num;
		int operator = 1;
		
		while (operator != 0){
			int remainder = result & operator;			
			result = result ^ operator;
			operator = remainder << 1;
		}
		
		return result;
	}
}
