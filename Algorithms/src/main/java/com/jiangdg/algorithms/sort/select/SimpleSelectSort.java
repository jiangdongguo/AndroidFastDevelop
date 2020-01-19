package com.jiangdg.algorithms.sort.select;

/** 简单选择排序，特点是向后比较大小并记录最小元素下标
 * author : jiangdg
 * date   : 2020/1/9 16:55
 * desc   : 算法思想：将第i个元素(从i=0开始)与后面的元素分别进行比较，
 *      当后面的某个元素比第i个元素小时记录该元素的下标，然后将剩余的元素与该元素继续比较，直到找到最小的那个元素，返回其下标，
 *      并将第i个元素与其交换，即完成了第一轮排序。再对后面的len-i元素进行相同的选择排序。
 *
 * 示例：
 *         11,3,1,-2,54,0,12,-23,3,99,32,0,10,2,-8
 *         ● →               ↑
 * 第一轮 -23,  3, 1,-2,54,0,12,11,3,99,32,0,10,2,-8
 *             ● →                               ↑
 * 第二轮 -23.-8, 1,   -2,54,0,12,11,3,99,32,0,10,2, 3
 *               ● → ↑
 * 第三轮 -23,-8,-2,1,54,0,12,11,3,99,32,0,10,2, 3
 * ...
 * version: 1.0
 */
public class SimpleSelectSort {

    public void selectSort(int[] a) {
        for(int i=0; i<a.length; i++) {
            // 取第i个元素，依次与后面的(len-i)个元素进行比较
            // 获得最小元素的下标
            int minIndex = i;
            for(int j=i+1; j<a.length; j++) {
                if(a[j] < a[minIndex]) {
                    minIndex = j;
                }
            }
            // 将找到的最小的元素与第i个元素交换
            int temp = a[i];
            a[i] = a[minIndex];
            a[minIndex] = temp;
        }
    }

    public static void main(String[] args) {
        int[] a = {11,3,1,-2,54,0,12,-23,3,99,32,0,10,2,-8};
        System.out.println("sort before:");
        printArray(a);
        SimpleSelectSort simpleSelect = new SimpleSelectSort();
        simpleSelect.selectSort(a);
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
