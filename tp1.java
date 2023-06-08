import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.FileWriter;

/**
 * tp1
 */
public class tp1 {

    public static void main(String[] args) {
        solution sol = new solution();
        sol.launch(args);
    }
}

class Warehouse {
    private double distance;
    private int boxAmount;
    private double[] originalCoords;

    public Warehouse(double doubleValue, int intValue, double[] originalCoords) {
        this.distance = doubleValue;
        this.boxAmount = intValue;
        this.originalCoords = originalCoords;
    }

    public double getDistance() {
        return distance;
    }

    public int getBoxAmount() {
        return boxAmount;
    }

    public double[] getOriginalCoords() {
        return originalCoords;
    }
}

class solution {
    private int[] boxes;
    private double[][] boxesPosition;
    private int boxAmount;
    private int truckMax;
    private double[] truckCoords;
    private ArrayList<Warehouse> haversine;
    private String[] args;

    public void launch(String[] args) {
        this.args = args;
        parseFile();
        findNextStop();
    }

    public void parseFile() {
        // STEP 1: PARSING
        String filePath = "./" + args[0];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();

            Scanner scanner = new Scanner(line);
            boxAmount = scanner.nextInt();
            truckMax = scanner.nextInt();

            LineNumberReader reader = new LineNumberReader(new FileReader(filePath));
            reader.skip(Integer.MAX_VALUE);
            int size = (reader.getLineNumber() - 1) * 2; // Bound le nombre maximal de inputs
            reader.close();

            boxes = new int[size];
            boxesPosition = new double[size][2];
            int i = 0;
            while ((line = br.readLine()) != null) {

                scanner = new Scanner(line);
                scanner.useDelimiter("[^0-9.-]+");

                boxes[i] = scanner.nextInt();

                boxesPosition[i][0] = scanner.nextDouble();
                boxesPosition[i][1] = scanner.nextDouble();

                i++;

                if (scanner.hasNext()) {

                    boxes[i] = scanner.nextInt();
                    boxesPosition[i][0] = scanner.nextDouble();
                    boxesPosition[i][1] = scanner.nextDouble();
                    i++;

                }

            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

    }

    public void convertToDist() {
        int r = 6371000; // earth radius
        haversine = new ArrayList<>();
        int i = 0;
        for (double[] position : boxesPosition) {
            int amount = boxes[i];
            i++;
            double lat = position[0];
            double lon = position[1];
            // haversine
            double in = Math.sqrt(Math.pow(Math.sin((lat - truckCoords[0]) / 2), 2) +
                    Math.cos(truckCoords[0]) * Math.cos(lat) * Math.pow(Math.sin((lon - truckCoords[1]) / 2), 2));
            double distance = 2 * r * Math.asin(in);
            haversine.add(new Warehouse(distance, amount, position));
        }
    }

    /* Algorithm */
    public void findNextStop() {
        // Initialize Truck Position
        int initPos = findMaxBoxes();
        // Initialize truck coordinates
        truckCoords = boxesPosition[initPos];
        convertToDist();
        // heapSort
        sort();
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(args[1], true));

            output.append("Truck position: (" + truckCoords[0] + " , "
                    + truckCoords[1] + ")\n");
            int i = 0;
            boxAmount = 0;

            // Traverse until max capacity

            while (boxAmount < truckMax) {
                Warehouse currWarehouse = haversine.get(i);
                boxAmount = boxAmount + currWarehouse.getBoxAmount();

                int num = Math.max(0, boxAmount - truckMax);
                int distance = (int) Math.floor(currWarehouse.getDistance());

                String line = "Distance:" + distance;

                int spaces = 15 - Integer.toString(distance).length();
                for (int j = 0; j < spaces; j++)
                    line += " ";

                line += "   Number of boxes:" + num;
                spaces = 15 - Integer.toString(num).length();
                for (int j = 0; j < spaces; j++)
                    line += " ";

                line += "   Position:" + "(" + currWarehouse.getOriginalCoords()[0] + ","
                        + currWarehouse.getOriginalCoords()[1] + ")\n";

                System.out.println(line);
                output.append(line);
                output.flush();

                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int findMaxBoxes() {
        int temp = Integer.MIN_VALUE;
        int position = -1;
        for (int i = 0; i < boxes.length; i++) {
            int box = boxes[i];
            if (box > temp) {
                temp = box;
                position = i;
            }
        }
        return position;
    }

    public void heapify(int n, int i) {
        int largest = i;
        int l = 2 * i;
        int r = 2 * i + 1;
        Warehouse leftWare = haversine.get(l);
        Warehouse rightWare = haversine.get(r);
        Warehouse root = haversine.get(largest);

        // if left child bigger than root
        if (l < n && leftWare.getDistance() > root.getDistance()) {
            largest = l;
        }

        // if right child larger
        if (r < n && rightWare.getDistance() > root.getDistance()) {
            largest = r;
        }

        // if root changed, then swap.
        if (largest != i) {
            Warehouse swap = haversine.get(i);
            Warehouse temp = haversine.get(largest);
            haversine.set(i, temp);
            haversine.set(largest, swap);

            heapify(n, largest);
        }
    }

    // sort haversine with .getDistance, use a FiFo structure (heap)
    public void sort() {
        int n = haversine.size();
        // build heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(n, i);
        }
        for (int i = n - 1; i > 0; i--) {
            Warehouse current = haversine.get(i);
            Warehouse temp = haversine.get(0);
            haversine.set(0, current);
            haversine.set(i, temp);
            heapify(i, 0);
        }

    }

}
