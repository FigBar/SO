package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;

import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;
import java.util.Collections;

public class FCFS extends DiscSchedulingAlgorithm {


    /**
     * Method implementing FCFS algorithm.
     *
     * @param requestsQueue queue of disc access requests
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

        this.requestsQueue = requestsQueue;

        //sorting requests by time of arrival
        Collections.sort(requestsQueue, DiscAccessRequest::compareByTimeOfArrival);

        //queue in which requests that already came will be held
        ArrayList<DiscAccessRequest> availableRequests = new ArrayList<>();

        DiscAccessRequest currentRequest;

        //the loop is executed until the queue is empty
        while (!requestsQueue.isEmpty()) {

            //checking whether time of arrival of requests has come and adding
            //them to available queue if yes
            requestsQueue.forEach(request -> {
                if (wasThereAnAccessRequestInLastCycle(request, prevClock, clock)) {
                    availableRequests.add(request);
                }
            });

            //checking whether there's a request in available queue
            //if not skipping to time when the first request arrives
            if (availableRequests.isEmpty()) {
                clock = requestsQueue.get(0).getTimeOfArrival();
            } else {

                //taking the oldest request in the available queue
                currentRequest = availableRequests.get(0);

                //checking if the request is addressable
                if (isTheAccessRequestValid(currentRequest)) {

                    //calculating the movement of disk head
                    //which is | request initial address - current head position |
                    //then adding it to the sum of head movements
                    super.addToSumOfHeadMovements(calcHeadMovement(currentRequest));

                    //setting start of cycle time;
                    prevClock = clock;

                    //setting end of cycle time;
                    clock += calcHeadMovement(currentRequest) * HEAD_MOVE_TIME;

                    // check for requests with deadlines
                    notExecutedBeforeDeadline(currentRequest);

                    //setting the head's position on current request's initial address
                    currentHeadPosition = currentRequest.getInitialAddress();

                    //requests is handled so removing it from both queues
                    availableRequests.remove(currentRequest);
                    requestsQueue.remove(currentRequest);

                }
            }
        }
        return super.getSumOfHeadMovements();
    }

    private int calcHeadMovement(DiscAccessRequest req1) {
        int movement = req1.getInitialAddress() - currentHeadPosition;

        return Math.abs(movement);
    }


}
