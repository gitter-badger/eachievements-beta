/*******************************************************************************
 * Copyright (c) 2012.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Max Leuthaeuser, Christian Vogel, Uwe Schmidt
 ******************************************************************************/
package de.tud.eclipse_achievements.index.cache;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import de.tud.eclipse_achievements.index.entities.Achievement;

/**
 * @author Max
 */
public class SQLiteAchievementCache implements AchievementCache {
	private static final String SEP = File.separator;
	private static final String USER_HOME = System.getProperty("user.home");
	private static final String DB_PATH = USER_HOME + SEP + ".eclipse" + SEP
			+ "achievements.db";

	private Connection conn;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void put(String a) throws SQLException {
		Statement stat = conn.createStatement();
		ResultSet rs = stat
				.executeQuery("select * from achievements where name='" + a
						+ "';");
		if (!rs.next())
			throw new SQLException();
		int value = Integer.parseInt(rs.getString("value")) + 1;
		rs.close();

		Statement stmt = conn.createStatement();
		// Prepare a statement to update a record
		String sql = "UPDATE achievements SET value='" + value
				+ "' WHERE name = '" + a + "';";

		// Execute the insert statement
		stmt.executeUpdate(sql);
	}
	
	@Override
	public boolean areAchievementsAlreadyCached() {
		File cacheFile = new File(DB_PATH);
		
		return cacheFile.exists();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int get(String a) throws SQLException {
		Statement stat = conn.createStatement();
		
		ResultSet rs = stat
				.executeQuery("select * from achievements where name='" + a
						+ "';");
		
		while (rs.next()) {
			return Integer.parseInt(rs.getString("value"));
		}
		
		throw new SQLException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void store(Collection<Achievement> achievements) throws SQLException {
		if(!areAchievementsAlreadyCached()) {
			File f = new File(USER_HOME + SEP + ".eclipse");
			f.mkdirs();
		}

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SQLException("DB connection failed!");
		}

		conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
		Statement stat = conn.createStatement();
		stat.executeUpdate("create table if not exists achievements (name, value);");
		PreparedStatement prep = conn
				.prepareStatement("insert into achievements values (?, ?);");

		for (Achievement a : achievements) {
			ResultSet rs = stat
					.executeQuery("select * from achievements where name='"
							+ a.getName() + "';");
			
			if (!rs.next()) {
				prep.setString(1, a.getName());
				prep.setInt(2, a.getCompletionCounter());
				prep.addBatch();
			}
			rs.close();
		}
		conn.setAutoCommit(false);
		prep.executeBatch();
		conn.setAutoCommit(true);
	}

}
