/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.core.util;

import java.util.Arrays;

/**
 * 本クラスはタイプIntegerのArrayListとほぼ同じですが、32-bitの整数を限定した簡単でかつ効率良いものです。
 * <p></p>
 * 効率上の問題で、本クラスではIteratorが実装されないので、foreachが使えません。
 */
public class DkIntArrayList {
	// Max size that array can be reach to
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	// Current snapshot of element data (changed if perform insert/delete...)
	private int[] arr;

	// Current element count (changed if perform insert/delete...)
	private int size;

	public DkIntArrayList() {
		this(10);
	}

	public DkIntArrayList(int capacity) {
		if (capacity <= 0) {
			capacity = 10;
		}
		this.arr = new int[capacity];
	}

	public int size() {
		return size;
	}

	/**
	 * @return Left-most element which equals to given `value`.
	 */
	public int indexOf(int value) {
		int[] arr = this.arr;
		for (int index = 0, N = size; index < N; ++index) {
			if (arr[index] == value) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * @return Right-most element which equals to given `value`.
	 */
	public int lastIndexOf(int value) {
		int[] arr = this.arr;
		for (int index = size - 1; index >= 0; --index) {
			if (arr[index] == value) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Add new `element` to array list.
	 */
	public void add(int element) {
		if (size >= arr.length) {
			growCapacity(size);
		}
		arr[size++] = element;
	}

	/**
	 * Add (insert at) new `element` at given `index` of array list.
	 */
	public void add(int index, int value) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		final int SIZE = size;
		if (index >= SIZE) {
			index = SIZE;
		}

		if (size >= arr.length) {
			growCapacity(size);
		}
		final int[] arr = this.arr;
		// 同じ配列の場合、効率の向上でSystem.arraycopy()を使用しないべき
		for (int i = SIZE; i > index; --i) {
			arr[i] = arr[i - 1];
		}
		arr[index] = value;
		++size;
	}

	/**
	 * Add new element if not exists.
	 */
	public boolean addIfAbsence(int element) {
		if (indexOf(element) < 0) {
			add(element);
			return true;
		}
		return false;
	}

	public void addAll(int[] elements) {
		addAll(size, elements);
	}

	public void addAll(int index, int[] elements) {
		final int oldSize = size;
		final int[] arr = this.arr;
		final int more = elements.length;

		size += more;
		if (size >= arr.length) {
			growCapacity(size);
		}

		// Move elements in [index, oldSize -1] to more units (that is to range [index + more, oldSize - 1 + more])
		for (int i = oldSize - 1; i >= index; --i) {
			arr[i + more] = arr[i];
		}

		// Insert new elements to [index, index + more - 1]
		for (int i = index + more - 1; i >= index; --i) {
			arr[i] = elements[i - index];
		}
	}

	/**
	 * This copy last element to element at given `index`.
	 * Use it if you do NOT care order of elements after call this.
	 */
	public void fastRemove(int index) {
		int lastIndex = size - 1;
		if (index >= 0 && index <= lastIndex) {
			if (index < lastIndex) {
				arr[index] = arr[lastIndex];
			}
			--size;
		}
	}

	/**
	 * Fast remove left-most element which equals to given `value`.
	 * Use it if you do NOT care order of elements after call this.
	 */
	public void fastRemoveElement(int element) {
		fastRemove(indexOf(element));
	}

	/**
	 * Remove left-most element which equals to given `value`.
	 */
	public void removeElement(int value) {
		remove(indexOf(value));
	}

	/**
	 * Remove element at given `index`.
	 */
	public void remove(int index) {
		int lastIndex = size - 1;
		if (index >= 0 && index <= lastIndex) {
			int[] arr = this.arr;
			// 同じ配列の場合、効率の向上でSystem.arraycopy()を使用しないべき
			for (int i = index; i < lastIndex; ++i) {
				arr[i] = arr[i + 1];
			}
			--size;
		}
	}

	/**
	 * Clear elements by set size to 0.
	 */
	public void clear() {
		size = 0;
	}

	/**
	 * Get value of element at given `index`, throw exception if not found.
	 */
	public int get(int index) {
		if (index >= 0 && index < size) {
			return arr[index];
		}
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Set value to element at given `index`, return `false` if not found.
	 */
	public boolean set(int index, int value) {
		if (index >= 0 && index < size) {
			arr[index] = value;
			return true;
		}
		return false;
	}

	/**
	 * Check existence of element which equals to given `value`.
	 */
	public boolean contains(int value) {
		return indexOf(value) >= 0;
	}

	public void ensureCapacity(int minCapacity) {
		if (arr.length <= minCapacity) {
			growCapacity(minCapacity);
		}
	}

	/**
	 * Utility method for asc-sort.
	 */
	public void sort() {
		Arrays.sort(arr, 0, size);
	}

	/**
	 * Get current snapshot of internal array.
	 * Note that, internal array be changed usualy when we perform insert/delete... actions.
	 */
	public int[] getSnapshotOfInternalArray() {
		return arr;
	}

	// Grow array to size at least given `minCapacity`, normally it is 1.5 times of current-capacity.
	private void growCapacity(int minCapacity) {
		// Calculate new capacity
		int oldCapacity = arr.length;
		int newCapacity = oldCapacity + (oldCapacity >> 1);

		// Check over-flow (must subtract, NOT compare)
		if (newCapacity - minCapacity < 0) {
			newCapacity = minCapacity;
		}
		if (newCapacity - MAX_ARRAY_SIZE > 0) {
			newCapacity = MAX_ARRAY_SIZE;
		}

		// Make new array and then copy from old array.
		int[] newArr = new int[newCapacity];
		System.arraycopy(arr, 0, newArr, 0, size);

		this.arr = newArr;
	}
}
