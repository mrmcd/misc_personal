package com.awmcdaniel.warbyparker;

import java.util.Arrays;
import java.util.regex.Pattern;

public class WPPattern implements Comparable<WPPattern>{
	
	
	public static final String WILDCARD_TOKEN = "*";
	public static final String PATTERN_DELIMITER = ",";
	
	private String[] matchTokens;
	private int wildcardCount = 0;
	
	
	
	
	/**
	 * Initializes a WPPattern line object from a source input string.
	 * @param inputPatternLine - Input string of the form a,b,c,*,d,this,that   (etc... see specifications)
	 */
	public WPPattern(String inputPatternLine){
		//split the string on "," delimiter into tokens		
		//note accoring to spec, pattens will not have empty fields, so we don't need to error check that for the moment. 
		matchTokens = inputPatternLine.split(Pattern.quote(PATTERN_DELIMITER));
		
		//scan each token to match the wildcard and then increment the count 
		for (String token : matchTokens){
			if (WILDCARD_TOKEN.equals(token)){
				wildcardCount++;
			}
		}
	}
	
	/**
	 * Returns how many wildcard tokens are in this pattern string. Used for ranking matches
	 * @return wildcard count. 
	 */
	public int getWildcardCount(){
		return wildcardCount;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < matchTokens.length; i++){
			sb.append(matchTokens[i]);
			if (i != (matchTokens.length - 1) ){
				sb.append(PATTERN_DELIMITER);
			}
		}
		return sb.toString();
	}
	
	public String[] getTokens(){
		return Arrays.copyOf(matchTokens, matchTokens.length); //copy token array. a tad slower but safer to so no one clobbers our internal data. 
	}

	public int compareTo(WPPattern other) {
		/**
		 * The best-matching pattern is the one which matches the path
			using the fewest wildcards.

			If there is a tie (that is, if two or more patterns with the same number
			of wildcards match a path), prefer the pattern whose leftmost wildcard
			appears in a field further to the right. If multiple patterns' leftmost
			wildcards appear in the same field position, apply this rule recursively
			to the remainder of the pattern.
			
			For example: given the patterns `*,*,c` and `*,b,*`, and the path
			`/a/b/c/`, the best-matching pattern would be `*,b,*`.
		 */
		 
		if (other == null){
			//assume null objects have infinite "size"
			return -1;
		}
		
		//technically we wouldn't be comparing patterns of different lengths, since they won't both match a single string. 
		//However, for the sake of safety and completeness, let's rank shorter patterns as less
		if ( this.getTokens().length != other.getTokens().length ){
			return this.getTokens().length - other.getTokens().length;
		}

		if ( other.getWildcardCount() != this.getWildcardCount() ){
			//rank based on wildcard count ...
			return this.getWildcardCount() - other.wildcardCount; 
		}
		
		//same number of wild cards, so scan through each token group until you find the earliest non-wildcard
		String[] thisTokens = this.getTokens();
		String[] otherToken = other.getTokens();
		
		for (int i = 0; i < thisTokens.length; i++){
			if ( thisTokens[i].equals(otherToken[i]) ){
				//both have wildcard, or none do, keep going
				continue;
			}
			
			if (! WILDCARD_TOKEN.equals(thisTokens[i])) {
				//this has first non-wildcard, return less than
				return -1;				
			}
			
			if (! WILDCARD_TOKEN.equals(otherToken[i])) {
				//other has first non-wildcard, return greater than
				return 1;
			}
		}
		
		
		//otherwise they're equal... which shouldn't happen because spec said they'd all be unique.. *shrug*
		return 0;
	}
	
}
