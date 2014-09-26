package vamixA3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;


public class MenuA3 extends JFrame implements ActionListener{
	
	
	//Class fields
	//Tab , copyright warning
	private JTabbedPane _menuTab = new JTabbedPane();

	//DOWNLOAD panel,buttons,textfield,labels,dialog window
	//progress bar, swingworker, process
	private JPanel _downloadPanel = new JPanel();
	private JButton _downloadButton = new JButton("GO!");
	private JTextField _urlTxt = new JTextField();
	private JLabel _urlLabel = new JLabel("Enter url below to downlaod file:");
	private JLabel _copyrightLabelDownload = new JLabel();
	protected static JProgressBar _progressBar = new JProgressBar();
	protected static JButton _cancelButton = new JButton("Cancel");
	protected static JDialog _processLog = new JDialog();
	protected static JLabel _waitLabel = new JLabel();
	static SwingDownload _task = new SwingDownload("null");
	protected static boolean _result;
	private Process _process;


	//EDIT panel,button,label,file chooser
	private JLabel _copyrightLabelEdit = new JLabel();
	private JPanel _editPanel = new JPanel();
	private JButton _editButton = new JButton("Edit!");
	private JButton _fileBrowserButton = new JButton("Browse");
	private JLabel _editLabel = new JLabel("Open media file to edit:");
	private JTextField _mediaPath = new JTextField();
	private JFileChooser _fc = new JFileChooser();


	
	public MenuA3(){
		
		this.initializeGUI();
		this.initializeDownload();
		this.initializeEdit();
		
		//Add tabs
		_menuTab.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=25 marginheight=5>Download</body></html>",_downloadPanel);
	 	_menuTab.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=25 marginheight=5>Edit</body></html>",_editPanel);
	 	this.add(_menuTab,BorderLayout.CENTER);
	 	
	 	//Set exit on close 
	 	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 	//display to true
		this.setVisible(true);
		
	}

	//Get file browser 
	public String[] pick() throws FileNotFoundException{
			String[] fileInfo = new String[2];
			FileFilter videoAudioFilter = new FileNameExtensionFilter("Video/Audio files",new String[] {"avi", "mp4","mp3","wav"} );
	
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

	public static void setVisibility(String componentName,boolean visiblity){
		if(componentName.equals("_processLog")){
			_processLog.setVisible(visiblity);
		}
	}
	private void initializeEdit(){
		
		//Set background color to white
		_editPanel.setLayout(null);
	
	 	
		
	 	//set components size and location
		_mediaPath.setEditable(false);
		_mediaPath.setBounds(20, 120, 350, 20);
	 	_editButton.setBounds(200, 150, 100, 30);
	 	_editLabel.setBounds(20, 100, 400, 20);
	 	_editLabel.setFont(new Font("Arial",Font.BOLD,14));
	 	_fileBrowserButton.setBounds(380, 117, 80, 25);

	 	
	 	_copyrightLabelEdit.setBounds(20,40,400,50);
	 	_copyrightLabelEdit.setText("<html>Please make sure the file is Open-Source:</html>");
	 	_copyrightLabelEdit.setFont(new Font("Arial",Font.BOLD,16));
	 	
	 	//Add actionlisteners to required componenets
	 	_editButton.addActionListener(this);
	 	_fileBrowserButton.addActionListener(this);
	 	
	 	//Add components to panel
	 	_editPanel.add(_fileBrowserButton);
	 	_editPanel.add(_copyrightLabelEdit);
	 	_editPanel.add(_mediaPath);
	 	_editPanel.add(_editLabel);
	 	_editPanel.add(_editButton);

	}
	private void initializeDownload(){
	 	_processLog.setLayout(null);
		//Set background color to white
		_downloadPanel.setLayout(null);
	 
	 	
	 	//Set size and location for components
	 	_downloadButton.setBounds(380, 117, 80, 25);
	 	_cancelButton.setBounds(100,80,100,30);
	 	_urlTxt.setBounds(20, 120, 350, 20);
	 	_urlLabel.setBounds(20, 100, 400, 20);
	 	_urlLabel.setFont(new Font("Arial",Font.BOLD,14));
	 
	 	_waitLabel.setBounds(20,10,400,50);
	 	_waitLabel.setText("<html>Please wait:</html>");
	 	_waitLabel.setFont(new Font("Arial",Font.BOLD,14));
	 	_progressBar.setBounds(20, 50, 260, 20);
	 	_progressBar.setValue(0);
	 	_progressBar.setMaximum(100);
	
	 
	 	_copyrightLabelDownload.setBounds(20,40,400,50);
	 	_copyrightLabelDownload.setText("<html>Please make sure the file is Open-Source:</html>");
	 	_copyrightLabelDownload.setFont(new Font("Arial",Font.BOLD,16));

	 	//Add actionlisteners to required componenets
	 	_downloadButton.addActionListener(this);
	 	_cancelButton.addActionListener(this);
	 	
	 	//Dialog pop up window
	 	_processLog.setTitle("Downloading..");
	 	JDialog.setDefaultLookAndFeelDecorated(true);
	 	_processLog.add(_waitLabel);
	 	_processLog.add(_progressBar);
	 	_processLog.add(_cancelButton);
	 	_processLog.setMinimumSize(new Dimension(300,150));
	 	_processLog.setLocationRelativeTo(null);
	 	_processLog.setVisible(false);

	 	
		//Add to panel and frame
	 	_downloadPanel.add(_copyrightLabelDownload);
	 	_downloadPanel.add(_urlLabel);
	 	_downloadPanel.add(_urlTxt);
	 	_downloadPanel.add(_downloadButton);
	 
	}

	private void initializeGUI(){
		//set size
		this.setSize(500,300);	
		Toolkit tk = Toolkit.getDefaultToolkit();

		Dimension dim = tk.getScreenSize();

		int xPos = (dim.width / 2) - (this.getWidth() / 2);
		int yPos = (dim.height / 2) - (this.getHeight() / 2);
		
		this.setLocation(xPos, yPos);
	 	this.setResizable(false);
	 	
		
	 	
	}
	public static void main(String[] args) {
		
		
		//Set look and feel
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new MenuA3();
	}
	

	
	class Download {
		
		private String _fileName;
		private String _url;
		
		public Download(String url){
			String[] tmp = url.split("/");
			_fileName = tmp[tmp.length-1];
			_url = url;
			startDownload();
		}
		
	
		private void startDownload(){
			
			
			String cmd =  "test -f " + _fileName + " && echo \"found\" || echo \"not found\"";
			cmd = this.execCmd(cmd);
			int dialogue = JOptionPane.showConfirmDialog(null, "Please Confirm this is Open-Source file","Open Source?",JOptionPane.YES_NO_OPTION);
			
			//Check if open source
			if(dialogue == JOptionPane.NO_OPTION){
				JOptionPane.showMessageDialog(null, "Please Download Open-Source file only");
				return;
			}
			//if file found, ask if override or resume
			if(cmd.equals("found")){
				//1 is resume, 0 is override
				int userChoice = JOptionPane.showOptionDialog(null, "File Exists!,Resume or Override?", "Warning!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Override","Resume"}, "default");
				
					//override existing file
					if(userChoice == 0){
						cmd = "rm " + _fileName;
						cmd = this.execCmd(cmd);
						MenuA3.setVisibility("_processLog",true);
						MenuA3._task = new SwingDownload(_url);
						MenuA3._task.execute();

					}
					//resume existing file
					else if (userChoice == 1){
						MenuA3.setVisibility("_processLog",true);
						MenuA3._task = new SwingDownload(_url);
						MenuA3._task.execute();
					}
			}
			else{
				MenuA3.setVisibility("_processLog",true);
				MenuA3._task = new SwingDownload(_url);
				MenuA3._task.execute();
			}
				
		}

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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == _downloadButton){
			if(_urlTxt.getText().equals("")){
			
			    JOptionPane.showMessageDialog(null,"Please enter a valid url","ERROR!",JOptionPane.INFORMATION_MESSAGE);
			}
			else{
				
				_waitLabel.setText("Please wait:");
				_cancelButton.setText("CANCEL");
				new Download(_urlTxt.getText());
			}			
		}
		else if(e.getSource() == _cancelButton){
			
			if(_task != null && _process != null){
				_processLog.setVisible(false);
				
				_task._cancelled = true;
				_task._process.destroy();
				_task.cancel(true);
			
			}
		}
		else if(e.getSource() == _fileBrowserButton){
			
			try {
				_mediaPath.setText(pick()[1]);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
		}
		else if(e.getSource() == _editButton){
			if(_mediaPath.getText().length() > 0){
				this.setVisible(false);
				
				SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		                new MediaPlayer(_mediaPath.getText());
		                
		            }
		        });
			}
			else{
				JOptionPane.showMessageDialog(null, "Please Choose a video or audio file!");
			}

		}
	}

}
