import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class ScapeGoatTreeTest {
    ScapeGoatTree<Integer> tree =  new ScapeGoatTree<Integer>() {{
        add(0,2);
        add(1,3);
        add(2,5);
        add(3,5);
    }};

    @Test
    public void size() {
        assertEquals(4, tree.size());
    }

    @Test
    public void height() {
        tree = new ScapeGoatTree<>();
        assertEquals(-1, tree.height());
        tree.add(7,6);
        assertEquals(0, tree.height());
    }

    ScapeGoatTree<Integer> newTree = new ScapeGoatTree<Integer>() {{
        add(50,50);
        add(42,50);
        add(40,89);
        add(47,99);
        add(58,0);
        add(55,10);
        add(60,90);
    }};

    @Test
    public void contains() {
        assertFalse(newTree.contains(90));
        assertTrue(newTree.contains(42));
    }

    @Test
    public void add() {
        assertEquals(2, newTree.height());
        tree.add(4,8);
        assertEquals(2,tree.height());
        assertFalse(newTree.add(60,90));
    }

    @Test
    public void isEmpty() {
        assertFalse(newTree.isEmpty());
        ScapeGoatTree treeSimple = new ScapeGoatTree();
        assertTrue(treeSimple.isEmpty());
    }

    @Test
    public void containsKey() {
        assertTrue(newTree.containsKey(40));
        assertFalse(newTree.containsKey(2003003));
    }

    @Test
    public void containsValue() {
        assertFalse(newTree.containsValue(500));
        assertTrue(newTree.containsValue(50));
    }

    @Test
    public void put() {
        assertEquals(50,newTree.put(50,100));
        assertEquals(null, newTree.put(3939,848484));

    }

    @Test
    public void testRemove() {
        newTree.remove(42);
        assertEquals(6, newTree.size());
    }

    @Test
    public void putAll() {
        ScapeGoatTree treeSimple = new ScapeGoatTree();
        Map map = new HashMap(){{
            put(454,447477447);
            put(88488484,999);
            put(65656656,0);
        }};
        treeSimple.putAll(map);
        assertEquals(3,treeSimple.size());
    }

    @Test
    public void clear() {
        newTree.clear();
        assertEquals(0,newTree.size());
    }
    @Test
    public void get(){
        assertEquals(50,newTree.get(50));
        assertEquals(null,newTree.get(37373773));
    }

    @Test
    public void keySet() {
        Set s = new HashSet(){{
            add(50);
            add(42);
            add(40);
            add(47);
            add(58);
            add(55);
            add(60);
        }};
        assertEquals(s,newTree.keySet());
    }

    @Test
    public void values() {
        List<Integer> list = new ArrayList<Integer>(){{
            add(89);
            add(50);
            add(99);
            add(50);
            add(10);
            add(0);
            add(90);
        }};
        assertEquals(list,newTree.values());
    }

    @Test
    public void entrySet() {
        Set<Map.Entry> set = new HashSet<Map.Entry>(){{
            add(new AbstractMap.SimpleEntry(50,50));
            add(new AbstractMap.SimpleEntry(42,50));
            add(new AbstractMap.SimpleEntry(40,89));
            add(new AbstractMap.SimpleEntry(47,99));
            add(new AbstractMap.SimpleEntry(58,0));
            add(new AbstractMap.SimpleEntry(55,10));
            add(new AbstractMap.SimpleEntry(60,90));
        }};
        assertEquals(set,newTree.entrySet());
    }
}