package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Disc_Scheduling.Direction.Direction;
import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;
import java.util.Collections;

abstract class MovingSchedulingAlgorithm extends DiscSchedulingAlgorithm {

    ArrayList<DiscAccessRequest> requestsInRightDirection = new ArrayList<>();

    public MovingSchedulingAlgorithm(int startingPosition) {
        super(startingPosition);
    }


    /**
     * This method, checks in which direction head is going, then adds only requests on this side, from all available
     * requests queue, to requests on correct side queue
     */

    void addOrdersOnCorrectSide() {
        availableRequests.forEach(request -> {
            if (!requestsInRightDirection.contains(request)) {
                if (currentDirection == Direction.RIGHT && currentHeadPosition <= request.getInitialAddress()) {
                    requestsInRightDirection.add(request);
                } else if (currentDirection == Direction.LEFT && request.getInitialAddress() <= currentHeadPosition) {
                    requestsInRightDirection.add(request);
                }
            }
        });
    }

    abstract void onEdge();

    @Override
    public long carryOutSimulation(ArrayList<DiscAccessRequest> requestsQueue) throws ImpossibleToSimulateException {

        reset();

        //checking whether it is possible to carry out simulation with provided data
        if (isDataInvalid(requestsQueue)) {
            throw new ImpossibleToSimulateException();
        }

        Collections.sort(requestsQueue, DiscAccessRequest::compareByTimeOfArrival);
        allRequests = requestsQueue;

        while (!allRequests.isEmpty()) {

            updateAvailableQueue();

            if (availableRequests.isEmpty()) {
                clock = allRequests.get(0).getTimeOfArrival();
            } else {
                addOrdersOnCorrectSide();

                if (requestsInRightDirection.isEmpty()) {
                    int newRequestComing = willRequestComeWithinThisCycle(MAX_ADDRESS);

                    if (newRequestComing == -1) {
                        onEdge();
                    } else {
                        if (currentDirection == Direction.RIGHT) {
                            moveTo(newRequestComing, new DiscAccessRequest(MAX_ADDRESS, 0));
                        } else {
                            moveTo(newRequestComing, new DiscAccessRequest(1, 0));
                        }
                    }
                } else {
                    DiscAccessRequest currentRequest = findNearestRequests(requestsInRightDirection);

                    int newRequestComing = willRequestComeWithinThisCycle(currentRequest.getInitialAddress());

                    if (newRequestComing == -1) {
                        finishRequest(currentRequest);
                    } else {
                        moveTo(newRequestComing, currentRequest);
                    }
                }
            }

        }


        return sumOfHeadMovements;
    }

    public static boolean checkIfInRightDirection(Direction currentDirection, int currentPosition, DiscAccessRequest toGo) {
        if (currentDirection == Direction.RIGHT && currentPosition <= toGo.getInitialAddress()) {
            return true;
        } else if (currentDirection == Direction.LEFT && toGo.getInitialAddress() <= currentPosition) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    void eraseRequest(DiscAccessRequest request) {
        super.eraseRequest(request);
        requestsInRightDirection.remove(request);
    }

    @Override
    void reset() {
        super.reset();
        requestsInRightDirection = new ArrayList<>();
    }
}
