package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//main class to manage form  (needs Scene builder app all instructions here - https://maxsite.org/page/visual-programming-java)
public class Controller implements Initializable {
    public ChoiceBox<Integer> n1Choose;
    public Button btnGener;
    public Slider scaleChooser;
    public ChoiceBox<Integer> n2Choose;
    public Button btnGood;
    public Button btnBad;
    Network net;
    XYSeries series;


    public void onClick(ActionEvent actionEvent) throws InterruptedException, MidiUnavailableException, InvalidMidiDataException, IOException {
        //ArrayList<Double> vision=Weights.generate(n1Choose.getValue(), n2Choose.getValue(),(int)scaleChooser.getValue(),16);
        //Node n=new Node(1),n2=new Node(2);
        //Connection c=new Connection(n,n2,0.5);
        //n.getOutputConnections().add(c);
        //n.engage();

       // btnGood.setDisable(false);
        //btnBad.setDisable(false);



        for(float i = 0; i < Math.PI; i+=0.1){
            series.add(i, Math.sin(i));
        }




    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Integer> cursors = FXCollections.observableArrayList(60, 62, 64,65,67,69);
        n1Choose.setItems(cursors);
        n2Choose.setItems(cursors);
        n1Choose.setValue(cursors.get(0));
        n2Choose.setValue(cursors.get(0));
        //Weights.randomWeights();
        try {
            Weights.learnFromSong("src/sample/New MIDI File 1.mid");
            Weights.learnFromSong("src/sample/New MIDI File 2.mid");
            Weights.learnFromSong("src/sample/New MIDI File 3.mid");
            Weights.learnFromSong("src/sample/New MIDI File 4.mid");
            Weights.learnFromSong("src/sample/New MIDI File 5.mid");
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }




        net= new Network(16,1,8);




        series = new XYSeries("error(iteration)");
        double sum=0;
        int g=0;
        while(g++<1000) {
            ArrayList<Double> test = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
                test.add((double) Math.round(Math.random()));
            }
            System.out.println(net.feedForward(test).get(0));
            sum+=net.countDeltas(test.get(0));
            if(g%1==0){
                series.add(g, sum/1);
                sum=0;
            }
        }

        XYDataset xyDataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory
                .createXYLineChart("error = msi(out)", "iteration", "error",
                        xyDataset,
                        PlotOrientation.VERTICAL,
                        true, true, true);
        JFrame frame =
                new JFrame("MinimalStaticChart");
        frame.getContentPane()
                .add(new ChartPanel(chart));
        frame.setSize(1000,700);
        frame.show();

    }

    public void onClckGood(ActionEvent actionEvent) {
        net.countDeltas(1);
    }

    public void onClickBad(ActionEvent actionEvent) {
        net.countDeltas(0);
    }
}
