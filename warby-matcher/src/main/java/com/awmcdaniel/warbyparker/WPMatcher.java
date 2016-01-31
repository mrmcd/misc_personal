package com.awmcdaniel.warbyparker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class WPMatcher {

	public static void main(String[] args){
		try{
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			List<WPPattern> patterns = loadPatterns(input);
			WPPatternTrie trie = new WPPatternTrie();
			for (WPPattern pattern : patterns){
				trie.insertPattern(pattern);
			}
			
			List<String> queries = loadQueries(input);
			for (String query : queries){
				WPPattern best = trie.searchForBest(query);
				if ( best == null ){
					System.out.print("NO MATCH\n");
				}else{
					System.out.print(best.toString()+"\n");
				}
			}
			
		}catch (Exception e){
			System.err.println("Exception: " + e.getMessage());
		}
	}
	
	
	private static List<WPPattern> loadPatterns(BufferedReader input) throws IOException{
		//first line is how many pattern lines to expect
		String lineStr = input.readLine();
		int lineCount = Integer.parseInt(lineStr);
		List<WPPattern> patterns = new LinkedList<WPPattern>();
		for (int i = 0; i < lineCount; i++){
			String patternString = input.readLine();
			patterns.add(new WPPattern(patternString));
		}
		
		return patterns;
	}
	
	private static List<String> loadQueries(BufferedReader input) throws IOException{
		//first line is how many pattern lines to expect
		String lineStr = input.readLine();
		int lineCount = Integer.parseInt(lineStr);
		List<String> queries = new LinkedList<String>();
		for (int i = 0; i < lineCount; i++){
			queries.add(input.readLine());
		}
		return queries;
	}
}
