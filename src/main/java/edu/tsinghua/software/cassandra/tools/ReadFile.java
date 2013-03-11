package edu.tsinghua.software.cassandra.tools;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ReadFile {
	String content="";
	String path;
	public ReadFile(String path)
	{
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				content+=strLine+"\n";
				
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			content="Error: " + e.getMessage();
		}
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		content = content;
	}
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
