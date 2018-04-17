package Simulation;


import Page_Replacing.*;


import java.util.ArrayList;
import java.util.Scanner;

public class Run {

    private static Scanner sc = new Scanner(System.in);
    private static int nOfPages;
    private static int nOfFrames;
    private static int amountOfOrders;

    private static float FIFOSimulationSum;
    private static float LRUSimulationSum;
    private static float ALRUSimulationSum;
    private static float OPTSimulationSum;
    private static float RANDOMSimulationSum;

    public static void main(String[] args) {

        System.out.print("How accurate do you want the simulation results to be?" + "\n");

        int accuracy = getNumberBetween(1, 100);

        createData();

        System.out.println("\n" + "---Conditions of the Simulation---");
        System.out.println("\n"+ "Accuracy: " + accuracy);
        System.out.println("\n" + "Number of pages in simulation: " + nOfPages);
        System.out.println("\n" + "Number of frames in simulation: " + nOfFrames);
        System.out.println("\n" + "Amount od memory orders in simulation: " + amountOfOrders);

        for (int i = 0; i < accuracy; i++) {
            ArrayList<PageReplacingAlgorithm> algorithms = new ArrayList<>();

            Assumptions simulationAssumptions = new Assumptions(nOfPages,nOfFrames,amountOfOrders);
            Assumptions FIFOasmp = new Assumptions(simulationAssumptions);
            Assumptions LRUasmp = new Assumptions(simulationAssumptions);
            Assumptions ALRUasmp = new Assumptions(simulationAssumptions);
            Assumptions OPTasmp = new Assumptions(simulationAssumptions);
            Assumptions RANDOMasmp = new Assumptions(simulationAssumptions);

            algorithms.add(new FIFO(FIFOasmp));
            algorithms.add(new LRU(LRUasmp));
            algorithms.add(new ALRU(ALRUasmp));
            algorithms.add(new OPT(OPTasmp));
            algorithms.add(new RANDOM(RANDOMasmp));


            System.out.println("\n" + "--Simulation-- " + i);

            int identifierOfAlgorithm = 0;

            for (PageReplacingAlgorithm algorithm : algorithms) {

                int pageErrors = algorithm.simulate();

                System.out.print(
                        algorithm.getClass().getSimpleName() +
                                ", page errors amount: " +
                                pageErrors

                );

                switch (identifierOfAlgorithm) {
                    case 0:
                        FIFOSimulationSum += pageErrors;
                        break;
                    case 1:
                        LRUSimulationSum += pageErrors;
                        break;
                    case 2:
                        ALRUSimulationSum += pageErrors;
                        break;
                    case 3:
                        OPTSimulationSum += pageErrors;
                        break;
                    case 4:
                        RANDOMSimulationSum += pageErrors;
                        break;
                    default:
                        System.out.println("Something went wrong");
                }
                identifierOfAlgorithm++;
                System.out.println();
            }
        }
        sc.close();

        System.out.println("\n\n");
        System.out.println("--- FINAL RESULT OF THE SIMULATION ---");
        System.out.println("FIFO page errors count in " + accuracy + " runs: " + String.format("%.2f", FIFOSimulationSum / accuracy) );
        System.out.println("LRU average page errors count in " + accuracy + " runs: " + String.format("%.2f", LRUSimulationSum / accuracy) );
        System.out.println("ALRU average page errors count in " + accuracy + " runs: " + String.format("%.2f", ALRUSimulationSum / accuracy) );
        System.out.println("OPT average page errors count in " + accuracy + " runs: " + String.format("%.2f", OPTSimulationSum / accuracy));
        System.out.println("RANDOM average sum page errors count in " + accuracy + " runs: " + String.format("%.2f", RANDOMSimulationSum / accuracy));

    }

    public static void createData() {
        System.out.print("Do you want to provide data (y/n)?: ");

        if (getYesOrNo()) {
            getRestrictedData();
        } else {
            generateData();
        }
    }

    private static boolean getYesOrNo() {

        char choice;

        do {
            choice = sc.next().toLowerCase().charAt(0);
        } while (choice != 'y' && choice != 'n');

        switch (choice) {
            case 'y':
                return true;
            case 'n':
                return false;
            default:
                System.out.println("Upps, something went wrong :/");
                return false;
        }
    }

    private static void getRestrictedData() {
        System.out.println("Provide the number of pages in the simulation: ");
        nOfPages = getNumberBetween(1, 500);

        System.out.println("Provide number of frames in the simulation: ");
        nOfFrames = getNumberBetween(1, 50);

        System.out.println("\n" + "How many memory orders should the simulation contain: ");
        amountOfOrders = getNumberBetween(1, 10000);
    }

    private static void generateData() {

        nOfPages = (int) (Math.random() * 500) + 1;
        nOfFrames = (int) (Math.random() * 50) + 1;
        amountOfOrders = (int) (Math.random() * 1000) + 1;

    }

    private static int getNumberBetween(int lowBorder, int highBorder) {
        int givenNumber;

        do {
            System.out.print("Write a integer between " + lowBorder + " and " + highBorder + ": ");
            try {
                givenNumber = sc.nextInt();
            } catch (NumberFormatException nfe) {
                givenNumber = -1;
            }
        } while (givenNumber < lowBorder || givenNumber > highBorder);

        return givenNumber;
    }
}
