package learning.algorithm.sort;

public class MergeSort {
	public void mergesort(int[] a, int[] work, int left, int right) {
		final int LENGTH = right - left + 1;

		if (LENGTH <= 45) {
			InsertionSort.insertionsort(a, left, right);
			return;
		}
		int mid = (left + right) >> 1;
		mergesort(a, work, left, mid);
		mergesort(a, work, mid + 1, right);
		// merge
		for (int i = left, j = mid + 1, cur = left; cur <= right; ++cur) {
			if ((i <= mid && (j > right || a[i] < a[j]))) {
				work[cur] = a[i++];
			}
			else if (j <= right) {
				work[cur] = a[j++];
			}
		}

		System.arraycopy(work, left, a, left, LENGTH);
	}
}
