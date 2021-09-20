package sample;

import java.util.ArrayList;

//controller of nodes & connections
public class Network {
    ArrayList<Connection> connections = new ArrayList<>();
    ArrayList<Node> nodes=new ArrayList<>();
    int inputs;
    int outputs;
    int inHidden;
    int layers=3;
    int nextNode=0;
    int biasNode;
    double learningRate=0.7;
    double alphaMomentum=0.4;
    ArrayList<Node> network=new ArrayList<>();

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public Network(int inputs, int outputs,int inHidden) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.inHidden=inHidden;

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
            nodes.get(i+inputs).setLayer(layers-1);
        }

        nodes.add(new Node(nextNode));
        nodes.get(nextNode).setLayer(0);
        biasNode=nextNode;
        nextNode++;

        for(int hids =1;hids<layers-1;hids++){
            for(int i=0;i<inHidden;i++){
                nodes.add(new Node(nextNode));
                nodes.get(nextNode).setLayer(hids);
                nextNode++;
            }
        }

        while (!fullyConnected()){
            addConnection();
        }

        generateNetwork();

    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

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

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //refresh connections
    void connectNodes()
    {
        for(int i=0;i<nodes.size();i++) {
            nodes.get(i).getOutputConnections().clear();
        }

        for(int i=0;i<connections.size();i++)
        {
            connections.get(i).getFromNode().getOutputConnections().add(connections.get(i));
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //update all synapse and neurons parameters, update weights  (also checking convergence)
    public void countDeltas(double ideal)
    {
        for(int i=0;i<outputs;i++){
            double delt1=(ideal-nodes.get(inputs+i).getOutputValue());
            double delt2=((1-nodes.get(inputs+i).getOutputValue())*nodes.get(inputs+i).getOutputValue());
            double delt=delt1*delt2; //ОЧЕНЬ МАЛЕНЬКАЯ
            nodes.get(inputs+i).setDelta(delt);  //delta_o = (out_ideal - out_actual)*derivative(in)
        }
        System.out.println("Err: "+countError(ideal));

        ArrayList<Node> nodesInLayer;
        for(int layer=layers-2;layer>=0;layer--){
            nodesInLayer=getListOfLayerNodes(layer);
            for (Node node : nodesInLayer) {
                node.setDelta(((1 - node.getOutputValue()) * node.getOutputValue()) * node.getSumOfOutgoingDeltasWeights());  //delta = derivative(in) * E(w_i*delta_i)
                for (Connection connection : node.getOutputConnections()) {
                    connection.setGradient(node.getOutputValue()*connection.getToNode().getDelta()); // GRAD = delta_b * out_a
                    connection.setLastChange((connection.getGradient()*learningRate)+(alphaMomentum*connection.getLastChange()));   //wchange = learningRate*GRAD + momentum*prevChange
                    connection.setWeight(connection.getWeight()+connection.getLastChange());
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //sets nodes as they should calculate during feed forward (probably called once)
    void generateNetwork()
    {
        connectNodes();
        for(int l=0;l<layers;l++){
            for(int i=0;i<nodes.size();i++){
                if(nodes.get(i).getLayer()==l)
                    network.add(nodes.get(i));
            }
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //adds connection between 2 random connections
    void addConnection()
    {
        if(fullyConnected())
            return;

        int rand1,rand2;
        do {
            rand1=(int) (Math.random() * nodes.size());
            rand2 = (int) (Math.random() * nodes.size());
        } while (randomConnectionNodesAreShit(rand1,rand2));

        if(nodes.get(rand1).getLayer() > nodes.get(rand2).getLayer())
        {
            rand1-=rand2;
            rand2+=rand1;
            rand1=rand2-rand1;
        }

        connections.add(new Connection(nodes.get(rand1),nodes.get(rand2),Math.random()));
        connectNodes();
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //checks possibility of connecting 2 nodes
    boolean randomConnectionNodesAreShit(int r1, int r2) {
        if (nodes.get(r1).getLayer() == nodes.get(r2).getLayer()) return true; // if the nodes are in the same layer
        if (nodes.get(r1).isConnectedTo(nodes.get(r2))) return true; //if the nodes are already connected

        return false;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //obvious
    boolean fullyConnected()
    {
        int maxConnections = 0;
        int[] nodesInLayers = new int[layers];

        for (int i =0; i< nodes.size(); i++) {
            nodesInLayers[nodes.get(i).getLayer()] +=1;
        }


        for (int i = 0; i < layers-1; i++) {
            int nodesInFront = 0;
            for (int j = i+1; j < layers; j++) {
                nodesInFront += nodesInLayers[j];
            }

            maxConnections += nodesInLayers[i] * nodesInFront;
        }

        if (maxConnections == connections.size()) {
            return true;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private double countError(double ideal)
    {
        double sum=0;
        for(int i=0;i<outputs;i++){
            sum+=Math.pow(ideal-nodes.get(inputs+i).getOutputValue(),2);
        }

        return sum/outputs;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    ArrayList<Node> getListOfLayerNodes(int layer)
    {
        ArrayList<Node> result=new ArrayList<>();
        for(int i =0;i<nodes.size();i++){
            if(nodes.get(i).getLayer()==layer)
                result.add(nodes.get(i));
        }

        return result;
    }

}
