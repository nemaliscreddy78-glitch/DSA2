import java.util.*;

class AVLNode {
    int binId, height;
    AVLNode left, right;

    AVLNode(int binId) {
        this.binId = binId;
        height = 1;
    }
}

class AVLTree {

    int height(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    int getBalance(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode t2 = x.right;

        x.right = y;
        y.left = t2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode t2 = y.left;

        y.left = x;
        x.right = t2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    AVLNode insert(AVLNode node, int binId) {

        if (node == null)
            return new AVLNode(binId);

        if (binId < node.binId)
            node.left = insert(node.left, binId);
        else if (binId > node.binId)
            node.right = insert(node.right, binId);
        else
            return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        // LL
        if (balance > 1 && binId < node.left.binId)
            return rightRotate(node);

        // RR
        if (balance < -1 && binId > node.right.binId)
            return leftRotate(node);

        // LR
        if (balance > 1 && binId > node.left.binId) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL
        if (balance < -1 && binId < node.right.binId) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    void inorder(AVLNode node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.binId + " ");
            inorder(node.right);
        }
    }
}

class Edge {
    String destination;
    int weight;

    Edge(String destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }
}

public class WasteSense {

    static Map<String, List<Edge>> graph = new HashMap<>();

    static void addEdge(String source, String destination, int weight) {
        graph.putIfAbsent(source, new ArrayList<>());
        graph.putIfAbsent(destination, new ArrayList<>());

        graph.get(source).add(new Edge(destination, weight));
        graph.get(destination).add(new Edge(source, weight));
    }

    static void bfs(String start) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);

        System.out.println("\nBFS Traversal:");

        while (!queue.isEmpty()) {
            String current = queue.poll();
            System.out.print(current + " ");

            for (Edge edge : graph.get(current)) {
                if (!visited.contains(edge.destination)) {
                    visited.add(edge.destination);
                    queue.add(edge.destination);
                }
            }
        }
    }

    static Map<String, Integer> dijkstra(String start) {

        Map<String, Integer> distance = new HashMap<>();

        for (String node : graph.keySet())
            distance.put(node, Integer.MAX_VALUE);

        distance.put(start, 0);

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(Map.Entry.comparingByValue());

        pq.add(new AbstractMap.SimpleEntry<>(start, 0));

        while (!pq.isEmpty()) {

            String current = pq.poll().getKey();

            for (Edge edge : graph.get(current)) {

                int newDist = distance.get(current) + edge.weight;

                if (newDist < distance.get(edge.destination)) {

                    distance.put(edge.destination, newDist);

                    pq.add(new AbstractMap.SimpleEntry<>(
                            edge.destination,
                            newDist));
                }
            }
        }

        return distance;
    }

    public static void main(String[] args) {

        System.out.println("WasteSense - Smart Waste Collection & Recycling Optimization System\n");

        // CO1 - AVL Tree
        AVLTree tree = new AVLTree();
        AVLNode root = null;

        int bins[] = {105, 102, 110, 101, 108};

        for (int bin : bins)
            root = tree.insert(root, bin);

        System.out.println("Smart Bin Records (AVL Tree):");
        tree.inorder(root);

        // CO2 - Graph & BFS
        addEdge("Depot", "Bin1", 4);
        addEdge("Depot", "Bin2", 2);
        addEdge("Bin1", "Bin3", 5);
        addEdge("Bin2", "Bin3", 1);
        addEdge("Bin3", "RecycleCenter", 3);

        bfs("Depot");

        // CO3 - Dijkstra
        Map<String, Integer> shortestPath = dijkstra("Depot");

        System.out.println("\n\nShortest Routes from Depot:");

        for (Map.Entry<String, Integer> entry : shortestPath.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}