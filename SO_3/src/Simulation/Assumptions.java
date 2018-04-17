package Simulation;

import Pages.Page;

import java.util.ArrayList;
import java.util.Random;

public class Assumptions {

    private Page[] arrayOfPages;
    private int nOfFrames;
    private ArrayList<Integer> orders;

    public Assumptions(int nOfPages, int nOfFrames, int nOfOrders) {
        this.nOfFrames = nOfFrames;
        this.arrayOfPages = new Page[nOfPages];
        for (int i = 0; i < arrayOfPages.length; i++) {
            arrayOfPages[i] = new Page(i);
        }
        this.orders = localityOfOrders(nOfPages, nOfOrders);

    }

    @SuppressWarnings("Duplicates")
    private ArrayList<Integer> localityOfOrders(int nOfPages, int nOfOrders) {
        ArrayList<Integer> ordersQueue = new ArrayList<>();

        Random generator = new Random();

        int firstPage = generator.nextInt(nOfPages);
        ordersQueue.add(firstPage);

        int mediumJump = nOfPages / 4;
        int bigJump = nOfPages / 2;

        int lastAddedPage = firstPage;

        for (int i = 1; i <= nOfOrders; i++) {
            double probability = Math.random() * 100;
            double probability2 = Math.random() * 100;
            double probability3 = Math.random() * 100;


            if (probability2 <= 50) {
                if (probability >= 0 && probability < 90) {
                    if (probability3 <= 50) {
                        ordersQueue.add(lastAddedPage);
                    } else {
                        if (lastAddedPage == nOfPages - 1) {
                            ordersQueue.add(0);
                            lastAddedPage = 0;
                        } else {
                            lastAddedPage++;
                            ordersQueue.add(lastAddedPage);
                        }
                    }
                } else if (probability >= 90 && probability < 99) {
                    if (lastAddedPage + mediumJump > nOfPages - 1) {
                        int howMuchToGoFromZero = lastAddedPage + mediumJump - (nOfPages - 1);
                        ordersQueue.add(howMuchToGoFromZero - 1);
                        lastAddedPage = howMuchToGoFromZero - 1;
                    } else {
                        lastAddedPage = lastAddedPage + mediumJump;
                        ordersQueue.add(lastAddedPage);
                    }
                } else if (probability >= 99 && probability < 100) {
                    if (lastAddedPage + bigJump > nOfPages - 1) {
                        int howMuchToGoFromZero = lastAddedPage + bigJump - (nOfPages - 1);
                        ordersQueue.add(howMuchToGoFromZero - 1);
                        lastAddedPage = howMuchToGoFromZero - 1;
                    } else {
                        lastAddedPage = lastAddedPage + bigJump;
                        ordersQueue.add(lastAddedPage);
                    }
                }
            } else {
                if (probability >= 0 && probability < 90) {
                    if (probability3 <= 50) {
                        ordersQueue.add(lastAddedPage);
                    } else {
                        if (lastAddedPage == 0) {
                            ordersQueue.add(nOfPages - 1);
                            lastAddedPage = nOfPages - 1;
                        } else {
                            lastAddedPage--;
                            ordersQueue.add(lastAddedPage);
                        }
                    }
                } else if (probability >= 90 && probability < 99) {
                    if (lastAddedPage - mediumJump < 0) {
                        int howMuchToGoFromEnd = mediumJump - lastAddedPage;
                        ordersQueue.add(nOfPages - howMuchToGoFromEnd);
                        lastAddedPage = nOfPages - howMuchToGoFromEnd;
                    } else {
                        lastAddedPage = lastAddedPage - mediumJump;
                        ordersQueue.add(lastAddedPage);
                    }
                } else if (probability >= 99 && probability < 100) {
                    if (lastAddedPage - bigJump < 0) {
                        int howMuchToGoFromEnd = bigJump - lastAddedPage;
                        ordersQueue.add(nOfPages - howMuchToGoFromEnd);
                        lastAddedPage = nOfPages - howMuchToGoFromEnd;
                    } else {
                        lastAddedPage = lastAddedPage - bigJump;
                        ordersQueue.add(lastAddedPage);
                    }
                }

            }
        }
        return ordersQueue;
    }

    public Assumptions(Page[] arrayOfPages, int nOfFrames, ArrayList<Integer> orders) {
        this.nOfFrames = nOfFrames;
        this.arrayOfPages = arrayOfPages;
        this.orders = new ArrayList<>();

        this.orders.addAll(orders);
    }

    public Assumptions(Assumptions data) {
        this.nOfFrames = data.getnOfFrames();
        this.arrayOfPages = data.getArrayOfPages();
        this.orders = new ArrayList<>();

        this.orders.addAll(data.getOrders());
    }

    public Page[] getArrayOfPages() {
        return arrayOfPages;
    }

    public void setArrayOfPages(Page[] arrayOfPages) {
        this.arrayOfPages = arrayOfPages;
    }

    public int getnOfFrames() {
        return nOfFrames;
    }

    public void setnOfFrames(int nOfFrames) {
        this.nOfFrames = nOfFrames;
    }

    public ArrayList<Integer> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Integer> orders) {
        this.orders = orders;
    }
}
