package com.xing.gfoxdialog.Source;

import java.util.Collection;
import java.util.NoSuchElementException;

public class HArrayDeque<E> {
    //transient不可序列化
    transient Object[] elements; //元素
    transient int head;//头索引
    transient int tail;//尾元素的下一位的索引

    /**
     * Constructs an empty array deque with an initial capacity
     * sufficient to hold 16 elements.
     * 构造一个初始容量为16的空数组
     */
    public HArrayDeque() {
        elements = new Object[16];
    }

    /**
     * Constructs an empty array deque with an initial capacity
     * sufficient to hold the specified number of elements.
     * 构造一个指定数组容量的空数组
     *
     * @param numElements lower bound on initial capacity of the deque
     *                    该数组容量的最小值
     */
    public HArrayDeque(int numElements) {
        allocateElements(numElements);
    }

    /**
     * Constructs a deque containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.  (The first element returned by the collection's
     * iterator becomes the first element, or <i>front</i> of the
     * deque.)
     * 集合转化为队列数组
     *
     * @param c the collection whose elements are to be placed into the deque
     * @throws NullPointerException if the specified collection is null
     */
    public HArrayDeque(Collection<? extends E> c) {
        allocateElements(c.size());
//        addAll(c);
    }

    /**
     * Allocates empty array to hold the given number of elements.
     * 分配空数组来容纳给定数量的元素。
     *
     * @param numElements the number of elements to hold
     */
    private void allocateElements(int numElements) {
        elements = new Object[calculateSize(numElements)];
    }

    /**
     * The minimum capacity that we'll use for a newly created deque.
     * Must be a power of 2.源自于C语言power，进行扩容。
     * 最小容量，必须为2的幂次方，方便移位计算
     */
    private static final int MIN_INITIAL_CAPACITY = 8;

    // ******  Array allocation and resizing utilities ******

    /**
     * 计算扩容大小
     *
     * @param numElements 元素数量
     * @return 大小
     * `>>>`是无符号右移操作，`|`是位或操作，经过五次右移和位或操作可以保证得到大小为2^k-1的数。看一下这个例子：
     * 0 0 0 0 1 ? ? ? ? ?     //n
     * 0 0 0 0 1 1 ? ? ? ?     //n |= n >>> 1;
     * 0 0 0 0 1 1 1 1 ? ?     //n |= n >>> 2;
     * 0 0 0 0 1 1 1 1 1 1     //n |= n >>> 4;
     */
    private static int calculateSize(int numElements) {
        int initialCapacity = MIN_INITIAL_CAPACITY;
        // Find the best power of two to hold elements.
        // Tests "<=" because arrays aren't kept full.
        // 大于8才需要扩容
        // 计算容量，这段代码的逻辑是算出大于numElements的最接近的2的n次方且不小于这个数字本身。
        // 比如3算出来是8，9算出来是16。
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>> 1);
            initialCapacity |= (initialCapacity >>> 2);
            initialCapacity |= (initialCapacity >>> 4);
            initialCapacity |= (initialCapacity >>> 8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            if (initialCapacity < 0)   // Too many elements, must back off
                initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
        }
        return initialCapacity;
    }

    public void addFirst(E e) {
        if (e == null)
            throw new NullPointerException();
        elements[head = (head - 1) & (elements.length - 1)] = e;
        if (head == tail)
            doubleCapacity();
    }

    /**
     * Inserts the specified element at the end of this deque.
     * 添加元素
     * <p>This method is equivalent to {@link #add}.
     *
     * @param e 要添加的元素
     * @throws NullPointerException if the specified element is null
     */
    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        elements[tail] = e;
        //tail + 1是让数组循环到尾部可以从头开始
        if ((tail = (tail + 1) & (elements.length - 1)) == head) {
            doubleCapacity();
        }
    }

    /**
     * 扩容
     * Doubles the capacity of this deque.  Call only when full, i.e.,
     * when head and tail have wrapped around to become equal.
     */
    private void doubleCapacity() {
        //两个指针是一样的时候需要扩容
        assert head == tail;
        int p = head;
        int n = elements.length;
        //头部索引位置到末端（length-1）的元素数
        int r = n - p; // number of elements to the right of p
        int newCapacity = n << 1;
        if (newCapacity < 0) throw new IllegalStateException("Sorry, deque too big");
        //开始新空间
        Object[] a = new Object[newCapacity];
        //复制头部索引到数组末端的元素到数组头部
        System.arraycopy(elements, p, a, 0, r);
        //复制其余元素
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        //头部索引处理
        head = 0;
        tail = n;
    }

    /**
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E removeFirst() {
        E x = pollFirst();
        if (x == null)
            throw new NoSuchElementException();
        return x;
    }

    /**
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E removeLast() {
        E x = pollLast();
        if (x == null)
            throw new NoSuchElementException();
        return x;
    }

    public E pollFirst() {
        int h = head;
        @SuppressWarnings("unchecked")
        E result = (E) elements[h];
        // Element is null if deque empty
        if (result == null) return null;
        elements[h] = null;     // Must null out slot
        head = (h + 1) & (elements.length - 1);
        return result;
    }

    public E pollLast() {
        int t = (tail - 1) & (elements.length - 1);
        @SuppressWarnings("unchecked")
        E result = (E) elements[t];
        if (result == null) return null;
        elements[t] = null;
        tail = t;
        return result;
    }

    /**
     * Returns {@code true} if this deque contains no elements.
     *
     * @return {@code true} if this deque contains no elements
     */
    public boolean isEmpty() {
        return head == tail;
    }
}
