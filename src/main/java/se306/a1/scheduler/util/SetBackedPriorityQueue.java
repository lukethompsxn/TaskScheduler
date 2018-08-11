package se306.a1.scheduler.util;

import java.util.*;

/**
 * Priority queue class that does not permit duplicates.
 * @param <T> the type of elements to store in the queue
 */
public class SetBackedPriorityQueue<T> implements Queue<T> {

    private final Set<T> set;
    private final Queue<T> queue;

    public SetBackedPriorityQueue() {
        set = new HashSet<>();
        queue = new PriorityQueue<>();
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return queue.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean add(T t) {
        if (set.add(t)) {
            queue.add(t);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (set.remove(o)) {
            queue.remove(o);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(item -> {
            if (!set.contains(item)) {
                set.add(item);
                queue.add(item);
            }
        });
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        set.removeAll(c);
        queue.removeAll(c);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        set.retainAll(c);
        queue.retainAll(c);
        return true;
    }

    @Override
    public void clear() {
        set.clear();
        queue.clear();
    }

    @Override
    public boolean offer(T t) {
        if (queue.offer(t)) {
            set.add(t);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public T remove() {
        T o = queue.remove();
        set.remove(o);
        return o;
    }

    @Override
    public T poll() {
        T o = queue.poll();
        set.remove(o);
        return o;
    }

    @Override
    public T element() {
        T o = queue.element();
        set.remove(o);
        return o;
    }

    @Override
    public T peek() {
        return queue.peek();
    }
}
