package com.awmcdaniel.knightmoves;

import java.util.Map;
import java.util.Map.Entry;

public class KnightMoves {

	public static void main(String[] args){
		KnightMovesBoard board = new KnightMovesBoard(ReferenceBoards.KNIGHT_MOVES_BOARD, ReferenceBoards.KNIGHT_MOVES_VOWELS, 
				  									  ReferenceBoards.KNIGHT_MOVES_ROW, ReferenceBoards.KNIGHT_MOVES_COLUMN);
		
			
		/**
		 * 
		 * Your program will be run with a single integer n (between 1 and 32, inclusive) as a command-line parameter. Given 
		 * this value for n, your program should print out the total count of valid n-key sequences on a single line to standard 
		 * out.
		 * 
		 */
		if (args.length < 1){
			System.out.println("Usage: KnightMoves <sequence_length>");
			System.exit(1);
		}
		
		int length = 0; 
		try{
			length = Integer.parseInt(args[0]);
		}catch(NumberFormatException e){
			System.out.println("Error: " + args[0] + " is invalid input.");
			System.out.println("Usage: KnightMoves <sequence_length>");
			System.exit(1);
		}
		
		if (length < 1){
			System.out.println("Error: Please enter an integer greater than 0. Entered: " + length);
			System.out.println("Usage: KnightMoves <sequence_length>");
			System.exit(1);
		}
		
		//specs fix vowel limit at 2
		long result = board.cachedSequenceSearch(2, length);
		System.out.println(result);
	}	

}
