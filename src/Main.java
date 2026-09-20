public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Divide-and-Conquer Assignment 1...");

        // Ensure results directory exists or experiment will throw IOException
        java.io.File resultsDir = new java.io.File("results");
        if (!resultsDir.exists()) {
            resultsDir.mkdir();
        }

        Experiment experiment = new Experiment();
        experiment.runAllExperiments();

        System.out.println("Program execution finished.");
    }
}