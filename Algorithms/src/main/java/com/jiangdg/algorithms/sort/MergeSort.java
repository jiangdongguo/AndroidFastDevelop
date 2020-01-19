package com.jiangdg.algorithms.sort;

/** 归并排序 https://sort.hust.cc/5.mergesort
 * author : jiangdg
 * date   : 2020/1/9 16:51
 * desc   :
 * ● 算法思想：归并排序是建立在归并操作上的一种有效的排序，它采用分治法(Divide and Conquer)
 *              即分而治之思想实现，和选择排序一样，其性能不受输入数据的影响，代价是需要额外的内存空间。
 * ● 算法步骤：
 *            （1）申请空间，使其大小为两个已经排序序列之和，该空间用来存放合并后的序列；
 *            （2）设定两个指针，最初位置分别为两个已经排序序列的起始位置；
 *            （3）比较两个指针所指向的元素，选择相对小的元素放入到合并空间，并移动指针到下一位置；
 *            （4）重复步骤 3 直到某一指针达到序列尾；
 *            （5）将另一序列剩下的所有元素直接复制到合并序列尾。
 * ● 算法性能：
 *            （1）时间复杂度：O(nlogn)
 *            （2）空间复杂度：
 *            （3）稳定性：
 *
 * ● 示例：left、right、mid均为下标值
 *
 *   36,-2,6,8,-9,2,8,11,-23, 56,0,34,90,-89,29,145,209,-320,-78,3
 * left                      mid                                right    其中，mid=(left+right)/2=(0+19)/2=9
 *   ....
 *  由下而上两两排序，最终得到两个已经排序的序列:
 *  {-23,-9,-2,2,6,8,8,11,36,56}， {-320,-89,-78,0,3,29,34,90,145,209}
 *
 *  最后一次执行归并排序结果：-320,-89,-78,-23,-9,-2,0,2,6,8,8,11,29,34,56,90,136,145,209
 *
 * version: 1.0
 */
public class MergeSort {

    /** 采用自下而上的迭代实现归并排序
     *
     * @param array 待排序数组
     * @param left 起始下标
     * @param right 终点下标
     */
    private void mergeSort(int[] array, int left, int right) {
        if(left < right) {
            int mid = (left + right) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid+1, right);
            // 合并
            merge(array, left, mid, right);
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        int[] tmpArray = new int[array.length];
        int rightStart = mid + 1;   // 右边小数组起始下标
        int tmp = left;
        int third = left;
        // 1. 比较左右两个小数组，数值较小的先拷贝到tmpArray
        while (left<=mid && rightStart<=right) {
            if(array[left] <= array[rightStart]) {
                tmpArray[third++] = array[left++];
            } else {
                tmpArray[third++] = array[rightStart++];
            }
        }
        // 2. 如果左边还有数据没有拷贝完，直接将剩余的数据其拷贝到tmpArray
        while (left <= mid) {
            tmpArray[third++] = array[left++];
        }
        // 3. 如果右边还有数据没有拷贝完，直接将剩余的数据拷贝到tmpArray
        while (rightStart <= right) {
            tmpArray[third++] = array[rightStart++];
        }
        // 4. 将tmpArray中已经排序的数据，拷贝到array数组
        while (tmp <= right) {
            array[tmp] = tmpArray[tmp++];
        }
    }

    public static void main(String[] args) {
        int[] a = {136,-2,6,8,-9,2,8,11,-23,56,0,34,90,-89,29,145,209,-320,-78};
        System.out.println("sort before:");
        printArray(a);
        MergeSort ms = new MergeSort();
        // 注意：left、right为两端下标
        ms.mergeSort(a, 0 , a.length-1);
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
