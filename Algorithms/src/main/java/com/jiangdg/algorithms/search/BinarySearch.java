package com.jiangdg.algorithms.search;

import com.jiangdg.algorithms.sort.select.SimpleSelectSort;

/** ���ַ������㷨��ʹ�õݹ�ͷǵݹ鷽ʽ
 * author : jiangdg
 * date   : 2020/1/10 9:31
 * desc   : �㷨˼�룺���ֲ����ֳ��۰���ң������Ƚ���������ֳ����룬�����м�ֵa[mid]��Ҫ���ҵ�Ԫ��element���бȽϡ�
 *      (1) ���a[mid]>element��˵��elementλ����벿�ַ�Χ����ʱ����right=mid-1�����¼���mid��
 *      (2) ���a[mid]<element��˵��elementλ���Ұ벿�ַ�Χ����ʱ����left=mid+1.���¼���mid;
 *    ����ֻ������˵�һ�ֲ��ң�����ѭ��(1)(2)������ֱ��a[mid]=element���ҵ��ˣ�����mid��Ϊelement��λ��
 *    ����(left>right)������ʧ�ܡ�ע��leftĬ��Ϊ0��rightĬ��Ϊlen-1��mid = (left+right) / 2.
 *
 * ʾ��: ����12��ע��left��right��mid��Ϊ�����±ꡣ
 *
 *                    -23,-8,-2,0,0,1,2, 3, 3,10,11,12,32,54,99         ���У�mid=(left+right)/2=(0+14)/2=7
 *                    left             mid                 right
 *                                        ��                         left = mid+1
 * ��һ�β���(3<12)�� -23,-8,-2,0,0,1,2, 3, 3, 10,11,12,32,54,99    ���У�mid=((mid+1)+right)/2=(8+14)/2=11
 *                                         left      mid     right
 * �ڶ��β���(12=12): �ҵ���
 *
 * version: 1.0
 */
public class BinarySearch {

    /** �ǵݹ鷽ʽʵ��
     *
     * @param a  ����������
     * @param element Ҫ���ҵ�Ԫ��
     * @return Ҫ����Ԫ�ص��±꣬-1��ʾ������
     */
    private int binarySearch2(int[] a, int element) {
        int left = 0;
        int right = a.length-1;
        while(left <= right) {
            int mid = (left + right) /2;
            // ����м�ֵ����element��˵���Ѿ��ҵ�
            if(a[mid] == element) {
                return mid;
            }
            // ����м�ֵС��element�������ұ߼�������
            if(a[mid] < element) {
                left = mid +1;
            }
            // ����м�ֵ����element���������������
            else if(a[mid] > element) {
                right = mid - 1;
            }
        }
        return -1;
    }

    private int binarySearch(int[] a, int element, int left, int right) {
        // �߽紦��
        if(left > right) {
            return -1;
        }
        int mid = (left + right) / 2;
        if(a[mid] == element) {
            return mid;
        }
        if(a[mid] > element) {
            // element���м�ֵҪС��˵��Ҫ�����߼�������
            return binarySearch(a, element, left, mid-1);
        } else if(a[mid] < element) {
            // element���м�ֵҪ��˵��Ҫ���Ұ�߼�������
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
        // �ǵݹ�
        System.out.println("�ǵݹ鷽ʽ��result="+bs.binarySearch2(a, 12));
        // �ݹ�
        System.out.println("�ݹ鷽ʽ��result="+bs.binarySearch(a, 12, 0, a.length-1));
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
