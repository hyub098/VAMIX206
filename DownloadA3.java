package vamixA3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class DownloadA3 implements ActionListener{
	private Process process;
	private String _url;
	
	public DownloadA3(String url){
		String[] tmp = url.split("/");
		_url = tmp[tmp.length-1];
		
		this.startDownload();
		
	}
	
	private void startDownload(){
		
		
		String cmd =  "test -f " + _url + " && echo \"found\" || echo \"not found\"";
		cmd = this.execCmd(cmd);
		int dialogue = JOptionPane.showConfirmDialog(null, "Please Confirm this is Open Source file","Open Source?",JOptionPane.YES_NO_OPTION);
		
		
		//if file found, ask if override or resume
		if(cmd.equals("found")){
			//1 is resume, 0 is override
			int userChoice = JOptionPane.showOptionDialog(null, "File Exists!,Resume or Override?", "Warning!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Override","Resume"}, "default");
			
			//Check if open source
			if(dialogue == JOptionPane.YES_OPTION){
				System.out.println("found");
	/* 			exist.setVisible(true);
				resume.setVisible(true);
				override.setVisible(true);
				Enter.setVisible(false); */
			}
			else{
	//			copyright.setVisible(true);
			} 
			return;
		}

		//check copyright
		if(dialogue == JOptionPane.YES_OPTION){
			System.out.println("Not found");

	/*		Enter.setVisible(false);
			Cancel.setVisible(true);
			pb.setVisible(true);
			
			//execute download in background
			task.setTask(urlTxt.getText());
			task.execute();		
		}
		else{
			copyright.setVisible(true);
		} 		*/
	}
	}

	private String execCmd(String cmd){
		try {
			ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",cmd);
			builder.redirectErrorStream(true);
			
		
		
			process = builder.start();
			InputStream stdout = process.getInputStream();
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
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
