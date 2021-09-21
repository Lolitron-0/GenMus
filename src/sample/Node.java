package sample;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class Node {

    private ArrayList<Connection> outputConnections=new ArrayList<>();
    private int no;
    private int layer;
    private double inputSum=5;
    private double outputValue;
    private double delta;

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public Node(int no) {
        this.no = no;
        layer=1;
    }

    /**
     * Calls activation function and pass output value to the next node
     */
    public void engage()
    {
        if(layer!=0)
        {
            outputValue=1/( 1 + Math.pow(Math.E,(-1*inputSum)));
        }

        for(int i=0;i<outputConnections.size();i++)
        {
           outputConnections.get(i).getToNode().inputSum+=outputValue*outputConnections.get(i).getWeight();
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     *Checks if a node connected to another
     * @param node node to check connection with
     * @return boolean of the result
     */
    boolean isConnectedTo(@NotNull Node node) {
        if (node.layer == layer) {
            return false;
        }

        if (node.layer < layer) {
            for (int i = 0; i < node.outputConnections.size(); i++) {
                if (node.outputConnections.get(i).getToNode() == this) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < outputConnections.size(); i++) {
                if (outputConnections.get(i).getToNode() == node) {
                    return true;
                }
            }
        }

        return false;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Counting specific value for gradient
     * @return sum of all weights*deltas for every output connection
     */
    public double getSumOfOutgoingDeltasWeights()
    {
        double res=0.;
        for (Connection outputConnection : outputConnections) {
            res += outputConnection.getWeight() * outputConnection.getToNode().getDelta();
        }
        return res;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    public ArrayList<Connection> getOutputConnections() {
        return outputConnections;
    }

    public int getNo() {
        return no;
    }

    public int getLayer() {
        return layer;
    }

    public double getInputSum() {
        return inputSum;
    }

    public double getOutputValue() {
        return outputValue;
    }

    public double getDelta() {
        return delta;
    }

    public void setOutputConnections(ArrayList<Connection> outputConnections) {
        this.outputConnections = outputConnections;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void setInputSum(double inputSum) {
        this.inputSum = inputSum;
    }

    public void setOutputValue(double outputValue) {
        this.outputValue = outputValue;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }
}
