package Page_Replacing;

import Pages.Page;
import Simulation.Assumptions;

import java.util.ArrayList;

public class ALRU extends PageReplacingAlgorithm {

    private Page[] arrayOfPages;
    private int nOfFrames;
    private ArrayList<Integer> ordersQueue;

    public ALRU(Assumptions data) {
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
                    p.setWasUsedRecently(true);
                }
            }

            if (didItArrive) {
                ordersQueue.remove(0);
            } else {

                pageErrors++;

                if (pagesInFrames.size() == nOfFrames) {
                    int pageToDelete = pagesInFrames.get(0).getNumberOfPage();

                    for (int p = 0; p < pagesInFrames.size() - 1; p++) {
                        if (pagesInFrames.get(p).wasUsedRecently()) {
                            pagesInFrames.get(p).setWasUsedRecently(false);

                            Page current = pagesInFrames.get(p);
                            pagesInFrames.remove(p);
                            pagesInFrames.add(current);
                            p--;

                        }
                    }

                    for (int i = 0; i < pagesInFrames.size() - 1; i++) {
                        if (!pagesInFrames.get(i).wasUsedRecently()) {
                            pageToDelete = pagesInFrames.get(i).getNumberOfPage();
                            break;
                        }
                    }

                    for (Page p : pagesInFrames) {
                        if (p.getNumberOfPage() == pageToDelete) {
                            pagesInFrames.remove(p);
                            break;
                        }
                    }
                }
                pagesInFrames.add(arrayOfPages[currentOrder]);
                pagesInFrames.get(pagesInFrames.size() - 1).setWasUsedRecently(true);
            }
        }
        return pageErrors;
    }
}
