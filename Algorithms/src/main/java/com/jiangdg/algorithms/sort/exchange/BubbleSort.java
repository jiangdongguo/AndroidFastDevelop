package com.jiangdg.algorithms.sort.exchange;

/** 冒泡排序
 * author : jiangdg
 * date   : 2020/1/9 16:57
 * desc   :
 *     11,3,1,-2,54,0,12,-23,3,99,32,0,10,2,-8
 * 第1轮冒泡排序：3,1,-2,11,0,12,-23,3,54,32,0,10,2,-8,    99
 *
 * exchange的作用（优化时间复杂度），比如：
 *     8,1,4,5,6,7
 * 只需比较一轮： 1,4,5,6,7,8
 * version: 1.0
 */
public class BubbleSort {

    private void exchangeSort(int[] a) {
        // 对数组进行i=(len-1)轮(最坏情况)比较
        // 且每次比较i次得到最大的值，并交换到第i位置
        for (int i=a.length-1; i>0; i--) {
            boolean exchange = false;
            // 对前i个元素仅仅两两比较
            // 找出最大的元素放到第i个位置处
            for(int j=0; j < i; j++) {
                if(a[j] > a[j+1]) {
                    int temp = a[j+1];
                    a[j+1] = a[j];
                    a[j] = temp;
                    exchange =true;
                }
            }
            // 如果已经有序了(exchange=false)，则结束剩余的轮
            if(!exchange) return;
        }
    }

    public static void main(String[] args) {
        int[] a = {11,3,1,-2,54,0,12,-23,3,99,32,0,10,2,-8};
        System.out.println("sort before:");
        printArray(a);
        BubbleSort bubbleSort = new BubbleSort();
        bubbleSort.exchangeSort(a);
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
