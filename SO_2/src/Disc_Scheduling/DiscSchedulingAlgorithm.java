package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Disc_Scheduling.Direction.Direction;
import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;

public abstract class DiscSchedulingAlgorithm {

    static final int MAX_ADDRESS = 1000;
    static final int HEAD_MOVE_TIME = 1;

    private long sumOfHeadMovements = 0;

    static int currentHeadPosition = (int) (Math.random() * MAX_ADDRESS) + 1;

    int executedAfterDeadline = 0;

    int clock = 0;
    int prevClock = -1;

    Direction currentDirection = Direction.RIGHT;


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

    /**
     * Method finding the nearest to head's position request
     *
     * @param queue               the queue to be checked
     * @param currentHeadPosition current position od disk's head
     * @return the requests to which the head has the shortest way
     */
    static DiscAccessRequest findNearestRequests(ArrayList<DiscAccessRequest> queue, int currentHeadPosition) {
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

    void notExecutedBeforeDeadline(DiscAccessRequest req1){
        if(req1.getExecutionDeadline() > 0 && req1.getExecutionDeadline() > clock)
            executedAfterDeadline++;
    }

    public long getSumOfHeadMovements() {
        return sumOfHeadMovements;
    }
     void reset(){
         sumOfHeadMovements = 0;
         clock = 0;
         prevClock = -1;
         currentDirection = Direction.RIGHT;
         executedAfterDeadline = 0;
     }
}
