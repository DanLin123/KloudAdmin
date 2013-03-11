package edu.tsinghua.software.pages.monitor;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DataReader {
	/**
	 * @param rrdDb
	 * @param metrics
	 * @param picName
	 * @param start
	 * @param end
	 * @return
	 * @throws IOException
	 * @throws Exception 
	 */
	public ArrayList<Point> createPic(String dbPath, String m,
			String mObject, long start, long end) throws Exception {
		ArrayList<Point> points = new ArrayList<Point>();
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
		Statement stat = conn.createStatement();
		// get the point for moniotrObject metric
		ResultSet rs = stat
				.executeQuery("select * from monitor where metric=\""+ m + "\" and monitorObject=\""+mObject+"\" and " +
						"time >"+start+" and time <" + end+ ";\"");
		while (rs.next()) {
			points.add(new Point(rs.getString("time")+"",rs.getString("value")+""));
		}

		rs.close();
		conn.close();
		return points;
	}

	
}
