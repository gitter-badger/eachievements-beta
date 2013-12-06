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
package de.tud.eclipse_achievements.core.parser;

import org.eclipse.core.resources.IResource;

public interface IResourceAdapter {
	/**
	 * Return IResource as String if applicable.
	 * Usually this means the contents of the file.
	 * @return resource as String
	 * @throws IllegalStateException if resource can not be converted to String
	 */
	String asString();
	
	/**
	 * Return original resource
	 * @return original resource object
	 */
	IResource getResource();
}
