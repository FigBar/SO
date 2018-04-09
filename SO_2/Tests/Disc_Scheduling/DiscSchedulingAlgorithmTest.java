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
    void FCFSTest() {
        assertEquals(559, (new FCFS(22)).carryOutSimulation(createQueue()));
        DiscSchedulingAlgorithm.currentHeadPosition = 53;

        //test with arrival times
        //0 -> 11 -> 63 -> 840 -> 1531 -> 1582 -> 1727 -> 1828 -> 1877 -> 1945 -> 2805 -> 3734
        assertEquals(3734, (new FCFS(53)).carryOutSimulation(createArrivingTestOrders()));
    }

    @Test
    void SSTFTest() {
        assertEquals(225, (new SSTF(22)).carryOutSimulation(createQueue()));


        //test with arrival times
        //0 -> 11 -> 63 -> 71 -> 165 -> 172 -> 216 -> 856 -> 1051 -> 1911 -> 1979 -> 1980
        assertEquals(2248, (new SSTF(53)).carryOutSimulation(createArrivingTestOrders()));



        assertEquals(447, (new SSTF(53)).carryOutSimulation(sneakyRequests()));
    }

    @Test
    void SCANTest() {
        SCAN scan1 = new SCAN(22);
        scan1.currentDirection = Direction.LEFT;


        assertEquals(1973, scan1.carryOutSimulation(createQueue()));


        //test with arrival times
        SCAN scan2 = new SCAN(53);


        scan2.currentDirection = Direction.RIGHT;
        //0 -> 11 -> 96 -> 736 -> 963 -> 1823 -> 1842 -> 1849 -> 1891 -> 1892 -> 1935 -> 1943
        assertEquals(1943, scan2.carryOutSimulation(createArrivingTestOrders()));

        SCAN scan3= new SCAN(53);
        assertEquals(447, scan3.carryOutSimulation(sneakyRequests()));
    }

    @Test
    void CSCANTest() {
        assertEquals(990, (new CSCAN(22)).carryOutSimulation(createQueue()));

        //test with arrival times

        //0 -> 11 -> 948 -> 951 -> 959 -> 1003 -> 1045 -> 1052 -> 1071 -> 1096 -> 1736 -> 1931 -> 1948 -> 2002
        assertEquals(1071, (new CSCAN(53)).carryOutSimulation(createArrivingTestOrders()));


        assertEquals(447, (new CSCAN(53)).carryOutSimulation(sneakyRequests()));
    }

    @Test
    void FDSCANTest() {


        assertEquals(1942, (new FDSCAN(53)).carryOutSimulation(createPriorityTestRequests()));

        //TODO test 1 priority requests not executed in time

    }

    @Test
    void EDFTest() {

        EDF edf1 = new EDF(53);


        assertEquals(1868, edf1.carryOutSimulation(createPriorityTestRequests()));
        assertEquals(1, edf1.getExecutedAfterDeadline());


    }

    private ArrayList<DiscAccessRequest> createQueue() {
        ArrayList<DiscAccessRequest> requestsQueue = new ArrayList<>();
        requestsQueue.add(new DiscAccessRequest(24, 0));
        requestsQueue.add(new DiscAccessRequest(12, 0));
        requestsQueue.add(new DiscAccessRequest(99, 0));
        requestsQueue.add(new DiscAccessRequest(174, 0));
        requestsQueue.add(new DiscAccessRequest(5, 0));
        requestsQueue.add(new DiscAccessRequest(209, 0));
        requestsQueue.add(new DiscAccessRequest(199, 0));

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

        requestsQueue.add(new DiscAccessRequest(109, 0));
        requestsQueue.add(new DiscAccessRequest(69, 0, 10));
        requestsQueue.add(new DiscAccessRequest(54, 0, 30));
        requestsQueue.add(new DiscAccessRequest(564, 23, 1000));
        requestsQueue.add(new DiscAccessRequest(614, 25, 900));
        requestsQueue.add(new DiscAccessRequest(314, 1050));
        requestsQueue.add(new DiscAccessRequest(505, 1100));
        requestsQueue.add(new DiscAccessRequest(813, 1200, 2000));
        requestsQueue.add(new DiscAccessRequest(5, 1200));

        return requestsQueue;
    }

    private ArrayList<DiscAccessRequest> sneakyRequests() {
        ArrayList<DiscAccessRequest> queue = new ArrayList<>();
        queue.add(new DiscAccessRequest(500, 0));
        queue.add(new DiscAccessRequest(300, 1));

        return queue;
    }
}