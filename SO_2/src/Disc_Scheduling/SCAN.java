package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Disc_Scheduling.Direction.Direction;
import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;
import java.util.Collections;

public class SCAN extends DiscSchedulingAlgorithm {

    public SCAN(ArrayList<DiscAccessRequest> queue) {
        super(queue);
    }

    /**
     * Method implementing SCAN algorithm
     *
     * @return sum of disc head movements
     * @throws ImpossibleToSimulateException is thrown when requestsQueue is empty or null
     */
    @Override
    @SuppressWarnings("Duplicates")
    public long carryOutSimulation() throws ImpossibleToSimulateException {

        reset();

        ArrayList<DiscAccessRequest> requestsQueue = super.getRequestsQueue();

        //checking whether it is possible to carry out simulation with provided data
        if (isDataInvalid(requestsQueue)) {
            throw new ImpossibleToSimulateException();
        }


        //sorting requests by time of arrival
        Collections.sort(requestsQueue, DiscAccessRequest::compareByTimeOfArrival);

        //queue in which requests that already came will be held
        ArrayList<DiscAccessRequest> availableRequests = new ArrayList<>();

        //queue in which requests being ahead of disc head will be held
        ArrayList<DiscAccessRequest> requestsInCurrentDirection = new ArrayList<>();

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

                availableRequests.forEach(request -> {
                    //taking into consideration two cases, and adding requests to directional queue
                    //the head moves right
                    if (currentDirection == Direction.RIGHT) {
                        if (request.getInitialAddress() > currentHeadPosition)
                            requestsInCurrentDirection.add(request);
                        //the head moves left
                    } else {
                        if (request.getInitialAddress() < currentHeadPosition)
                            requestsInCurrentDirection.add(request);
                    }
                });

                //when directional queue is empty, a change of scanning direction occurs
                //taking into consideration two cases
                //adding the movement of head to the sum
                //adding time of movement
                if (requestsInCurrentDirection.isEmpty()) {
                    //the head moves right
                    if (currentDirection == Direction.RIGHT) {
                        currentDirection = Direction.LEFT;
                        prevClock = clock;
                        clock += (MAX_ADDRESS - currentHeadPosition) * HEAD_MOVE_TIME;
                        super.addToSumOfHeadMovements(MAX_ADDRESS - currentHeadPosition);
                        currentHeadPosition = MAX_ADDRESS;
                    } else { //the head moves left
                        currentDirection = Direction.RIGHT;
                        prevClock = clock;
                        clock += (currentHeadPosition - 1) * HEAD_MOVE_TIME;
                        super.addToSumOfHeadMovements(currentHeadPosition - 1);
                        currentHeadPosition = 1;

                    }
                    //the directional queue isn't empty
                } else {
                    //finding the nearest request in directional queue
                    currentRequest = findNearestRequests(requestsInCurrentDirection, currentHeadPosition);

                    //checking if new better accessible processes are coming until head arrives destination
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

                            //setting new position of head
                            currentHeadPosition = currentRequest.getInitialAddress();

                            //execution of request is finished
                            //so remove it from all queues
                            requestsInCurrentDirection.remove(currentRequest);
                            availableRequests.remove(currentRequest);
                            requestsQueue.remove(currentRequest);
                        }
                    } else{
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
