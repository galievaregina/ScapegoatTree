import java.util.*;

public class ScapeGoatTree<T extends Comparable<T>> {
    private Node<T> root;
    private int maxSize = 0;// максимальный размер дерева-максимальное число вершин в дереве после последней перебалансировки

    public class Node<T extends Comparable<T>> {
        T data;
        Node<T> right;
        Node<T> left;

        public Node(T data) {
            this.right = null;
            this.left = null;
            this.data = data;
        }
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
    public boolean add(T data) {
        Node<T> closest = find(data);
        int comparison = closest == null ? -1 : data.compareTo(closest.data);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<T>(data);
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
            this.balance();
        }
        return true;
    }

    private void balance() {//балансировка дерева
        if (root == null) return;
        Iterator<T> iterator = this.inorderIterator();
        List<T> list = new ArrayList<T>();
        while (iterator.hasNext()) {
            list.add(iterator.next());//получаем список со всеми значениями
        }
        root = null;
        balancedTree(list);
    }

    private void balancedTree(List<T> list) {//строим новое сбалансированное дерево
        if (list.isEmpty()) return;
        if (list.size() == 1) {
            this.add(list.get(0));
            return;
        }
        int mediana = (list.size() - 1) / 2;
        this.add(list.get(mediana));
        list.remove(mediana);
        //нашли медиану сделали ее корнем,после чего удаляем ее из списка
        //повторяем все тоже самое для левого и правого поддерева
        ArrayList<T> leftList = new ArrayList<T>(list.subList(0, (list.size() - 1) / 2));
        balancedTree(leftList);
        ArrayList<T> rightList = new ArrayList<T>(list.subList((list.size() - 1) / 2, list.size()));
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
        queue.add(node.data);
        inorderTraverse(queue, node.right);
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T data) {
        int comparison = data.compareTo(start.data);
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
        return closest != null && data.compareTo(closest.data) == 0;
    }

    // T=O(logN)
    public boolean remove(T data) {
        maxSize--;
        boolean contains = contains(data);
        if (contains) {
            root = removeRecursive(root, data);
        }
        if (this.height() > Math.log(maxSize) / Math.log(3.0 / 2.0)) {
            this.balance();
        }
        return contains;
    }

    public Node<T> removeRecursive(Node<T> node, T data) {
        if (node == null) return null;

        int comparison = data.compareTo(node.data);
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
            node.right = removeRecursive(right, node.data);
            node.left = left;
            return node;
        } else {
            node.right = removeRecursive(node.right, data);
            return node;
        }

    }

    public Node findSmallestValue(Node<T> root) {
        return root.left == null ? root : findSmallestValue(root.left);
    }

}
