import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CorrectnessTest {

    @Test
    public void testSortingCorrectness() {
        int size = 1000;
        int[][] testCases = {
                generateData(size, "Random"),
                generateData(size, "Sorted"),
                generateData(size, "Reverse"),
                generateData(size, "Duplicates"),
                new int[]{},       // Empty array
                new int[]{42}      // Single-element array
        };

        for (int i = 0; i < testCases.length; i++) {
            int[] original = testCases[i];

            // Reference Java Sort
            int[] expected = original.clone();
            Arrays.sort(expected);

            // Test MergeSort
            int[] msArr = original.clone();
            new MergeSorter().sort(msArr);
            assertArrayEquals(expected, msArr, "MergeSort failed on test case index: " + i);

            // Test QuickSort
            int[] qsArr = original.clone();
            new QuickSorter().sort(qsArr);
            assertArrayEquals(expected, qsArr, "QuickSort failed on test case index: " + i);
        }
    }

    @Test
    public void testDeterministicSelectCorrectness() {
        DeterministicSelector selector = new DeterministicSelector();
        Random rand = new Random();
        int numTests = 100; // Run at least 100 random tests

        for (int i = 0; i < numTests; i++) {
            int size = rand.nextInt(1000) + 10;
            int[] testArray = generateData(size, "Random");
            int k = rand.nextInt(size);

            int[] arrayForJavaSort = testArray.clone();
            int[] arrayForMySelector = testArray.clone();

            // Reference method: Arrays.sort(a)[k]
            Arrays.sort(arrayForJavaSort);
            int expectedValue = arrayForJavaSort[k];

            // Student method
            int actualValue = selector.select(arrayForMySelector, k);

            assertEquals(expectedValue, actualValue,
                    "Mismatch on Select test " + i + ". Size: " + size + ", k: " + k);
        }
    }

    @Test
    public void testClosestPairCorrectness() {
        int n = 2000; // Small dataset n <= 2000
        Point[] points = new Point[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            points[i] = new Point(rand.nextDouble() * 10000, rand.nextDouble() * 10000);
        }

        // Divide-and-conquer result
        ClosestPairSolver solver = new ClosestPairSolver();
        double dncResult = solver.findClosest(points);

        // O(n^2) brute-force solution for reference
        double bruteForceResult = Double.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                bruteForceResult = Math.min(bruteForceResult, points[i].distance(points[j]));
            }
        }

        // Use a small delta (1e-6) for floating-point math comparisons
        assertEquals(bruteForceResult, dncResult, 1e-6, "Closest Pair divide-and-conquer does not match brute-force");
    }

    // Helper method to generate arrays (matching Experiment.java logic)
    private int[] generateData(int size, String type) {
        int[] data = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            switch (type) {
                case "Random": data[i] = rand.nextInt(); break;
                case "Sorted": data[i] = i; break;
                case "Reverse": data[i] = size - i; break;
                case "Duplicates": data[i] = rand.nextInt(10); break;
            }
        }
        return data;
    }
}