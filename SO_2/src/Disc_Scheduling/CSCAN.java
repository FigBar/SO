package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;


public class CSCAN extends MovingSchedulingAlgorithm {

    public CSCAN(int startingPosition) {
        super(startingPosition);
    }

    /**
     * Method implementing CSCAN algorithm
     *
     * @return sum of disc head movements
     * @throws ImpossibleToSimulateException is thrown when requestsQueue is empty or null
     */
    @Override
    @SuppressWarnings("Duplicates")
    public long carryOutSimulation(ArrayList<DiscAccessRequest> requestsQueue) throws ImpossibleToSimulateException {
        return super.carryOutSimulation(requestsQueue);

    }

    /**
     * This method, evoked when from heads position to the edge there are no ordered sectors, moves head past the edge,
     * to the first sector
     */
    @Override
    void onEdge() {
        sumOfHeadMovements += MAX_ADDRESS - currentHeadPosition + 1;
        clock += (MAX_ADDRESS - currentHeadPosition + 1) * HEAD_MOVE_TIME;
        currentHeadPosition = 1;
    }


}
