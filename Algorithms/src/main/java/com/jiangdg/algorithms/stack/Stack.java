package com.jiangdg.algorithms.stack;

/** 栈
 * author : jiangdg
 * date   : 2020/1/18 10:29
 * desc   : 栈是一种先进后出(FILO，First Input Last Output)的数据结构，删除、插入操作均是在"栈顶"完成的。
 *
 * version: 1.0
 */
public class Stack {
    private int[] array;   // 存储数据数组
    private int top;       // 栈顶指针（实质为数组下标）
    private int maxSize;   // 栈大小

    public Stack(int maxSize) {
        this.maxSize = maxSize;
        this.array = new int[maxSize];
        top = -1;  // 初始化栈，栈内无元素
    }

    /** 入栈操作
     *
     * @param data 待入栈的元素
     */
    private void push(int data) {
        if(isFull()) {
            System.out.println("stack is full.");
            return;
        }
        array[++top] = data;
    }

    /** 出栈操作
     *
     * @return 被出栈元素
     */
    private int pop() {
        if(isEmpty()) {
            System.out.println("stack is empty.");
            return -1;
        }
        return array[top--];
    }

    /** 返回栈顶元素
     *
     * @return 栈顶元素
     */
    private int peek() {
        if(isEmpty()) {
            System.out.println("stack is empty.");
            return -1;
        }
        return array[top];
    }

    /** 判断是否为空栈
     *
     * @return true 栈空
     */
    private boolean isEmpty() {
        return top == -1;
    }

    /** 判断是否栈满
     *
     * @return true 栈满
     */
    private boolean isFull() {
        return top == maxSize-1;
    }

    public static void main(String[] args) {
        Stack stack = new Stack(10);
        // 入栈，0,1,2,3,4,5,6,7,8,9
        for(int i= 0; i<10; i++){
            stack.push(i);
        }
        // 先打印栈顶，再出栈
        // 9,8,7,6,5,4,3,2,1,0
        for (int i=0; i<10; i++) {
            System.out.println(stack.peek());
            stack.pop();
        }
    }
}
