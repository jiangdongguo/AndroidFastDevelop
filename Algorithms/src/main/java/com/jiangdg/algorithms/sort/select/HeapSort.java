package com.jiangdg.algorithms.sort.select;

/** 堆排序
 * author : jiangdg
 * date   : 2020/1/9 16:56
 * desc   :堆是具有以下性质的完全二叉树：每个结点的值都大于或等于其左右孩子结点的值，称为大顶堆；
 * 或者每个结点的值都小于或等于其左右孩子结点的值，称为小顶堆
 * version: 1.0
 */
public class HeapSort {

    private void sort(int[] a) {

    }

    public static void main(String[] args) {
        int[] a = {11,3,1,-2,54,0,12,-23,3,99,32,0,10,2,-8,20};
        System.out.println("sort before:");
        printArray(a);
        HeapSort heap = new HeapSort();
        heap.sort(a);
        System.out.println("sort after:");
        printArray(a);
    }

    private static void printArray(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int element : array) {
            sb.append(element);
            sb.append(",");
        }
        System.out.println(sb.toString());
    }
}
