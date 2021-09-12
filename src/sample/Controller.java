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
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ChoiceBox n1Choose;
    public Button btnGener;
    public Slider scaleChooser;
    public ChoiceBox n2Choose;


    public void onClick(ActionEvent actionEvent) throws InterruptedException, MidiUnavailableException {
        Weights.generate((int)n1Choose.getValue(),(int)n2Choose.getValue(),(int)scaleChooser.getValue(),16);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Integer> cursors = FXCollections.observableArrayList(60, 62, 64,65,67,69);
        n1Choose.setItems(cursors);
        n2Choose.setItems(cursors);
        n1Choose.setValue(cursors.get(0));
        n2Choose.setValue(cursors.get(0));
        System.out.println(n1Choose.getValue());
        try {
            Weights.learnFromSong("C:\\Users\\ClarVik\\source\\repos\\src\\com\\company\\New MIDI File 1.mid");
            Weights.learnFromSong("C:\\Users\\ClarVik\\source\\repos\\src\\com\\company\\New MIDI File 2.mid");
            Weights.learnFromSong("C:\\Users\\ClarVik\\source\\repos\\src\\com\\company\\New MIDI File 3.mid");
            Weights.learnFromSong("C:\\Users\\ClarVik\\source\\repos\\src\\com\\company\\New MIDI File 4.mid");
            Weights.learnFromSong("C:\\Users\\ClarVik\\source\\repos\\src\\com\\company\\New MIDI File 5.mid");
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }

    }
}
