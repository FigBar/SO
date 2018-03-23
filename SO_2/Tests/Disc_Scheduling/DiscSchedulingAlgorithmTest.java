package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Disc_Scheduling.Direction.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DiscSchedulingAlgorithmTest {

    @BeforeEach
    void setUp() {

        DiscSchedulingAlgorithm.currentHeadPosition = 22;
    }

    @Test
    void FCFSTest(){
        assertEquals(559, (new FCFS()).carryOutSimulation(createQueue()));
        DiscSchedulingAlgorithm.currentHeadPosition = 53;

        //test with arrival times
        //0 -> 11 -> 63 -> 840 -> 1531 -> 1582 -> 1727 -> 1828 -> 1877 -> 1945 -> 2805 -> 3734
        assertEquals(3734, (new FCFS()).carryOutSimulation(createArrivingTestOrders()));
    }

    @Test
    void SSTFTest(){
        assertEquals(225, (new SSTF()).carryOutSimulation(createQueue()));

        DiscSchedulingAlgorithm.currentHeadPosition = 53;
        //test with arrival times
        //0 -> 11 -> 63 -> 71 -> 165 -> 172 -> 216 -> 856 -> 1051 -> 1911 -> 1979 -> 1980
        assertEquals(1980, (new SSTF()).carryOutSimulation(createArrivingTestOrders()));
    }

    @Test
    void SCANTest(){
        SCAN scan1 = new SCAN();
        scan1.currentDirection = Direction.LEFT;
        assertEquals(229, scan1.carryOutSimulation(createQueue()));


        //test with arrival times
        SCAN scan2 = new SCAN();

        DiscSchedulingAlgorithm.currentHeadPosition = 53;
        scan2.currentDirection = Direction.RIGHT;
        //0 -> 11 -> 96 -> 736 -> 963 -> 1823 -> 1842 -> 1849 -> 1891 -> 1892 -> 1935 -> 1943
        assertEquals(1943, scan2.carryOutSimulation(createArrivingTestOrders()));
    }

    @Test
    void CSCANTest(){
        assertEquals(990, (new CSCAN()).carryOutSimulation(createQueue()));

        //test with arrival times
        DiscSchedulingAlgorithm.currentHeadPosition = 53;
        //0 -> 11 -> 948 -> 951 -> 959 -> 1003 -> 1045 -> 1052 -> 1071 -> 1096 -> 1736 -> 1931 -> 1948 -> 2002
        assertEquals(2002, (new CSCAN()).carryOutSimulation(createArrivingTestOrders()));
    }

    @Test
    void FDSCANTest(){

        DiscSchedulingAlgorithm.currentHeadPosition = 53;

        assertEquals(2448, (new FDSCAN()).carryOutSimulation(createPriorityTestRequests()));

        //TODO test 1 priority requests not executed in time

    }

    @Test
    void EDFTest(){
        DiscSchedulingAlgorithm.currentHeadPosition = 53;

        assertEquals(2990, (new EDF()).carryOutSimulation(createPriorityTestRequests()));

        //TODO test 2 priority requests not executed in time
    }

    private ArrayList<DiscAccessRequest> createQueue(){
        ArrayList<DiscAccessRequest> requestsQueue = new ArrayList<>();
        requestsQueue.add(new DiscAccessRequest(24,0));
        requestsQueue.add(new DiscAccessRequest(12,0));
        requestsQueue.add(new DiscAccessRequest(99,0));
        requestsQueue.add(new DiscAccessRequest(174,0));
        requestsQueue.add(new DiscAccessRequest(5,0));
        requestsQueue.add(new DiscAccessRequest(209,0));
        requestsQueue.add(new DiscAccessRequest(199,0));

        return requestsQueue;
    }


    private ArrayList<DiscAccessRequest> createArrivingTestOrders() {
        ArrayList<DiscAccessRequest> ordersQueue = new ArrayList<>();
        ordersQueue.add(new DiscAccessRequest(64, 0));
        ordersQueue.add(new DiscAccessRequest(12, 0));
        ordersQueue.add(new DiscAccessRequest(789, 14));
        ordersQueue.add(new DiscAccessRequest(98, 62));
        ordersQueue.add(new DiscAccessRequest(149, 62));
        ordersQueue.add(new DiscAccessRequest(4, 62));
        ordersQueue.add(new DiscAccessRequest(105, 148));
        ordersQueue.add(new DiscAccessRequest(56, 257));
        ordersQueue.add(new DiscAccessRequest(124, 257));
        ordersQueue.add(new DiscAccessRequest(984, 670));
        ordersQueue.add(new DiscAccessRequest(55, 1000));

        return ordersQueue;
    }

    private ArrayList<DiscAccessRequest> createPriorityTestRequests() {
        ArrayList<DiscAccessRequest> requestsQueue = new ArrayList<>();

        requestsQueue.add(new DiscAccessRequest(109,0));
        requestsQueue.add(new DiscAccessRequest(69,0,10));
        requestsQueue.add(new DiscAccessRequest(54,0,30));
        requestsQueue.add(new DiscAccessRequest(564,23,1000));
        requestsQueue.add(new DiscAccessRequest(614,25,900));
        requestsQueue.add(new DiscAccessRequest(314,1050));
        requestsQueue.add(new DiscAccessRequest(505,1100));
        requestsQueue.add(new DiscAccessRequest(813,1200, 2000));
        requestsQueue.add(new DiscAccessRequest(5,1200));

        return requestsQueue;
    }
}