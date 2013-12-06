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
package de.tud.eclipse_achievements.index;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.tud.eclipse_achievements.index.cache.AchievementCache;
import de.tud.eclipse_achievements.index.cache.SQLiteAchievementCache;
import de.tud.eclipse_achievements.index.entities.Achievement;

/**
 * @author Christian Vogel
 * @version 0.0.1.a 2012-04-21
 * 
 * @since 0.0.1.a
 */
public class AchievementIndex {
	private AchievementCache cache;

	private Map<String, Achievement> achievements;
	
	private static AchievementIndex instance;
	
	private AchievementIndex() {
		achievements = new TreeMap<String, Achievement>();
		cache = new SQLiteAchievementCache();
	}
	
	public static AchievementIndex getInstance() {
		if(instance == null) {
			instance = new AchievementIndex();
		}
		
		return instance;
	}
 
	/*public void update(IAchievementParser a) {
		try {
			cache.put(a.getName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/


	public List<Achievement> getAllAchievements() {
		if (achievements.size() == 0) {
			return Collections.emptyList();
		}
		
		return new LinkedList<Achievement>(achievements.values());
	}

	public List<Achievement> getCompletedAchievements() {
		if (achievements.size() == 0) {
			return Collections.emptyList();
		}

		List<Achievement> completed = new LinkedList<Achievement>();

		for (Achievement achievement : achievements.values()) {
			if (achievement.isCompleted()) {
				completed.add(achievement);
			}
		}
		
		return completed;
	}


	public Achievement getAchievementByName(String name) {
		if (!achievements.containsKey(name)) {
			return null;
		}

		return achievements.get(name);
	}
	
	public void cacheAchievements() {
		try {
			cache.store(achievements.values());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clear() {
		achievements = null;
	}

	public void addArchievement(Achievement achievement) {
		if(achievement == null) {
			return;
		}
		
		System.out.println("Adding new Achievement: " + achievement);
		achievements.put(achievement.getName(), achievement);
	}
	
	public void addAchievements(Collection<Achievement> achievements) {
		if(achievements == null) {
			return;
		}
		
		for(Achievement achievement : achievements) {
			addArchievement(achievement);
		}
	}
}
