package com.awmcdaniel.knightmoves;

import java.util.Map;
import java.util.Map.Entry;

public class KnightMovesDemo {

	
	public static void main(String[] args) {
		
		//this is the old main used for testing... no time for unit tests in this exercise :(
		
		KnightMovesBoard board = new KnightMovesBoard(ReferenceBoards.KNIGHT_MOVES_BOARD, ReferenceBoards.KNIGHT_MOVES_VOWELS, 
													  ReferenceBoards.KNIGHT_MOVES_ROW, ReferenceBoards.KNIGHT_MOVES_COLUMN);
		
		board.printBoard(System.out);
		board.clearSearchCache();
				
		
		System.out.println("trying single threaded brute to 16");
		for (int i = 1; i <= 16; i++){
			long sum = board.fullSequenceSearch(2, i);
			System.out.println(i + " --> " + sum);
		}
		
		//board.precomputeCache(2, 32);
		
		System.out.println("Trying cached search to 32");
		for (int i = 1; i <= 32; i++){
			long sum = board.cachedSequenceSearch(2, i);
			System.out.println(i + " --> " + sum + "  (c:" + board.getSearchCacheSize() + " h:" + board.getCacheHits() + ")");
		}
		
		board.clearSearchCache();		
		System.out.println("trying parallel/cache to 32");		
		for (int i = 1; i <= 32; i++){
			long sum;
			try {
				sum = board.parallelSequenceSearch(2, i);
				System.out.println(i + " --> " + sum);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}			
		}
		
		System.out.println("Outputting Cached Computation Table.");
		Map<String, Long> computationTable = board.getSearchCacheTable();
		for (Entry<String, Long> entry : computationTable.entrySet()){
			System.out.println(entry.getKey() + " --> " + entry.getValue());
		}
	}
	
	
}
