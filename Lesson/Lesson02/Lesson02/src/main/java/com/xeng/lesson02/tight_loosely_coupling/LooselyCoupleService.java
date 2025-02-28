/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson02
 * @date: 2/21/2025
 * @time: 07:45 PM
 * @package: com.xeng.lesson02.tight_loosely_coupling
 */

package com.xeng.lesson02.tight_loosely_coupling;

import java.util.Arrays;

interface SortAlgorithm {
    void sort(int[] array);
}

class LooselyBubbleSortAlgorithm implements SortAlgorithm {
    @Override
    public void sort(int[] array) {
        System.out.println("Sorted using bubble sort algorithm");

        Arrays.stream(array).sorted().forEach(System.out::println);
    }
}

public class LooselyCoupleService {
    private SortAlgorithm sortAlgorithm;
    public LooselyCoupleService() {}
    public LooselyCoupleService(SortAlgorithm sortAlgorithm) {
        this.sortAlgorithm = sortAlgorithm;
    }
    public void complexBusiness(int[] array) {
        sortAlgorithm.sort(array);
    }

    public static void main(String[] args) {
        LooselyCoupleService lcos = new LooselyCoupleService(new LooselyBubbleSortAlgorithm());
        lcos.complexBusiness(new int[]{11, 21, 13, 42, 15});
    }
}
