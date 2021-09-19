package sample;

import java.util.ArrayList;

//controller of nodes & connections
public class Network {
    ArrayList<Connection> connections = new ArrayList<>();
    ArrayList<Node> nodes=new ArrayList<>();
    int inputs;
    int outputs;
    int layers;
    int nextNode=0;
    int biasNode;
    ArrayList<Node> network=new ArrayList<>();


    public Network(int inputs, int outputs) {
        this.inputs = inputs;
        this.outputs = outputs;

        for(int i=0;i<inputs;i++)
        {
            nodes.add(new Node(i));
            nextNode++;
            nodes.get(i).setLayer(0);
        }

        for(int i=0;i<outputs;i++)
        {
            nodes.add(new Node(i+inputs));
            nextNode++;
            nodes.get(i+inputs).setLayer(1);
        }

        nodes.add(new Node(nextNode));
        nodes.get(nextNode).setLayer(0);
        biasNode=nextNode;
        nextNode++;
    }

    //get a list of outputs from list of inputs
    public ArrayList<Double> feedForward(ArrayList<Double> inputValues)
    {
        for(int i=0;i<inputs;i++)
            nodes.get(i).setOutputValue(inputValues.get(i));
        nodes.get(biasNode).setOutputValue(1);

        for(int i=0;i<network.size();i++) {
            network.get(i).engage();
        }

        ArrayList<Double> outs =new ArrayList<Double>();
        for(int i=0;i<outputs;i++) {
            outs.add(nodes.get(inputs+i).getOutputValue());
        }

        for(int i=0;i<nodes.size();i++){
            nodes.get(i).setInputSum(0);
        }

        return outs;

    }

    //refresh connections (probably not needed)
    public void reconnectNodes()
    {
        for(int i=0;i<nodes.size();i++) {
            nodes.get(i).getOutputConnections().clear();
        }

        for(int i=0;i<connections.size();i++)
        {
            connections.get(i).getFromNode().getOutputConnections().add(connections.get(i));
        }
    }

    //sets nodes as they should calculate during feed forward (probably called once)
    public void generateNetwork()
    {
        reconnectNodes();
        for(int l=0;l<layers;l++){
            for(int i=0;i<nodes.size();i++){
                if(nodes.get(i).getLayer()==l)
                    network.add(nodes.get(i));
            }
        }
    }

}
