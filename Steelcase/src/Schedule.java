import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.IOException;

public class Schedule {
	private ArrayList<Course> schedule = null;
	private int customs = Integer.MAX_VALUE;

	public Schedule() {
		schedule = new ArrayList<>();
	}

	public ArrayList<Course> getSchedule() {
		return this.schedule;
	}

	// TODO: check for duplicate course and time conflicts
	public boolean add(Course c) {
		// If not already in and doesn't conflict.
		return schedule.add(c);
	}

	// Can add conflicting courses to schedule (not db, just local obj)

	/**
	 * Checks if the schedule has any classes in it.
	 * 
	 * @return true if schedule.size() > 0, false if not.
	 */
	public boolean hasSchedule() {
		return schedule.size() > 0;
	}

	public boolean conflicts(Course c1) {
		if(GuiMain.loggedIn && !GuiMain.account.getCoursesTaken().isEmpty())
		{
			for(int i = 0; i < c1.getPreReqs().size(); i++)
			{
			  if(!GuiMain.account.getCoursesTaken().contains(c1.getPreReqs().get(i)))
			  {
				System.out.println("Prerequisite course required.");
				return true;
			  }
			}
		}

		for (Course c : schedule) {
			if (c.conflicts(c1))
				return true;
		}
		return false;
	}

	/**
	 * Queries the database for the user's schedule
	 * 
	 * @param account The user whose schedule we're pulling
	 * @return a Schedule object with the user's schedule in it.
	 */
	public static Schedule retrieveSchedule(Account account) {
		/**
			SELECT * FROM Course
			WHERE
					code IN (SELECT prereqCode FROM Prereq WHERE courseCode = 321 AND courseDep = "MECE")
				AND
				department IN (SELECT prereqDep FROM Prereq WHERE courseCode = 321 and courseDep = "MECE")
			)
		 */
		String sql = "SELECT * FROM Course c INNER JOIN Schedule s ON c.id = s.courseID WHERE s.email = ?";
		String sql2 = "SELECT id FROM Course c "
		+"INNER JOIN Prereq p "
		+"on c.code = p.prereqCode "
		+ "and c.department = p.prereqDep "
		+ "WHERE p.courseCode = ? AND p.courseDep = ?;";
		ArrayList<Integer> ids = new ArrayList<>();
		Schedule schedule = new Schedule();
		try (
			Connection conn = DataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
		) {
			ps.setString(1, account.getEmail());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// TODO: This can't be right...
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				int id 						= rs.getInt("id");
				int code					= rs.getInt("code");
				String department = rs.getString("department");
				char section 			= rs.getString("section").toCharArray()[0];
				String building 	= rs.getString("building");
				String long_title = rs.getString("long_title");
				String short_title= rs.getString("short_title");
				String description= rs.getString("description");
				String professor 	= rs.getString("professor");
				String day 				= rs.getString("day");
				String begin_time = rs.getString("begin_time") != null ? rs.getString("begin_time") : "null";
				String end_time 	= rs.getString("end_time") != null ? rs.getString("end_time") : "null";
				int capacity 			= rs.getInt("capacity");
				int enrollment		= rs.getInt("enrollment");
				String room 			= rs.getString("room");

				ps2.setInt(1, code);
				ps2.setString(2, department);
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next())
				{
					ids.add(rs2.getInt("id"));
				}
				// TODO: only add to schedule when all courses are successfully retrieved.
				schedule.add(new Course(id, code, department, section, building, long_title, short_title, description,
						professor, day, begin_time, end_time, capacity, enrollment, room, ids));
				// All courses have the same prerequisites with this one line. Make a new object
				// instead of clearing the old one.
				// TODO
				ids = new ArrayList<>();
				// ids.clear();
			}
		} catch (SQLException e) {
			// TODO: make log function
			System.out.println("Failed to retrieve Schedule");
			e.printStackTrace();
		}
		return schedule;// may return an empty Schedule
	}

	/**
	 * Works, gets correct number of milliseconds.
	 * 
	 * @param start
	 * @return
	 */
	public static long strToTime(String start) {
		int multiplier[] = { 3600000, 60000, 1000 };
		String startString = start; // read in string
		String splits[]; // array of strings split by colons

		long startTime = 0;

		splits = startString.split(":");
		// converts to milliseconds
		for (int x = 0; x < splits.length; x++) {
			startTime += (Integer.parseInt(splits[x]) * multiplier[x]);
		}
		return startTime;
	}

	/**
	 * Returns schedule as a calendar, with each day on a new line, and all of its
	 * classes
	 * including where the class is and the time
	 * 
	 * @return
	 */
	public ArrayList<ArrayList<Course>> calData() {
		ArrayList<ArrayList<Course>> cal = new ArrayList<ArrayList<Course>>();
		ArrayList<Course> mon = new ArrayList<Course>();
		ArrayList<Course> tue = new ArrayList<Course>();
		ArrayList<Course> wed = new ArrayList<Course>();
		ArrayList<Course> thu = new ArrayList<Course>();
		ArrayList<Course> fri = new ArrayList<Course>();
		ArrayList<Course> copy = (ArrayList<Course>) schedule.clone();
		ArrayList<Course> ordered = new ArrayList<Course>();
		ArrayList<Long> times = new ArrayList<Long>();
		for (int i = 0; i < schedule.size(); i++) {
			times.add(strToTime(copy.get(i).getBegin_time()));
		}
		for (int i = 0; i < schedule.size(); i++) {
			Long earliest = times.get(0);
			int index = 0;
			for (int j = 0; j < copy.size(); j++) {
				if (times.get(j) < earliest) {
					index = j;
				}
			}
			ordered.add(copy.get(index));
			copy.remove(index);
			times.remove(index);
		}
		for (int i = 0; i < ordered.size(); i++) {
			Scanner days = new Scanner(ordered.get(i).getDay());
			days.useDelimiter("");
			while (days.hasNext()) {
				char day = days.next().charAt(0);
				switch (day) {
					case 'M':
						mon.add(ordered.get(i));
						break;
					case 'T':
						tue.add(ordered.get(i));
						break;
					case 'W':
						wed.add(ordered.get(i));
						break;
					case 'R':
						thu.add(ordered.get(i));
						break;
					case 'F':
						fri.add(ordered.get(i));
						break;
					default:
						System.out.println("not a valid day");
						break;
				}
			}
		}
		cal.add(mon);
		cal.add(tue);
		cal.add(wed);
		cal.add(thu);
		cal.add(fri);
		return cal;
	}

	public String export(String file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			String output = "List view:\n" + toString();
			fos.write(output.getBytes());
			fos.flush();
			return "export successful";
		} catch (IOException e) {
			return "export failed";
		}
	}

	/**
	 * Connects to the database to save this schedule object
	 * to the given account. First it clears the account's
	 * current schedule.
	 * 
	 * @param account The account to save to.
	 * @return true if successfull, false if it fails at any point
	 */
	public boolean saveSchedule(Account account) {
		// check for conflicts in schedule.

		// clear current schedule
		Connection conn = null;
		boolean status = true;
		boolean updatedSchedule = true;

		try {
			conn = DataSource.getConnection();
		} catch (SQLException e) {
			// TODO: make log function
			System.err.println("Failed to connect to database.");
			e.printStackTrace();
			status = false;
		}

		// if connection failed, skip this
		if (status) {
			// try(Statement statement = conn.createStatement()) {
			try {
				conn.setAutoCommit(false);// set to false to send as one transaction

				// delete current schedule
				// String sql = String.format("DELETE FROM Schedule WHERE email LIKE %s",
				// account.getEmail());
				String sql = String.format("DELETE FROM Schedule WHERE email LIKE ?");
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, account.getEmail());
				ps.execute();
				// statement.addBatch(sql);

				// insert new schedule
				sql = String.format("INSERT INTO Schedule(email, courseID) VALUES(?, ?)");
				ps = conn.prepareStatement(sql);
				for (Course c : schedule) {
					ps.setString(1, account.getEmail());
					ps.setInt(2, c.getId());
					// sql = String.format("INSERT INTO Schedule(email, courseID) VALUES(%s, %s)",
					// account.getEmail(), c.getID());
					// statement.addBatch(sql);
					ps.addBatch();
				}

				// passing conn because this throws if anything failed
				// throwIfFailed(statement.executeBatch(), conn);
				throwIfFailed(ps.executeBatch(), conn);

				conn.commit();
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				updatedSchedule = false;
				// TODO: make log function
				e.printStackTrace();
				System.err.println("Failed to update schedule. Rolling back DB...");
				status = false;
			}
		}
		// I know, it's bad, but it had to be done. conn.rollback needs
		// to be called but it can throw, so it needs to be caught.
		if (!updatedSchedule) {
			try {
				conn.rollback();
				// TODO: make log function
				System.err.println("DB Rolled Back successfully.");
			} catch (SQLException sqlE) {
				// TODO: make log function
				System.err.println("Failed to rollback database after the schedule failed save!");
				sqlE.printStackTrace();
			}
		}
		return status;
	}

	// private function to check if each sql statement in saveSchedule succeeded.
	// if any of them failed, rollsback the transaction and throws a SQLException
	private void throwIfFailed(int[] batchStatuses, Connection conn) throws SQLException {
		int i = 0;
		while (i < batchStatuses.length) {
			// if any of them failed, rollback and throw an exception.
			if (batchStatuses[i++] == Statement.EXECUTE_FAILED) {
				conn.rollback();
				if ((i - 1) == 0) {// batchStatus[0] = delete statement.
					throw new SQLException("Failed to clear current schedule, rolling back transaction.");
				} // any other batch is an insert statement.
				throw new SQLException("Failed to insert new schedule, rolling back transaction.");
			}
		}
	}

	public void removeCourse(int id) {
		boolean removed = false;
		for (int i = 0; i < schedule.size(); i++) {
			if (schedule.get(i).getId() == id) {
				schedule.remove(i);
				removed = true;
				break;
			}
		}
		if (!removed) {
			System.out.println("Course not found in schedule.");
		}
	}

	public Course getCourse(int id)
	{
		for (int i = 0; i < schedule.size(); i++) {
			if (schedule.get(i).getId() == id) {
				return schedule.get(i);
			}
		}
		return null;
	}

	public void addCustom(String title, String description, String day, String begin, String end){
		try {
			LocalTime.parse(begin);
			LocalTime.parse(end);
			Course temp = new Course();
			temp.setLong_title(title);
			temp.setDescription(description);
			temp.setDay(day);
			temp.setBegin_time(begin);
			temp.setEnd_time(end);

			schedule.add(temp);
			customs--;
		} catch (DateTimeParseException | NullPointerException e) {
			System.err.println("user gave invalid times");
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < schedule.size(); i++) {
			sb.append(schedule.get(i).toString());
		}
		return sb.toString();
	}
}
