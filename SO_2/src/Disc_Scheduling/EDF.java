package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;
import java.util.Collections;

public class EDF extends DiscSchedulingAlgorithm {

    public EDF(ArrayList<DiscAccessRequest> queue) {
        super(queue);
    }

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

        //regular request queue with arrival time considered
        ArrayList<DiscAccessRequest> availableRequests = new ArrayList<>();

        //only priority request queue
        ArrayList<DiscAccessRequest> priorityRequests = new ArrayList<>();

        //priority request queue with arrival time considered
        ArrayList<DiscAccessRequest> availablePriority = new ArrayList<>();

        if (!requestsQueue.isEmpty()) {
            requestsQueue.forEach(request -> {
                if (request.getExecutionDeadline() > 0)
                    priorityRequests.add(request);
            });
        }

        DiscAccessRequest currentRequest;

        while (!requestsQueue.isEmpty()) {

            requestsQueue.forEach(request -> {
                if (request.getExecutionDeadline() == -1 && wasThereAnAccessRequestInLastCycle(request, prevClock, clock)) {
                    availableRequests.add(request);
                }
            });

            Collections.sort(priorityRequests, DiscAccessRequest::compareByExecutionDeadline);

            priorityRequests.forEach(request -> {
                if (wasThereAnAccessRequestInLastCycle(request, prevClock, clock)) {
                    availablePriority.add(request);
                }

            });

            Collections.sort(availablePriority, DiscAccessRequest::compareByExecutionDeadline);

            if (!availablePriority.isEmpty()) {
                currentRequest = availablePriority.get(0);
            } else if (!availableRequests.isEmpty()) {
                currentRequest = availableRequests.get(0);
            } else {
                clock += requestsQueue.get(0).getTimeOfArrival();
                continue;
            }

            DiscAccessRequest smallerDeadlineComing = null;

            for (DiscAccessRequest request : priorityRequests) {
                if (request.getTimeOfArrival() < clock + (Math.abs(currentRequest.getInitialAddress() - currentHeadPosition) * HEAD_MOVE_TIME)) {
                    if (currentRequest.getExecutionDeadline() == -1) {
                        if (smallerDeadlineComing == null) {
                            smallerDeadlineComing = request;
                        } else if (request.getTimeOfArrival() < smallerDeadlineComing.getTimeOfArrival()) {
                            smallerDeadlineComing = request;
                        }
                    } else if (request.getExecutionDeadline() < currentRequest.getExecutionDeadline()) {
                        if (smallerDeadlineComing == null) {
                            smallerDeadlineComing = request;
                        } else if (request.getExecutionDeadline() < smallerDeadlineComing.getExecutionDeadline()) {
                            smallerDeadlineComing = request;
                        }
                    }
                }
            }

            if (smallerDeadlineComing != null) {
                int addressesToGo = (smallerDeadlineComing.getTimeOfArrival() - clock) / HEAD_MOVE_TIME;
                super.addToSumOfHeadMovements(addressesToGo);
                if (currentRequest.getInitialAddress() < currentHeadPosition)
                    currentHeadPosition -= addressesToGo;
                else
                    currentHeadPosition += addressesToGo;

                clock = smallerDeadlineComing.getTimeOfArrival();

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

                    //removing for regular requests
                    availableRequests.remove(currentRequest);
                    requestsQueue.remove(currentRequest);

                    //special removing for priority requests
                    if (currentRequest.getExecutionDeadline() > 0) {
                        priorityRequests.remove(currentRequest);
                        availablePriority.remove(currentRequest);
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
