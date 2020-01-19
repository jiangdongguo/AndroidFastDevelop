package com.jiangdg.algorithms.sort.insert;

/** 希尔排序, 是直接插入排序改进版
 * author : jiangdg
 * date   : 2020/1/9 16:54
 * desc   : 算法思想：以某个增量，如delta=len/2，将要排序的数据分为多个组,，再对每个组进行直接插入排序。
 *       然后，再继续取一个增量，delta=delta/2=(len/2)/2，将要排序的数据分为多个组，再对每个组进行直接插入排序。
 *       再次，继续取增量完成上述操作，直到delta=1。其中，{len/2,(len/2)/2,....1}为增量序列
 *
 * 示例：11,3,1,-2,54,0,12,-23,3,99,32,0,10,2,-8,20
 *      ↑ ● →              ↑●→
 *
 * 第一轮分组(delta=8)：(11,3),(3,99),(1,32),(-2,0),(54,10),(0,2),(12,-8),(-23,20)
 *     对每个分组插入排序后： 3,11,3,99,1,32，-2,0,10,54,0,2，-8,12，-23,20
 *                           ↑ ●     ↑●        ↑●       ↑ ●
 *
 * 第二轮分组(delta=8/2=4) : (3,1,10,-8),(11,32,54,12),(3,-2,0,-23),(99,0,2,20)
 *     对每个分组插入排序后：-8,1,3,10, 11,12,32,54,  -23,-2,0,3,  0,2,20,99
 *                           ↑●↑●  ↑ ●  ↑ ●   ↑  ●↑●  ↑● ↑●
 *
 *  第三轮分组(delta=4/2=2): (-8,3,11,32,-23,0,0,20),(1,10,12,54,-2,3,2,99)
 *     对每个分组插入排序后：-23,-8,0,0,3,11,20,32,  -2,1,2,3,10,12,54,99
 *                            ↑ ↑↑↑↑↑↑↑↑    ↑↑↑↑↑↑↑↑↑↑
 *
 *  第四轮分组(delta=2/2=1)：(-23,-8,0,0,3,11,20,32,-2,1,2,3,10,12,54,99)
 *    即就是一次直接插入排序：-23,-8,-2,0,0,1,2,3,3,10,11,12,20,32,54,99
 * version: 1.0
 */
public class ShellSort {

    private void sort(int[] a) {
        // 依次取某个增量，根据增量得到分组数据
        // 其中，增量序列为{len/2,(len/2)/2,....1}
        for(int delta= a.length/2; delta>0; delta/=2) {
            // 依次对每个分组进行直接插入排序
            for(int i=delta; i<a.length; i++) {
                // 将a[i]插入到所在分组正确位置上
                insertSort(a, delta, i);
            }
        }
    }

    // 将a[i]插入到所在分组的正确位置上
    // a[i]所在分组为：...a[i-2*delta], a[i-delta], a[i],a[i+delta],a[i+2*delta]...
    private void insertSort(int[] a, int delta, int i) {
        int insertI = a[i];
        int j;
        // 插入的时候按组进行插入(组内元素两两相隔delta距离)
        for(j=i-delta; j>=0; j-=delta) {
            if(a[j] > insertI) {
                a[j+delta] = a[j];
            }else {
                break;
            }
        }
        a[j+delta] = insertI;
    }


    public static void main(String[] args) {
        int[] a = {11,3,1,-2,54,0,12,-23,3,99,32,0,10,2,-8,20};
        System.out.println("sort before:");
        printArray(a);
        ShellSort shell = new ShellSort();
        shell.sort(a);
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
