package Page_Replacing;

import Pages.Page;
import Simulation.Assumptions;

import java.util.ArrayList;

public class LRU extends PageReplacingAlgorithm{

    private Page[] arrayOfPages;
    private int nOfFrames;
    private ArrayList<Integer> ordersQueue;

    public LRU(Assumptions data) {
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

            for(Page p : pagesInFrames){
                if(p.getNumberOfPage() == currentOrder){
                    didItArrive = true;
                    p.setTimeOfLastUsage(0);
                }
            }

            if(didItArrive){
                ordersQueue.remove(0);
                for(Page p : pagesInFrames){
                    int clock = p.getTimeOfLastUsage();
                    p.setTimeOfLastUsage(clock + 1);
                }
            } else {
                pageErrors++;
                if(pagesInFrames.size() == nOfFrames){
                    int latestTimeOfUsage = 0;
                    int latestUsedPage = pagesInFrames.get(0).getNumberOfPage();

                    for(Page p : pagesInFrames){
                        if(p.getTimeOfLastUsage() > latestTimeOfUsage){
                            latestTimeOfUsage = p.getTimeOfLastUsage();
                            latestUsedPage = p.getNumberOfPage();
                        }
                    }

                    for(Page p : pagesInFrames){
                        if(p.getNumberOfPage() == latestUsedPage){
                            pagesInFrames.remove(p);
                            break;
                        }
                    }
                }

                for(Page p : pagesInFrames){
                    int clock = p.getTimeOfLastUsage();
                    p.setTimeOfLastUsage(clock+1);
                }
                pagesInFrames.add(arrayOfPages[currentOrder]);
            }
        }
        return pageErrors;
    }
}
