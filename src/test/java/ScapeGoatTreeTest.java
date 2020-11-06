import org.junit.Test;

import static org.junit.Assert.*;

public class ScapeGoatTreeTest {
    ScapeGoatTree<Integer> tree;

    @Test
    public void size() {
        tree = new ScapeGoatTree<Integer>() {{
            add(0);
            add(1);
            add(2);
            add(3);
        }};
        assertEquals(4, tree.size());
    }

    @Test
    public void height() {
        tree = new ScapeGoatTree<>();
        assertEquals(-1, tree.height());
        tree.add(7);
        assertEquals(0, tree.height());
    }

    ScapeGoatTree<Integer> newTree = new ScapeGoatTree<Integer>() {{
        add(50);
        add(42);
        add(40);
        add(47);
        add(58);
        add(55);
        add(60);
    }};

    @Test
    public void balance() {

    }

    @Test
    public void contains() {
        assertFalse(newTree.contains(90));
        assertTrue(newTree.contains(42));
    }

    @Test
    public void add() {
        assertEquals(2, newTree.height());
        assertFalse(newTree.add(60));

    }

    @Test
    public void remove() {
        newTree.remove(42);
        assertEquals(6, newTree.size());
    }
}