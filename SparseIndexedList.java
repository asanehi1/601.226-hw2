package hw2;

import exceptions.IndexException;
import exceptions.LengthException;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * An implementation of an IndexedList designed for cases where
 * only a few positions have distinct values from the initial value.
 *
 * @param <T> Element type.
 */
public class SparseIndexedList<T> implements IndexedList<T> {
  // empty list for now, head = null default
  private Node<T> head;
  private final int length;
  private final T defaultV;

  private static class Node<E> {
    E value;
    int index;
    Node<E> next;

    Node(int index, E value) {
      this.index = index;
      this.value = value;
    }
  }


  /**
   * Constructs a new SparseIndexedList of length size
   * with default value of defaultValue.
   *
   * @param size Length of list, expected: size > 0.
   * @param defaultValue Default value to store in each slot.
   * @throws LengthException if size <= 0.
   */
  public SparseIndexedList(int size, T defaultValue) throws LengthException {
    if (size <= 0) {
      throw new LengthException();
    }
    length = size;
    defaultV = defaultValue;
  }

  private boolean isNotValid(int index) {
    return index < 0 || index >= length();
  }

  private Node<T> find(int index) throws IndexException {
    if (isNotValid(index)) {
      throw new IndexException();
    }
    // check entire list
    Node<T> target = head;
    for (int counter = 0; counter < length; counter++) {
      if (target == null) {
        return null;
      } else if (index == target.index) {
        return target;
      }
      target = target.next;
    }
    return null;
  }

  private void append(int index, T t) {
    // if empty list
    if (head == null) {
      head = new Node<>(index, t);
    } else {
      // if not empty list
      Node<T> tail = head;
      while (tail.next != null) {
        tail = tail.next;
      }
      tail.next = new Node<>(index, t);
    }
  }


  private void delete(int index) {
    // index must be valid (should already be checked in other functions)
    Node<T> node = find(index);
    if (node != null) {
      Node<T> temp = head;
      // in the case that the to be deleted index is head
      if (index == head.index) {
        head = temp.next;
      } else {
        for (int counter = 0; counter < length; counter++) {
          while (index != node.index) {
            temp = temp.next;
          }
        }
        // replace node with next node
        if (!(temp.next == null || temp.next.next == null)) {
          temp.next = temp.next.next;
        }
      }
    }
  }

  @Override
  public int length() {
    return length;
  }

  @Override
  public T get(int index) throws IndexException {
    if (isNotValid(index)) {
      throw new IndexException();
    }
    // return value at index
    Node<T> node = find(index);
    if (node != null) {
      return node.value;
    }
    // Since sparse list, if index isn't in list, it's just default value
    return defaultV;
  }

  @Override
  public void put(int index, T value) throws IndexException {
    if (isNotValid(index)) {
      throw new IndexException();
    }
    Node<T> node = find(index);
    // if T is defaultValue, remove node with that T s.t. null
    if (value == defaultV) {
      if (node != null) {
        // delete node
        delete(index);
      }
      // else do nothing since it's already null + doesn't exist
    }
    // change value even tho index already exists
    if (node != null) {
      node.value = value;
    }
    // if there's no node for that index, add it
    append(index, value);

  }

  @Override
  public Iterator<T> iterator() {
    return new SparseIndexedListIterator();
  }

  private class SparseIndexedListIterator implements Iterator<T> {
    private int nextIndex;

    SparseIndexedListIterator() {
      nextIndex = 0;
    }

    @Override
    public boolean hasNext() {
      return nextIndex < length;
    }

    @Override
    public T next() throws NoSuchElementException {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      T t = get(nextIndex);
      nextIndex += 1;
      return t;
    }
  }
}
