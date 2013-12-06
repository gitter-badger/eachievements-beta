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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IResource;


public class RegexpAchievementParser implements IAchievementAnalyzer {

	private String m_regexp;
	private Pattern m_pattern;
	private String m_fileSuffix;

	public RegexpAchievementParser() {
		super();
		m_regexp = "";
	}
	
	public RegexpAchievementParser(String regex, String suffix) {
		this.m_regexp = regex;
		this.m_pattern = Pattern.compile(regex);
		this.m_fileSuffix = suffix;
	}

	public String getRegexp() {
		return m_regexp;
	}

	@Override
	public boolean analyze(IResourceAdapter adapter) throws Exception {
		if(!accepts(adapter)) {
			return false;
		}
		
		String s = adapter.asString();

		if (m_pattern == null) {
			m_pattern = Pattern.compile(m_regexp);
		}
		
		Matcher m = m_pattern.matcher(s);
		boolean b = m.find();
		
		return b;
	}

	public boolean accepts(IResourceAdapter adapter) {
		IResource res = adapter.getResource();
		if (res.exists() && res.getType() == IResource.FILE
				&& res.getName().endsWith(m_fileSuffix)) {
			return true;
		}
		return false;
	}
}
