package vamixA3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MediaPlayer implements ActionListener{
	
	//initialize components
	//Media 
    private final EmbeddedMediaPlayerComponent _mediaPlayerComponent;
    private  final EmbeddedMediaPlayer _video;
    private String _filePath;
    private String _fileName;
    private JPanel _panel = new JPanel();
    
    //Basic Operations
    private JButton _mute = new JButton("mute");
    private JButton _forward = new JButton(">>");
    private JButton _backward = new JButton("<<");
    private JButton _playPause = new JButton("||");
    private Timer _ticker = new Timer(900,this);
    private JLabel _timePlayed = new JLabel();
    
    //Edit Options
    private JButton _extractA = new JButton("Extract Audio");
    private JButton _extractV = new JButton("Extract Video");
    private JButton _save = new JButton("Save");
    
    //bash command process
	private Process _process;

    //constructor for class
    protected MediaPlayer(String args) {
    	//Get media file name and full path
    	_filePath = args;
    	String[] tmp = _filePath.split("/");
		_fileName = tmp[tmp.length-1];
		
		//Create frame and set title
        JFrame frame = new JFrame("206VAMIX: "+_fileName);
        JPanel editPanel = new JPanel();
        
        //construct media player
        _mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        _video =  _mediaPlayerComponent.getMediaPlayer();
        
        
        //set LAF
        FlowLayout flow = new FlowLayout();
        flow.setVgap(5);
        _panel.setLayout(new BorderLayout());
        editPanel.setLayout(flow);
        editPanel.setBackground(Color.gray);
        editPanel.setPreferredSize(new Dimension(150,300));
    
       
        //Set Button size
        this.setButtonSize(_extractA);
        this.setButtonSize(_extractV);
        this.setButtonSize(_mute);
        this.setButtonSize(_extractA);
        this.setButtonSize(_save);
        _forward.setPreferredSize(new Dimension(30,30));
        _backward.setPreferredSize(new Dimension(30,30));
        _playPause.setPreferredSize(new Dimension(30,30));


        //set time display font
        _timePlayed.setFont(new Font("Arial",Font.BOLD,14));
        _timePlayed.setForeground(Color.white);
        _timePlayed.setText("00:00");
       
        
        //Add ActionListener
        _mute.addActionListener(this);
        _forward.addActionListener(this);
        _backward.addActionListener(this);
        _playPause.addActionListener(this);
        _extractA.addActionListener(this);
        _extractV.addActionListener(this);

        //add to edit panel,adding order relates to display order
        editPanel.add(_save);
        editPanel.add(_extractA);
        editPanel.add(_extractV);     
        editPanel.add(_backward);
        editPanel.add(_playPause);
        editPanel.add(_forward);
        editPanel.add(_mute);
        editPanel.add(_timePlayed);

        //Main panel
        _panel.add(_mediaPlayerComponent,BorderLayout.CENTER);
        _panel.add(editPanel,BorderLayout.WEST);


        //set JFrame to Main panel
        frame.setContentPane(_panel);
        
        //Set JFrame GUI
        frame.setLocation(100, 100);
        frame.setSize(1050, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //play video and start timer
        _video.playMedia(args);
        _ticker.start();
    }

    //Set button size to the same
    private void setButtonSize(JButton e){
    	e.setPreferredSize(new Dimension(120,30));
    }
    
    //Execute bash command
	private String execCmd(String cmd){
		try {
			ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",cmd);
			builder.redirectErrorStream(true);
		
			_process = builder.start();
			InputStream stdout = _process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String output = stdoutBuffered.readLine();
			String line = null;
			while ((line = stdoutBuffered.readLine()) != null ) {
				output = output + line;
			}
			return output;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cmd;
	}
	
	
	//Convert seconds to min:sec format
    private void timeConvert(int time){
    	if(time < 10){
    		_timePlayed.setText("00:0" + time);
    	}
    	else if(time > 9 && time < 60){
    		_timePlayed.setText("00:"+time);
    	}
    	else if(time >60){
    		
    		int minute = time/60;
    		int seconds = time%60;
    		if(minute <10){
    			if(seconds < 10){
    				_timePlayed.setText("0"+minute+":0"+seconds);
    			}
    			else{
    				_timePlayed.setText("0"+minute+":"+seconds);
    			}
    		}
    		else{
    			if(seconds < 10){
    				_timePlayed.setText(minute+":0"+seconds);
    			}
    			else{
    				_timePlayed.setText(minute+":"+seconds);
    			}	
    		}	
    	}
    }
	
    @Override
	public void actionPerformed(ActionEvent e) {
		//mute button
		if(e.getSource() == _mute){
			if(_video.isMute()){
				_mute.setText("mute");
				_video.mute();

			}else{
				_mute.setText("unmute");
				_video.mute();
			}
		}
		//forward
		else if(e.getSource() == _forward){
			_video.skip(10000);
		}
		//backward
		else if(e.getSource() == _backward){
			_video.skip(-10000);
		}
		//show time
		else if(e.getSource() == _ticker){
			int time = (int)(_video.getTime() / 1000.0);
			this.timeConvert(time);
		}
		//play or pause video
		else if(e.getSource() == _playPause){
			if(_playPause.getText().equals("||")){
				_video.pause();
				_playPause.setText(">");
			}
			else if(_playPause.getText().equals(">")){
				_video.play();
				_playPause.setText("||");
			}
		}
		//extract audio
		else if(e.getSource() == _extractA){
			if(_video.getAudioTrackCount() != 0){
				String output = JOptionPane.showInputDialog(null,"Enter Output Audio file Name(No File Extension needed):");
				if(output != null){
					String cmd = "avconv -i " + _filePath + " -vn -acodec copy " + output+".mp3";
					cmd = this.execCmd(cmd);
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "No Audio Track", "Error!",JOptionPane.INFORMATION_MESSAGE);
			}
		}
		//extract video
		else if(e.getSource() == _extractV){
			if(_video.getVideoTrackCount() != 0){
				String output = JOptionPane.showInputDialog(null,"Enter Output Video file Name(No File Extension needed):");
				if(output != null){
					String cmd = "avconv -i " + _filePath + " -vn -acodec copy -map 0:v " + output+".mp4";
					cmd = this.execCmd(cmd);
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "No Video Track", "Error!",JOptionPane.INFORMATION_MESSAGE);

			}
		}
	}
}
