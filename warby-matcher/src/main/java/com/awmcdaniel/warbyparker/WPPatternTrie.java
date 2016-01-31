package com.awmcdaniel.warbyparker;

import static com.awmcdaniel.warbyparker.WPPattern.WILDCARD_TOKEN;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class WPPatternTrie {

	private static class WPTrieNode {
				
		private Map<String, WPTrieNode> childNodes;
		private WPTrieNode wildcardChild;
		
		private WPPattern pattern; // "...  You may assume every pattern is unique."
		
		private WPTrieNode(){
			childNodes = new HashMap<String, WPPatternTrie.WPTrieNode>();
			wildcardChild = null;
			pattern = null;
		}
		
		private WPTrieNode getOrAddChild(String childToken){
			if ( childNodes.containsKey(childToken) ){
				return childNodes.get(childToken);
			}else{
				WPTrieNode newChild = new WPTrieNode();
				childNodes.put(childToken, newChild);
				return newChild;
			}
		}
		
		private WPTrieNode getChildIfPresent(String childToken){
			return childNodes.get(childToken);
		}
		
		private WPTrieNode getOrAddWildChild(){
			if (wildcardChild == null){
				wildcardChild = new WPTrieNode();
			}
			return wildcardChild;
		}
		
		private boolean hasWildChild(){
			return wildcardChild != null;
		}
	}
	
	
	public static final String QUERY_STRING_DELIMITER = "/";
	
	private WPTrieNode rootNode;
	
	/**
	 * Initializes an empty {@link WPPatternTrie}. Add patterns to it with {@link #insertPattern(WPPattern)} and search for queriest with {@link #search(String)} and {@link #searchForBest(String)}
	 */
	public WPPatternTrie(){
		rootNode = new WPTrieNode();
	}
	
	/**
	 * Inserts a {@link WPPattern} object into the search trie. 
	 * @param pattern The pattern to insert
	 */
	public void insertPattern(WPPattern pattern){
		String[] tokens = pattern.getTokens();
		doInsertPattern(rootNode, tokens, 0, pattern);
	}
	
	/**
	 * Search for a query string and return all the {@link WPPattern} objects that match the query
	 * @param queryString string to search for
	 * @return A list of all matching patterns
	 */
	public List<WPPattern> search(String queryString){
		return doPatternSearchAll(rootNode, tokenizeQueryString(queryString), 0);
	}
	
	/**
	 * Search for a query string and return ONLY the best {@link WPPattern} object that matches the query.
	 * @param queryString string to search for
	 * @return the best (least) of all matching patterns
	 */
	public WPPattern searchForBest(String queryString){
		return doPatternSearchBest(rootNode, tokenizeQueryString(queryString), 0);
	}
	
	private void doInsertPattern(WPTrieNode node, String[] tokens, int tokenIndex, WPPattern pattern){
		if (tokenIndex == tokens.length){
			//at the end of the token string, put the pattern here, return
			node.pattern = pattern;
			return;
		}
		
		//else, find the correct child and recurse
		String targetToken = tokens[tokenIndex];
		
		WPTrieNode nextNode = null;
		if (WILDCARD_TOKEN.equals(targetToken)){
			nextNode = node.getOrAddWildChild();
		}else{
			nextNode = node.getOrAddChild(targetToken);
		}
		
		doInsertPattern(nextNode,tokens,tokenIndex+1,pattern);
    }
	
	private List<WPPattern> doPatternSearchAll(WPTrieNode node, String[] tokens, int tokenIndex){
		//strictly speaking, this method isn't required, since the specs said only return the BEST pattern, not all of them,
		//but since I wrote it first, hey, this thing now has extra features where you could search for all matches not just the best
		
		if (tokenIndex == tokens.length){
			//at the end of the search string, if there is a pattern return it up
			if (node.pattern != null){
				LinkedList<WPPattern> result = new LinkedList<WPPattern>();
				result.add(node.pattern);
				return result;
			}else{
				//there can be empty nodes that match a pattern, but were empty because they are subpatterns of one or more actual patterns
				return Collections.emptyList();
			}
		}
		
		String token = tokens[tokenIndex];
		
		//otherwise, we want to transverse the branch matching this token, plus the wildcard branch, if either exists.
		LinkedList<WPPattern> results = new LinkedList<WPPattern>();
		
		WPTrieNode childNode = node.getChildIfPresent(token);
		if (childNode != null){
			results.addAll( doPatternSearchAll(childNode, tokens, tokenIndex+1) );
		}
		
		//now check wildcard branch
		if (node.hasWildChild()){
			WPTrieNode wildChild = node.getOrAddWildChild();
			results.addAll(  doPatternSearchAll(wildChild, tokens, tokenIndex+1) );
		}
		
		return results;
	}
	
	/**
	 * Same as {@link WPPatternTrie#doPatternSearchAll(WPTrieNode, String[], int)} but instead of returning a list, return only the "smallest" (best) pattern as you recurse up
	 * @param node
	 * @param tokens
	 * @param tokenIndex
	 * @return
	 */
	private WPPattern doPatternSearchBest(WPTrieNode node, String[] tokens, int tokenIndex){
		if (tokenIndex == tokens.length){
			//return pattern on this node or null
			return node.pattern;
		}
		
		String token = tokens[tokenIndex];
		WPPattern childPattern = null, wildPattern = null;
		
		//get best child pattern
		WPTrieNode childNode = node.getChildIfPresent(token);
		if (childNode != null){
			childPattern = doPatternSearchBest(childNode, tokens, tokenIndex+1);
		}
		
		//get best wild pattern
		if (node.hasWildChild()){
			WPTrieNode wildChild = node.getOrAddWildChild();
			wildPattern = doPatternSearchBest(wildChild, tokens, tokenIndex+1);
		}
		
		//return whichever is less
		if (childPattern == null){
			return wildPattern; //no child result, so return wild or null (dead end)
		}
		
		if (childPattern.compareTo(wildPattern) < 0){
			return childPattern; //child pattern is better
		}else{
			return wildPattern;
		}
	}
	
	private static String[] tokenizeQueryString(String queryString){
		//first, trim any whitespace
		queryString.trim();
		if (queryString.startsWith(QUERY_STRING_DELIMITER)){
			queryString = queryString.substring(1); //drop the leading delimiter			
		}
		
		if (queryString.endsWith(QUERY_STRING_DELIMITER)){
			queryString = queryString.substring(0, queryString.length()-1); //drop the trailing delimiter
		}
		
		return queryString.split(Pattern.quote(QUERY_STRING_DELIMITER));
	}
}
