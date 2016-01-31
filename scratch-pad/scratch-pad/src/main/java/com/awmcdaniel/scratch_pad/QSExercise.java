package com.awmcdaniel.scratch_pad;

import java.util.Arrays;
import java.util.Random;

public class QSExercise {

	
	public static void main(String[] args){
		
		Random r = new Random();
		//generate an array of random ints 20 units long
		int[] array = new int[100];
		for (int i = 0; i < array.length; i++){
			array[i] = r.nextInt(100);
		}
		
		int[] copyArray = Arrays.copyOf(array, array.length);
		int[] mergeArray = Arrays.copyOf(array, array.length);
		
		System.out.println("Before: " + Arrays.toString(array));		
		
		quickSort(array, 0, array.length-1);
		System.out.println("After: " + Arrays.toString(array));
		
		mergeArray = doMergeSort(mergeArray, 0, mergeArray.length-1);
		System.out.println("Merge: " + Arrays.toString(mergeArray));
		
		Arrays.sort(copyArray);		
		System.out.println("Verify: " + Arrays.toString(copyArray));
		
		System.out.println("FB: " + fizzBuzz(0, 100));
	}
	
	
	public static void quickSort(int[] array, int start, int end){
		if (start >= end){
			return;
		}
		
		int pivotIdx = end;
		int pivot = array[pivotIdx];
		int storeIdx = start;
		for (int i = start; i < end; i++){
			if (array[i] < pivot){
				//swap them
				swap(array,storeIdx,i);
				storeIdx++;
			}
		}
		//swap pivot into final place (the next storage location)
		swap(array,storeIdx,pivotIdx);
		
		//storeIdx is the new partition
		quickSort(array, start, storeIdx - 1);
		quickSort(array, storeIdx + 1, end);
		
	}
	
	
	public static void swap(int[] array, int i, int j){
		int t = array[i];
		array[i] = array[j];
		array[j] = t;
	}
	
	
	public static int[] doMergeSort(int[] srcArray, int start, int end){
		if (start == end){
			//bottom of recursion
			return new int[] { srcArray[start] };
		}
		
		
		//else, divide the array in half and recurse
		int midpoint = start + ( (end-start)/2 );
		int[] left = doMergeSort(srcArray,start,midpoint);
		int[] right = doMergeSort(srcArray,midpoint+1,end);
		
		//merge left and right and return up the recursion
		return merge(left, right);
		
	}


	public static int[] merge(int[] left, int[] right){
		int[] merged = new int[left.length + right.length];
		int lIdx = 0;
		int rIdx = 0;
		
		for (int i = 0; i < merged.length; i++){
			if (lIdx == left.length){
				//left is spent, go only with right
				merged[i] = right[rIdx];
				rIdx++;
			}else if (rIdx == right.length){
				//right is spent, go only right left
				merged[i] = left[lIdx];
				lIdx++;
			}else{
				//both being spent should only happen when loop terminates, so now compare the two and select smallest
				if (left[lIdx] <= right[rIdx]){
					//select left
					merged[i] = left[lIdx];
					lIdx++;
				}else{
					//select right
					merged[i] = right[rIdx];
					rIdx++;
				}			
			}
		}
		return merged;
	}
	
	public static String fizzBuzz(int start, int end){
		StringBuilder sb = new StringBuilder();
		for (int i = start; i <= end; i++){
			boolean printNum = true;			
			if ( i % 3 == 0){
				sb.append("fizz");
				printNum = false;
			}
			
			if ( i % 5 == 0){
				sb.append("buzz");
				printNum = false;
			}		
			if (printNum){
				sb.append(i);
			}
			
			sb.append(" ");
		}
		
		return sb.toString();
	}
	
}
