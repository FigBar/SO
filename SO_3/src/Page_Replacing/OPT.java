package Page_Replacing;

import Pages.Page;
import Simulation.Assumptions;

import java.util.ArrayList;

public class OPT extends PageReplacingAlgorithm {

    private Page[] arrayOfPages;
    private int nOfFrames;
    private ArrayList<Integer> ordersQueue;

    public OPT(Assumptions data) {
        this.nOfFrames = data.getnOfFrames();
        this.arrayOfPages = data.getArrayOfPages();
        this.ordersQueue = data.getOrders();
    }

    public int simulate() {
        int pageErrors = 0;

        ArrayList<Page> pagesInFrames = new ArrayList<>();

        while (!ordersQueue.isEmpty()) {
            int currentOrder = ordersQueue.get(0);
            boolean didItArrive = false;

            for (Page p : pagesInFrames) {
                if (p.getNumberOfPage() == currentOrder) {
                    didItArrive = true;
                }
            }

            if (didItArrive) {
                ordersQueue.remove(0);
            } else {
                pageErrors++;

                if (pagesInFrames.size() == nOfFrames) {

                    int howSoonWillItRepeat = 0;
                    int nextOccurrenceOfOrder = 0;
                    int latestOrder = 0;

                    for(Page p : pagesInFrames){
                        if(ordersQueue.contains(p.getNumberOfPage())){
                            nextOccurrenceOfOrder = ordersQueue.indexOf(p.getNumberOfPage());
                            if(nextOccurrenceOfOrder > howSoonWillItRepeat){
                                howSoonWillItRepeat = nextOccurrenceOfOrder;
                                latestOrder = p.getNumberOfPage();
                            }
                        } else {
                            latestOrder = p.getNumberOfPage();
                            break;
                        }
                    }

                    for(Page p : pagesInFrames){
                        if(p.getNumberOfPage() == latestOrder){
                            pagesInFrames.remove(p);
                            break;
                        }
                    }
                }

                pagesInFrames.add(arrayOfPages[currentOrder]);

            }

        }
        return pageErrors;
    }
}
