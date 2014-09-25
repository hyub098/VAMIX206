package vamixA3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    private JFileChooser _fc = new JFileChooser();
    private JButton _mute = new JButton("mute");
    private JButton _forward = new JButton(">>");
    private JButton _backward = new JButton("<<");
    private JButton _playPause = new JButton("||");
    private Timer _ticker = new Timer(900,this);
    private JLabel _timePlayed = new JLabel();
    
    //Edit Options
    private JButton _extractA = new JButton("Extract Audio");
    private JButton _extractV = new JButton("Extract Video");
    private JButton _save = new JButton("Export");
    private JButton _replaceBtn = new JButton("Replace Audio");
    private JButton _mergeBtn = new JButton("Merge Audio");
    
    //Color Options
    private JButton _colorBtn = new JButton("Color");
    private String _rgb;
    
    //Font Options
    private JButton _fontBtn = new JButton("Font");
    private JDialog _fontWindow = new JDialog();
    private ArrayList<String> _fontList = new ArrayList<String>();
    private JButton _fontSize = new JButton("FontSize:16"); //default size
	private JButton _Mono = new JButton("Mono");
	private JButton _Sans = new JButton("Sans");
	private JButton _Serif = new JButton("Serif");
	private JButton _Italic = new JButton("ITALIC");
	private JButton _Bold = new JButton("BOLD");
	private JLabel _font = new JLabel();
	private Boolean _isMono = false;
	private Boolean _isSans = true; //default font
	private Boolean _isSerif = false;
	private Boolean _isBold = false;
	private Boolean _isItalic = false;
	
	//Input Text
	private JButton _textBtn = new JButton("AddText");
	
	//Time Options
	private JButton _startTimeBtn = new JButton("StartTimeTo");
	private JButton _endTimeBtn = new JButton("ToEndTime");

	//edit Option 0=font file 1=text 2=fontsize 3=font color 4= start to this time 5 = this time to end
	//private ArrayList<String> _cmdList = new ArrayList<String>();
	private String[] _cmdList = new String[6];
	private JButton _preview = new JButton("Preview");
	
	//extract window
	protected static JDialog _waitWin = new JDialog();
	protected static JLabel _waitLabel= new JLabel("<html>Please wait and not touch any buttons!</html>");
	protected static JLabel _doneLabel = new JLabel("File Exported!");
	
	//Command History
	
	
   
    
    //bash command process and swing worker
	private Process _process;
	private SwingExtract _extractTask = new SwingExtract();
	

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
        _video = _mediaPlayerComponent.getMediaPlayer();
        
        
        //set LAF
        FlowLayout flow = new FlowLayout();
        flow.setVgap(5);
        _panel.setLayout(new BorderLayout());
        editPanel.setLayout(flow);
        editPanel.setBackground(Color.gray);
        editPanel.setPreferredSize(new Dimension(150,300));
        
        //set extractWin
        _waitWin.setLayout(null);
        _waitWin.setMinimumSize(new Dimension(230,100));
        _waitWin.setLocationRelativeTo(null);
        _waitLabel.setFont(new Font("Arial",Font.BOLD,16));
        _doneLabel.setFont(new Font("Arial",Font.BOLD,20));
        _waitLabel.setBounds(20, 10, 200, 50);
        _doneLabel.setBounds(35, 10, 200, 50);

        _waitWin.add(_waitLabel);
        _waitWin.add(_doneLabel);
        _doneLabel.setVisible(false);
        
        //initialize available fonts and window
        _font.setFont(new Font("Sans",Font.PLAIN,20));
        _font.setText("Sans");
        this.createFont();
    	this.setButtonSize(_Mono);
    	this.setButtonSize(_Sans);
    	this.setButtonSize(_Serif);
    	this.setButtonSize(_Italic);
    	this.setButtonSize(_Bold);
    
       
        //Set Button size
    	this.setButtonSize(_preview);
    	this.setButtonSize(_textBtn);
    	this.setButtonSize(_fontSize);
        this.setButtonSize(_extractA);
        this.setButtonSize(_extractV);
        this.setButtonSize(_mute);
        this.setButtonSize(_extractA);
        this.setButtonSize(_save);
        this.setButtonSize(_replaceBtn);
        this.setButtonSize(_mergeBtn);
        this.setButtonSize(_fontBtn);
        this.setButtonSize(_colorBtn);
        this.setButtonSize(_startTimeBtn);
        this.setButtonSize(_endTimeBtn);
        _font.setPreferredSize(new Dimension(130,30));
        _forward.setPreferredSize(new Dimension(30,30));
        _backward.setPreferredSize(new Dimension(30,30));
        _playPause.setPreferredSize(new Dimension(30,30));
       

        //Position to add Text (x,y)
        _cmdList[0] = _fontList.get(4);
        _cmdList[2] = "16";
        _cmdList[3] = "0xffffff";
        //set time display font
        _timePlayed.setFont(new Font("Arial",Font.BOLD,14));
        _timePlayed.setForeground(Color.white);
        _timePlayed.setText("00:00");
       
        //Add ActionListener
        _startTimeBtn.addActionListener(this);
        _endTimeBtn.addActionListener(this);
        _save.addActionListener(this);
        _preview.addActionListener(this);
        _textBtn.addActionListener(this);
        _fontSize.addActionListener(this);
        _Mono.addActionListener(this);
        _Sans.addActionListener(this);
        _Serif.addActionListener(this);
        _Bold.addActionListener(this);
        _Italic.addActionListener(this);
        _colorBtn.addActionListener(this);
        _fontBtn.addActionListener(this);
        _replaceBtn.addActionListener(this);
        _mute.addActionListener(this);
        _forward.addActionListener(this);
        _backward.addActionListener(this);
        _playPause.addActionListener(this);
        _extractA.addActionListener(this);
        _extractV.addActionListener(this);
        _mergeBtn.addActionListener(this);

        //add to edit panel,adding order relates to display order
        editPanel.add(_save);
        editPanel.add(_replaceBtn);
        editPanel.add(_mergeBtn);
        editPanel.add(_extractA);
        editPanel.add(_extractV);  
        editPanel.add(_textBtn);
        editPanel.add(_fontSize);
        editPanel.add(_fontBtn);  
        editPanel.add(_Bold);
        editPanel.add(_Italic);
        editPanel.add(_colorBtn);
        editPanel.add(_font);
        editPanel.add(_startTimeBtn);
        editPanel.add(_endTimeBtn);
        editPanel.add(_preview);
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
        frame.setDefaultCloseOperation(MenuA3.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
     

        //play video and start timer
       
      	 _video.playMedia(_filePath);
      	 _ticker.start();                
 
    }

   //font list
    private void createFont(){
    	String path = "/usr/share/fonts/truetype/freefont/";
    	_fontList.add(path+"FreeMono.ttf");
    	_fontList.add(path+"FreeMonoBold.ttf");
    	_fontList.add(path+"FreeMonoOblique.ttf");
    	_fontList.add(path+"FreeMonoBoldOblique.ttf");
    	_fontList.add(path+"FreeSans.ttf");
    	_fontList.add(path+"FreeSansBold.ttf");
    	_fontList.add(path+"FreeSansOblique.ttf");
    	_fontList.add(path+"FreeSansBoldOblique.ttf");
    	_fontList.add(path+"FreeSerif.ttf");
    	_fontList.add(path+"FreeSerifBold.ttf");
    	_fontList.add(path+"FreeSerifItalic.ttf");
    	_fontList.add(path+"FreeSerifBoldItalic.ttf");	
    
    	FlowLayout flow = new FlowLayout();
    	flow.setHgap(200);
    	_fontWindow.setLayout(flow);
    	_fontWindow.setVisible(false);
    	_fontWindow.setMinimumSize(new Dimension(150,150));
    	
    	_fontWindow.add(_Mono);
    	_fontWindow.add(_Sans);
    	_fontWindow.add(_Serif);
    

    }

	//Get file browser 
	private String[] pick() throws FileNotFoundException{
			String[] fileInfo = new String[2];
			FileFilter videoAudioFilter = new FileNameExtensionFilter("Audio files",new String[] {"mp3","wav"} );
	
			_fc.setFileFilter(videoAudioFilter);
			
			if(_fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				
				File file = _fc.getSelectedFile();	
				fileInfo[0] = file.getName();
				fileInfo[1] = file.getPath();
			}
			else{
				System.out.println("No file");
			}
			return fileInfo;
	}

    //Set button size to the same
    private void setButtonSize(JButton e){
    	e.setPreferredSize(new Dimension(120,25));
    }
    
    //Execute bash command
	private String execCmd(String cmd){
		try {
		/*	_waitLabel.setVisible(true);
			_doneLabel.setVisible(false);
			_waitWin.setVisible(true);*/
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
		/*	_waitLabel.setVisible(false);
			_doneLabel.setVisible(true);
			*/
			
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
    
  //write to file
  	protected static void AddHistory(String toBeAdded) throws IOException{

  		//	BufferedWriter writer = new BufferedWriter(new FileWriter(Menu._file,true));
  		//	writer.append(toBeAdded);
  		//	writer.close();
  		
  	}
  	
    //convert decimal to Hex
    public String convert(int n) {
    	  return Integer.toHexString(n);
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
					if(_video.isPlaying()){
						_video.pause();
						_playPause.setText(">");
					}
					String cmd = "avconv -i " + _filePath + " -vn -acodec copy " + output+".mp3";
					_doneLabel.setVisible(false);
					_waitWin.setVisible(true);
					_waitLabel.setVisible(true);

					_extractTask.setCmd(cmd);
					_extractTask.execute();
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
					if(_video.isPlaying()){
						_video.pause();
						_playPause.setText(">");
					}
					String cmd = "avconv -i " + _filePath + " -vn -acodec copy -map 0:v " + output+".mp4";
					
					_doneLabel.setVisible(false);
					_waitWin.setVisible(true);
					_waitLabel.setVisible(true);

					_extractTask.setCmd(cmd);
					_extractTask.execute();
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "No Video Track", "Error!",JOptionPane.INFORMATION_MESSAGE);

			}
		}
		//Replace audio with chosen file
		else if(e.getSource() == _replaceBtn){
			try {
				String audioPath = this.pick()[1];
				if(audioPath != null){
					String output = JOptionPane.showInputDialog(null,"Enter Output Video file Name(No File Extension needed):");
					if(output != null){
						if(_video.isPlaying()){
							_video.pause();
							_playPause.setText(">");
						}
						
						String cmd = "avconv -i "+_filePath + " -i "+audioPath + " -map 0:v -map 1:a -strict experimental "+output+".mp4";
						_doneLabel.setVisible(false);
						_waitWin.setVisible(true);
						_waitLabel.setVisible(true);

						_extractTask.setCmd(cmd);
						_extractTask.execute();
					}
				}
			} catch (FileNotFoundException e1) {
				
				e1.printStackTrace();
			}
		}
		/*
		 * Only works on UG4 labs
		 * not my laptop, not compsci lab level 1
		 */
		else if(e.getSource() == _mergeBtn){
			try {
				String audioPath = this.pick()[1];
				if(audioPath != null){
					String output = JOptionPane.showInputDialog(null,"Enter Output Video file Name(No File Extension needed):");
					if(output != null){
						if(_video.isPlaying()){
							_video.pause();
							_playPause.setText(">");
						}
						String cmd = "avconv -i "+_filePath + " -i "+audioPath + " -filter_complex amix=inputs=2 -strict experimental "+output+".mp4";
						_doneLabel.setVisible(false);
						_waitWin.setVisible(true);
						_waitLabel.setVisible(true);

						_extractTask.setCmd(cmd);
						
						_extractTask.execute();
					}
				}
			} catch (FileNotFoundException e1) {
				
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == _fontBtn){
		
			_fontWindow.setVisible(true);
		}
		
		//get color chosen
		else if(e.getSource() == _colorBtn){
			
			
	        Color chosenColor = JColorChooser.showDialog(_colorBtn, "Choose Font Color", Color.black);
	        
	        if(chosenColor !=null){
	        	_font.setForeground(chosenColor);
	        	String r = this.convert(chosenColor.getRed());
	        	String g = this.convert(chosenColor.getGreen());
	        	String b = this.convert(chosenColor.getBlue());
	        	_rgb = "0x"+r+g+b;	
	        	_cmdList[3] = _rgb;
	        }
		}
		//get font choices
		else if(e.getSource() == _Mono){
			_fontWindow.setVisible(false);
			if(_isMono){
				_font.setText("Sans");
				_isMono = false;
				
				if(_isBold && _isItalic){
					_font.setFont(new Font("Sans",Font.BOLD | Font.ITALIC,20));
					_cmdList[0] = _fontList.get(7);
				}
				else if(_isBold){
					_font.setFont(new Font("Sans",Font.BOLD,20));
					_cmdList[0] = _fontList.get(5);

				}
				else if(_isItalic){
					_font.setFont(new Font("Sans",Font.ITALIC,20));
					_cmdList[0] = _fontList.get(6);

				}else {
					_font.setFont(new Font("Sans",Font.PLAIN,20));
					_cmdList[0] = _fontList.get(4);

				}
				
			
			}
			else{
				_font.setText("Mono");
				_isMono = true;
				if(_isBold && _isItalic){
					_font.setFont(new Font("Mono",Font.BOLD | Font.ITALIC,20));
					_cmdList[0] = _fontList.get(3);

				}
				else if(_isBold){
					_font.setFont(new Font("Mono",Font.BOLD,20));
					_cmdList[0] = _fontList.get(1);

				}
				else if(_isItalic){
					_font.setFont(new Font("Mono",Font.ITALIC,20));
					_cmdList[0] = _fontList.get(2);

				}else {
					_font.setFont(new Font("Mono",Font.PLAIN,20));
					_cmdList[0] = _fontList.get(0);

				}
				
			}
		}
		
		else if(e.getSource() == _Sans){
			_fontWindow.setVisible(false);
			
			if(_isSans){
				_isSans=false;
			}
			else{
				_isSans=true;
			}
			_font.setText("Sans");
			_isMono = false;

			if(_isBold && _isItalic){
				_font.setFont(new Font("Sans",Font.BOLD | Font.ITALIC,20));
				_cmdList[0] = _fontList.get(7);

			}
			else if(_isBold){
				_font.setFont(new Font("Sans",Font.BOLD,20));
				_cmdList[0] = _fontList.get(5);

			}
			else if(_isItalic){
				_font.setFont(new Font("Sans",Font.ITALIC,20));
				_cmdList[0] = _fontList.get(6);

			}else {
				_font.setFont(new Font("Sans",Font.PLAIN,20));
				_cmdList[0] = _fontList.get(4);

			}
			
		}
		else if(e.getSource() == _Serif){
			_fontWindow.setVisible(false);
			if(_isSerif){
				_font.setText("Sans");
				_isSerif = false;

				if(_isBold && _isItalic){
					_font.setFont(new Font("Sans",Font.BOLD | Font.ITALIC,20));
					_cmdList[0] = _fontList.get(7);

				}
				else if(_isBold){
					_font.setFont(new Font("Sans",Font.BOLD,20));
					_cmdList[0] = _fontList.get(5);

				}
				else if(_isItalic){
					_font.setFont(new Font("Sans",Font.ITALIC,20));
					_cmdList[0] = _fontList.get(6);

				}else {
					_font.setFont(new Font("Sans",Font.PLAIN,20));
					_cmdList[0] = _fontList.get(4);

				}
				
			
			}
			else{
				_font.setText("Serif");
				_isSerif = true;
				if(_isBold && _isItalic){
					_font.setFont(new Font("Serif",Font.BOLD | Font.ITALIC,20));
					_cmdList[0] = _fontList.get(11);

				}
				else if(_isBold){
					_font.setFont(new Font("Serif",Font.BOLD,20));
					_cmdList[0] = _fontList.get(9);
				}
				else if(_isItalic){
					_font.setFont(new Font("Serif",Font.ITALIC,20));
					_cmdList[0] = _fontList.get(10);

				}else {
					_font.setFont(new Font("Serif",Font.PLAIN,20));
					_cmdList[0] = _fontList.get(8);

				}
				
			}
		}
		else if(e.getSource() == _Bold){
			Font font = _font.getFont();
			if(_isBold && _isItalic){
				_isBold = false;
				_font.setFont(new Font(font.getName(),Font.ITALIC,20));
				if(font.getName().equals("Sans")){
					_cmdList[0] = _fontList.get(6);
				}
				else if(font.getName().equals("Mono")){
					_cmdList[0] = _fontList.get(2);

				}
				else if(font.getName().equals("Serif")){
					_cmdList[0] = _fontList.get(10);
				}	
			}
			else if(_isBold && !_isItalic){
				_isBold=false;
				_font.setFont(new Font(font.getName(),Font.PLAIN,20));
				if(font.getName().equals("Sans")){
					_cmdList[0] = _fontList.get(4);
				}
				else if(font.getName().equals("Mono")){
					_cmdList[0] = _fontList.get(0);

				}
				else if(font.getName().equals("Serif")){
					_cmdList[0] = _fontList.get(8);
				}	
			}
			else if(!_isBold && _isItalic){
				_isBold=true;
				_font.setFont(new Font(font.getName(),Font.BOLD | Font.ITALIC,20));
				if(font.getName().equals("Sans")){
					_cmdList[0] = _fontList.get(7);
				}
				else if(font.getName().equals("Mono")){
					_cmdList[0] = _fontList.get(3);

				}
				else if(font.getName().equals("Serif")){
					_cmdList[0] = _fontList.get(11);
				}	
			}
			else if(!_isBold && !_isItalic){
				_isBold=true;
				_font.setFont(new Font(font.getName(),Font.BOLD,20));
				if(font.getName().equals("Sans")){
					_cmdList[0] = _fontList.get(5);
				}
				else if(font.getName().equals("Mono")){
					_cmdList[0] = _fontList.get(1);

				}
				else if(font.getName().equals("Serif")){
					_cmdList[0] = _fontList.get(9);
				}	
			}
		}
		else if(e.getSource() == _Italic){
			Font font = _font.getFont();

			if(_isBold && _isItalic){
				_isItalic = false;
				_font.setFont(new Font(font.getName(),Font.BOLD,20));
				if(font.getName().equals("Sans")){
					_cmdList[0] = _fontList.get(5);
				}
				else if(font.getName().equals("Mono")){
					_cmdList[0] = _fontList.get(1);

				}
				else if(font.getName().equals("Serif")){
					_cmdList[0] = _fontList.get(9);
				}	
			}
			else if(_isBold && !_isItalic){
				_isItalic=true;
				_font.setFont(new Font(font.getName(),Font.BOLD|Font.ITALIC,20));
				if(font.getName().equals("Sans")){
					_cmdList[0] = _fontList.get(7);
				}
				else if(font.getName().equals("Mono")){
					_cmdList[0] = _fontList.get(3);

				}
				else if(font.getName().equals("Serif")){
					_cmdList[0] = _fontList.get(11);
				}	
			}
			else if(!_isBold && _isItalic){
				_isItalic=false;
				_font.setFont(new Font(font.getName(),Font.PLAIN,20));
				if(font.getName().equals("Sans")){
					_cmdList[0] = _fontList.get(4);
				}
				else if(font.getName().equals("Mono")){
					_cmdList[0] = _fontList.get(0);

				}
				else if(font.getName().equals("Serif")){
					_cmdList[0] = _fontList.get(8);
				}	
			}
			else if(!_isBold && !_isItalic){
				_isItalic=true;
				_font.setFont(new Font(font.getName(),Font.ITALIC,20));
				if(font.getName().equals("Sans")){
					_cmdList[0] = _fontList.get(6);
				}
				else if(font.getName().equals("Mono")){
					_cmdList[0] = _fontList.get(2);

				}
				else if(font.getName().equals("Serif")){
					_cmdList[0] = _fontList.get(10);
				}	
			}
		}
		else if(e.getSource() == _fontSize){
			String size = JOptionPane.showInputDialog("Enter font size(number 1 to 70):");
			int fontSize = 16;
			if(size !=null){
				try{
					fontSize = Integer.parseInt(size);
					if(fontSize >= 1 && fontSize <= 70){
						_fontSize.setText("FontSize:"+fontSize);
						_cmdList[2]=size;
					}
					else{
						JOptionPane.showMessageDialog(null, "Please Enter numbers(1 to 70) only.");
					}
				}
				catch(NumberFormatException e1){
					JOptionPane.showMessageDialog(null, "Please Enter numbers(1 to 70) only.");
				}
			}
		}
		else if(e.getSource() == _textBtn){
			if(_cmdList[1] != null){
				String text = JOptionPane.showInputDialog(null, _cmdList[1], "Add Text", JOptionPane.INFORMATION_MESSAGE);
				_cmdList[1] = text;
			}
			else{
				String text = JOptionPane.showInputDialog(null, "Enter Text to add:", "Add Text", JOptionPane.INFORMATION_MESSAGE);
				_cmdList[1] = text;
			}
		}
		else if(e.getSource() == _preview){
			if(_video.isPlaying()){
				_video.pause();
			}
			if(_cmdList[1] == null){
				JOptionPane.showMessageDialog(null, "Please Enter text to add!", "Error!", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(_cmdList[4] == null || _cmdList[5] == null){
				JOptionPane.showMessageDialog(null, "Please Enter StartTimeTo or ToEndTime!", "Error!", JOptionPane.INFORMATION_MESSAGE);

			}
			else{
				if(_cmdList[4] == null || _cmdList[4].equals("-1")){
					String cmd = "avplay -i "+_filePath + " -vf \"drawtext=fontfile='" + _cmdList[0]+"': text='"+_cmdList[1]+"': x=600: y=300: "+"fontsize="+_cmdList[2]+": fontcolor='"+_cmdList[3]+"': draw='gt(t,"+_cmdList[5]+")'\" ";
					System.out.println(cmd);
					cmd = this.execCmd(cmd);
				}
				else if(_cmdList[5] == null || _cmdList[5].equals("-1"))
				{
					String cmd = "avplay -i "+_filePath + " -vf \"drawtext=fontfile='" + _cmdList[0]+"': text='"+_cmdList[1]+"': x=600: y=300: "+"fontsize="+_cmdList[2]+": fontcolor='"+_cmdList[3]+"': draw='lt(t,"+_cmdList[4]+")'\" ";
					System.out.println(cmd);

					cmd = this.execCmd(cmd);
				}
				
			}
			
			
		}
		//Get Starting time
		else if(e.getSource() == _startTimeBtn){
			int startTime = 0;
			int length =(int)_video.getLength()/1000;
			int min = length/60;
			int sec = length%60;
			JOptionPane.showMessageDialog(null, "The text will appear from start of the video to the time given. Please enter in order MINUTE -> SECOND. NOTE:This will set ToEndTime to 0!","Readme",JOptionPane.INFORMATION_MESSAGE);
			String userMin = JOptionPane.showInputDialog(null,"Enter the minute to end text, length of video is:"+min+" minutes "+sec+" seconds");
			if(userMin != null){
				try{
					int minGiven = Integer.parseInt(userMin);
					if(minGiven >= 0 && minGiven <= min){
						startTime = minGiven*60;
						String userSec = JOptionPane.showInputDialog(null,"Enter the second to end text, length of video is:"+min+" minutes "+sec+" seconds");
						if(userSec != null){
							try{
								int SecGiven = Integer.parseInt(userSec);
								if(!userMin.equals(min)){
									if(SecGiven >= 0 && SecGiven < 60){
										startTime = startTime + SecGiven;
										_cmdList[4] = String.valueOf(startTime);
										_cmdList[5] = "-1";

									}
									else{
										JOptionPane.showMessageDialog(null, "Please Enter a number for second less than 60", "Error!", JOptionPane.INFORMATION_MESSAGE);

									}
								}
								else if(userMin.equals(min)){
									if(SecGiven >= 0 && SecGiven < sec){
										startTime = startTime + SecGiven;
										_cmdList[4] = String.valueOf(startTime);
										_cmdList[5] = "-1";
									}
									else{
										JOptionPane.showMessageDialog(null, "Please Enter a number for second less than "+sec, "Error!", JOptionPane.INFORMATION_MESSAGE);

									}
								}
							}
							catch(NumberFormatException e1){
								JOptionPane.showMessageDialog(null, "Please Enter a number for second!","Error!", JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "Please Enter a number for minute less than "+min, "Error!", JOptionPane.INFORMATION_MESSAGE);

					}
				}
				catch(NumberFormatException e1){
					JOptionPane.showMessageDialog(null, "Please Enter a number for minute less than "+min, "Error!", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		
		}
		//Get Ending time
		else if(e.getSource() == _endTimeBtn){
			int startTime = 0;
			int length =(int)_video.getLength()/1000;
			int min = length/60;
			int sec = length%60;
			JOptionPane.showMessageDialog(null, "The text will appear from time given to end of the video. Please enter in order MINUTE -> SECOND. NOTE:This will set StartTimeTo to 0!","Readme",JOptionPane.INFORMATION_MESSAGE);
			String userMin = JOptionPane.showInputDialog(null,"Enter the minute to end text, length of video is:"+min+" minutes "+sec+" seconds");
			if(userMin != null){
				try{
					int minGiven = Integer.parseInt(userMin);
					if(minGiven >= 0 && minGiven <= min){
						startTime = minGiven*60;
						String userSec = JOptionPane.showInputDialog(null,"Enter the second to end text, length of video is:"+min+" minutes "+sec+" seconds");
						if(userSec != null){
							try{
								int SecGiven = Integer.parseInt(userSec);
								if(!userMin.equals(min)){
									if(SecGiven >= 0 && SecGiven < 60){
										startTime = startTime + SecGiven;
										_cmdList[5] = String.valueOf(startTime);
										_cmdList[4] = "-1";


									}
									else{
										JOptionPane.showMessageDialog(null, "Please Enter a number for second less than 60", "Error!", JOptionPane.INFORMATION_MESSAGE);

									}
								}
								else if(userMin.equals(min)){
									if(SecGiven >= 0 && SecGiven < sec){
										startTime = startTime + SecGiven;
										_cmdList[5] = String.valueOf(startTime);
										_cmdList[4] = "-1";
										
									}
									else{
										JOptionPane.showMessageDialog(null, "Please Enter a number for second less than "+sec, "Error!", JOptionPane.INFORMATION_MESSAGE);

									}
								}
							}
							catch(NumberFormatException e1){
								JOptionPane.showMessageDialog(null, "Please Enter a number for second!","Error!", JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "Please Enter a number for minute less than "+min, "Error!", JOptionPane.INFORMATION_MESSAGE);

					}
				}
				catch(NumberFormatException e1){
					JOptionPane.showMessageDialog(null, "Please Enter a number for minute less than "+min, "Error!", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
			
		}
		else if(e.getSource() == _save){
			
			
		}
	}		
}
