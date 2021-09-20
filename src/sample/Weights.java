package sample;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//main class with notes matrix (Markov's chain)
public class Weights
{
    private static float[][][] weights= new float[12][12][12];

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static void randomWeights() {
        for(int y=0;y<12;y++){
            for(int x=0;x<12;x++){
                for(int z=0;z<12;z++){
                    weights[y][x][z]=(float)Math.random();
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static void updateWeights(int n1,int n2,int n3){
        weights[n1-60][n2-60][n3-60]++;
    }

    //sums all chances after 2 specific notes
    public static int sumAll(int n1,int n2) {
        int sum=0;
        for(int i=0;i<12;sum+=weights[n1][n2][i],i++);
        return sum;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //convert times of usage to chances (from 0 to 1)
    public static void normalize()
    {
        int sum;

        for(int i=0;i<12;i++)
        {
            for(int j=0;j<12;j++) {
                sum = sumAll(i,j);
                if (sum != 0)
                    for (int n = 0; n < 12; n++)
                        weights[i][j][n] /= sum;
            }
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //parses MIDI file and updates weights
    public static void learnFromSong(String midiFile) throws InvalidMidiDataException, IOException {
        Sequence sequence = MidiSystem.getSequence(new File(midiFile));
        MidiMessage message;
        int key;

        int id = 0;
        int[] noteArray=new int[2];

        for(Track track : sequence.getTracks()) //for each track
        {
            for(int i=0;i< track.size();i++) //for each signal
            {
                message = track.get(i).getMessage();

                if(message instanceof ShortMessage) //if ShortMessage
                {
                    ShortMessage sm=(ShortMessage)message;

                    if(sm.getCommand() == ShortMessage.NOTE_ON)  //if note
                    {
                        key=sm.getData1();  //key in MIDI numder (from 0 to 127)

                        if(id==2)
                        {
                            updateWeights(noteArray[0],noteArray[1],key);
                            noteArray[0]=noteArray[1];
                            noteArray[1]=key;
                        }
                        else
                            noteArray[id++]=key;
                    }
                }
            }
        }

        Weights.normalize();

    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //generates a melody starting with n1 & n2 with phrase length of [phrase] (exports a MIDI file as src/sample/result.mid)
    public static ArrayList<Double> generate(int n1, int n2, int ton, int phrase) throws MidiUnavailableException, InterruptedException, InvalidMidiDataException, IOException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        final MidiChannel[] channels = synth.getChannels();
        ArrayList<Double> normalizedResult=new ArrayList<>();

        int fn=n1,sn=n2,nn,tr;
        Sequence seq= new Sequence(Sequence.PPQ,10,2);
        Track track=seq.getTracks()[0];
        Track track2=seq.getTracks()[1];

        for(int i=0;i<phrase;i++)
        {
            nn=Weights.nextNote(fn,sn);

            channels[0].noteOn(nn+ton,127);
            //tr=nn+4;
            //if(Math.random()<0.5) channels[0].noteOn(tr+ton,127);
            //channels[0].noteOn(tr+3+ton,127);
            //if(Math.random()<0.2) channels[0].noteOn(tr+8+ton,127);

            ShortMessage sm=new ShortMessage();
            sm.setMessage(ShortMessage.NOTE_ON,nn,100);
            track.add(new MidiEvent(sm,i*4));sm=new ShortMessage();
            sm.setMessage(ShortMessage.NOTE_ON,nn+3,90);
            track.add(new MidiEvent(sm,i*4));



            if(i%2==0) {
                Thread.sleep(350);
            }
            else {
                Thread.sleep(150);
            }

            normalizedResult.add(1./nn);


            channels[0].noteOff(nn+ton);
            //channels[0].noteOff(tr+ton);
            //channels[0].noteOff(tr+3+ton);
            //channels[0].noteOff(tr+8+ton);
            fn=sn;
            sn=nn;
        }

        MidiSystem.write(seq, 1,new File("src/sample/result.mid"));
        System.out.println("GG end");
        return normalizedResult;
    }


    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //returns the MIDI number of the note basing on n1 & n2 in [weights]
    public static int nextNote(int n1,int n2) {
        double rnd,sum=0;
        rnd=Math.random();
        for(int i=0;i<12;i++)
        {
            sum+=weights[n1-60][n2-60][i];

            if(rnd<=sum)
                return i+60;
        }
        return 62;
    }
}
