public class MergeSorter {
    private static final int CUTOFF = 15;
    public long comparisons = 0;
    public int maxDepth = 0;

    public void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        int[] aux = new int[arr.length];
        comparisons = 0;
        maxDepth = 0;
        System.arraycopy(arr, 0, aux, 0, arr.length);
        sort(aux, arr, 0, arr.length - 1, 1);
    }

    private void sort(int[] src, int[] dest, int low, int high, int depth) {
        maxDepth = Math.max(maxDepth, depth);

        // Small-input cutoff
        if (high - low <= CUTOFF) {
            insertionSort(dest, low, high);
            return;
        }

        int mid = low + (high - low) / 2;
        sort(dest, src, low, mid, depth + 1);
        sort(dest, src, mid + 1, high, depth + 1);
        merge(src, dest, low, mid, high);
    }

    private void merge(int[] src, int[] dest, int low, int mid, int high) {
        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            comparisons++;
            if (i > mid) dest[k] = src[j++];
            else if (j > high) dest[k] = src[i++];
            else if (src[j] < src[i]) dest[k] = src[j++];
            else dest[k] = src[i++];
        }
    }

    private void insertionSort(int[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= low && arr[j] > key) {
                comparisons++;
                arr[j + 1] = arr[j];
                j--;
            }
            if (j >= low) comparisons++; // Count final failed check
            arr[j + 1] = key;
        }
    }
}