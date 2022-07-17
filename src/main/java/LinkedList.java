import java.util.*;

public class LinkedList<T> implements List<T> {

    private Item<T> firstInList = null;

    private Item<T> lastInList = null;

    private int size;


    private static class Item<T> {

        private T element;

        private Item<T> nextItem;

        private Item<T> prevItem;

        Item(final T element, final Item<T> prevItem, final Item<T> nextItem) {
            this.element = element;
            this.nextItem = nextItem;
            this.prevItem = prevItem;
        }

        public Item(T element) {
            this.element = element;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean contains(final Object o) {
        for (Object item : this) {
            if (Objects.equals(item, o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new ElementsIterator(0);
    }

    @Override
    public Object[] toArray() {
        final Object[] newM = new Object[this.size()];
        int i = 0;

        for (Object element : this) {
            newM[i++] = element;
        }
        return newM;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (this.size() > a.length) {
            return (T1[]) Arrays.copyOf(this.toArray(), size, a.getClass());
        }
        System.arraycopy(this.toArray(), 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean add(final T newElement) {
        Item<T> newItem = new Item<>(newElement);
        if (firstInList == null) {
            firstInList = newItem;
        } else {
            newItem.prevItem = lastInList;
            lastInList.nextItem = newItem;
        }
        lastInList = newItem;
        size++;
        return true;
    }


    @Override
    public void add(final int index, final T element) {
        Item<T> newItem = new Item<>(element);
        if (firstInList == null) {
            firstInList = newItem;
        }
        if (index == 0) {
            newItem.nextItem = firstInList;
            firstInList.prevItem = newItem;
            firstInList = newItem;
        } else if (index == size - 1) {
            lastInList.nextItem = newItem;
            newItem.prevItem = lastInList;
            lastInList = newItem;
        } else {
            Item<T> current = firstInList;
            for (int i = 0; i < index; i++) {
                current = current.nextItem;
            }
            newItem.nextItem = current.nextItem;
            current.nextItem.prevItem = newItem;
            current.nextItem = newItem;
            newItem.prevItem = current;
        }
        size++;
    }

    @Override
    public boolean remove(final Object o) {
        if (firstInList.element == o) {
            firstInList = firstInList.nextItem;
        } else if (lastInList == o) {
            lastInList = lastInList.prevItem;
        } else {
            Item<T> current = firstInList;
            for (int i = 0; i < size - 1; i++) {
                current = current.nextItem;
                if (current.element == o) {
                    current.prevItem.nextItem = current.nextItem;
                }
            }
        }
        size--;
        return true;
    }

    @Override
    public T remove(final int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        T oldElement;
        if (index == 0) {
            oldElement = firstInList.element;
            firstInList = firstInList.nextItem;

        } else if (index == size - 1) {
            oldElement = lastInList.element;
            lastInList = lastInList.prevItem;
            ;
        } else {
            Item<T> current = firstInList;
            for (int i = 0; i < index; i++) {
                current = current.nextItem;
            }
            oldElement = current.element;
            current.nextItem.prevItem = current.prevItem;
            current.prevItem.nextItem = current.nextItem;
        }
        size--;
        return oldElement;
    }

    private void remove(final Item<T> current) {

    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        for (final Object item : c) {
            if (!this.contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        for (final T item : c) {
            add(item);
        }
        return true;
    }

    @Override
    public boolean addAll(final int index, final Collection elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        for (final Object item : c) {
            if (this.contains(item)) {
                remove(item);
            }
        }
        return true;
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        this.removeIf(item -> !c.contains(item));
        return true;
    }

    @Override
    public void clear() {
        Item<T> next;
        for (Item<T> x = this.firstInList; x != null; x = next) {
            next = x.nextItem;
            x.element = null;
            x.nextItem = null;
            x.prevItem = null;
        }

        this.firstInList = this.lastInList = null;
        this.size = 0;

    }

    @Override
    public List<T> subList(final int start, final int end) {
        return null;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ElementsIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return new ElementsIterator(index);
    }

    @Override
    public int lastIndexOf(final Object target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(final Object o) {
        int count = 0;
        for (Item<T> target = firstInList; target != null; target = target.nextItem, count++) {
            if (Objects.equals(target.element, o)) {
                return count;
            }
        }
        return -1;
    }

    @Override
    public T set(final int index, final T element) {
        Item<T> target = firstInList;
        for (int count = 0; count != index; count++) {
            target = target.nextItem;
        }
        T oldElement = target.element;
        target.element = element;
        return oldElement;
    }

    @Override
    public T get(final int index) {
        Item<T> item = getItemByIndex(index);
        if (item == null) {
            return null;
        }
        return item.element;
    }

    private Item<T> getItemByIndex(final int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        int count = 0;
        for (Item<T> target = firstInList; target != null; target = target.nextItem, count++) {
            if (Objects.equals(count, index)) {
                return target;
            }
        }
        return null;
    }

    private class ElementsIterator implements ListIterator<T> {

        private Item<T> next;

        private Item<T> lastReturned;

        private int cursor;
        private boolean nextOrPreviousWereCalled;

        ElementsIterator() {
            next = firstInList;
        }

        ElementsIterator(final int index) {
            cursor = index;
            next = getItemByIndex(index);
        }

        @Override
        public boolean hasNext() {
            return cursor < LinkedList.this.size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            ++cursor;
            this.lastReturned = this.next;
            this.next = this.next.nextItem;
            nextOrPreviousWereCalled = true;
            return this.lastReturned.element;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            --cursor;
            next = getItemByIndex(cursor);
            lastReturned = next;
            nextOrPreviousWereCalled = true;
            if (lastReturned == null) {
                return null;
            }
            return lastReturned.element;

        }

        @Override
        public void add(final T element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(final T element) {
            if (!nextOrPreviousWereCalled) {
                throw new IllegalStateException();
            }
            lastReturned.element = element;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public int nextIndex() {
            if (LinkedList.this.size == 0) {
                return 0;
            }

            return cursor;
        }

        @Override
        public void remove() {
            if (!nextOrPreviousWereCalled) {
                throw new IllegalStateException();
            }
            LinkedList.this.remove(lastReturned.element);
            nextOrPreviousWereCalled = false;
            cursor--;
        }
    }

}
