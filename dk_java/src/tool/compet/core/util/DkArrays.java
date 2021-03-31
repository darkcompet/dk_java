/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.core.util;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class, provides basic common operations on an array.
 */
public class DkArrays {
	public static boolean empty(Object[] arr) {
		return arr == null || arr.length == 0;
	}

	public static void swap(int[] arr, int i1, int i2) {
		int N = arr.length;

		if (i1 >= 0 && i2 >= 0 && i1 < N && i2 < N && i1 != i2) {
			arr[i1] ^= arr[i2];
			arr[i2] ^= arr[i1];
			arr[i1] ^= arr[i2];
		}
	}

	public static void reverse(int[] arr) {
		int N = arr.length;
		int tmp;

		for (int i = 0, j = N - 1; i < j; ++i, --j) {
			tmp = arr[i];
			arr[i] = arr[j];
			arr[j] = tmp;
		}
	}

	public static void reverse(long[] arr) {
		int N = arr.length;
		long tmp;

		for (int i = 0, j = N - 1; i < j; ++i, --j) {
			tmp = arr[i];
			arr[i] = arr[j];
			arr[j] = tmp;
		}
	}

	public static void reverse(float[] arr) {
		int N = arr.length;
		float tmp;

		for (int i = 0, j = N - 1; i < j; ++i, --j) {
			tmp = arr[i];
			arr[i] = arr[j];
			arr[j] = tmp;
		}
	}

	public static void reverse(double[] arr) {
		int N = arr.length;
		double tmp;

		for (int i = 0, j = N - 1; i < j; ++i, --j) {
			tmp = arr[i];
			arr[i] = arr[j];
			arr[j] = tmp;
		}
	}

	@SafeVarargs
	public static <T> List<T> asList(@Nullable T... args) {
		return (args == null || args.length == 0) ? new ArrayList<>() : Arrays.asList(args);
	}

	public static boolean inArray(char target, char[] arr) {
		for (char elm : arr) {
			if (elm == target) {
				return true;
			}
		}
		return false;
	}

	public static boolean inArray(int target, int[] arr) {
		for (int elm : arr) {
			if (elm == target) {
				return true;
			}
		}
		return false;
	}

	public static boolean inArray(float target, float[] arr) {
		for (float elm : arr) {
			if (elm == target) {
				return true;
			}
		}
		return false;
	}

	public static int indexOf(char target, char[] arr) {
		for (int index = arr.length - 1; index >= 0; --index) {
			if (arr[index] == target) {
				return index;
			}
		}
		return -1;
	}

	public static int indexOf(int target, int[] arr) {
		for (int index = arr.length - 1; index >= 0; --index) {
			if (arr[index] == target) {
				return index;
			}
		}
		return -1;
	}

	public static int indexOf(float target, float[] arr) {
		for (int index = arr.length - 1; index >= 0; --index) {
			if (arr[index] == target) {
				return index;
			}
		}
		return -1;
	}
}
