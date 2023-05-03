package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import datastructures.MyPQueue;
import datastructures.PQueue;
import org.junit.jupiter.api.Test;

public class MyPQueueTest {
    @Test void reversed() {
        PQueue<Integer> q = new MyPQueue<>();
        assertTrue(q.isEmpty());
        assertEquals(0, q.size());
        for (int i = 10; i >= 0; i--) q.add(i, i);
        assertEquals(11, q.size());
        for (int i = 0; i <= 10; i++) {
            int k = q.peek();
            int j = q.extractMin();
            assertEquals(i, j, k);
        }
        assertTrue(q.isEmpty());
    }
    @Test void inorder() {
        PQueue<Integer> q = new MyPQueue<>();
        assertTrue(q.isEmpty());
        for (int i = 0; i < 10; i++) q.add(i, i);
        assertEquals(10, q.size());
        for (int i = 0; i < 10; i++) {
            int k = q.peek();
            int j = q.extractMin();
            assertEquals(i, j, k);
        }
        assertTrue(q.isEmpty());
    }
    @Test void throwTest() {
        PQueue<Integer> q = new MyPQueue<>();
        q.add(1,1);
        assertThrows(IllegalArgumentException.class, () -> q.add(1,2));
    }
}
