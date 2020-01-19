package com.jiangdg.algorithms.array;

/** 有序数组
 * author : jiangdg
 * date   : 2020/1/18 10:46
 * desc   : 所谓的有序数组就是指数组中的元素是按一定规则排列的，其好处就是在根据元素值查找时可以是使用二分查找，
 *     查找效率要比无序数组高很多，在数据量很大时更加明显。当然缺点也显而易见，当插入一个元素时，首先要判断该元素
 *     应该插入的下标，然后对该下标之后的所有元素后移一位，才能进行插入，这无疑增加了很大的开销。
 *
 * 特点： 有序数组适用于查找频繁(使用二分法)，而插入、删除操作较少的情况
 *
 * version: 1.0
 */
public class OrderArray {
    private int[] array;  // 内部数组
    private int length;   // 数组中元素的个数
    private int maxSize;  // 数组长度

    public OrderArray(int maxSize) {
        this.maxSize = maxSize;
        this.length = 0;
        array = new int[maxSize];
    }

    /** 查找某个元素。
     *      由于数据有序且顺序存储，因此使用二分法(折半)查找。
     *
     * @param data 待查找元素
     * @return 元素下标，-1表示查找失败
     */
    private int contain(int data) {
        if(isEmpty()) {
            return -1;
        }
        int left = 0;
        int right = length-1;
        while (left <= right) {
            int mid = (right + left) / 2;
            if(array[mid] == data) {
                return mid;
            }
            if(array[mid] > data) {
                // 中间值大于目标值，左边继续折半查找
                right = mid - 1;
            } else if(array[mid] < data) {
                // 中间值小于目标值，右边继续折半查找
                left = mid + 1;
            }
        }
        return -1;
    }

    /** 插入元素
     *
     * @param data 待插入元素
     * @return 元素下标，-1表示插入失败
     */
    private int insert(int data) {
        if(isFull()) {
            return -1;
        }
        // 1. 获取插入的位置
        int location = 0;
        for(;location<length; location++) {
            if(array[location] > data)
                break;
        }
        // 2. 将后面的元素依次向后移动一位
        for(int i=length;i>location;i--) {
            array[i] = array[i-1];
        }
        // 3. 在元素插入到location处
        array[location] = data;
        length++;
        return location;
    }

    /** 删除一个元素
     *
     * @param data 待删除的元素
     * @return 被删除的元素下标
     */
    private int delete(int data) {
        if(isEmpty()) {
            return -1;
        }
        // 1. 找到待删除元素所在下标
        int location = contain(data);
        if(location == -1)
            return -1;
        // 2. 将index之后的元素依次向前移动一位
        for (int i=location; i<length; i++) {
            array[i] = array[i+1];
        }
        length--;
        return location;
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
        OrderArray orderArray = new OrderArray(10);
        orderArray.insert(14);
        orderArray.insert(31);
        orderArray.insert(-7);
        orderArray.insert(65);
        orderArray.insert(-12);
        orderArray.insert(26);
        orderArray.insert(5);
        printArray(orderArray);
        System.out.println("element's index = " + orderArray.contain(31));
        System.out.println("delete element,index = " + orderArray.delete(5));
        printArray(orderArray);
    }

    private static void printArray(OrderArray a) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<a.size();i++) {
            sb.append(a.get(i));
            sb.append(",");
        }
        System.out.println(sb.toString());
    }
}
