package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Disc_Scheduling.Direction.Direction;
import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;
import java.util.Collections;

public abstract class DiscSchedulingAlgorithm {

    static final int MAX_ADDRESS = 1000;
    static final int HEAD_MOVE_TIME = 1;

    long sumOfHeadMovements = 0;

    static int currentHeadPosition;
    static int startPosition;


    ArrayList<DiscAccessRequest> allRequests;
    ArrayList<DiscAccessRequest> availableRequests = new ArrayList<>();


    int executedAfterDeadline = 0;

    int clock = 0;
    int prevClock = -1;

    Direction currentDirection = Direction.RIGHT;

    public DiscSchedulingAlgorithm(int startingPosition) {
        currentHeadPosition = startingPosition;
        startPosition = startingPosition;
    }

    public static void setCurrentHeadPosition(int currentHeadPosition) {
        DiscSchedulingAlgorithm.currentHeadPosition = currentHeadPosition;
    }


    void addToSumOfHeadMovements(int movement) {
        sumOfHeadMovements += movement;
    }

    /**
     * Abstract method allowing to implement
     * a disc scheduling algorithm
     *
     * @return Sum of disc head movements
     */
    public abstract long carryOutSimulation(ArrayList<DiscAccessRequest> requestsQueue) throws ImpossibleToSimulateException;

    /**
     * Method checks whether there was an disc access request in last cycle
     *
     * @param req1         access request the method is checking
     * @param startOfCycle beginning of the cycle
     * @param endOfCycle   end of the cycle
     * @return true if there was a disc access request in last cycle or false if there was not
     */
    boolean wasThereAnAccessRequestInLastCycle(DiscAccessRequest req1, int startOfCycle, int endOfCycle) {
        return (startOfCycle < req1.getTimeOfArrival() && endOfCycle >= req1.getTimeOfArrival());
    }

    void updateAvailableQueue() {
        allRequests.forEach(request -> {
            if (prevClock < request.getTimeOfArrival() && clock >= request.getTimeOfArrival()) {
                availableRequests.add(request);
            }
        });
    }

    void finishRequest(DiscAccessRequest currentRequest) {

        if (isTheAccessRequestValid(currentRequest)) {

            //calculating the movement of disk head
            //which is | request initial address - current head position |
            //then adding it to the sum of head movements
            sumOfHeadMovements += (calcHeadMovement(currentRequest));

            //setting start of cycle time;
            prevClock = clock;

            //setting end of cycle time;
            clock += calcHeadMovement(currentRequest) * HEAD_MOVE_TIME;

            // check for requests with deadlines
            notExecutedBeforeDeadline(currentRequest);

            //setting the head's position on current request's initial address
            currentHeadPosition = currentRequest.getInitialAddress();

            eraseRequest(currentRequest);

        }
    }

    void eraseRequest(DiscAccessRequest request) {
        allRequests.remove(request);
        availableRequests.remove(request);
    }

    /**
     * Method finding the nearest to head's position request
     *
     * @param queue the queue to be checked
     * @return the requests to which the head has the shortest way
     */
    static DiscAccessRequest findNearestRequests(ArrayList<DiscAccessRequest> queue) {
        int minimalDist = Integer.MAX_VALUE;
        int minimalDistIndex = 0;

        DiscAccessRequest reqToCheck;

        for (int i = 0; i < queue.size(); i++) {
            reqToCheck = queue.get(i);
            if (Math.abs(reqToCheck.getInitialAddress() - currentHeadPosition) < minimalDist) {
                minimalDist = Math.abs(reqToCheck.getInitialAddress() - currentHeadPosition);
                minimalDistIndex = i;
            }
        }
        return queue.get(minimalDistIndex);
    }

    boolean isTheAccessRequestValid(DiscAccessRequest req1) {
        return (req1.getInitialAddress() > 0 && req1.getInitialAddress() <= MAX_ADDRESS);
    }

    boolean isDataInvalid(ArrayList<DiscAccessRequest> q) {
        return (q == null || q.isEmpty());
    }

    void notExecutedBeforeDeadline(DiscAccessRequest req1) {
        if (req1.getExecutionDeadline() > 0 && req1.getExecutionDeadline() < clock)
            executedAfterDeadline++;
    }

    int calcHeadMovement(DiscAccessRequest req1) {
        int movement = req1.getInitialAddress() - currentHeadPosition;

        return Math.abs(movement);
    }

    int willRequestComeWithinThisCycle(int addressToGo) {


        for (DiscAccessRequest request : allRequests) {
            if (request.getTimeOfArrival() > clock && request.getTimeOfArrival() < (clock + (Math.abs(addressToGo - currentHeadPosition)) * HEAD_MOVE_TIME)) {
                return request.getTimeOfArrival();
            }
        }
        return -1;
    }


    void moveTo(int time, DiscAccessRequest currentRequest) {
        sumOfHeadMovements += (time - clock) / HEAD_MOVE_TIME;
        if (currentHeadPosition > currentRequest.getInitialAddress()) {
            currentHeadPosition -= (time - clock) / HEAD_MOVE_TIME;
        } else {
            currentHeadPosition += (time - clock) / HEAD_MOVE_TIME;
        }
        prevClock = clock;
        clock = time;
    }

    public int getExecutedAfterDeadline() {
        return executedAfterDeadline;
    }


    void reset() {
        sumOfHeadMovements = 0;
        clock = 0;
        prevClock = -1;
        currentDirection = Direction.RIGHT;
        executedAfterDeadline = 0;
        allRequests = new ArrayList<>();
        availableRequests = new ArrayList<>();
        currentHeadPosition = startPosition;
    }

    public static int getMaxAddress() {
        return MAX_ADDRESS;
    }

    public long getSumOfHeadMovements() {
        return sumOfHeadMovements;
    }
}
