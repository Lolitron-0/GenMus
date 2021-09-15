package sample;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.spi.FileTypeDetector;

public class Weights
{
    private static float[][][] weights= new float[12][12][12];

    public static void updateWeights(int n1,int n2,int n3){
        weights[n1-60][n2-60][n3-60]++;
    }

    public static int sumAll(int n1,int n2) {
        int sum=0;
        for(int i=0;i<12;sum+=weights[n1][n2][i],i++);
        return sum;
    }

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


    public static void learnFromSong(String midiFile) throws InvalidMidiDataException, IOException {
        Sequence sequence = MidiSystem.getSequence(new File(midiFile));
        MidiMessage message;
        int key;

        int id = 0;
        int[] noteArray=new int[2];

        for(Track track : sequence.getTracks())
        {
            for(int i=0;i< track.size();i++)
            {
                message = track.get(i).getMessage();

                if(message instanceof ShortMessage)
                {
                    ShortMessage sm=(ShortMessage)message;

                    if(sm.getCommand() == ShortMessage.NOTE_ON)
                    {
                        key=sm.getData1();

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




    public static void generate(int n1,int n2,int ton,int phrase) throws MidiUnavailableException, InterruptedException, InvalidMidiDataException, IOException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        final MidiChannel[] channels = synth.getChannels();

        int fn=n1,sn=n2,nn,tr;
        Sequence seq= new Sequence(Sequence.PPQ,10,1);
        Track track=seq.getTracks()[0];

        for(int i=0;i<33;i++)
        {
            nn=Weights.nextNote(fn,sn);

            channels[0].noteOn(nn+ton,127);
            tr=nn+4;
            if(Math.random()<0.5) channels[0].noteOn(tr+ton,127);
            channels[0].noteOn(tr+3+ton,127);
            if(Math.random()<0.2) channels[0].noteOn(tr+8+ton,127);
            ShortMessage sm=new ShortMessage();
            sm.setMessage(ShortMessage.NOTE_ON,nn,100);
            track.add(new MidiEvent(sm,i*5));
            if (i%phrase==0 && i!=0) {
                Thread.sleep(1000);

            }
            else if(i%2==0) {
                Thread.sleep(350);
            }
            else {
                Thread.sleep(150);
            }




            channels[0].noteOff(nn+ton);
            channels[0].noteOff(tr+ton);
            channels[0].noteOff(tr+3+ton);
            channels[0].noteOff(tr+8+ton);
            fn=sn;
            sn=nn;
        }

        MidiSystem.write(seq, 1,new File("C:\\Users\\ClarVik\\source\\repos\\AAAAAAAAA\\src\\sample\\New MIDI File 6.mid"));

    }


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
