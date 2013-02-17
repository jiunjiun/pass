package jiunling.root;

import static jiunling.config.config.havaRoot;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class SuperUser {
	//Debugging
	private static final String TAG = "command";
	private static final boolean D = true;
		
	public SuperUser() {
		havaRoot = getRootAhth();		
	}
		
	private synchronized boolean getRootAhth() {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("exit\n");
			os.flush();
			int exitValue = process.waitFor();
			if (exitValue == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void runRoot(String command) {
		if(havaRoot) {
			
		}
	}
	
	public boolean runRootCommand(String command) {
		try {
			Process mProcess = Runtime.getRuntime().exec("su");
			OutputStream os = mProcess.getOutputStream();
		    InputStream is = mProcess.getInputStream();
		    writeLine( os, null, command );
		    writeLine( os, null, "exit" );
		    mProcess.waitFor();
			String headerLine = readString( is, null, false );
//			if(D) Log.e(TAG, "BufferedReader: "+ headerLine.toString());
			
			Pattern pattern = Pattern.compile("network=[{][^}]+[}]", Pattern.DOTALL | Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(headerLine);
			
			int i=0;
			Log.e(TAG, "------------------------------");
	        while (matcher.find()) {
	        	Log.e(TAG, "matcher.group("+i+"):\t"+matcher.group());
	        	i++;
	        }
		} catch (Exception e) {
			if(D) Log.d(TAG, "the device is not rooted¡A error message¡G " + e.getMessage());
			return false;
		} 
		return true;
	}
	
	/** ref:http://fecbob.pixnet.net/blog/post/36062349-%5Bandroid%5D-get-process-id-with-shell-command-and-read-by-java **/
	
	private void writeLine(OutputStream os, PrintWriter logWriter, String value) throws IOException {
		String line = value + "\n";
	    os.write( line.getBytes() );
	    if( logWriter != null ) {
	      logWriter.println( value );
	    }
	}
	
	private String readString(InputStream is, PrintWriter logWriter, boolean block) throws IOException {
	    if( !block && is.available() == 0 ) {
	    	//Caller doesn't want to wait for data and there isn't any available right now
	    	return null;
	    }
	    byte firstByte = (byte)is.read(); //wait till something becomes available
	    int available = is.available();
	    byte[] characters = new byte[available + 1];
	    characters[0] = firstByte;
	    is.read( characters, 1, available );
	    String string = new String( characters );
	    if( logWriter != null ) {
	      logWriter.println( string );
	    }
	    return string;
	}
}
