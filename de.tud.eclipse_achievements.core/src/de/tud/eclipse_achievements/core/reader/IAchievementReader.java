/*******************************************************************************
 Copyright (c) 2012.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Max Leuthaeuser, Christian Vogel, Uwe Schmidt
 ******************************************************************************/
package de.tud.eclipse_achievements.core.reader;

import java.util.Collection;

import de.tud.eclipse_achievements.index.entities.Achievement;

/**
 * Interface providing methods to read achievements 
 * from a storage.
 * 
 * @author Christian Vogel
 * @version 0.0.1.a 2012-04-21
 * 
 * @since 0.0.1.a
 */
public interface IAchievementReader {
	
	/**
	 * Reads all available achievements from a given storage.
	 * 
	 * @return
	 */
	Collection<Achievement> read();

}
