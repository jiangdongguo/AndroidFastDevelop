package com.jiangdg.algorithms.array;


/** 无序数组
 * author : jiangdg
 * date   : 2020/1/18 10:47
 * desc   : 无序数组是一种顺序存储结构，存储的数据是无序的，第一个元素从下标0开始。
 *  特点：优点是插入快，但是指定位置插入也较慢；缺点是查找、删除较慢、大小固定。
 *
 * version: 1.0
 */
public class Array {

    private int[] array; // 内部数组
    private int length;  // 数组中元素的个数
    private int maxSize; // 数组长度

    public Array(int maxSize) {
        this.maxSize = maxSize;
        this.array = new int[maxSize];
        this.length = 0;
    }

    /** 判断某个元素是否存在
     *
     * @param data 待判断的元素
     * @return 元素下标，-1表示不存在
     */
    private int contain(int data) {
        if(isEmpty()) {
            throw new IllegalStateException("array is empty.");
        }
        for(int i=0; i<length; i++) {
            if(array[i] == data) {
                return i;
            }
        }
        return -1;
    }

    /** 插入一个元素
     *
     * @param data 待插入的元素
     */
    private void insert(int data) {
        if(isFull()) {
            throw new IllegalStateException("array is full.");
        }
        array[length++] = data;
    }

    /** 向某个位置插入一个元素
     *
     * @param index 指定位置下标
     * @param data 待插入的元素
     */
    private void insert(int index, int data) {
        if(isFull()) {
            throw new IllegalStateException("array is full.");
        }
        if(index < 0 || index > size())  {
            throw new IllegalArgumentException("index is wrong");
        }
        // 找到第index位置处
        // 将后面的元素向后移动一位
        for(int i=length-1; i>0; i--) {
            if(i == index) {
                array[i] = data;
                length++;
                break;
            }
            array[i+1] = array[i];
        }
    }

    /** 删除元素
     *
     * @param data 待删除的元素
     * @return 被删除的元素
     */
    private int delete(int data) {
        if(isEmpty()) {
            throw new IllegalStateException("array is empty.");
        }
        // 判断data是否存在数组中
        int index = contain(data);
        if(index == -1) {
            return -1;
        }
        // 将数组长度减1
        // 再将index后面的元素依次向前移动一位
        length--;
        for (int i= index; i<length; i++) {
            array[i] = array[i+1];
        }
        return index;
    }

    /** 根据下标获取某个元素
     *
     * @param index 要查找的元素下标
     * @return 返回的元素
     */
    private int get(int index) {
        if(isEmpty()) {
            throw new IllegalStateException("array is empty.");
        }
        if(index < 0 || index > size()) {
            throw new IllegalArgumentException("index is wrongg.");
        }
        return array[index];
    }

    private boolean isFull() {
        return length == maxSize;
    }

    private boolean isEmpty() {
        return length == 0;
    }

    private int size() {
        return length;
    }

    public static void main(String[] args) {
        Array array = new Array(6);
        array.insert(7);
        array.insert(-1);
        array.insert(5);
        array.insert(-99);
        array.insert(2);
        array.insert(34);
        printArray(array);
        int containIndex = array.contain(-99);
        System.out.println("contain index = "+ containIndex);

        int data = array.get(3);
        System.out.println("get index of data= "+ data);

        int deleteIndex = array.delete(-99);
        System.out.println("delete index = "+ deleteIndex);
        printArray(array);

        array.insert(2, 666);
        System.out.println("now array'size = "+ array.size());
        printArray(array);
    }

    private static void printArray(Array a) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<a.size();i++) {
            sb.append(a.get(i));
            sb.append(",");
        }
        System.out.println(sb.toString());
    }
}
