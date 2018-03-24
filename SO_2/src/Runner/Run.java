package Runner;

import Disc_Access.DiscAccessRequest;
import Disc_Scheduling.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Run {

    private static Scanner sc = new Scanner(System.in);
    private static int requestsAmount;
    private static int maxArrivalTime;
    private static int deadlinesAmount;

    private static int minDeadline;
    private static int maxDeadline;

    private static float FCFSSimulationSum;
    private static float SSTFSimulationSum;
    private static float SCANSimulationSum;
    private static float CSCANSimulationSum;
    private static float EDFSimulationSum;
    private static float FDSCANSimulationSum;


    public static void main(String[] args) {

        System.out.print("How accurate do you want the simulation results to be?" + "\n");

        int accuracy = getNumberBetween(1, 100);

        createData();

        for (int i = 0; i < accuracy; i++) {


            ArrayList<DiscSchedulingAlgorithm> algorithms = new ArrayList<>();
            ArrayList<DiscAccessRequest> requestsQueue = generateQueue(requestsAmount, deadlinesAmount, maxArrivalTime, maxDeadline, minDeadline);

            algorithms.add(new FCFS(new ArrayList<>(requestsQueue)));
            algorithms.add(new SSTF(new ArrayList<>(requestsQueue)));
            algorithms.add(new SCAN(new ArrayList<>(requestsQueue)));
            algorithms.add(new CSCAN(new ArrayList<>(requestsQueue)));
            algorithms.add(new EDF(new ArrayList<>(requestsQueue)));
            algorithms.add(new FDSCAN(new ArrayList<>(requestsQueue)));


            System.out.println("\n" + "Simulation " + i);

            int identifierOfAlgorithm = 0;

            for (DiscSchedulingAlgorithm algorithm : algorithms) {



                algorithm.carryOutSimulation();

                System.out.print(
                        algorithm.getClass().getSimpleName() +
                                ", sum of disc head movements: " +
                                algorithm.getSumOfHeadMovements() +
                                " Requests executed after deadline: " +
                                algorithm.getExecutedAfterDeadline()
                );

                switch (identifierOfAlgorithm) {
                    case 0:
                        FCFSSimulationSum += algorithm.getSumOfHeadMovements();
                        break;
                    case 1:
                        SSTFSimulationSum += algorithm.getSumOfHeadMovements();
                        break;
                    case 2:
                        SCANSimulationSum += algorithm.getSumOfHeadMovements();
                        break;
                    case 3:
                        CSCANSimulationSum += algorithm.getSumOfHeadMovements();
                        break;
                    case 4:
                        EDFSimulationSum += algorithm.getSumOfHeadMovements();
                        break;
                    case 5:
                        FDSCANSimulationSum += algorithm.getSumOfHeadMovements();
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
        System.out.println("FCFS average sum of head movements in " + accuracy + " runs: " + String.format("%.3f", FCFSSimulationSum/accuracy));
        System.out.println("SSTF average sum of head movements in " + accuracy + " runs: " + String.format("%.3f", SSTFSimulationSum/accuracy));
        System.out.println("SCAN average sum of head movements in " + accuracy + " runs: " + String.format("%.3f", SCANSimulationSum/accuracy));
        System.out.println("C-SCAN average sum of head movements in " + accuracy + " runs: " + String.format("%.3f", CSCANSimulationSum/accuracy));

        System.out.println("EDF average sum of head movements in " + accuracy + " runs: " + String.format("%.3f", EDFSimulationSum/accuracy));
        System.out.println("FDSCAN average sum of head movements in " + accuracy + " runs: " + String.format("%.3f", FDSCANSimulationSum/accuracy));

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
        System.out.println("Provide the number of disc access requests in the queue: ");
        requestsAmount = getNumberBetween(1, 1000);

        System.out.println("How many requests of all should have deadlines: ");
        deadlinesAmount = getNumberBetween(1, (requestsAmount / 2));

        System.out.println("Provide how late can a process arrive: ");
        maxArrivalTime = getNumberBetween(100, 500 + (requestsAmount * 2));

        System.out.println("Provide maximum deadline of real-time request: ");
        maxDeadline = getNumberBetween(100, 10000);

        System.out.println("Provide minimum deadline of real-time request: ");
        minDeadline = getNumberBetween(1, 99);


    }

    private static void generateData() {
        ArrayList<Process> queue = new ArrayList<>();

        requestsAmount = (int) (Math.random() * 900) + 100;
        deadlinesAmount = (int) (Math.random() * (requestsAmount / 2)) + 1;
        maxArrivalTime = (int) (Math.random() * (500 + (requestsAmount * 2)));

        maxDeadline = (int) (Math.random() * maxArrivalTime) + 100;
        minDeadline = (int) (Math.random() * 98) + 1;
    }

    private static ArrayList<DiscAccessRequest> generateQueue(int nOfRequests, int nOfDeadlines, int maxArrival, int maxDeadline, int minDeadline) {

        ArrayList<DiscAccessRequest> queue = new ArrayList<>();

        for (int i = 0; i < (nOfRequests - nOfDeadlines); i++) {
            queue.add(new DiscAccessRequest(
                    (int) (Math.random() * (1000 - 1)) + 1,
                    (int) (Math.random() * (maxArrival - 1) + 1)

            ));
        }

        for (int i = 0; i < nOfDeadlines; i++) {
            queue.add(new DiscAccessRequest(
                    (int) (Math.random() * (1000 - 1)) + 1,
                    (int) (Math.random() * (maxArrival - 1) + 1),
                    (int) (Math.random() * (maxDeadline - minDeadline)) + maxArrival

            ));
        }
        return queue;
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
