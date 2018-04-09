package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Exceptions.ImpossibleToSimulateException;

import java.util.ArrayList;
import java.util.Collections;

public class EDF extends DeadlineSchedulingAlgorithm {

    public EDF(int startingPosition) {
        super(startingPosition);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public long carryOutSimulation(ArrayList<DiscAccessRequest> requestsQueue) throws ImpossibleToSimulateException {

        reset();

        //checking whether it is possible to carry out simulation with provided data
        if (isDataInvalid(requestsQueue)) {
            throw new ImpossibleToSimulateException();
        }

        Collections.sort(requestsQueue, DiscAccessRequest::compareByTimeOfArrival);

        allRequests = requestsQueue;
        initializePriorityQueue();


        while (!requestsQueue.isEmpty()) {

            updateAvailableQueue();

            Collections.sort(availablePriorityRequests, DiscAccessRequest::compareByExecutionDeadline);

            DiscAccessRequest currentRequest;

            if (!availablePriorityRequests.isEmpty()) {
                currentRequest = availablePriorityRequests.get(0);
            } else if (!availableRequests.isEmpty()) {
                currentRequest = availableRequests.get(0);
            } else {
                clock = requestsQueue.get(0).getTimeOfArrival();
                continue;
            }

            int deadlineComing = willRequestComeWithinThisCycle(currentRequest.getInitialAddress());

            if (deadlineComing != -1) {
                moveTo(deadlineComing, currentRequest);
            } else {
                finishRequest(currentRequest);
            }


        }
        return sumOfHeadMovements;
    }


}
