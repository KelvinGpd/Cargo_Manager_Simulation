import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * tp1
 */

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
        return  originalCoords;
    }
}

class solution {
    private int[] boxes;
    private double[][] boxesPosition;
    private int boxAmount;
    private int truckMax;
    private double[] truckCoords;
    private ArrayList<Warehouse> haversine;


    public void launch() {
        parseFile();
        findNextStop();
    }

    public void parseFile() {
        // STEP 1: PARSING
        String filePath = "./camion_entrepot.txt";

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
        int r = 6371000; //earth radius
        haversine = new ArrayList<>();
        int i = 0;
        for (double[] position : boxesPosition) {
            int amount = boxes[i];
            i++;
            double lat = position[0];
            double lon = position[1];
            //haversine
            double in = Math.sqrt(Math.pow(Math.sin((lat-truckCoords[0])/2), 2) +
                    Math.cos(truckCoords[0]) * Math.cos(lat) * Math.pow(Math.sin((lon-truckCoords[1])/2), 2));
            double distance = 2 * r * Math.asin(in);
            haversine.add(new Warehouse(distance, amount, position));
        }
    }

    /* Algorithm */
    public void findNextStop() {
        //Initialize Truck Position
        int max[] = findMaxBoxes();
        updateState(max);
        //Initialize truck coordinates
        truckCoords = boxesPosition[max[1]];
        convertToDist();
        //heapSort
        sort();
        do {
            //Traverse until max capacity
        }while (boxAmount != truckMax);
        //print out final results TODO
    }

    public int[] findMaxBoxes() {
        int temp = Integer.MIN_VALUE;
        int position = -1;
        for (int i = 0; i < boxes.length; i++) {
            int box = boxes[i];
            if (box > temp) {
                temp = box;
                position = i;
            }
        }
        int max[] = new int[2];
        max[0] = temp;
        max[1] = position;
        return max;
    }

    public void updateState(int[] info) {
        int addedBoxes = info[0];
        int newAmount = boxAmount + addedBoxes;
        //update cargo amount
        if( newAmount > truckMax ) {
            boxAmount = truckMax;
            //update Warehouse content TODO
        }else {
            boxAmount = newAmount;
            //update Warehouse content TODO
        }
    }

    public void heapify(int n, int i){
        int largest = i;
        int l = 2 * i ;
        int r = 2 * i + 1;
        Warehouse leftWare = haversine.get(l);
        Warehouse rightWare = haversine.get(r);
        Warehouse root = haversine.get(largest);

        //if left child bigger than root
        if (l < n && leftWare.getDistance() > root.getDistance()) {
            largest = l;
        }

        //if right child larger
        if (r < n && rightWare.getDistance() > root.getDistance()) {
            largest = r;
        }

        //if root changed, then swap.
        if (largest != i) {
            Warehouse swap = haversine.get(i);
            Warehouse temp = haversine.get(largest);
            haversine.set(i, temp);
            haversine.set(largest, swap);

            heapify(n, largest);
        }
    }

    //sort haversine with .getDistance, use a FiFo structure (heap)
    public void sort(){
        int n = haversine.size();
        //build heap
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


    /* Testing purposes */

    public double[][] getBoxesPosition(){
        return boxesPosition;
    }
    public int[] getBoxes(){
        return boxes;
    }
    public int getBoxAmount(){
        return boxAmount;
    }
    public int getTruckMax(){
        return truckMax;
    }

    public ArrayList<Warehouse> getHaversine(){
        return haversine;
    }


}

public class tp1 {

    public static void main(String[] args) {
        solution sol = new solution();
        sol.launch();

        /* Testing purposes */

        ArrayList<Warehouse> haver = sol.getHaversine();
        for (Warehouse some : haver){
            System.out.println(some.getDistance()+ " " + some.getBoxAmount() + " " + some.getOriginalCoords()) ;
        }
        System.out.println();

        int[] boxes = sol.getBoxes();
        System.out.print("Boxes: ");
        for (int box : boxes) {
            System.out.print(box + " ");
        }
        System.out.println();


        double[][] boxesPosition = sol.getBoxesPosition();
        System.out.println("Boxes Position:");
        for (double[] position : boxesPosition) {
            for (double coordinate : position) {
                System.out.print(coordinate + " ");
            }
            System.out.println();
        }

    }
}