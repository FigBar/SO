package Pages;

public class Page {

    private int numberOfPage;
    private int timeOfArrival;
    private int timeOfLastUsage;

    private boolean wasUsedRecently;

    public Page(int numberOfPage){
        this.numberOfPage = numberOfPage;
        this.timeOfArrival = 0;
        this.timeOfLastUsage = 0;
        this.wasUsedRecently = false;
    }

    public Page(int numberOfPage, int timeOfArrival){
        this.numberOfPage = numberOfPage;
        this.timeOfArrival = timeOfArrival;
        this.timeOfLastUsage = 0;
        this.wasUsedRecently = false;
    }

    public boolean wasUsedRecently() {
        return wasUsedRecently;
    }

    public void setWasUsedRecently(boolean wasUsedRecently) {
        this.wasUsedRecently = wasUsedRecently;
    }

    public int getNumberOfPage() {
        return numberOfPage;
    }

    public void setNumberOfPage(int numberOfPage) {
        this.numberOfPage = numberOfPage;
    }

    public int getTimeOfArrival() {
        return timeOfArrival;
    }

    public void setTimeOfArrival(int timeOfArrival) {
        this.timeOfArrival = timeOfArrival;
    }

    public int getTimeOfLastUsage() {
        return timeOfLastUsage;
    }

    public void setTimeOfLastUsage(int timeOfLastUsage) {
        this.timeOfLastUsage = timeOfLastUsage;
    }
}
