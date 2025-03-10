/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson02
 * @date: 2/21/2025
 * @time: 07:31 PM
 * @package: com.xeng.lesson02.tight_loosely_coupling
 */

package com.xeng.lesson02.tight_loosely_coupling;

import java.util.Arrays;

//Tight coupling
class BubbleSortAlgorithm {
    public void sort(int[] arr) {
        System.out.println("Sắp xếp theo giải thuật BubbleSort");
        int n = arr.length;
        boolean swapped = false;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) { //swapped == false
                break;
            }
        }
    }
}

public class TightCouplingService {
    private BubbleSortAlgorithm bubbleSortAlgorithm;
    public TightCouplingService() {
        this.bubbleSortAlgorithm = new BubbleSortAlgorithm();
    }
    public TightCouplingService(BubbleSortAlgorithm bubbleSortAlgorithm) {
        this.bubbleSortAlgorithm = bubbleSortAlgorithm;
    }

    public void complexBussinessSort(int[] arr) {
        bubbleSortAlgorithm.sort(arr);
        Arrays.stream(arr).forEach(System.out::println);
    }

    public static void main(String[] args) {
        TightCouplingService tightCouplingService = new TightCouplingService();
        tightCouplingService.complexBussinessSort(new int[]{1, 9, 4, 2, 8, 3, 1});
    }
}
