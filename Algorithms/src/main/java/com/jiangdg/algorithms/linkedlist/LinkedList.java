package com.jiangdg.algorithms.linkedlist;


/** 单向链表。注意：链表元素下标从1开始，与数组不同
 * author : jiangdg
 * date   : 2020/1/16 21:15
 * desc   : 创建链表
 *          插入一个元素
 *          删除一个元素
 *          查找一个元素
 * version: 1.0
 */
public class LinkedList {
    // 头结点
    private Node headNode;

    /** 链表的节点定义
     *       节点对象
     *       节点引用
     */
    class Node {
        int data;   // 节点对象
        Node next;  // 节点引用，指向下一个节点

        public Node(int data) {
            this.data = data;
        }
    }

    /** 创建一个单向链表
     *
     * @param array
     * @return
     */
    private boolean createLinkedList(int[] array) {
        if(array==null|| array.length==0) {
            return false;
        }
        for (int i=0; i<array.length; i++) {
            addNode(array[i]);
        }
        return true;
    }

    /** 插入一个节点到链表末尾(已知头结点情况)
     *
     * @param data 要添加的对象数据
     */
    private void addNode(int data) {
        // 1. 创建头结点，如果不存在的话
        Node node = new Node(data);
        if(headNode == null) {
            headNode = node;
            return;
        }
        // 2. 从头结点开始遍历，插入到链表末尾
        Node tmp = headNode;
        while (tmp.next != null) {
            tmp = tmp.next;
        }
        tmp.next = node;
    }

    /**  将一个元素插入到index位置
     *
     * @param index 要插入的下标
     * @param data 被插入的元素
     */
    private void addNode(int index, int data) {
        if(index<0 || index>length()) {
            return;
        }
        Node newNode = new Node(data);
        // 1. 如果Index=1,替换头结点
        // 将新结点赋值给头结点，并将新节点的指针指向旧的头结点
        if(index == 1) {
            Node oldNode = headNode;
            headNode = newNode;
            newNode.next = oldNode;
            return;
        }
        // 2. 使用“前后指针”找到Index位置，完成插入操作
        int i = 2;  // 后指针下标位置
        Node preNode = headNode;
        Node curNode = headNode.next;
        while (curNode != null) {
            // 执行curNode节点处插入操作
            if(i == index) {
                preNode.next = newNode;
                newNode.next = curNode;
                return;
            }
            // 由左向右移动前后指针
            preNode = curNode;
            curNode = curNode.next;
            i++;
        }
    }

    /** 删除链表的第index个元素(已经头结点情况)
     *
     * @param index 要删除的元素下标
     */
    private void deleteNode(int index) {
        if(index < 0 || index > length()) return;
        // 1. 如果删除第一个元素，直接将第二个元素指定为头结点
        if(index == 1) {
            headNode = headNode.next;
            return;
        }

        // 2. 使用“前后指针”遍历链表找到第index元素进行删除
        // 由于第1个元素为头结点，已经特殊特例了，因此从第2元素开始比较
        int i = 2;  // 表示后指针位置
        Node preNode = headNode;
        Node curNode = preNode.next;
        while (curNode != null) {
            // 删除第Index个元素(节点)
            // 将preNode的指针指向下一个节点的指针，指向的节点
            if(i == index) {
                preNode.next = curNode.next;
                return;
            }
            // 还没有到第index元素位置
            preNode = curNode;
            curNode = curNode.next;
            i++;
        }
    }

    /** 删除节点(未知头结点情形)
     *
     * @param node 待删除节点
     */
    private void deleteNode(Node node) {
        if(node == null) {
            return;
        }
        // 将node的下一个节点成员，赋值给node
        Node nextNode = node.next;
        node.data = nextNode.data;
        node.next = nextNode.next;
    }

    /** 查找元素
     *
     * @param index 元素下标
     * @return null表示查找失败
     */
    private Node findNode(int index) {
        if(index < 0 || index > length() || headNode == null) {
            return null;
        }
        // 开始由左向右依次遍历链表(起始)
        int i = 1;
        Node tmp = headNode;
        while (tmp != null) {
            if(i == index) {
                return tmp;
            }
            tmp = tmp.next;
            i++;
        }
        return null;
    }

    /** 获取链表长度
     *
     * @return 长度
     */
    private int length() {
        if(headNode==null) return 0;
        Node tmp = headNode;
        int len = 0;
        while (tmp != null) {
            len++;
            tmp = tmp.next;
        }
        return len;
    }

    /**
     *  打印链表
     */
    private void printLinkedList() {
        StringBuilder sb = new StringBuilder();

        Node tmp = headNode;
        while (tmp != null) {
            sb.append(tmp.data);
            sb.append(",");
            tmp = tmp.next;
        }

        System.out.println(sb.toString());
    }


    public static void  main(String[] args) {
        int[] a = {2,4,7,-1,88,5,0,-34,99,87,-22,99};
        LinkedList linkedList = new LinkedList();
        linkedList.createLinkedList(a);
        linkedList.printLinkedList();
        System.out.println("len = "+ linkedList.length());
        // 删除元素
        linkedList.deleteNode(3);
        linkedList.printLinkedList();
        System.out.println("len = "+ linkedList.length());
        // 插入元素
        linkedList.addNode(3, 6666);
        linkedList.printLinkedList();
        System.out.println("len = "+ linkedList.length());
        // 查找元素
        Node node = linkedList.findNode(1);
        System.out.println("find data = "+ (node!=null?node.data:"err"));
        // 删除指定节点
        linkedList.deleteNode(node);
        linkedList.printLinkedList();
    }
}
