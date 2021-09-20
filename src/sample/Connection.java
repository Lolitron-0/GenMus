package sample;

//connection between nodes
public class Connection {
    private Node fromNode;
    private Node toNode;
    private double weight;
    private double gradient;
    private double lastChange=0;

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public Connection(Node fromNode, Node toNode, double weight) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.weight = weight;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public Node getFromNode() {
        return fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public double getWeight() {
        return weight;
    }

    public double getGradient() {
        return gradient;
    }

    public void setGradient(double gradient) {
        this.gradient = gradient;
    }

    public double getLastChange() {
        return lastChange;
    }

    public void setLastChange(double lastChange) {
        this.lastChange = lastChange;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
