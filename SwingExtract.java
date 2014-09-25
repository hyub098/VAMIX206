package vamixA3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.SwingWorker;

public class SwingExtract extends SwingWorker<Void,Integer> {
	
	private static String _cmd;
	protected Process _process;
	protected boolean _cancelled = false;
	
	protected void setCmd(String cmd){
		_cmd = cmd;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		try {
			
			
			ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",_cmd);
			builder.redirectErrorStream(true);
		
			_process = builder.start();
			InputStream stdout = _process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = null;
			
			while ((line = stdoutBuffered.readLine()) != null && !_cancelled ) {

			
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	@Override
	protected void process(List<Integer> Chunk){
		
	}
	
	@Override
	protected void done(){
		MediaPlayer._waitLabel.setVisible(false);
		MediaPlayer._doneLabel.setVisible(true);
		MediaPlayer._waitWin.setVisible(true);
	
	}

	
}

