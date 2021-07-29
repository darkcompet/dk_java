/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for array or array-list.
 */
public class DkArrays {
	public static boolean empty(@Nullable Object[] arr) {
		return arr == null || arr.length == 0;
	}

	@SafeVarargs
	@NonNull
	public static <T> List<T> asList(@Nullable T... args) {
		return (args == null || args.length == 0) ? new ArrayList<>() : Arrays.asList(args);
	}

	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	public static void reverse(int[] arr) {
		int tmp;
		for (int left = 0, right = arr.length - 1; left < right; ++left, --right) {
			tmp = arr[left];
			arr[left] = arr[right];
			arr[right] = tmp;
		}
	}

	public static void reverse(long[] arr) {
		long tmp;
		for (int left = 0, right = arr.length - 1; left < right; ++left, --right) {
			tmp = arr[left];
			arr[left] = arr[right];
			arr[right] = tmp;
		}
	}

	public static void reverse(float[] arr) {
		float tmp;
		for (int left = 0, right = arr.length - 1; left < right; ++left, --right) {
			tmp = arr[left];
			arr[left] = arr[right];
			arr[right] = tmp;
		}
	}

	public static void reverse(double[] arr) {
		double tmp;
		for (int left = 0, right = arr.length - 1; left < right; ++left, --right) {
			tmp = arr[left];
			arr[left] = arr[right];
			arr[right] = tmp;
		}
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
