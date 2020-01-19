package com.jiangdg.algorithms.sort.insert;

/** 二分法插入排序
 * author : jiangdg
 * date   : 2020/1/9 16:53
 * desc   : 二分法插入排序是在插入第i个元素时，对前面的0～i-1元素进行折半，先跟他们中间的那个元素比
 *         如果小，则对前半再进行折半，否则对后半进行折半，循环条件是left<right
 *         即当left!<right时，跳出循环然后再把第i个元素前1位与目标位置
 *         之间的所有元素后移，再把第i个元素放在目标位置上。
 *  示例：
 *          11,3,1,-2,54,0,12,-23,3,99,32,0,10,2,-8
 *         -2 1 3 11 54   0
 *       left   m   right    即mid = (left+right)/2 = (0 + 4)/ 2 = 2（left、mid、right均为数组下标）
 *            r=m-1
 * version: 1.0
 */
public class BinaryInsertSort {

    private void insertSort(int[] a) {
        // 第1个元素已经有序，从第2个元素开始排序
        for(int i=1; i<a.length; i++) {
            int left = 0;
            int right = i-1;
            int temp = a[i];
            // 对前面的0~i-1元素进行折半比较
            // 如果a[mid]>temp，则对前半再进行折半，即right=mid - 1
            // 如果a[mid]<temp，则对后半再进行折半，即left=mid + 1
            while(left < right) {
                int mid = (left + right) / 2;
                if(a[mid] > temp) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            // 当下标left>=right时，则将left之后的元素均后移一位，再将第i个元素插入到left前面
            // 注意：第left个元素也要参与比较，即j>=left
            int j;
            for (j=i-1; j>=left; j--) {
                a[j+1] = a[j];
            }
            a[j+1] = temp;
        }
    }


    public static void main(String[] args) {
        int[] a = {11,3,1,-2,54,0,12,-23,3,99,32,0,10,2,-8};
        System.out.println("sort before:");
        printArray(a);
        BinaryInsertSort binary = new BinaryInsertSort();
        binary.insertSort(a);
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
