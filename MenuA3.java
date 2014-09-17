package vamixA3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
			

	public MenuA3(){
		
		try
	    {
	            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	            UIManager.put(_menuTab,Color.GREEN);

	    } catch (Exception ee){
	        ee.printStackTrace();
	    }
		this.initializeGUI();
		this.initializeDownload();
		this.initializeEdit();
	/*	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/	

	//	_menuTab.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=25 marginheight=5>Download</body></html>",_downloadPanel);
	// 	_menuTab.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=25 marginheight=5>Edit</body></html>",_editPanel);
		_menuTab.addTab("Download",_downloadPanel);
		_menuTab.addTab("Edit", _editPanel);
	 	this.add(_menuTab,BorderLayout.CENTER);
	 	
	 	//Set exit on close 
	 	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 	//display to true
		this.setVisible(true);
		
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
		
		//Set background color to white
		_downloadPanel.setLayout(null);
	 	_downloadPanel.setBackground(Color.white);
	 	
	 	//Set size and location for components
	 	_downloadButton.setBounds(380, 80, 80, 20);
	 	_urlTxt.setBounds(20, 80, 350, 20);
	 	_urlLabel.setBounds(20, 60, 400, 20);
	 	
	 	//Add actionlisteners to required componenets
	 	_downloadButton.addActionListener(this);
	 	
		//Add to panel and frame
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
		new MenuA3();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == _downloadButton){
			JOptionPane.showInputDialog("Enter url for Download");
			
		}
	}

}
