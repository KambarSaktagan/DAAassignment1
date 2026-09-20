import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Experiment {
    public void runAllExperiments() {
        try (FileWriter writer = new FileWriter("results/results.csv")) {
            // CSV Header
            writer.write("Algorithm,InputType,Size,ExecutionTimeNs,MaxDepth,MetricName,MetricValue\n");

            int[] sizes = {1000, 10000, 100000}; // Small, Medium, Large
            String[] types = {"Random", "Sorted", "Reverse", "Duplicates"};

            for (int size : sizes) {
                // 1. Run 1D Array Algorithms (MergeSort, QuickSort, Deterministic Select)
                for (String type : types) {
                    int[] data = generateData(size, type);

                    // MergeSort
                    int[] msData = data.clone();
                    MergeSorter ms = new MergeSorter();
                    long start = System.nanoTime();
                    ms.sort(msData);
                    long time = System.nanoTime() - start;
                    writer.write(String.format("MergeSort,%s,%d,%d,%d,Comparisons,%d\n",
                            type, size, time, ms.maxDepth, ms.comparisons));

                    // QuickSort
                    int[] qsData = data.clone();
                    QuickSorter qs = new QuickSorter();
                    start = System.nanoTime();
                    qs.sort(qsData);
                    time = System.nanoTime() - start;
                    writer.write(String.format("QuickSort,%s,%d,%d,%d,Swaps,%d\n",
                            type, size, time, qs.maxDepth, qs.swaps));

                    // Deterministic Select (Median-of-Medians searching for k = size / 2)
                    int[] dsData = data.clone();
                    DeterministicSelector ds = new DeterministicSelector();
                    start = System.nanoTime();
                    ds.select(dsData, size / 2);
                    time = System.nanoTime() - start;
                    // Depth tracking was omitted in DeterministicSelector to save space, outputting 0
                    writer.write(String.format("DeterministicSelect,%s,%d,%d,0,Comparisons,%d\n",
                            type, size, time, ds.comparisons));
                }

                // 2. Run 2D Point Algorithm (Closest Pair of Points)
                Point[] points = generatePoints(size);
                ClosestPairSolver cp = new ClosestPairSolver();
                long start = System.nanoTime();
                double minDistance = cp.findClosest(points);
                long time = System.nanoTime() - start;
                writer.write(String.format("ClosestPair,Random2D,%d,%d,%d,MinDistance,%s\n",
                        size, time, cp.maxDepth, String.valueOf(minDistance).replace(",", ".")));
            }
            System.out.println("All benchmarking experiments completed. Saved to results/results.csv");
        } catch (IOException e) {
            System.err.println("Failed to write results: " + e.getMessage());
        }
    }

    private int[] generateData(int size, String type) {
        int[] data = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            switch (type) {
                case "Random": data[i] = rand.nextInt(); break;
                case "Sorted": data[i] = i; break;
                case "Reverse": data[i] = size - i; break;
                case "Duplicates": data[i] = rand.nextInt(10); break; // Heavy duplicates
            }
        }
        return data;
    }

    private Point[] generatePoints(int size) {
        Point[] points = new Point[size];
        Random rand = new Random();
        // Generate random coordinates within a 10,000 x 10,000 grid
        for (int i = 0; i < size; i++) {
            points[i] = new Point(rand.nextDouble() * 10000, rand.nextDouble() * 10000);
        }
        return points;
    }
}