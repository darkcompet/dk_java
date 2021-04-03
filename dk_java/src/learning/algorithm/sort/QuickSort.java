package learning.algorithm.sort;

import tool.compet.core.DkArrays;

import java.util.LinkedList;


public class QuickSort {
	// worst case takes O(n^2)
	public void quicksort(int[] a, int left, int right) {
		LinkedList<int[]> stack = new LinkedList<>();
		stack.addLast(new int[]{left, right});
		while (stack.size() > 0) {
			int[] range = stack.removeLast();
			int start = range[0], end = range[1];

			int N = right - left + 1;
			if (N <= 45) {
				InsertionSort.insertionsort(a, start, end);
				continue;
			}

			int pivot = partition(a, start, end);
			stack.addLast(new int[]{start, pivot - 1});
			stack.addLast(new int[]{pivot + 1, end});
		}
	}

	private int partition(int[] a, int left, int right) {
		int mid = (left + right) >> 1, key = a[mid];
		DkArrays.swap(a, left, mid);
		int cur = left; // a[cur] <= key
		for (int i = left + 1; i <= right; ++i) {
			if (a[i] < key) {
				DkArrays.swap(a, ++cur, i);
			}
		}
		DkArrays.swap(a, cur, left);
		return cur;
	}

	public void quicksort3way(int[] a, int left, int right) {
		int N = right - left + 1;
		if (N <= 45) {
			InsertionSort.insertionsort(a, left, right);
			return;
		}
		int key = a[(left + right) >> 1];
		int lt = left - 1, gt = right + 1;
		for (int i = lt + 1; i < gt; ) {
			if (a[i] < key) DkArrays.swap(a, ++lt, i++);
			else if (a[i] > key) DkArrays.swap(a, --gt, i);
			else ++i;
		}
		quicksort3way(a, left, lt);
		quicksort3way(a, gt, right);
	}
}
