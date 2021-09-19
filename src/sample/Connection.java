package sample;

//connection between nodes
public class Connection {
    private Node fromNode;
    private Node toNode;
    private double weight;


    public Connection(Node fromNode, Node toNode, double weight) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.weight = weight;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public double getWeight() {
        return weight;
    }
}
