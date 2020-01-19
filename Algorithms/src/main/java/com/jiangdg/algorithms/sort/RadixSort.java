package com.jiangdg.algorithms.sort;

import java.util.ArrayList;

/** 基数排序
 * author : jiangdg
 * date   : 2020/1/9 16:58
 * desc   :
 *
 * 示例：
 *      136,2,6,8,9,2,8,11,23,56,34,90,89,29,145,209,320,78,3
 *       ↑↑↑↑↑↑↑ ↑ ↑ ↑ ↑ ↑ ↑ ↑  ↑  ↑  ↑ ↑↑
 *
 * 个位(0~9)：{[90,320],[11],[2,2],[23,3],[34],[145],[136,6,56],[],[8,8,78],[9,89,29,209]}
 *       排序后：90,320, 11, 2,2, 3,23, 34, 145, 6,56,136, 8,8,78, 9,29,89,209
 *              ↑  ↑  ↑         ↑  ↑   ↑    ↑  ↑      ↑    ↑ ↑  ↑
 *
 * 十位(0~9)：{[2,2,3,6,8,8,9，209],[11],[320,23,29],[34,136],[145],[56],[],[78],[89],[90]}
 *       排序后：2,2,3,6,8,8,9,209,11,23,29,34,136,145,56,78,89,90
 *                            ↑              ↑  ↑
 * 百位(0~9)：{[2,2,3,6,8,8,9,11,23,29,34,56,78,89,90], [136,145],[209],[],[],[],[],[],[],[]}
 *      排序后：2,2,3,6,8,8,9,11,23,29,34,56,78,89,90,136,145,209
 * version: 1.0
 */
public class RadixSort {

    private void sort(int[] array) {
        // 区分正数、负数
        int negtiveLen = 0;
        int positiveLen = 0;
        int[] negtiveArray = new int[array.length];
        int[] positiveArray = new int[array.length];
        for(int i=0; i<array.length; i++) {
            if(array[i] < 0) {
                negtiveArray[negtiveLen++] = Math.abs(array[i]);
            } else {
                positiveArray[positiveLen++] = array[i];
            }
        }
        // 分别对左右数组进行基数排序
        radixSort(negtiveArray, negtiveLen);
        radixSort(positiveArray, positiveLen);
        // 将negtiveArray元素均取反，且从后向前再与positive元素合并
        int count = 0;
        for(int j=negtiveLen-1; j>0; j--) {
            array[count] = - negtiveArray[j];
            count++;
        }
        for (int j=0; j<positiveLen; j++) {
            array[count] = positiveArray[j];
            count++;
        }
    }

    private void radixSort(int[] array, int len) {
        // 1. 计算待排序数据的最大值位数
        int max = array[0];
        for(int i=1; i<len; i++) {
            if(array[i] > max) {
                max = array[i];
            }
        }
        int times = 0;
        while (max > 0) {
            max = max / 10;
            times ++;
        }
        // 2. 创建一个数组，数组的元素也是数组，共10个
        // 对应于下标0~9，因为无论是个位、十位还是百位...都是由0~9这十个数字表示
        ArrayList<ArrayList> queue = new ArrayList<>();
        for(int i=0; i<10; i++) {
            ArrayList q= new ArrayList();
            queue.add(q);
        }
        // 3. 遍历待排序数组。
        for(int i=0; i<times; i++) {
            // (1)根据个位(times=0)、十位(times=1)、百位(times=2)...依次将相应的数据存储到对应的数组中
            // 计算一个数的某位公式：n%(10^(times+1))/(10^times)，比如789，它的十位=789%(10^2)/10^1=89/10=8
            for (int j=0; j<len; j++) {
                int num = array[j] % (int)Math.pow(10, i+1) / (int)Math.pow(10, i);
                // 获取queue中下标为num的数组元素，将array[j]插入
                ArrayList q= queue.get(num);
                q.add(array[j]);
            }

            // (2) 开始收集。即将queue存入的数据依次取出，放入array中(覆盖)以完成一轮排序
            // 需要注意的是，只有queue的子数组不为空才取，且取出一个删除一个便于下一轮排序
            int count = 0;
            for(int k=0; k<10; k++) {
                ArrayList<Integer> q = queue.get(k);
                while (q.size()>0) {
                    array[count] = q.get(0); // 这里注意总是第一个
                    q.remove(0);
                    count++;
                }
            }
        }
    }


    public static void main(String[] args) {
        int[] a = {136,-2,6,8,-9,2,8,11,-23,56,0,34,90,-89,29,145,209,-320,-78,3};
        System.out.println("sort before:");
        printArray(a);
        RadixSort radix = new RadixSort();
        radix.sort(a);
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
