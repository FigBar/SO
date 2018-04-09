package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Disc_Scheduling.Direction.Direction;
import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;

public class SCAN extends MovingSchedulingAlgorithm {

    public SCAN(int startingPosition) {
        super(startingPosition);
    }

    /**
     * Method implementing SCAN algorithm
     *
     * @return sum of disc head movements
     * @throws ImpossibleToSimulateException is thrown when requestsQueue is empty or null
     */
    @Override
    public long carryOutSimulation(ArrayList<DiscAccessRequest> requestsQueue) throws ImpossibleToSimulateException {
        return super.carryOutSimulation(requestsQueue);
    }

    /**
     * This method, evoked when from heads position to the edge there are no ordered addresses, moves head to the edge
     * and changes heads direction
     */
    @Override
    void onEdge() {
        if (currentDirection == Direction.RIGHT) {
            currentDirection = Direction.LEFT;
            clock += (MAX_ADDRESS - currentHeadPosition) * HEAD_MOVE_TIME;
            sumOfHeadMovements += MAX_ADDRESS - currentHeadPosition;
            currentHeadPosition = MAX_ADDRESS;
        } else { //the head moves left
            currentDirection = Direction.RIGHT;
            clock += (currentHeadPosition - 1) * HEAD_MOVE_TIME;
            sumOfHeadMovements += currentHeadPosition - 1;
            currentHeadPosition = 1;

        }
    }
}
