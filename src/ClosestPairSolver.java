import java.util.Arrays;
import java.util.Comparator;

public class ClosestPairSolver {
    public int maxDepth = 0;

    public double findClosest(Point[] points) {
        if (points == null || points.length < 2) return Double.MAX_VALUE;
        maxDepth = 0;

        Point[] pointsSortedByX = points.clone();
        Point[] pointsSortedByY = points.clone();

        Arrays.sort(pointsSortedByX, Comparator.comparingDouble(p -> p.x));
        Arrays.sort(pointsSortedByY, Comparator.comparingDouble(p -> p.y));

        return closestPairRec(pointsSortedByX, pointsSortedByY, 1);
    }

    private double closestPairRec(Point[] px, Point[] py, int depth) {
        maxDepth = Math.max(maxDepth, depth);
        int n = px.length;

        if (n <= 3) {
            return bruteForce(px);
        }

        int mid = n / 2;
        Point midPoint = px[mid];

        // Split Y sorted array into left and right subsets to maintain O(n log n) total
        Point[] pyLeft = new Point[mid];
        Point[] pyRight = new Point[n - mid];
        int leftIdx = 0, rightIdx = 0;

        for (Point p : py) {
            if (p.x <= midPoint.x && leftIdx < mid) {
                pyLeft[leftIdx++] = p;
            } else {
                pyRight[rightIdx++] = p;
            }
        }

        double dLeft = closestPairRec(Arrays.copyOfRange(px, 0, mid), pyLeft, depth + 1);
        double dRight = closestPairRec(Arrays.copyOfRange(px, mid, n), pyRight, depth + 1);
        double d = Math.min(dLeft, dRight);

        // Strip construction and y-order checking
        Point[] strip = new Point[n];
        int j = 0;
        for (Point p : py) {
            if (Math.abs(p.x - midPoint.x) < d) {
                strip[j++] = p;
            }
        }

        for (int i = 0; i < j; i++) {
            for (int k = i + 1; k < j && (strip[k].y - strip[i].y) < d; k++) {
                d = Math.min(d, strip[i].distance(strip[k]));
            }
        }
        return d;
    }

    private double bruteForce(Point[] px) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < px.length; i++) {
            for (int j = i + 1; j < px.length; j++) {
                min = Math.min(min, px[i].distance(px[j]));
            }
        }
        return min;
    }
}