import java.util.*;

public class ScapeGoatTree<T extends Comparable<T>> implements Map {
    private Node<T> root;
    private int maxSize = 0;// максимальный размер дерева-максимальное число вершин в дереве после последней перебалансировки

    public static class Node<T extends Comparable<T>> {
        T key;
        T value;
        Node<T> right;
        Node<T> left;

        public Node(T data,T value) {
            this.right = null;
            this.left = null;
            this.key = data;
            this.value = value;
        }
    }

    public Node getNode(T key){
        if(contains(key)){
            return find(key);
        }
        return null;
    }


    public int size() {//вес вершины(х).Кол-во всех дочерних элементов + она сама

        return subtreeSize(root);
    }
    private int subtreeSize(Node<T> node) {
        if (node == null) {
            return 0;
        } else {
            return 1 + subtreeSize(node.right) + subtreeSize(node.left);
        }
    }

    public int height() {//Глубина дерева-самый длинный путь от корня до листа
        if (root == null) {
            return -1;
        } else if (size() == 1) {
            return 0;
        }
        return heightSubtree(root) - 1;
    }

    private int heightSubtree(Node<T> node) {
        if (node == null) {
            return 0;
        } else {
            return Math.max(heightSubtree(node.left), heightSubtree(node.right)) + 1;
        }
    }

    /**
     * Добавление элемента в дерево. Сначала действуем как и в бинарном дереве поиска.
     * Ищем место куда подвесить новый узел,добавляем.
     * Далее находим узел,в котором потерян баланс и перестраеваем его поддерево.
     */
    //T = O(logN)
    public boolean add(T key ,T value) {
        Node<T> closest = find(key);
        int comparison = closest == null ? -1 : key.compareTo(closest.key);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<T>(key,value);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        maxSize++;
        double coefficient = Math.log(maxSize) / Math.log(3.0 / 2.0);//число определяющее требуемую степень балансировки
        if (this.height() > coefficient) {
            balance();
        }
        return true;
    }

    private void balance() {//балансировка дерева
        if (root == null) return;
        Iterator<T> iterator = this.inorderIterator();
        List<Node<T>> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(getNode(iterator.next()));//получаем список со всеми значениями
        }
        root = null;
        balancedTree(list);
    }

    private void balancedTree(List<Node<T>> list) {//строим новое сбалансированное дерево
        if (list.isEmpty()) return;
        if (list.size() == 1) {
            Node<T> node = list.get(0);
            this.add(node.key,node.value);
            return;
        }
        int mediana = (list.size() - 1) / 2;
        Node<T> n = list.get(mediana);
        this.add(n.key,n.value);
        list.remove(mediana);
        //нашли медиану сделали ее корнем,после чего удаляем ее из списка
        //повторяем все тоже самое для левого и правого поддерева
        ArrayList<Node<T>> leftList = new ArrayList<Node<T>>(list.subList(0, (list.size() - 1) / 2));
        balancedTree(leftList);
        ArrayList<Node<T>> rightList = new ArrayList<>(list.subList((list.size() - 1) / 2, list.size()));
        balancedTree(rightList);
    }

    private Iterator<T> inorderIterator() {
        Queue<T> queue = new LinkedList<T>();
        inorderTraverse(queue, root);
        return queue.iterator();

    }

    private void inorderTraverse(Queue<T> queue, Node<T> node) {//обход дерева от конечных узлов к корню
        if (node == null) return;
        inorderTraverse(queue, node.left);
        queue.add(node.key);
        inorderTraverse(queue, node.right);
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T data) {
        int comparison = data.compareTo(start.key);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, data);
        } else {
            if (start.right == null) return start;
            return find(start.right, data);
        }
    }

    public boolean contains(T data) {
        Node<T> closest = find(data);
        return closest != null && data.compareTo(closest.key) == 0;
    }

    public Node<T> removeRecursive(Node<T> node, T data) {
        if (node == null) return null;

        int comparison = data.compareTo(node.key);
        if (comparison < 0) {
            node.left = removeRecursive(node.left, data);
            return node;
        } else if (comparison == 0) {
            if (node.left == null && node.right == null) {
                return null;
            }
            if (node.right == null) {
                return node.left;
            } else if (node.left == null) {
                return node.right;
            }
            Node<T> left = node.left;
            Node<T> right = node.right;
            Node<T> min = findSmallestValue(node.right);
            node = min;
            node.right = removeRecursive(right, node.key);
            node.left = left;
            return node;
        } else {
            node.right = removeRecursive(node.right, data);
            return node;
        }

    }

    public Node<T> findSmallestValue(Node<T> root) {
        return root.left == null ? root : findSmallestValue(root.left);
    }
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.keySet().contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.values().contains(value);
    }

    @Override
    public Object get(Object key) {
        if(contains((T)key)) return getNode((T)key).value;
        return null;

    }

    @Override
    public Object put(Object key, Object value) {
        if(this.contains((T)key)){
            Node node =  getNode((T)key);
            T lastValue = (T)node.value;
            node.value = (T) value;
            return lastValue;
        } else{
            add((T)key,(T)value);
            return null;
        }
    }

    @Override
    // T=O(logN)
    public Object remove(Object key) {
        maxSize--;
        boolean contains = contains((T)key);
        if (contains) {
            root = removeRecursive(root, (T)key);
        }
        if (this.height() > Math.log(maxSize) / Math.log(3.0 / 2.0)) {
            this.balance();
        }
        return contains;
    }

    @Override
    public void putAll(Map m) {
        m.forEach((key, value) -> put(key,value));
    }

    @Override
    public void clear() {
        Iterator<T> iterator = this.inorderIterator();
        while (iterator.hasNext()){
            remove(iterator.next());
        }
    }

    @Override
    public Set keySet() {
        if(this.size()==0) return null;
        Iterator<T> iterator = this.inorderIterator();
       Set<T> keySet = new HashSet<>();
       while(iterator.hasNext()){
           keySet.add(iterator.next());
       }
       return keySet;
    }

    @Override
    public Collection values() {
        if(this.size()==0) return null;
        List<T> values =  new ArrayList<>();
        Iterator<T> iterator = this.inorderIterator();
        while(iterator.hasNext()){
            Node<T> node = getNode(iterator.next());
            values.add(node.value);
        }
        return values;
    }

    @Override
    public Set<Entry> entrySet() {
        if(this.size()==0) return null;
        Iterator<T> iterator = this.inorderIterator();
        Set<Entry> entrySet = new HashSet<>();
        while (iterator.hasNext()){
            Node<T> node = getNode(iterator.next());
            AbstractMap.SimpleEntry<T, T> entry = new AbstractMap.SimpleEntry<T, T>(node.key, node.value);
            entrySet.add(entry);
        }
        return entrySet;
    }
}
