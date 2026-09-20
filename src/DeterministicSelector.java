import java.util.Arrays;

public class DeterministicSelector {
    public long comparisons = 0;
    public int maxDepth = 0; // Added depth tracker

    public int select(int[] arr, int k) {
        comparisons = 0;
        maxDepth = 0;
        if (arr == null || arr.length == 0 || k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("Invalid input or k");
        }
        return select(arr, 0, arr.length - 1, k, 1);
    }

    private int select(int[] arr, int low, int high, int k, int depth) {
        maxDepth = Math.max(maxDepth, depth); // Update max depth
        if (low == high) return arr[low];

        int pivotIndex = medianOfMedians(arr, low, high, depth);
        pivotIndex = partition(arr, low, high, pivotIndex);

        if (k == pivotIndex) {
            return arr[k];
        } else if (k < pivotIndex) {
            return select(arr, low, pivotIndex - 1, k, depth + 1);
        } else {
            return select(arr, pivotIndex + 1, high, k, depth + 1);
        }
    }

    private int medianOfMedians(int[] arr, int low, int high, int depth) {
        int n = high - low + 1;
        if (n <= 5) {
            return partition5(arr, low, high);
        }

        int numMedians = n / 5 + (n % 5 == 0 ? 0 : 1);
        int[] medians = new int[numMedians];

        for (int i = 0; i < numMedians; i++) {
            int subLow = low + i * 5;
            int subHigh = Math.min(subLow + 4, high);
            int medianIndex = partition5(arr, subLow, subHigh);
            medians[i] = arr[medianIndex];
        }

        DeterministicSelector subSelector = new DeterministicSelector();
        int medianValue = subSelector.select(medians, 0, numMedians - 1, numMedians / 2, depth + 1);
        this.comparisons += subSelector.comparisons;
        this.maxDepth = Math.max(this.maxDepth, subSelector.maxDepth); // Sync depth from sub-calls

        for (int i = low; i <= high; i++) {
            if (arr[i] == medianValue) return i;
        }
        return low;
    }

    private int partition5(int[] arr, int low, int high) {
        Arrays.sort(arr, low, high + 1);
        return low + (high - low) / 2;
    }

    private int partition(int[] arr, int low, int high, int pivotIndex) {
        int pivotValue = arr[pivotIndex];
        swap(arr, pivotIndex, high);
        int storeIndex = low;

        for (int i = low; i < high; i++) {
            comparisons++;
            if (arr[i] < pivotValue) {
                swap(arr, storeIndex, i);
                storeIndex++;
            }
        }
        swap(arr, high, storeIndex);
        return storeIndex;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}