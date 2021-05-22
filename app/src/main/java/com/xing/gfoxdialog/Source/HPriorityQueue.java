package com.xing.gfoxdialog.Source;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.SortedSet;

/**
 * 优先级队列-堆
 * @param <E>
 */
public class HPriorityQueue<E> {
    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    transient Object[] queue; // non-private to simplify nested class access
    /**
     * The number of elements in the priority queue.
     */
    private int size = 0;
    /**
     * The number of times this priority queue has been
     * <i>structurally modified</i>.  See AbstractList for gory details.
     */
    transient int modCount = 0; // non-private to simplify nested class access
    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    /**
     * The comparator, or null if priority queue uses elements'
     * natural ordering.
     * 寻找对比数据，默认是空的
     */
    private final Comparator<? super E> comparator;

    /**
     * Creates a {@code PriorityQueue} with the default initial
     * capacity (11) that orders its elements according to their
     * {@linkplain Comparable natural ordering}.
     * 默认构造方法，使用默认的初始大小来构造一个优先队列，比较器comparator为空，
     * 这里要求入队的元素必须实现Comparator接口
     */
    public HPriorityQueue() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    /**
     * Creates a {@code PriorityQueue} with the specified initial
     * capacity that orders its elements according to their
     * {@linkplain Comparable natural ordering}.
     *
     * @param initialCapacity the initial capacity for this priority queue
     * @throws IllegalArgumentException if {@code initialCapacity} is less
     *                                  than 1
     *                                  <p>
     *                                  使用指定的初始大小来构造一个优先队列，比较器comparator为空，这里要求入队的元素必须实现Comparator接口
     */
    public HPriorityQueue(int initialCapacity) {
        this(initialCapacity, null);
    }

    /**
     * Creates a {@code PriorityQueue} with the default initial capacity and
     * whose elements are ordered according to the specified comparator.
     *
     * @param comparator the comparator that will be used to order this
     *                   priority queue.  If {@code null}, the {@linkplain Comparable
     *                   natural ordering} of the elements will be used.
     * @since 1.8
     */
    public HPriorityQueue(Comparator<? super E> comparator) {
        this(DEFAULT_INITIAL_CAPACITY, comparator);
    }

    /**
     * Creates a {@code PriorityQueue} with the specified initial capacity
     * that orders its elements according to the specified comparator.
     *
     * @param initialCapacity the initial capacity for this priority queue
     * @param comparator      the comparator that will be used to order this
     *                        priority queue.  If {@code null}, the {@linkplain Comparable
     *                        natural ordering} of the elements will be used.
     * @throws IllegalArgumentException if {@code initialCapacity} is
     *                                  less than 1
     *                                  使用指定的初始大小和比较器来构造一个优先队列
     */
    public HPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        // Note: This restriction of at least one is not actually needed,
        // but continues for 1.5 compatibility
        // 初始大小不允许小于1
        if (initialCapacity < 1) throw new IllegalArgumentException();
        // 使用指定初始大小创建数组
        this.queue = new Object[initialCapacity];
        // 初始化比较器
        this.comparator = comparator;
    }

    // 构造一个指定Collection集合参数的优先队列
    public HPriorityQueue(Collection<? extends E> c) {
        // 如果集合c是包含比较器Comparator的(SortedSet/PriorityQueue)，
        // 则使用集合c的比较器来初始化队列的Comparator
        if (c instanceof SortedSet<?>) {
            SortedSet<? extends E> ss = (SortedSet<? extends E>) c;
            this.comparator = (Comparator<? super E>) ss.comparator();
            initElementsFromCollection(ss);
        } else if (c instanceof PriorityQueue<?>) {
            PriorityQueue<? extends E> pq = (PriorityQueue<? extends E>) c;
            this.comparator = (Comparator<? super E>) pq.comparator();
            initFromPriorityQueue(pq);
        } else {
            // 如果集合c没有包含比较器，则默认比较器Comparator为空
            this.comparator = null;
            // 从集合c中初始化数据到队列
            initFromCollection(c);
        }
    }

    private void initElementsFromCollection(Collection<? extends E> c) {
        Object[] a = c.toArray();
        // If c.toArray incorrectly doesn't return Object[], copy it.
        if (a.getClass() != Object[].class)
            a = Arrays.copyOf(a, a.length, Object[].class);
        int len = a.length;
        if (len == 1 || this.comparator != null)
            for (int i = 0; i < len; i++)
                if (a[i] == null)
                    throw new NullPointerException();
        this.queue = a;
        this.size = a.length;
    }

    private void initFromPriorityQueue(PriorityQueue<? extends E> c) {
        if (c.getClass() == PriorityQueue.class) {
            this.queue = c.toArray();
            this.size = c.size();
        } else {
            initFromCollection(c);
        }
    }

    /**
     * Initializes queue array with elements from the given Collection.
     *
     * @param c the collection
     */
    private void initFromCollection(Collection<? extends E> c) {
        initElementsFromCollection(c);
        // 调用heapify方法重新将数据调整为一个二叉堆
        heapify();
    }

    /**
     * Establishes the heap invariant (described above) in the entire tree,
     * assuming nothing about the order of the elements prior to the call.
     * 构建了一个堆
     */
    @SuppressWarnings("unchecked")
    private void heapify() {
        //非叶子节点元素，下一层一定有值，size/2，
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, (E) queue[i]);
        }
    }

    /**
     * Inserts item x at position k, maintaining heap invariant by
     * demoting x down the tree repeatedly until it is less than or
     * equal to its children or is a leaf.
     *
     * @param k the position to fill
     * @param x the item to insert
     */
    private void siftDown(int k, E x) {
        if (comparator != null)
            siftDownUsingComparator(k, x);
        else
            siftDownComparable(k, x);
    }

    @SuppressWarnings("unchecked")
    private void siftDownUsingComparator(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = queue[child];
            int right = child + 1;
            if (right < size &&
                    comparator.compare((E) c, (E) queue[right]) > 0)
                c = queue[child = right];
            if (comparator.compare(x, (E) c) <= 0)
                break;
            queue[k] = c;
            k = child;
        }
        queue[k] = x;
    }

    @SuppressWarnings("unchecked")
    private void siftDownComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1;        // loop while a non-leaf
        while (k < half) {
            int child = (k << 1) + 1; // assume left child is least
            Object c = queue[child];
            int right = child + 1;
            if (right < size &&
                    ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0)
                c = queue[child = right];
            if (key.compareTo((E) c) <= 0)
                break;
            queue[k] = c;
            k = child;
        }
        queue[k] = key;
    }

    /**
     * 添加一个元素
     *
     * @param e
     * @return
     */
    public boolean add(E e) {
        return offer(e);
    }

    /**
     * Inserts the specified element into this priority queue.
     *
     * @throws ClassCastException   if the specified element cannot be
     *                              compared with elements currently in this priority queue
     *                              according to the priority queue's ordering
     * @throws NullPointerException if the specified element is null
     */
    public boolean offer(E e) {
        //如果元素e为空，则抛出空指针异常
        if (e == null) throw new NullPointerException();
        modCount++; // 修改版本+1，无视
        int i = size;  // 记录当前队列中元素的个数
        // 如果当前元素个数大于等于队列底层数组的长度，则进行扩容
        if (i >= queue.length) {
            grow(i + 1);
        }
        size = i + 1; // 元素个数+1
        // 如果队列中没有元素，则将元素e直接添加至根（数组小标0的位置）
        // 否则调用siftUp方法，将元素添加到尾部，进行上移判断
        if (i == 0)
            queue[0] = e;
        else
            siftUp(i, e);
        return true;
    }

    /**
     * Inserts item x at position k, maintaining heap invariant by
     * promoting x up the tree until it is greater than or equal to
     * its parent, or is the root.
     * <p>
     * To simplify and speed up coercions and comparisons. the
     * Comparable and Comparator versions are separated into different
     * methods that are otherwise identical. (Similarly for siftDown.)
     * 上移，x表示新插入元素，k表示新插入元素在数组的位置
     *
     * @param k the position to fill
     * @param x the item to insert
     */
    private void siftUp(int k, E x) {
        if (comparator != null)
            siftUpUsingComparator(k, x);
        else
            siftUpComparable(k, x);
    }

    @SuppressWarnings("unchecked")
    private void siftUpComparable(int k, E x) {
        // 比较器comparator为空，需要插入的元素实现Comparable接口，用于比较大小
        Comparable<? super E> key = (Comparable<? super E>) x;
        // k>0表示判断k不是根的情况下，也就是元素x有父节点
        while (k > 0) {
            // 计算元素x的父节点位置[(n-1)/2]
            int parent = (k - 1) >>> 1;
            // 取出x的父亲e
            Object e = queue[parent];
            // 如果新增的元素k比其父亲e大，则不需要"上移"，跳出循环结束
            if (key.compareTo((E) e) >= 0)
                break;
            // x比父亲小，则需要进行"上移"
            // 交换元素x和父亲e的位置
            queue[k] = e;
            // 将新插入元素的位置k指向父亲的位置，进行下一层循环
            k = parent;
        }
        // 找到新增元素x的合适位置k之后进行赋值
        queue[k] = key;
    }

    //方法同上
    @SuppressWarnings("unchecked")
    private void siftUpUsingComparator(int k, E x) {
        while (k > 0) {//根节点=0
            int parent = (k - 1) >>> 1;//找到父节点(n-1)/2
            Object e = queue[parent];
            if (comparator.compare(x, (E) e) >= 0)
                break;
            //不断地往上滚
            queue[k] = e;
            k = parent;
        }
        queue[k] = x;
    }

    /**
     * Increases the capacity of the array.
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        int oldCapacity = queue.length;
        // Double size if small; else grow by 50%
        int newCapacity = oldCapacity + ((oldCapacity < 64) ?
                (oldCapacity + 2) :
                (oldCapacity >> 1));
        // overflow-conscious code
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        queue = Arrays.copyOf(queue, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    private E removeAt(int i) {
        // assert i >= 0 && i < size;
        modCount++;
        int s = --size;
        if (s == i) // removed last element
            queue[i] = null;
        else {
            E moved = (E) queue[s];
            queue[s] = null;
            siftDown(i, moved);
            if (queue[i] == moved) {
                siftUp(i, moved);
                if (queue[i] != moved)
                    return moved;
            }
        }
        return null;
    }

}
