package com.jiangdg.algorithms.queue;

/** 优先队列
 * author : jiangdg
 * date   : 2020/1/17 16:50
 * desc   : 像普通队列一样，优先级队列有一个队头和一个队尾，并且也是从队头移除数据，从队尾插入数据，
 * 不同的是，在优先级队列中，数据项按关键字(基准点)的值排序，数据项插入的时候会按照顺序插入到合适的位置。
 * -------------------------------------------------------------------------------------------------
 * 在本例中，没有设置队头和队尾指针，而是指定以数组末端为队头，以数组首段为队尾。
 * 因为队头有移除操作，所以将队头放在数组的末端，便于移除
 * 如果放在首段，每次移除队头都需要将队列向前移动。
 * version: 1.0
 */
public class PriorityQueue {
    private int[] array; // 用于存储元素的数组
    private int length;  // 队列存储的元素个数
    private int maxSize; // 队列空间大小
    private int referencePoint;  //基准点

    public PriorityQueue(int maxSize, int referencePoint) {
        this.maxSize = maxSize;
        array = new int[maxSize];
        this.referencePoint = referencePoint;
    }

    private void insert(int data) {

    }

    private int remove() {
        // 1. 判断队列是否为空
        if(isEmpty()) {
            throw new IllegalStateException("queue is empty");
        }
        // 2. 取出队头第一个元素
        return array[--length];
    }

    /** 查看队头元素
     *
     * @return 队头元素
     */
    private int perk() {
        // 1. 判断队列是否为空
        if(isEmpty()) {
            throw new IllegalStateException("queue is empty");
        }
        // 2. 取出队头第一个元素
        return array[length-1];
    }


    /** 队列的长度
     *
     * @return 长度
     */
    private int size() {
        return length;
    }

    /** 判断队列是否为空
     *
     * @return true 空队列
     */
    private boolean isEmpty() {
        return length==0;
    }

    /** 判断队列是否为满
     *
     * @return true 已满
     */
    private boolean isFull() {
        return length == maxSize;
    }

    public static void main(String[] args) {
        PriorityQueue q = new PriorityQueue(10, 3);
        q.insert(12); // 队头，删除操作
        q.insert(2);
        q.insert(-6);
        q.insert(0);
        q.insert(2);
        q.insert(8);
        q.insert(3); // 队尾，插入操作
//        System.out.println("queue'size = " + q.size());          // 7
//        System.out.println("queue'first element = " +q.perk());  // 12
//        System.out.println("queue remove elment = " + q.remove()); // 22
//        System.out.println("queue'size = " + q.size());    //6
    }
}
