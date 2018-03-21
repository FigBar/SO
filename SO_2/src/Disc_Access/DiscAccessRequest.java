package Disc_Access;

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
        this.executionDeadline = executionDeadline;
    }

    private static int assignAccessNumber(){
        return ++accessNumber;
    }

    public static int compareByTimeOfArrival(DiscAccessRequest req1, DiscAccessRequest req2){
        return Integer.compare(req1.timeOfArrival, req2.timeOfArrival);
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
