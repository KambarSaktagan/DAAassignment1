import java.util.Random;

public class QuickSorter {
    private final Random rand = new Random();
    public long swaps = 0;
    public int maxDepth = 0;

    public void sort(int[] arr) {
        swaps = 0;
        maxDepth = 0;
        if (arr != null && arr.length > 1) {
            sort(arr, 0, arr.length - 1, 1);
        }
    }

    private void sort(int[] arr, int low, int high, int depth) {
        while (low < high) {
            maxDepth = Math.max(maxDepth, depth);

            // Randomized pivot handling duplicate values using 3-way partition
            int pivotIndex = low + rand.nextInt(high - low + 1);
            swap(arr, low, pivotIndex);

            int[] bounds = partition3Way(arr, low, high);
            int lt = bounds[0];
            int gt = bounds[1];

            // Recurse on smaller partition, iterate on larger to guarantee O(log n) depth
            if (lt - low < high - gt) {
                sort(arr, low, lt - 1, depth + 1);
                low = gt + 1;
            } else {
                sort(arr, gt + 1, high, depth + 1);
                high = lt - 1;
            }
        }
    }

    private int[] partition3Way(int[] arr, int low, int high) {
        int pivot = arr[low];
        int lt = low;
        int i = low + 1;
        int gt = high;

        while (i <= gt) {
            if (arr[i] < pivot) {
                swap(arr, lt++, i++);
            } else if (arr[i] > pivot) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    private void swap(int[] arr, int i, int j) {
        if (i != j) {
            swaps++;
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}