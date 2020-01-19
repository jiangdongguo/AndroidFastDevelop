package com.jiangdg.algorithms.queue;

/** 队列（循环队列）
 * author : jiangdg
 * date   : 2020/1/17 16:50
 * desc   : 队列是先进先出（FIFO，First In First Out）的数据结构。在操作队列时，会使用两个指针，一个
 * 指针front指向队头，一个指针rear指向队尾，其中队头删除，队尾插入。另外，由于在从队头删除元素时不会将后面的元素向前移动，
 * 为了提高空间利用率，当rear指向最后的位置时，会将其重新指向空间的起始位置。
 *
 * version: 1.0
 */
public class Queue {
    private int[] array; // 用于存储元素的数组
    private int lenght;  // 队列存储的元素个数
    private int maxSize; // 队列空间大小
    private int front;   // 队头指针
    private int rear;    // 队尾指针

    public Queue(int maxSize) {
        this.maxSize = maxSize;
        array = new int[maxSize];
        front = 0;
        rear = -1;
    }

    /** 插入操作
     *
     * @param element 待插入队列的元素
     */
    private void insert(int element) {
        // 1. 首先判断队列是否满
        if(isFull()) {
            throw new IllegalStateException("queue is full");
        }
        // 2.判断尾部指针是否已经到达最后(maxSize-1)
        // 如果rear=maxSize，则将rear指向空间的最前面
        // 如果没有，将元素插入到队尾
        if(rear == maxSize-1) {
            rear = -1;
        }
        array[++rear] = element;
        lenght++;
    }

    /** 删除操作
     *
     * @return 被删除的元素
     */
    private int remove() {
        // 1. 判断队列是否为空
        if(isEmpty()) {
            throw new IllegalStateException("queue is empty");
        }
        // 2. 从队头删除元素
        // 如果队头到达最后位置(maxSize)，将其重新指向到空间的最前面
        int element = array[front++];
        if(front == maxSize) {
            front = 0;
        }
        lenght--;
        return element;
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
        return array[front];
    }


    /** 队列的长度
     *
     * @return 长度
     */
    private int size() {
        return lenght;
    }

    /** 判断队列是否为空
     *
     * @return true 空队列
     */
    private boolean isEmpty() {
        return lenght==0;
    }

    /** 判断队列是否为满
     *
     * @return true 已满
     */
    private boolean isFull() {
        return lenght == maxSize;
    }

    public static void main(String[] args) {
        Queue q = new Queue(10);
        q.insert(12); // 队头，删除操作
        q.insert(2);
        q.insert(-6);
        q.insert(0);
        q.insert(2);
        q.insert(99);
        q.insert(33); // 队尾，插入操作
        System.out.println("queue'size = " + q.size());          // 7
        System.out.println("queue'first element = " +q.perk());  // 12
        System.out.println("queue remove elment = " + q.remove()); // 22
        System.out.println("queue'size = " + q.size());    //6
    }
}
