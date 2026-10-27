package com.cyco.uni.sortbench.sorting;

import java.util.Arrays;

/**
 *
 * @author CYCO TECHNOLOGIES
 */
public class QuickSort implements SortingAlgorithm {
    
    @Override
    public String getName(){
        return "Quick Sort";
    }
    
    @Override
    public double[] sort(double[] data){
        
        if(data == null){
            throw new IllegalArgumentException("Input array cannot be NULL");
        }
        
        //take a copy of original array for sorting
        double[] sorted = Arrays.copyOf(data, data.length);
        
        if(sorted.length > 1){
            quickSort(sorted, 0, sorted.length - 1);
        }
        
        return sorted;
    }   
    
    private void quickSort(double[] data, int low, int high){
        if(low >= high){
            return;
        }
        
        int pivotIndex = partition(data, low, high);
        
        quickSort(data, low, pivotIndex - 1);
        quickSort(data, pivotIndex + 1, high);
        
    }
    
    private int partition(double[] data, int low, int high){
        
        int middle= low + (high - low) / 2;
        double pivot = data[middle];
        
        swap(data, middle, high);
        
        int i = low;
        
        for(int j = low; j< high; j++){
            if(data[j] <= pivot){
                swap(data, i, j);
                i++;
            }
        }
        
        swap(data, i, high);
        
        return i;
    
    }
    
    private void swap(double[] data, int i, int j){
        double temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }
}
