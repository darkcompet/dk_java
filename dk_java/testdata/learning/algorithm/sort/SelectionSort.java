package learning.algorithm.sort;

import tool.compet.core4j.DkArrays;

public class SelectionSort {
	public void selectionsort(int[] a, int left, int right) {
		for (int i = left; i <= right; ++i) {
			int minIndex = i;
			int min = a[i];

			for (int j = i + 1; j <= right; ++j) {
				if (min > a[j]) {
					min = a[j];
					minIndex = j;
				}
			}
			DkArrays.swap(a, i, minIndex);
		}
	}
}
