package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Disc_Scheduling.Direction.Direction;
import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;
import java.util.Collections;

public class FDSCAN extends DeadlineSchedulingAlgorithm {

    ArrayList<DiscAccessRequest> availableCorrectSide = new ArrayList<>();
    ArrayList<DiscAccessRequest> availablePriorityCorrectSide = new ArrayList<>();

    public FDSCAN(int startingPosition) {
        super(startingPosition);
    }

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

        initializePriorityQueue();


        while (!allRequests.isEmpty()) {

            updateAvailableQueue();

            availableRequests.forEach(request -> {
                if (!availableCorrectSide.contains(request)) {
                    if (MovingSchedulingAlgorithm.checkIfInRightDirection(currentDirection, currentHeadPosition, request)) {
                        availableCorrectSide.add(request);
                    }
                }
            });

            availablePriorityRequests.forEach(request -> {
                if (!availablePriorityCorrectSide.contains(request)) {
                    if (MovingSchedulingAlgorithm.checkIfInRightDirection(currentDirection, currentHeadPosition, request)) {
                        availablePriorityCorrectSide.add(request);
                    }
                }
            });

            DiscAccessRequest currentRequest;

            if (!availablePriorityCorrectSide.isEmpty()) {
                currentRequest = findNearestRequests(availablePriorityCorrectSide);
            } else if (!availablePriorityRequests.isEmpty()) {
                changeLOOKDirection();
                continue;
            } else if(!availableCorrectSide.isEmpty()){
                currentRequest = findNearestRequests(availableCorrectSide);
            } else if(!availableRequests.isEmpty()){
                changeSCANDirection();
                continue;
            } else {
                clock = allRequests.get(0).getTimeOfArrival();
                continue;
            }


            int requestComing = willRequestComeWithinThisCycle(currentRequest.getInitialAddress());

            if (requestComing != -1) {
                moveTo(requestComing, currentRequest);
            } else {
                finishRequest(currentRequest);
            }

                availableCorrectSide.clear();
                availablePriorityCorrectSide.clear();

        }
        return sumOfHeadMovements;
    }

    private void changeLOOKDirection() {
        if (currentDirection == Direction.RIGHT) {
            currentDirection = Direction.LEFT;
        } else {
            currentDirection = Direction.RIGHT;
        }
    }

    private void changeSCANDirection(){

        if (currentDirection == Direction.RIGHT) {
            currentDirection = Direction.LEFT;
            prevClock = clock;
            clock += (MAX_ADDRESS - currentHeadPosition) * HEAD_MOVE_TIME;
            sumOfHeadMovements += MAX_ADDRESS - currentHeadPosition;
            currentHeadPosition = MAX_ADDRESS;
        } else if (currentDirection == Direction.LEFT){
            currentDirection = Direction.RIGHT;
            prevClock = clock;
            clock += (currentHeadPosition - 1) * HEAD_MOVE_TIME;
            sumOfHeadMovements += currentHeadPosition - 1;
            currentHeadPosition = 1;

        }
    }
}
