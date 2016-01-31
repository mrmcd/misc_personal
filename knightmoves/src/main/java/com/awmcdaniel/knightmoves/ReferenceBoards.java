package com.awmcdaniel.knightmoves;

public interface ReferenceBoards {

	public static final char[][] KNIGHT_MOVES_BOARD = { { 'A' , 'B' , 'C' , 'D' , 'E' } , 
														{ 'F' , 'G' , 'H' , 'I' , 'J' } ,
														{ 'K' , 'L' , 'M' , 'N' , 'O' } , 
														{ '\0' , '1' , '2' , '3' , '\0'} };
	
	public static final char[] KNIGHT_MOVES_VOWELS = { 'A' , 'E' , 'I' , 'O' , 'U' }; //board doesn't have a U but include for completeness
	
	public static final int KNIGHT_MOVES_ROW = 4;
	public static final int KNIGHT_MOVES_COLUMN = 5;
	
}
