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

import java.sql.SQLException;
import java.util.Collection;

import de.tud.eclipse_achievements.index.entities.Achievement;

/**
 * Interface to store and load {@link IAchievementParser}s to the database.
 * 
 * @author Max
 */
public interface AchievementCache {
	/**
	 * Updates an achievement in the database.
	 * 
	 * @param a
	 *            The {@link IAchievementParser} name to store.
	 * @return true if the operation was successful, false otherwise.
	 * @throws SQLException
	 *             if the {@link IAchievementParser} was not stored correctly.
	 */
	void put(String a) throws SQLException;

	/**
	 * Get the value for the given {@link IAchievementParser}.
	 * 
	 * @param a
	 *            The {@link IAchievementParser} name to look up.
	 * @return the value for the given achievement
	 * @throws SQLException
	 */
	int get(String a) throws SQLException;

	/**
	 * Initialize the database with a given instance of
	 * {@link IAchievementStore}. <b>Hint:</b> will do nothing if the DB was
	 * already initialized.
	 * 
	 * @throws SQLException
	 *             if the database could not be initialized correctly.
	 * @param achievements
	 */
	void store(Collection<Achievement> achievements) throws SQLException;
	
	boolean areAchievementsAlreadyCached();
}
