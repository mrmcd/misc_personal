package com.awmcdaniel.knightmoves;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class KnightMovesBoard {

	private KnightBoardNode[][] board;
	private Set<Character> vowels;
	private int rowSize;
	private int colSize;
	
	private ConcurrentHashMap<String, Long> boardSearchCache;
	private int cacheHits;
	
	public KnightMovesBoard(char[][] referenceBoard, char[] referenceVowels, int rowSize, int columnSize){
		//initialize the vowels		
		vowels = new HashSet<Character>();
		for (char v : referenceVowels){
			vowels.add(v);
		}
		
		
		//initialize the board nodes
		
		//for simplicity, requiring that the maximum board dimensions are passed into us (only one board is given in spec, no 
		//mention of input or storage of board data in requirements). 		
		board = new KnightBoardNode[rowSize][]; 
		for (int row = 0; row < rowSize; row++){
			board[row] = new KnightBoardNode[columnSize];
			for (int col = 0;  col < columnSize; col++){
				char id = referenceBoard[row][col];
				if (id == '\0'){
					//null square
					board[row][col] = null;
				}else{
					board[row][col] = new KnightBoardNode(id, vowels.contains(id));
				}
			}
		}
		
		this.rowSize = rowSize;
		this.colSize = columnSize;
		
		//board initialized, now go to each one and precompute graph edges for each legal move from that square
		for (int row = 0; row < rowSize; row++){
			for (int col = 0; col < colSize; col++){
				computeValidMovesForNode(row, col);
			}
		}
		
		//initialize/reset cache
		boardSearchCache = new ConcurrentHashMap<String, Long>();
		cacheHits = 0;
	}
	
	public void clearSearchCache(){
		boardSearchCache.clear();
		cacheHits = 0;
	}
	
	public int getSearchCacheSize(){
		return boardSearchCache.size();
	}
	
	public Map<String, Long> getSearchCacheTable(){
		return Collections.unmodifiableMap(boardSearchCache);
	}
	
	public int getCacheHits(){
		return cacheHits;
	}
	
	private void computeValidMovesForNode(int row, int col){
		KnightBoardNode origin = board[row][col];
		if (origin == null){
			//null space, skip it
			return;
		}
		/*
		 * note: 
		 * U - up - col-1
		 * D - down - col+1
		 * L - left - row-1
		 * R - right - row+1
		 */
		
		origin.addIfNotNullMove(attemptMove(row, col, -1, -2, false)); //UUL
		origin.addIfNotNullMove(attemptMove(row, col, +1, -2, false)); //UUR
		origin.addIfNotNullMove(attemptMove(row, col, -1, +2, false)); //DDL
		origin.addIfNotNullMove(attemptMove(row, col, +1, +2, false)); //DDR
		
		origin.addIfNotNullMove(attemptMove(row, col, +2, -1, true)); //RRU
		origin.addIfNotNullMove(attemptMove(row, col, +2, +1, true)); //RRD
		origin.addIfNotNullMove(attemptMove(row, col, -2, -1, true)); //LLU
		origin.addIfNotNullMove(attemptMove(row, col, -2, +1, true)); //LLD
	}
	
	/**
	 * This method will allow you to precompute the value cache for a given max vowels and length
	 * @param maxVowel
	 * @param maxLength
	 */
	public void precomputeCache(int maxVowel, int maxLength){
		for (int l = 1; l <= maxLength; l++){
			for (int v = 0; v <= maxVowel; v++){
				precomputeBoard(v, l);
			}
			//System.out.println("Precomputed board for l = " + l + ", cacheSize=" + getSearchCacheSize() );
		}
	}
		
	private void precomputeBoard(int vowel, int length){
		for (int row = 0; row < rowSize; row++){
			for (int col = 0; col < colSize; col++){
				if (board[row][col] != null){
					KnightBoardNode node = board[row][col];
					doSearchCaching(node, vowel, length);
				}
			}
		}
	}
	
	private KnightBoardNode attemptMove(int row, int col, int rowAdd, int colAdd, boolean rowFirst){
		if (rowFirst){
			//add row offset first
			int rowNew = row + rowAdd;
			if ( (rowNew < 0) || (rowNew >= rowSize) ){
				return null; //went off board
			}
			int colNew = col + colAdd;
			if ( (colNew < 0) || (colNew >= colSize) ){
				return null; //went off board
			}
			return board[rowNew][colNew];
		}else{
			//add column first
			int colNew = col + colAdd;
			if ( (colNew < 0) || (colNew >= colSize) ){
				return null; //went off board
			}
			int rowNew = row + rowAdd;
			if ( (rowNew < 0) || (rowNew >= rowSize) ){
				return null; //went off board
			}
			return board[rowNew][colNew];
		}
	}
	
	/**
	 * Prints the board to the provided {@link PrintStream}, for diagnostic purposes. 
	 * @param out
	 */
	public void printBoard(PrintStream out){
		for (int row = 0; row < rowSize; row++){
			for (int col = 0; col < colSize; col++){
				KnightBoardNode node = board[row][col];
				if (node == null){
					out.print("-----  ");
					
				}else{
					out.print(node.getNodeId() + "/" + node.getMovesFromHere() + "/" + (node.isVowel()? "Y" : "N") + "  ");
				}
			}
			out.println();
		}
	}
	
	public long sequenceSearch(int row, int col, int vowelLimit, int length){
		KnightBoardNode originNode = board[row][col];
		return doSearch(originNode, vowelLimit, length);
	}
	
	/**
	 * Search the board graph for the total number of valid sequences of a given length and vowel limit. This is very slow brute force attack. 
	 * @param vowelLimt
	 * @param length
	 * @return
	 */
	public long fullSequenceSearch(int vowelLimt, int length){
		long searchSum = 0;
		for (int row = 0; row < rowSize; row++){
			for (int col = 0; col < colSize; col++){
				if (board[row][col] != null){
					//don't start from a null square!
					searchSum += sequenceSearch(row, col, vowelLimt, length);
				}
			}
		}
		return searchSum;
	}
	
	/**
	 * Do a cached search, but run each starting cell in parallel on a {@link ThreadPoolExecutor}. Uses the number of threads as reported available cores. 
	 * 
	 * Note not really necessary now that cached search is fast enough but interesting I guess. 
	 * @param vowelLimit
	 * @param length
	 * @return
	 * @throws InterruptedException
	 */
	public long parallelSequenceSearch(int vowelLimit, int length) throws InterruptedException{
		AtomicLong resultAccumulator = new AtomicLong();
		int cores = Runtime.getRuntime().availableProcessors();
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(cores, cores, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000000000));
		executor.prestartAllCoreThreads();
		for (int row = 0; row < rowSize; row++){
			for (int col = 0; col < colSize; col++){
				if (board[row][col] != null){
					executor.execute(new KnightBoardRunner(board[row][col], vowelLimit, length, resultAccumulator));
				}
			}
		}
		
		executor.shutdown();
		boolean done = false;
		while (!done){
			done = executor.awaitTermination(100,TimeUnit.MILLISECONDS);			
		}
			
		
		return resultAccumulator.get();
	}
	
	/**
	 * Search the board for a result similar to {@link KnightMovesBoard#fullSequenceSearch(int, int)}, but cache intermediate results to make performance much
	 * faster. 
	 * 
	 * @param vowelLimit
	 * @param length
	 * @return
	 */
	public long cachedSequenceSearch(int vowelLimit, int length){
				
		long totalSum = 0;
		for (int row = 0; row < rowSize; row++){
			for (int col = 0; col < colSize; col++){
				if (board[row][col] != null){
					KnightBoardNode node = board[row][col];
					totalSum += doSearchCaching(node, vowelLimit, length);
				}
			}
		}
		
		return totalSum;
	}
	
	private long doSearchCaching(KnightBoardNode node, int vowels, int length){
		if (length == 1){
			return 1;
		}
		
		if ( boardSearchCache.containsKey(cacheKey(node, vowels, length)) ) {
			cacheHits++;
			long value = boardSearchCache.get(cacheKey(node, vowels, length));
			return value;
		}
		

		
		//cache miss, recurse/cache/compute instead
		int vowelsRemaining = vowels;
		if (node.isVowel()){
			vowelsRemaining = Math.max(vowelsRemaining-1,0);
		}
		int lengthRemaining = length - 1;
		
		long recurseSum = 0;

		Set<KnightBoardNode> outboundMoves = node.getOutboundMoves();
		for ( KnightBoardNode move : outboundMoves ){
			if ( ( vowelsRemaining < 1) && ( move.isVowel() ) ){
				//can't do any more vowels, skip it
				continue;
			}else{			
				recurseSum += doSearchCaching(move, vowelsRemaining, lengthRemaining);
			}
		}	
		
		boardSearchCache.putIfAbsent(cacheKey(node, vowels, length), recurseSum);
		return recurseSum;
	}
	
	private static String cacheKey(KnightBoardNode node, int vowels, int length){		
		String k = Character.toString(node.getNodeId()) + ":v" + Integer.toString(vowels) + ":l" + Integer.toString(length);
		return k;
	}
	
	private long doSearch(KnightBoardNode node, int vowelsRemaining, int lengthRemaining){
		if (lengthRemaining == 1){
			//termination condition, we found a valid sequence
			return 1;
		}
		
		if (node.isVowel()){
			//decrement vowel allowance
			vowelsRemaining--;
		}
		//decrement length allowance 
		lengthRemaining--;
		
		//recurse and sum for n-1 length
		long recurseSum = 0;
		Set<KnightBoardNode> outboundMoves = node.getOutboundMoves();
		for ( KnightBoardNode move : outboundMoves ){
			if ( ( vowelsRemaining < 1) && ( move.isVowel() ) ){
				//can't do any more vowels, skip it
				continue;
			}			
			recurseSum += doSearch(move, vowelsRemaining, lengthRemaining);
		}
		
		//return sum up call stack
		return recurseSum;
	}
	
	private class KnightBoardRunner implements Runnable {

		private KnightBoardNode node;
		private int vowelsRemaining;
		private int lengthRemaining;
		private AtomicLong resultAccumulator;
		
		
		public KnightBoardRunner(KnightBoardNode node, int vowelsRemaining, int lengthRemaining, AtomicLong resultAccumulator) {
			this.node = node;
			this.vowelsRemaining = vowelsRemaining;
			this.lengthRemaining = lengthRemaining;
			this.resultAccumulator = resultAccumulator;			
		}


		public void run() {
			long sum = doSearchCaching(node, vowelsRemaining, lengthRemaining);
			resultAccumulator.addAndGet(sum);
		}
		
	}
}
