package vamixA3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.SwingWorker;

public class SwingDownload extends SwingWorker<Void,Integer> {
	
	private String _url;
	protected Process _process;
	private String _fileName;
	protected boolean _cancelled = false;
	public SwingDownload(String url){
		String[] tmp = url.split("/");
		_fileName = tmp[tmp.length-1];
		_url = url;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		try {
			
			int progress =0;
			String cmd = "wget -c --progress=dot " + _url;
			ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",cmd);
			builder.redirectErrorStream(true);
		
			_process = builder.start();
			InputStream stdout = _process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = null;
			
			while ((line = stdoutBuffered.readLine()) != null && !_cancelled ) {

				progress++;
				publish(progress);
			}
			
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
		
		//check if file download completed
		String cmd =  "test -f " + _fileName + " && echo \"found\" || echo \"not found\"";
		cmd = execCmd(cmd);
		if(cmd.equals("found")){
			MenuA3._progressBar.setValue(100);
			MenuA3._cancelButton.setText("CLOSE");
			MenuA3._waitLabel.setText("DONE!");
		}
		//Display Error message
		else{
			MenuA3._waitLabel.setText("ERROR!Please Try Again");
		}
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
}
