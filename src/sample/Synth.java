package sample;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

class Synth
{
    public AudioFormat audioFormat;
    public SourceDataLine sourceDataLine;
    byte[] buf;

    public void playNote(float hertz, float time)
    {
        for( int i = 0; i < time * (float )44100 / 1000; i++ ) {
            double angle = i / ( (float )44100 / hertz ) * 2.0 * Math.PI;
            buf[ 0 ] = (byte )( Math.sin( angle ) * 100 );
            sourceDataLine.write( buf, 0, 1 );
        }
    }

    public void playNote(int fromCIn24Tet, float time)
    {
        double hz=261.626*Math.pow(1.0297,fromCIn24Tet);
        for( int i = 0; i < time * (float )44100 / 1000; i++ ) {
            double angle = i / ( (float )44100 / hz ) * 2.0 * Math.PI;
            buf[ 0 ] = (byte )( Math.sin( angle ) * 100 );
            sourceDataLine.write( buf, 0, 1 );
        }
    }

    //for 200ms
    public void playNote(float hertz)
    {
        for( int i = 0; i < 200 * (float )44100 / 1000; i++ ) {
            double angle = i / ( (float )44100 / hertz ) * 2.0 * Math.PI;
            buf[ 0 ] = (byte )( Math.sin( angle ) * 100 );
            sourceDataLine.write( buf, 0, 1 );
        }
    }

    public Synth()
    {
        buf=new byte[1];
        audioFormat = new AudioFormat( (float )44100, 8, 1, true, false );
        try {
            sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
            sourceDataLine.open();
            sourceDataLine.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void delete(){
        sourceDataLine.drain();
        sourceDataLine.stop();
    }

}