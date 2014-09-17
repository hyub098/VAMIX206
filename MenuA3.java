package vamixA3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI.TabbedPaneLayout;

public class MenuA3 extends JFrame implements ActionListener{
	
	//initialize all components
	private JPanel _downloadPanel = new JPanel();
	private JPanel _editPanel = new JPanel();
	private JTabbedPane _menuTab = new JTabbedPane();
	private JButton _downloadButton = new JButton("GO!");
	private JButton _editButton = new JButton("Edit");
	private JTextField _urlTxt = new JTextField();
	private JLabel _urlLabel = new JLabel("Enter url below to downlaod file:");
	private JLabel _editLabel = new JLabel("Open media file to edit:");
	protected static JProgressBar _progressBar = new JProgressBar();
	private static JButton _cancelButton = new JButton("Cancel");
	JDialog _processLog = new JDialog();

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
	public static void setVisibility(String componentName,boolean visiblity){
		if(componentName.equals("_progressBar")){
			_progressBar.setVisible(visiblity);
		}
	}
	private void initializeEdit(){
		
		//Set background color to white
		_editPanel.setLayout(null);
	 	_editPanel.setBackground(Color.white);
	 	
	 	//set components size and location
	 	_editButton.setBounds(200, 150, 100, 30);
	 	_editLabel.setBounds(200, 100, 400, 20);
	 	
	 	//Add components to panel
	 	_editPanel.add(_editLabel);
	 	_editPanel.add(_editButton);

	}
	private void initializeDownload(){
		
		JLabel copyrightLabel = new JLabel();
		//Set background color to white
		_downloadPanel.setLayout(null);
	 	_downloadPanel.setBackground(Color.white);
	 	
	 	//Set size and location for components
	 	_downloadButton.setBounds(380, 120, 80, 20);
	 	_cancelButton.setBounds(185,180,100,30);
	 	_urlTxt.setBounds(20, 120, 350, 20);
	 	_urlLabel.setBounds(20, 100, 400, 20);
	 	_urlLabel.setFont(new Font("Arial",Font.BOLD,14));
	 	copyrightLabel.setBounds(20,40,400,50);
	 	copyrightLabel.setText("<html>Please make sure you are only downloading Open-Source file:</html>");
	 	copyrightLabel.setFont(new Font("Arial",Font.BOLD,16));
	 	_progressBar.setBounds(12, 150, 470, 20);
	 	_progressBar.setValue(0);
	 	_progressBar.setMaximum(100);
	 //	_progressBar.setVisible(false);
	 	_cancelButton.setVisible(false);

	 	//Add actionlisteners to required componenets
	 	_downloadButton.addActionListener(this);
	 	
	 	_processLog.setTitle("here");
	 	_processLog.add(_progressBar);
	 	_processLog.add(_cancelButton);
	 	_processLog.setMinimumSize(new Dimension(300,300));
	 	_processLog.setLocationRelativeTo(this);
	 	_processLog.setVisible(true);

		//Add to panel and frame
	 	_downloadPanel.add(_cancelButton);
	 	//_downloadPanel.add(_progressBar);
	 	_downloadPanel.add(copyrightLabel);
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
	 	this.setResizable(true);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == _downloadButton){
			if(_urlTxt.getText().equals("")){
				JOptionPane.showMessageDialog(null,"Please enter a valid url");
			}
			else{
				new DownloadA3(_urlTxt.getText());
			}
			
			
		}
	}

}
