package com.jiangdg.algorithms.sort.exchange;

/** 快速排序 https://blog.csdn.net/qq_40941722/article/details/94396010
 * author : jiangdg
 * date   : 2020/1/9 16:57
 * desc   :  左边找小于基准，右边找大于基准
 * version: 1.0
 */
public class QuickSort {

    private void sort(int[] array) {
        if(array.length>0) {
            quickSort(array, 0, array.length-1);
        }
    }

    private void quickSort(int[] array, int low, int high) {
        if(low < high) {
            int middle = getMiddle(array, low, high);
            quickSort(array, low, middle-1);
            quickSort(array, middle+1, high);
        }
    }

    private int getMiddle(int[] a, int low, int high) {
        // 基准
        int base = a[low];
        while (low < high) {
            // 1. high指针由右向左移动找“小于”base的值
            // 如果大于等于base，则high--向左继续找；
            // 如果小于base，则high停止向左前进，结束循环；
            while (low<high && a[high]>=base) {
                high--;
            }
            a[low] = a[high];
            // 2. low指针由左向右移动找“大于”base的值
            // 如果小于等于base，则low++向右继续找；
            // 如果大于base，则low停止向右前进，结束循环
            while (low<high && a[low]<=base) {
                low++;
            }
            a[high] = a[low];
        }
        // 3. 将base插入到low位置
        // 这个a[low]将成为下一轮快速排序的基准
        a[low] = base;
        return low;
    }

    public static void main(String[] args) {
        int[] a = {11,3,1,-2,54,0,12,-23,3,99,32,0,10,2,-8,20};
        System.out.println("sort before:");
        printArray(a);
        QuickSort fast = new QuickSort();
        fast.sort(a);
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
