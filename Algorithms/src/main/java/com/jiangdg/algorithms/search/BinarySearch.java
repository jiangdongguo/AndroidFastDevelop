package com.jiangdg.algorithms.search;

import com.jiangdg.algorithms.sort.select.SimpleSelectSort;

/** 二分法查找算法，使用递归和非递归方式
 * author : jiangdg
 * date   : 2020/1/10 9:31
 * desc   : 算法思想：二分查找又称折半查找，即首先将整个数组分成两半，并将中间值a[mid]与要查找的元素element进行比较。
 *      (1) 如果a[mid]>element，说明element位于左半部分范围，此时调整right=mid-1，重新计算mid；
 *      (2) 如果a[mid]<element，说明element位于右半部分范围，此时调整left=mid+1.重新计算mid;
 *    上述只是完成了第一轮查找，后续循环(1)(2)操作，直到a[mid]=element即找到了，返回mid即为element的位置
 *    否则(left>right)，查找失败。注：left默认为0，right默认为len-1，mid = (left+right) / 2.
 *
 * 示例: 查找12。注：left、right、mid均为数组下标。
 *
 *                    -23,-8,-2,0,0,1,2, 3, 3,10,11,12,32,54,99         其中，mid=(left+right)/2=(0+14)/2=7
 *                    left             mid                 right
 *                                        ↑                         left = mid+1
 * 第一次查找(3<12)： -23,-8,-2,0,0,1,2, 3, 3, 10,11,12,32,54,99    其中，mid=((mid+1)+right)/2=(8+14)/2=11
 *                                         left      mid     right
 * 第二次查找(12=12): 找到了
 *
 * version: 1.0
 */
public class BinarySearch {

    /** 非递归方式实现
     *
     * @param a  待查找数组
     * @param element 要查找的元素
     * @return 要查找元素的下标，-1表示不存在
     */
    private int binarySearch2(int[] a, int element) {
        int left = 0;
        int right = a.length-1;
        while(left <= right) {
            int mid = (left + right) /2;
            // 如果中间值等于element，说明已经找到
            if(a[mid] == element) {
                return mid;
            }
            // 如果中间值小于element，则向右边继续查找
            if(a[mid] < element) {
                left = mid +1;
            }
            // 如果中间值大于element，则向左继续查找
            else if(a[mid] > element) {
                right = mid - 1;
            }
        }
        return -1;
    }

    private int binarySearch(int[] a, int element, int left, int right) {
        // 边界处理
        if(left > right) {
            return -1;
        }
        int mid = (left + right) / 2;
        if(a[mid] == element) {
            return mid;
        }
        if(a[mid] > element) {
            // element比中间值要小，说明要向左半边继续查找
            return binarySearch(a, element, left, mid-1);
        } else if(a[mid] < element) {
            // element比中间值要大，说明要向右半边继续查找
            return binarySearch(a, element, mid+1, right);
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] a = {11,3,1,-2,54,0,12,-23,3,99,32,0,10,2,-8};
        SimpleSelectSort simpleSelect = new SimpleSelectSort();
        simpleSelect.selectSort(a);
        System.out.println("sort after:");
        printArray(a);

        BinarySearch bs = new BinarySearch();
        // 非递归
        System.out.println("非递归方式，result="+bs.binarySearch2(a, 12));
        // 递归
        System.out.println("递归方式，result="+bs.binarySearch(a, 12, 0, a.length-1));
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
