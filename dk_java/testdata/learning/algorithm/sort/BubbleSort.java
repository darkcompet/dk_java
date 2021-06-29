package learning.algorithm.sort;

import tool.compet.core4j.DkArrays;

public class BubbleSort {
	public void bubblesort(int[] a, int left, int right) {
		for (int d = 0; d < right - left; ++d) {
			for (int i = left; i < right - d; ++i) {
				if (a[i] > a[i + 1]) {
					DkArrays.swap(a, i, i + 1);
				}
			}
		}
	}
}
