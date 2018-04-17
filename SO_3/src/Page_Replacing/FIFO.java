package Page_Replacing;

import Pages.Page;
import Simulation.Assumptions;

import java.util.ArrayList;

public class FIFO extends PageReplacingAlgorithm {

    private Page[] arrayOfPages;
    private int nOfFrames;
    private ArrayList<Integer> ordersQueue;

    public FIFO(Assumptions data){
        this.nOfFrames = data.getnOfFrames();
        this.arrayOfPages = data.getArrayOfPages();
        this.ordersQueue = data.getOrders();
    }

    public int simulate(){
        int pageErrors = 0;

        ArrayList<Page> pagesInFrames = new ArrayList<>();

        while(!ordersQueue.isEmpty()){
            int currentOrder = ordersQueue.get(0);

            boolean didItArrive = false;

            for(Page p : pagesInFrames){
                if(p.getNumberOfPage() == currentOrder){
                    didItArrive = true;
                }
            }

            if(didItArrive){
                ordersQueue.remove(0);
            } else {
                pageErrors ++;
                if(pagesInFrames.size() == nOfFrames){
                    pagesInFrames.remove(0);
                }
                pagesInFrames.add(arrayOfPages[currentOrder]);
            }
        }
        return pageErrors;
    }
}
