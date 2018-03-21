package Disc_Scheduling;

import Disc_Access.DiscAccessRequest;
import Disc_Scheduling.Direction.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    void SSTFTest(){
        assertEquals(225, (new SSTF()).carryOutSimulation(createQueue()));
    }

    @Test
    void SCANTest(){
        SCAN scan1 = new SCAN();
        scan1.currentDirection = Direction.LEFT;
        assertEquals(231, scan1.carryOutSimulation(createQueue()));
    }

    @Test
    void CSCANTest(){
        assertEquals(991, (new CSCAN()).carryOutSimulation(createQueue()));
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
}