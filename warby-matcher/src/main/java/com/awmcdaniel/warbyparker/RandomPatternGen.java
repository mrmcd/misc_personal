package com.awmcdaniel.warbyparker;

import java.util.Random;

public class RandomPatternGen {

	/**
	 * hint:
	 * 
	 * java -cp target/warby-matcher-1.0.0-SNAPSHOT.jar com.awmcdaniel.warbyparker.RandomPatternGen | java -cp target/warby-matcher-1.0.0-SNAPSHOT.jar com.awmcdaniel.warbyparker.WPMatcher | less
	 */
	
	private static String[] source = { "a","b","c","d","e","f","g","h","i","j","k","l","m","o","p","q","r","s","t","u","v","w","x","y","z" };
	private static Random r = new Random();
	
	public static void main(String args[]){
		
		
		
		//generate 500 random patterns 5 units long, with a 5% chance of a wildcard instead
		System.out.println("500");
		for (int i = 0; i < 500; i++){
			StringBuilder sb = new StringBuilder();
			sb.append(getRandomLetter(true)).append(",")
			  .append(getRandomLetter(true)).append(",")
			  .append(getRandomLetter(true)).append(",")
			  .append(getRandomLetter(true)).append(",")
			  .append(getRandomLetter(true));
			System.out.println(sb.toString());
		}
		
		//now generate 500 random paths 5 untis long, and see how many match
		System.out.println("500");
		for (int i = 0; i < 500; i++){
			StringBuilder sb = new StringBuilder();
			sb.append(getRandomLetter(false)).append("/")
			  .append(getRandomLetter(false)).append("/")
			  .append(getRandomLetter(false)).append("/")
			  .append(getRandomLetter(false)).append("/")
			  .append(getRandomLetter(false));
			System.out.println(sb.toString());
		}
	}
	
	private static String getRandomLetter(boolean wildcard){
		if (wildcard){
			if (r.nextInt(100) < 5){
				return "*";
			}
		}
		return source[r.nextInt(source.length)];
	}
}
