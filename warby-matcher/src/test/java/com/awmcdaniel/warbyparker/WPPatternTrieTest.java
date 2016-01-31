package com.awmcdaniel.warbyparker;

import java.util.List;

import org.junit.Test;

import static junit.framework.Assert.*;

public class WPPatternTrieTest {

	
	private static WPPatternTrie generateSimpleTrie(){
		WPPattern p1 = new WPPattern("a,b,c");
		WPPattern p2 = new WPPattern("a,*,*");
		WPPattern p3 = new WPPattern("a,*,c");
		WPPattern p4 = new WPPattern("z,y,x");
		WPPattern p5 = new WPPattern("z,y,*");
		
		WPPatternTrie trie = new WPPatternTrie();
		trie.insertPattern(p1);
		trie.insertPattern(p2);
		trie.insertPattern(p3);
		trie.insertPattern(p4);
		trie.insertPattern(p5);
		
		return trie;
	}
	
	
	@Test
	public void testBasicPatternTrieSearch(){
		WPPatternTrie trie = generateSimpleTrie();
		
		List<WPPattern> result = trie.search("/a/b/c/");
		assertEquals(3, result.size());
		
		result = trie.search("a/x/d/");
		assertEquals(1, result.size());
		
		result = trie.search("lol/nope/not/here/");
		assertEquals(0, result.size());
		
		result = trie.search("/z/y/x");
		assertEquals(2, result.size());
		
		result = trie.search("/a/b/");
		assertEquals(0, result.size());
	}
	
	@Test
	public void testBasicPatternSearchBest(){
		WPPatternTrie trie = generateSimpleTrie();
		WPPattern result;
		
		result = trie.searchForBest("/a/b/c");
		assertEquals("a,b,c",result.toString());
		
		result = trie.searchForBest("/a/x/d");
		assertEquals("a,*,*",result.toString());
		
		result = trie.searchForBest("/lol/nope/not/here");
		assertNull(result);
		
		result = trie.searchForBest("z/y/x");
		assertEquals("z,y,x",result.toString());
		
		result = trie.searchForBest("/a/b");
		assertNull(null);
	}
	
	private static WPPatternTrie generateSpecTrie(){
		WPPatternTrie trie = new WPPatternTrie();
		
		trie.insertPattern(new WPPattern("*,b,*"));
		trie.insertPattern(new WPPattern("a,*,*"));
		trie.insertPattern(new WPPattern("*,*,c"));
		trie.insertPattern(new WPPattern("foo,bar,baz"));
		trie.insertPattern(new WPPattern("w,x,*,*"));
		trie.insertPattern(new WPPattern("*,x,y,z"));
		
		return trie;
	}
	
	@Test
	public void testSpecPatternSearch(){
		/*
		 * Example Input
			-------------
			
			    6
			    *,b,*
			    a,*,*
			    *,*,c
			    foo,bar,baz
			    w,x,*,*
			    *,x,y,z
			    5
			    /w/x/y/z/
			    a/b/c
			    foo/
			    foo/bar/
			    foo/bar/baz/
			
			Example Output
			--------------
			
			    *,x,y,z
			    a,*,*
			    NO MATCH
			    NO MATCH
			    foo,bar,baz
		 */
		
		WPPatternTrie trie = generateSpecTrie();
		
		assertEquals("*,x,y,z" , trie.searchForBest("/w/x/y/z").toString() );
		assertEquals("a,*,*" , trie.searchForBest("a/b/c").toString() );
		assertNull( trie.searchForBest("foo/") );
		assertNull( trie.searchForBest("foo/bar") );
		assertEquals("foo,bar,baz" , trie.searchForBest("foo/bar/baz/").toString() );
	}
}
