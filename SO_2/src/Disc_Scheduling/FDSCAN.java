package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Disc_Scheduling.Direction.Direction;
import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;
import java.util.Collections;

public class FDSCAN extends DiscSchedulingAlgorithm {

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

        //regular request queue with arrival time considered
        ArrayList<DiscAccessRequest> availableRequests = new ArrayList<>();
        ArrayList<DiscAccessRequest> correctDirectionRequests = new ArrayList<>();

        //only priority request queue
        ArrayList<DiscAccessRequest> priorityRequests = new ArrayList<>();


        //priority request queue with arrival time considered
        ArrayList<DiscAccessRequest> availablePriority = new ArrayList<>();
        ArrayList<DiscAccessRequest> correctDirectionPriority = new ArrayList<>();

        requestsQueue.forEach(request -> {
            if (request.getExecutionDeadline() > 0)
                priorityRequests.add(request);
        });

        DiscAccessRequest currentRequest;

        while (!requestsQueue.isEmpty()) {

            requestsQueue.forEach(request -> {
                if (wasThereAnAccessRequestInLastCycle(request, prevClock, clock)) {
                    if (request.getExecutionDeadline() == -1) {
                        if (!availableRequests.contains(request)) {
                            availableRequests.add(request);
                        }
                    } else {
                        if (!availablePriority.contains(request)) {
                            availablePriority.add(request);
                        }
                        if (!correctDirectionPriority.contains(request)) {
                            if (currentDirection == Direction.RIGHT && currentHeadPosition <= request.getInitialAddress()) {
                                correctDirectionPriority.add(request);
                            } else if (currentDirection == Direction.LEFT && request.getInitialAddress() <= currentHeadPosition) {
                                correctDirectionPriority.add(request);
                            }
                        }
                    }
                }
            });

            availableRequests.forEach(request -> {
                if (currentDirection == Direction.RIGHT && currentHeadPosition <= request.getInitialAddress()) {
                    correctDirectionRequests.add(request);
                } else if (currentDirection == Direction.LEFT && request.getInitialAddress() <= currentHeadPosition) {
                    correctDirectionRequests.add(request);
                }
            });

            if (!correctDirectionPriority.isEmpty()) {
                currentRequest = findNearestRequests(correctDirectionPriority, currentHeadPosition);
            } else if (!availablePriority.isEmpty()) {
                if (currentDirection == Direction.RIGHT) {
                    currentDirection = Direction.LEFT;
                } else {
                    currentDirection = Direction.RIGHT;
                }
                continue;
            } else if (!correctDirectionRequests.isEmpty()) {
                currentRequest = findNearestRequests(correctDirectionRequests, currentHeadPosition);
            } else if (!availableRequests.isEmpty()) {
                if (currentDirection == Direction.RIGHT) {
                    prevClock = clock;
                    clock += (MAX_ADDRESS - currentHeadPosition) * HEAD_MOVE_TIME;
                    super.addToSumOfHeadMovements(MAX_ADDRESS - currentHeadPosition);
                    currentHeadPosition = MAX_ADDRESS;
                    currentDirection = Direction.LEFT;
                } else if (currentDirection == Direction.LEFT) {
                    prevClock = clock;
                    clock += (currentHeadPosition - 1) * HEAD_MOVE_TIME;
                    super.addToSumOfHeadMovements(currentHeadPosition - 1);
                    currentHeadPosition = 1;
                    currentDirection = Direction.RIGHT;
                }
                continue;
            } else {
                clock = requestsQueue.get(0).getTimeOfArrival();
                continue;
            }

            int requestComing = willRequestComeWithinThisCycle(currentRequest);

            if (requestComing != -1) {
                moveTo(requestComing, currentRequest);
            } else {
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

                    requestsQueue.remove(currentRequest);

                    if (currentRequest.getExecutionDeadline() == -1) {
                        availableRequests.remove(currentRequest);
                        correctDirectionRequests.remove(currentRequest);
                    } else {
                        priorityRequests.remove(currentRequest);
                        availablePriority.remove(currentRequest);
                        correctDirectionPriority.remove(currentRequest);
                    }
                }
                correctDirectionRequests.clear();
            }
        }
        return super.getSumOfHeadMovements();
    }

    private int calcHeadMovement(DiscAccessRequest req1) {
        int movement = req1.getInitialAddress() - currentHeadPosition;

        return Math.abs(movement);
    }

}
