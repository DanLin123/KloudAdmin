package edu.tsinghua.software.deploy;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			System.out.println("start cassandra deamon");
			Process proc = Runtime.getRuntime().exec("/home/ld/Downloads/apache-cassandra-1.0.8/bin/cassandra");
			//to do check if the cassandra is up by checking node status. up/down
			BufferedReader read = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));

			while (read.ready()) {
				System.out.println(read.readLine());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
