package com.awmcdaniel.warbyparker;

import static junit.framework.Assert.*;

import org.junit.Test;

public class WPPatternTest {
	
	@Test
	public void testPatternTokenization(){
	
		WPPattern pattern = new WPPattern("a,b,c,d,*");
		String[] tokens = pattern.getTokens();
		assertEquals("a", tokens[0]);
		assertEquals("b", tokens[1]);
		assertEquals("c", tokens[2]);
		assertEquals("d", tokens[3]);
		assertEquals("*", tokens[4]);
		
	}
	
	@Test
	public void testPatternToString(){
		String patternString = "a,b,c,d,*";
		WPPattern pattern = new WPPattern(patternString);	
		assertEquals(patternString, pattern.toString());
	}
	
	@Test
	public void testPatternComparable(){
		WPPattern p1 = new WPPattern("a,b,c");
		WPPattern p2 = new WPPattern("*,b,*");
		WPPattern p3 = new WPPattern("*,*,c");
		
		assertTrue( (p1.compareTo(p2)) < 0 );  // p1 < p2 (less is 'better')
		assertTrue( (p1.compareTo(p3)) < 0 ); // p1 < p3 
		
		assertTrue( (p2.compareTo(p1)) > 0 ); //p2 > p1
		assertTrue( (p3.compareTo(p1)) > 0 ); //p3 > p1
		
		assertTrue( (p2.compareTo(p3)) < 0 ); //p2 < p3
		assertTrue( (p3.compareTo(p2)) > 0 ); //p3 > p2
		
		//test null condition
		assertTrue( (p1.compareTo(null)) < 0 );
		
		//test identity condition
		assertTrue( (p1.compareTo(p1)) == 0);
	}
}
