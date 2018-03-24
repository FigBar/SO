package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;
import java.util.Collections;

public class CSCAN extends DiscSchedulingAlgorithm {

    /**
     * Method implementing CSCAN algorithm
     *
     * @param requestsQueue queue of disc access requests
     * @return sum of disc head movements
     * @throws ImpossibleToSimulateException is thrown when requestsQueue is empty or null
     */
    @Override
    @SuppressWarnings("Duplicates")
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

        //queue in which requests being ahead on the right of disc head will be held
        ArrayList<DiscAccessRequest> requestsInDirection = new ArrayList<>();

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

                //checking if there are requests
                //ahead on the right of disc head
                //and adding them to directional queue
                availableRequests.forEach(request -> {
                    if (request.getInitialAddress() > currentHeadPosition)
                        requestsInDirection.add(request);
                });

                //when there are no requests ahead of disc head
                //it has to come  back to the start
                if (requestsInDirection.isEmpty()) {
                    DiscAccessRequest helper = new DiscAccessRequest(MAX_ADDRESS, 0);
                    int newRequestComing = willRequestComeWithinThisCycle(helper);
                    if(newRequestComing == -1) {
                        prevClock = clock;
                        clock += ((MAX_ADDRESS - currentHeadPosition) + 1) * HEAD_MOVE_TIME;
                        super.addToSumOfHeadMovements((MAX_ADDRESS - currentHeadPosition) + 1);
                        currentHeadPosition = 1;
                    } else{
                        moveTo(newRequestComing, helper);
                    }
                } else { //there are requests ahead of disc head

                    //finding the nearest request in directional queue
                    currentRequest = findNearestRequests(requestsInDirection, currentHeadPosition);

                    int newRequestComing = willRequestComeWithinThisCycle(currentRequest);

                    if(newRequestComing == -1) {

                        //checking if the request is valid
                        if (isTheAccessRequestValid(currentRequest)) {

                            //calculating head movement and adding it to sum
                            super.addToSumOfHeadMovements(calcHeadMovement(currentRequest));

                            //start of cycle
                            prevClock = clock;
                            //end of cycle
                            clock += calcHeadMovement(currentRequest) * HEAD_MOVE_TIME;

                            // check for requests with deadlines
                            notExecutedBeforeDeadline(currentRequest);

                            //setting new position of disc head
                            currentHeadPosition = currentRequest.getInitialAddress();

                            //request execution is finished
                            //so remove it from all queues
                            requestsInDirection.remove(currentRequest);
                            availableRequests.remove(currentRequest);
                            requestsQueue.remove(currentRequest);
                        }
                    } else {
                        moveTo(newRequestComing,currentRequest);
                    }
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
