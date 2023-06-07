import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * tp1
 */

class solution {
    private int[] boxes;
    private double[][] boxesPosition;
    private int boxAmount;
    private int truckMax;

    public void launch() {
        parseFile();
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

    public void merge(int[] arr1, int[] arr2){
        // On fait un troixieme array de la taille des deux autres


    }
    public void sort(){

    }

}

public class tp1 {

    public static void main(String[] args) {
        solution sol = new solution();
        sol.launch();
    }
}