/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j;

import java.lang.reflect.Array;

/**
 * This helps growing (append, insert...) an primitive and generic type array.
 */
class MyArrayHelper {
	/**
	 * From current capacity, this calculates next capacity with perfomance considered.
	 */
	static int calcNextCapacity(int curCapacity, int minCapacity, int maxCapacity) {
		int newCapacity = curCapacity + (curCapacity >> 1);

		// Check over-flow (must subtract, NOT compare)
		if (newCapacity - minCapacity < 0) {
			newCapacity = minCapacity;
		}
		if (newCapacity - maxCapacity > 0) {
			newCapacity = maxCapacity;
		}

		return newCapacity;
	}

	/**
	 * Given the current size of an array, returns an ideal size to which the array should grow.
	 * This is typically double the given size, but should not be relied upon to do so in the
	 * future.
	 */
	static int growSize(int currentSize) {
		return currentSize <= 4 ? 8 : currentSize << 1;
	}

	/**
	 * Appends an element to the end of the array, growing the array if there is no more room.
	 *
	 * @param array       The array to which to append the element. This must NOT be null.
	 * @param currentSize The number of elements in the array. This must be in [0, array.length].
	 * @param element     The element to append.
	 * @return the array to which the element was appended. This may be different than the given array.
	 */
	@SuppressWarnings("unchecked")
	static <T> T[] append(T[] array, int currentSize, T element) {
		if (currentSize + 1 > array.length) {
			T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), growSize(currentSize));
			System.arraycopy(array, 0, newArray, 0, currentSize);
			array = newArray;
		}
		array[currentSize] = element;
		return array;
	}

	/**
	 * Primitive boolean version of append.
	 */
	static boolean[] append(boolean[] array, int currentSize, boolean element) {
		if (currentSize + 1 > array.length) {
			boolean[] newArray = new boolean[growSize(currentSize)];
			System.arraycopy(array, 0, newArray, 0, currentSize);
			array = newArray;
		}
		array[currentSize] = element;
		return array;
	}

	/**
	 * Primitive int version of append.
	 */
	static int[] append(int[] array, int currentSize, int element) {
		if (currentSize + 1 > array.length) {
			int[] newArray = new int[growSize(currentSize)];
			System.arraycopy(array, 0, newArray, 0, currentSize);
			array = newArray;
		}
		array[currentSize] = element;
		return array;
	}

	/**
	 * Primitive long version of append.
	 */
	static long[] append(long[] array, int currentSize, long element) {
		if (currentSize + 1 > array.length) {
			long[] newArray = new long[growSize(currentSize)];
			System.arraycopy(array, 0, newArray, 0, currentSize);
			array = newArray;
		}
		array[currentSize] = element;
		return array;
	}

	/**
	 * Primitive float version of append.
	 */
	static float[] append(float[] array, int currentSize, float element) {
		if (currentSize + 1 > array.length) {
			float[] newArray = new float[growSize(currentSize)];
			System.arraycopy(array, 0, newArray, 0, currentSize);
			array = newArray;
		}
		array[currentSize] = element;
		return array;
	}

	/**
	 * Primitive double version of append.
	 */
	static double[] append(double[] array, int currentSize, double element) {
		if (currentSize + 1 > array.length) {
			double[] newArray = new double[growSize(currentSize)];
			System.arraycopy(array, 0, newArray, 0, currentSize);
			array = newArray;
		}
		array[currentSize] = element;
		return array;
	}

	/**
	 * Inserts an element into the array at the specified index, growing the array if there is no
	 * more room.
	 *
	 * @param array       The array to which to append the element. Must NOT be null.
	 * @param currentSize The number of elements in the array. This must be in [0, array.length].
	 * @param element     The element to insert.
	 * @return the array to which the element was appended. This may be different than the given array.
	 */
	@SuppressWarnings("unchecked")
	static <T> T[] insert(T[] array, int currentSize, int index, T element) {
		if (currentSize + 1 <= array.length) {
			System.arraycopy(array, index, array, index + 1, currentSize - index);
			array[index] = element;
			return array;
		}
		T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), growSize(currentSize));
		System.arraycopy(array, 0, newArray, 0, index);
		newArray[index] = element;
		System.arraycopy(array, index, newArray, index + 1, array.length - index);
		return newArray;
	}

	/**
	 * Primitive boolean version of insert.
	 */
	static boolean[] insert(boolean[] array, int currentSize, int index, boolean element) {
		if (currentSize + 1 <= array.length) {
			System.arraycopy(array, index, array, index + 1, currentSize - index);
			array[index] = element;
			return array;
		}
		boolean[] newArray = new boolean[growSize(currentSize)];
		System.arraycopy(array, 0, newArray, 0, index);
		newArray[index] = element;
		System.arraycopy(array, index, newArray, index + 1, array.length - index);
		return newArray;
	}

	/**
	 * Primitive int version of insert.
	 */
	static int[] insert(int[] array, int currentSize, int index, int element) {
		if (currentSize + 1 <= array.length) {
			System.arraycopy(array, index, array, index + 1, currentSize - index);
			array[index] = element;
			return array;
		}
		int[] newArray = new int[growSize(currentSize)];
		System.arraycopy(array, 0, newArray, 0, index);
		newArray[index] = element;
		System.arraycopy(array, index, newArray, index + 1, array.length - index);
		return newArray;
	}

	/**
	 * Primitive long version of insert.
	 */
	static long[] insert(long[] array, int currentSize, int index, long element) {
		if (currentSize + 1 <= array.length) {
			System.arraycopy(array, index, array, index + 1, currentSize - index);
			array[index] = element;
			return array;
		}
		long[] newArray = new long[growSize(currentSize)];
		System.arraycopy(array, 0, newArray, 0, index);
		newArray[index] = element;
		System.arraycopy(array, index, newArray, index + 1, array.length - index);
		return newArray;
	}

	/**
	 * Primitive long version of insert.
	 */
	static float[] insert(float[] array, int currentSize, int index, float element) {
		if (currentSize + 1 <= array.length) {
			System.arraycopy(array, index, array, index + 1, currentSize - index);
			array[index] = element;
			return array;
		}
		float[] newArray = new float[growSize(currentSize)];
		System.arraycopy(array, 0, newArray, 0, index);
		newArray[index] = element;
		System.arraycopy(array, index, newArray, index + 1, array.length - index);
		return newArray;
	}

	/**
	 * Primitive double version of insert.
	 */
	static double[] insert(double[] array, int currentSize, int index, double element) {
		if (currentSize + 1 <= array.length) {
			System.arraycopy(array, index, array, index + 1, currentSize - index);
			array[index] = element;
			return array;
		}
		double[] newArray = new double[growSize(currentSize)];
		System.arraycopy(array, 0, newArray, 0, index);
		newArray[index] = element;
		System.arraycopy(array, index, newArray, index + 1, array.length - index);
		return newArray;
	}
}
