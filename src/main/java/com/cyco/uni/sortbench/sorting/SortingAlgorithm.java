package com.cyco.uni.sortbench.sorting;

/**
 *
 * @author CYCO TECHNOLOGIES
 */
public interface SortingAlgorithm {
    //Returns the name of sorting algoriithem
    String getName();
    
    //return a new sorted array (ascending order)
    //The Original Arraay keep unchanged
    
    double[] sort(double[] data);
    
}
