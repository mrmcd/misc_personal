package com.awmcdaniel.knightmoves;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class KnightBoardNode {

	private char nodeId;
	private boolean vowel;
	private Set<KnightBoardNode> moveEdges;
	
	public KnightBoardNode(char nodeId, boolean isVowel){
		this.nodeId = nodeId;
		this.vowel = isVowel;
		moveEdges = new LinkedHashSet<KnightBoardNode>();
	}
	
	public char getNodeId(){
		return nodeId;		
	}
	
	public boolean isVowel(){
		return vowel;
	}
	
	public boolean addValidMove(KnightBoardNode destination){
		if (!moveEdges.contains(destination)){
			moveEdges.add(destination);
			return true;
		}else{
			return false;
		}
	}
	
	public boolean addIfNotNullMove(KnightBoardNode destination){
		if (destination != null){
			return addValidMove(destination);
		}else{
			return false;
		}			
	}
	
	public int getMovesFromHere(){
		return moveEdges.size();				
	}
	
	public Set<KnightBoardNode> getOutboundMoves(){
		return Collections.unmodifiableSet(moveEdges);
	}
	
	public boolean equals(Object other){
		if (other instanceof KnightBoardNode ){
			return this.nodeId == ((KnightBoardNode)other).getNodeId();
		}else{
			return false;
		}
	}
	
	public String toString(){
		return Character.toString(nodeId);  
	}
}
