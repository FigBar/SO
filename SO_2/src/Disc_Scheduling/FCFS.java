package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;

import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;
import java.util.Collections;

public class FCFS extends DiscSchedulingAlgorithm {

    public FCFS(int startingPosition) {
        super(startingPosition);
    }


    /**
     * Method implementing FCFS algorithm.
     *
     * @return Sum of disc head movements
     * @throws ImpossibleToSimulateException is thrown when requestsQueue is empty or null
     */
    @SuppressWarnings("Duplicates")
    @Override
    public long carryOutSimulation(ArrayList<DiscAccessRequest> requestsQueue) throws ImpossibleToSimulateException {

        reset();

        //checking whether it is possible to carry out simulation with provided data
        if (isDataInvalid(requestsQueue)) {
            throw new ImpossibleToSimulateException();
        }

        //sorting requests by time of arrival
        Collections.sort(requestsQueue, DiscAccessRequest::compareByTimeOfArrival);
        allRequests = requestsQueue;


        //the loop is executed until the queue is empty
        while (!allRequests.isEmpty()) {

            updateAvailableQueue();

            //checking whether there's a request in available queue
            //if not skipping to time when the first request arrives
            if (availableRequests.isEmpty()) {
                clock = allRequests.get(0).getTimeOfArrival();
            } else {
                finishRequest(availableRequests.get(0));

            }
        }
        return sumOfHeadMovements;
    }


}
