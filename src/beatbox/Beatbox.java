package beatbox;

import javax.sound.midi.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class Beatbox implements ControllerEventListener{

    static JFrame frame = new JFrame("My First Ever Music Video");
    static MyDrawPanel ml;

    public static void main(String[] args) 
    {

        Beatbox mini = new Beatbox();
        mini.initialize();
        
        
        
    }
    
    public void setUpGUI()
    {
        ml = new MyDrawPanel();
        frame.setContentPane(ml);
        frame.setBounds(30,30,300,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void initialize()
    {
        setUpGUI();

        try
        {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();

            sequencer.addControllerEventListener(ml, new int[] {127});

            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();

            int r = 0;
            for (int i = 0; i<60; i+=4) 
            {
                r = (int)(Math.random()*50+1);

                track.add(makeEvent(144,1,r,100,i));

                track.add(makeEvent(176,1,127,0,i));

                track.add(makeEvent(128,1,r,100,i+2));
            }

            sequencer.setSequence(seq);
            sequencer.start();
            sequencer.setTempoInBPM(220);

        }catch(Exception ex){ex.printStackTrace();};
    }

    public static MidiEvent makeEvent(int comd, int chan, int one, int two, int tick)
    {
        MidiEvent event = null;
        
        try
        {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
            
        }catch(Exception e){}
        
        return event;
    }

    public void controlChange(ShortMessage event)
    {
        System.out.println("la");
    }

    class MyDrawPanel extends JPanel implements ControllerEventListener
    {
        boolean msg = false;

        public void controlChange(ShortMessage event)
        {
            msg = true;
            repaint();
        }

        public void paint(Graphics g)
        {
            if(msg)
            {
                Graphics2D g2 = (Graphics2D) g;

                int red = (int)(Math.random()*250);
                int green = (int)(Math.random()*250);
                int blue = (int)(Math.random()*250);

                g.setColor(new Color(red, green, blue));

                int ht = (int)((Math.random()*120) + 10);
                int width = (int)((Math.random()*120) + 10);

                int x = (int)((Math.random()*40) + 10);
                int y = (int)((Math.random()*40) + 10);

                g.fillRect(x, y, ht, width);
                msg = false;
            }
        }
    }
}

class MiniMusicApp
{
    public void play(int instrument, int note)
    {
        try
        {
        Sequencer player = MidiSystem.getSequencer();
        player.open();
        Sequence seq = new Sequence(Sequence.PPQ, 4);
        Track track = seq.createTrack();
        
        MidiEvent event = null;
        
        ShortMessage first = new ShortMessage();
        first.setMessage(192,1,instrument,100);
        MidiEvent changeInstrument = new MidiEvent(first,1);
        track.add(changeInstrument);
        
        ShortMessage a = new ShortMessage();
        a.setMessage(144,1,44,100);
        MidiEvent noteOn = new MidiEvent(a,1);
        
        track.add(noteOn);
        
        ShortMessage b = new ShortMessage();
        b.setMessage(128,1,44,100);
        MidiEvent noteOff = new MidiEvent(b,1);
        
        track.add(noteOff);
        
        player.setSequence(seq);
        
        player.start();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}