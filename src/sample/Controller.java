package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
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


    public void onClick(ActionEvent actionEvent) throws InterruptedException, MidiUnavailableException, InvalidMidiDataException, IOException {
        ArrayList<Double> vision=Weights.generate(n1Choose.getValue(), n2Choose.getValue(),(int)scaleChooser.getValue(),16);
        //Node n=new Node(1),n2=new Node(2);
        //Connection c=new Connection(n,n2,0.5);
        //n.getOutputConnections().add(c);
        //n.engage();
        System.out.println(net.feedForward(vision).get(0));
        btnGood.setDisable(false);
        btnBad.setDisable(false);
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

        net= new Network(16,1,16);

    }

    public void onClckGood(ActionEvent actionEvent) {
        net.countDeltas(1);
        btnGood.setDisable(true);
        btnBad.setDisable(true);
    }

    public void onClickBad(ActionEvent actionEvent) {
        net.countDeltas(0);
        btnGood.setDisable(true);
        btnBad.setDisable(true);
    }
}
