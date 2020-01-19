package com.jiangdg.algorithms.sort.insert;

/** 直接插入排序，特点是比较元素后向后移动插入
 * author : jiangdg
 * date   : 2020/1/9 16:52
 * desc   : 算法思想：将第i个元素(由于第1个元素已经有序，故i>=2)依次与前(i-1)个"有序"元素"由后向前"依次进行比较
 *          当某个元素大于第i个元素时，将该元素(包含)及其之后的元素向后移动一位
 *          当某个元素小于等于i元素时，结束循环再将第i个元素插入到该元素前面
 *
 * 示例：
 *             11,  3,1,-2,99,0,12,-23,3,99,32,0,10,2,-8
 *             ↑←●
 *第一轮插入： 3,11,   1,-2,99,0,12,-23,3,99,32,0,10,2,-8
 *            ↑    ←●
 *第二轮插入：1,3,11,  -2,99,0,12,-23,3,99,32,0,10,2,-8
 *           ↑      ←●
 *第三轮插入：-2,1,3,11,   99,0,12,-23,3,99,32,0,10,2,-8
 *                  ↑  ←●
 * 第四轮插入：-2,1,3,11,99,  0,12,-23,3,99,32,0,10,2,-8
 * ...
 * version: 1.0
 */
public class DirectInsertSort {

    private void insertSort(int[] a) {
        // 第1个元素已经有序，从第2个元素开始排序
        for(int i=1; i<a.length; i++) {
            int temp = a[i];
            // 将第i个元素依次与前i-1个元素比较
            // 当某个元素大于i元素时，将该元素之后的元素向后移动一位
            // 当某个元素小于等于i元素时，结束循环再将第i个元素插入到该元素前面
            int j;
            for(j=i-1; j>=0; j--) {
                if(a[j] > temp) {
                    a[j+1] = a[j];
                } else {
                    break;
                }
            }
            a[j+1] = temp;
        }
    }

    public static void main(String[] args) {
//        int[] a = {11,3,1,-2,54,0,12,-23,3,99,32,0,10,2,-8};
        int a[] = {-23,-8,0,0,3,11,20,32,-2,1,2,3,10,12,54,99};
        System.out.println("sort before:");
        printArray(a);
        DirectInsertSort direct = new DirectInsertSort();
        direct.insertSort(a);
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
