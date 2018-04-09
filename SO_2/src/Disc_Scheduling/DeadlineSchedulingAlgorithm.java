package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;

import java.util.ArrayList;

abstract class DeadlineSchedulingAlgorithm extends DiscSchedulingAlgorithm {

    ArrayList<DiscAccessRequest> priorityRequests = new ArrayList<>();
    ArrayList<DiscAccessRequest> availablePriorityRequests = new ArrayList<>();

    public DeadlineSchedulingAlgorithm(int startingPosition) {
        super(startingPosition);
    }

    void initializePriorityQueue() {
        if (!allRequests.isEmpty()) {
            allRequests.forEach(request -> {
                if (0 < request.getExecutionDeadline()) {
                    priorityRequests.add(request);
                }
            });
        }
    }

    @Override
    void updateAvailableQueue() {
        allRequests.forEach(request -> {
            if (wasThereAnAccessRequestInLastCycle(request, prevClock, clock)) {
                if (request.getExecutionDeadline() == -1) {
                    if (!availableRequests.contains(request)) {
                        availableRequests.add(request);
                    }
                } else {
                    if (!availablePriorityRequests.contains(request)) {
                        availablePriorityRequests.add(request);
                    }
                }
            }
        });
    }

    @Override
    void finishRequest(DiscAccessRequest currentRequest) {
        ArrayList<DiscAccessRequest> requestsToErase = findRequestsInBetweenToErase(currentRequest);

        for (DiscAccessRequest request : requestsToErase) {
            eraseRequest(request);
        }

        checkIfErasedPastDeadline(requestsToErase);
        super.finishRequest(currentRequest);
    }

    @Override
    void moveTo(int time, DiscAccessRequest currentRequest) {
        int nextPosition = currentHeadPosition;

        if (currentHeadPosition < currentRequest.getInitialAddress()) {
            nextPosition += (time - clock) / HEAD_MOVE_TIME;
        } else {
            nextPosition -= (time - clock) / HEAD_MOVE_TIME;
        }

        ArrayList<DiscAccessRequest> requestsToErase = findRequestsInBetweenToErase(new DiscAccessRequest(time, nextPosition));

        for (DiscAccessRequest request : requestsToErase) {
            eraseRequest(request);
        }
        checkIfErasedPastDeadline(requestsToErase);

        super.moveTo(time, currentRequest);
    }

    @SuppressWarnings("Duplicates")
    ArrayList<DiscAccessRequest> findRequestsInBetweenToErase(DiscAccessRequest currentRequest) {
        ArrayList<DiscAccessRequest> requestsToErase = new ArrayList<>();

        for (DiscAccessRequest request : availableRequests) {
            if (request != currentRequest) {
                if (currentHeadPosition < request.getInitialAddress() && request.getInitialAddress() <= currentRequest.getInitialAddress()) {
                    requestsToErase.add(request);
                } else if (currentRequest.getInitialAddress() <= request.getInitialAddress() && request.getInitialAddress() < currentHeadPosition) {
                    requestsToErase.add(request);
                }
            }
        }

        for (DiscAccessRequest request : availablePriorityRequests) {
            if (request != currentRequest) {
                if (currentHeadPosition < request.getInitialAddress() && request.getInitialAddress() <= currentRequest.getInitialAddress()) {
                    requestsToErase.add(request);
                } else if (currentRequest.getInitialAddress() <= request.getInitialAddress() && request.getInitialAddress() < currentHeadPosition) {
                    requestsToErase.add(request);
                }
            }
        }

        return requestsToErase;
    }


    @Override
    void eraseRequest(DiscAccessRequest request) {
        super.eraseRequest(request);
        priorityRequests.remove(request);
        availablePriorityRequests.remove(request);
    }

    void checkIfErasedPastDeadline(ArrayList<DiscAccessRequest> requestsToErase) {
        requestsToErase.forEach(request -> {
            if (0 < request.getExecutionDeadline()) {
                if (request.getExecutionDeadline() < clock + ((request.getInitialAddress() - currentHeadPosition) * HEAD_MOVE_TIME)) {
                    executedAfterDeadline++;
                }
            }
        });
    }
}
