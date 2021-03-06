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
    private static int startPosition;

    private static int minDeadline;
    private static int maxDeadline;

    private static float FCFSSimulationSum;
    private static float FCFSDeadlinesSum;

    private static float SSTFSimulationSum;
    private static float SSTFDeadlinesSum;

    private static float SCANSimulationSum;
    private static float SCANDeadlinesSum;

    private static float CSCANSimulationSum;
    private static float CSCANDeadlinesSum;

    private static float EDFSimulationSum;
    private static float EDFDeadlinesSum;

    private static float FDSCANSimulationSum;
    private static float FDSCANDeadlinesSum;


    public static void main(String[] args) {

        System.out.print("How accurate do you want the simulation results to be?" + "\n");

        int accuracy = getNumberBetween(1, 100);

        createData();

        System.out.println("\n" + "Number of requests in simulation: " + requestsAmount);
        System.out.println("\n" + "Number of real-time requests in simulation: " + deadlinesAmount);
        System.out.println("\n" + "Maximum arrival time of request: " + maxArrivalTime);
        System.out.println("\n" + "Starting position of disc's head: " + startPosition);


        for (int i = 0; i < accuracy; i++) {


            ArrayList<DiscSchedulingAlgorithm> algorithms = new ArrayList<>();
            ArrayList<DiscAccessRequest> requestsQueue = generateQueue(requestsAmount, deadlinesAmount, maxArrivalTime, maxDeadline, minDeadline);

            algorithms.add(new FCFS(startPosition));
            algorithms.add(new SSTF(startPosition));
            algorithms.add(new SCAN(startPosition));
            algorithms.add(new CSCAN(startPosition));
            algorithms.add(new EDF(startPosition));
            algorithms.add(new FDSCAN(startPosition));


            System.out.println("\n" + "--Simulation-- " + i);

            int identifierOfAlgorithm = 0;

            for (DiscSchedulingAlgorithm algorithm : algorithms) {


                algorithm.carryOutSimulation(new ArrayList<>(requestsQueue));

                System.out.print(
                        algorithm.getClass().getSimpleName() +
                                ", sum of disc head movements: " +
                                algorithm.getSumOfHeadMovements() +
                                " ||| Requests executed after deadline: " +
                                algorithm.getExecutedAfterDeadline()
                );

                switch (identifierOfAlgorithm) {
                    case 0:
                        FCFSSimulationSum += algorithm.getSumOfHeadMovements();
                        FCFSDeadlinesSum += algorithm.getExecutedAfterDeadline();
                        break;
                    case 1:
                        SSTFSimulationSum += algorithm.getSumOfHeadMovements();
                        SSTFDeadlinesSum += algorithm.getExecutedAfterDeadline();
                        break;
                    case 2:
                        SCANSimulationSum += algorithm.getSumOfHeadMovements();
                        SCANDeadlinesSum += algorithm.getExecutedAfterDeadline();
                        break;
                    case 3:
                        CSCANSimulationSum += algorithm.getSumOfHeadMovements();
                        CSCANDeadlinesSum += algorithm.getExecutedAfterDeadline();
                        break;
                    case 4:
                        EDFSimulationSum += algorithm.getSumOfHeadMovements();
                        EDFDeadlinesSum += algorithm.getExecutedAfterDeadline();
                        break;
                    case 5:
                        FDSCANSimulationSum += algorithm.getSumOfHeadMovements();
                        FDSCANDeadlinesSum += algorithm.getExecutedAfterDeadline();
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
        System.out.println("FCFS average sum of head movements in " + accuracy + " runs: " + String.format("%.2f", FCFSSimulationSum / accuracy) + " ||| Requests executed after deadline: " + String.format("%.2f", FCFSDeadlinesSum / accuracy));
        System.out.println("SSTF average sum of head movements in " + accuracy + " runs: " + String.format("%.2f", SSTFSimulationSum / accuracy) + " ||| Requests executed after deadline: " + String.format("%.2f", SSTFDeadlinesSum / accuracy));
        System.out.println("SCAN average sum of head movements in " + accuracy + " runs: " + String.format("%.2f", SCANSimulationSum / accuracy) + " ||| Requests executed after deadline: " + String.format("%.2f", SCANDeadlinesSum / accuracy));
        System.out.println("C-SCAN average sum of head movements in " + accuracy + " runs: " + String.format("%.2f", CSCANSimulationSum / accuracy) + " ||| Requests executed after deadline: " + String.format("%.2f", CSCANDeadlinesSum / accuracy));

        System.out.println("EDF average sum of head movements in " + accuracy + " runs: " + String.format("%.2f", EDFSimulationSum / accuracy) + " ||| Requests executed after deadline: " + String.format("%.2f", EDFDeadlinesSum / accuracy));
        System.out.println("FDSCAN average sum of head movements in " + accuracy + " runs: " + String.format("%.2f", FDSCANSimulationSum / accuracy) + " ||| Requests executed after deadline: " + String.format("%.2f", FDSCANDeadlinesSum / accuracy));

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

        System.out.println("Provide disc's head starting position: ");
        startPosition = getNumberBetween(1, 1000);

        System.out.println("\n" + "How many requests of all should have deadlines: ");
        deadlinesAmount = getNumberBetween(1, (requestsAmount / 2));

        System.out.println("\n" + "Provide how late can a process arrive: ");
        maxArrivalTime = getNumberBetween(0, ((requestsAmount * DiscSchedulingAlgorithm.getMaxAddress())/4));

        System.out.println("\n" + "Provide maximum deadline of real-time request: ");
        maxDeadline = getNumberBetween(100, 10000);

        System.out.println("\n" + "Provide minimum deadline of real-time request: ");
        minDeadline = getNumberBetween(1, 99);


    }

    private static void generateData() {
        ArrayList<Process> queue = new ArrayList<>();

        requestsAmount = (int) (Math.random() * 1000) + 100;
        startPosition = (int) (Math.random() * 1000) + 1;
        deadlinesAmount = (int) (Math.random() * (requestsAmount / 2)) + 1;
        maxArrivalTime = (int) (Math.random() * ((requestsAmount * DiscSchedulingAlgorithm.getMaxAddress())/4));

        maxDeadline = (int) (Math.random() * maxArrivalTime) + 100;
        minDeadline = (int) (Math.random() * 98) + 1;
    }

    private static ArrayList<DiscAccessRequest> generateQueue(int nOfRequests, int nOfDeadlines, int maxArrival, int maxDeadline, int minDeadline) {

        ArrayList<DiscAccessRequest> queue = new ArrayList<>();

        for (int i = 0; i < (nOfRequests - nOfDeadlines); i++) {
            queue.add(new DiscAccessRequest(
                    (int) (Math.random() * (1000)) + 1,
                    (int) (Math.random() * (maxArrival) + 1)

            ));
        }

        for (int i = 0; i < nOfDeadlines; i++) {
            queue.add(new DiscAccessRequest(
                    (int) (Math.random() * (1000)) + 1,
                    (int) (Math.random() * (maxArrival) + 1),
                    (int) (Math.random() * (maxDeadline - minDeadline)) + minDeadline

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
