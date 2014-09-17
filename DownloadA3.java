package vamixA3;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class DownloadA3 implements ActionListener{
	private Process _process;
	private String _fileName;
	private String _url;
	private DownloadTask _task = new DownloadTask();
	
	public DownloadA3(String url){
		String[] tmp = url.split("/");
		_fileName = tmp[tmp.length-1];
		_url = url;
		
		this.startDownload();
		
	}
	
	private void startDownload(){
		
		
		String cmd =  "test -f " + _fileName + " && echo \"found\" || echo \"not found\"";
		cmd = this.execCmd(cmd);
		int dialogue = JOptionPane.showConfirmDialog(null, "Please Confirm this is Open-Source file","Open Source?",JOptionPane.YES_NO_OPTION);
		
		
		//if file found, ask if override or resume
		if(cmd.equals("found")){
			//1 is resume, 0 is override
			int userChoice = JOptionPane.showOptionDialog(null, "File Exists!,Resume or Override?", "Warning!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Override","Resume"}, "default");
			
			//Check if open source
			if(dialogue == JOptionPane.YES_OPTION){
				//override existing file
				if(userChoice == 0){
					cmd = "rm" + _fileName;
					cmd = this.execCmd(cmd);
					_task.setTask(_url);
					_task.execute();
				}
				//resume existing file
				else if (userChoice == 1){
					_task.setTask(_url);
					_task.execute();
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "Please Download Open-Source file only");
			} 
			return;
		}

		//check copyright for file not found
		if(dialogue == JOptionPane.YES_OPTION){
			_task.setTask(_url);
			_task.execute();
			MenuA3.setVisibility("_progressBar",true);
			MenuA3.setVisibility("_cancelButton",true);
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
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	//Swing worker for download
	class DownloadTask extends SwingWorker<Void, Integer> {
			
			private String _url;
			
			public void setTask(String url){
				_url = url;
			}
			
			@Override
			protected Void doInBackground() throws Exception {
				try {
					
					
					String cmd = "wget -c --progress=dot " + _url;
					ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",cmd);
					builder.redirectErrorStream(true);
					Process process;
				
					process = builder.start();
					InputStream stdout = process.getInputStream();
					BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
				
			}
			@Override
			protected void process(List<Integer> Chunk){
				for(int i : Chunk){
					MenuA3._progressBar.setValue(i);
				}
			}
			
			@Override
			protected void done(){
	
			}
		}


}
