package Disc_Access;

import Disc_Scheduling.DiscSchedulingAlgorithm;

public class DiscAccessRequest {

    private static int accessNumber = 0;

    private int initialAddress;
    private int timeOfArrival;

    private int executionDeadline = -1;

    public DiscAccessRequest(int initialAddress,int timeOfArrival){

        accessNumber = assignAccessNumber();
        this.initialAddress = initialAddress;
        this.timeOfArrival = timeOfArrival;
    }

    public DiscAccessRequest(int initialAddress, int timeOfArrival, int executionDeadline){
        this(initialAddress, timeOfArrival);
        this.executionDeadline = timeOfArrival + executionDeadline;
    }

    private static int assignAccessNumber(){
        return ++accessNumber;
    }

    public static int compareByInitialAddress(DiscAccessRequest req1, DiscAccessRequest req2){
        return Integer.compare(req1.initialAddress,req2.initialAddress);
    }

    public static int compareByTimeOfArrival(DiscAccessRequest req1, DiscAccessRequest req2){
        return Integer.compare(req1.timeOfArrival, req2.timeOfArrival);
    }

    public static int compareByExecutionDeadline(DiscAccessRequest req1, DiscAccessRequest req2){
        return Integer.compare(req1.executionDeadline, req2.executionDeadline);
    }

    public int getInitialAddress() {
        return initialAddress;
    }

    public void setInitialAddress(int initialAddress) {
        this.initialAddress = initialAddress;
    }

    public int getTimeOfArrival() {
        return timeOfArrival;
    }

    public void setTimeOfArrival(int timeOfArrival) {
        this.timeOfArrival = timeOfArrival;
    }

    public int getExecutionDeadline() {
        return executionDeadline;
    }

    public void setExecutionDeadline(int executionDeadline) {
        this.executionDeadline = executionDeadline;
    }
}
