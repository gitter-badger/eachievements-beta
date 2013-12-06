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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;



public class ResourceAdapter implements IResourceAdapter {
	
	private IResource m_res = null;
	private String m_asString = null;
	
	public ResourceAdapter(IResource res) {
		if (res == null) {
			throw new IllegalArgumentException();
		}
		m_res = res;
	}
	
	private String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
						int n;
						while ((n = reader.read(buffer)) != -1) {
							writer.write(buffer, 0, n);
						}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {        
			return "";
		}
	}
	
	@Override
	public String asString() {
		if (m_asString == null) {
			if (m_res.getType() == IResource.FILE) {
				IFile file = (IFile)m_res;
				try {
					InputStream ins = file.getContents();
					m_asString = convertStreamToString(ins);
				} catch (IOException ioe) {
					throw new IllegalStateException(ioe);
				} catch (CoreException ce) {
					throw new IllegalStateException(ce);
				}
			}
		}
		
		return m_asString;
	}

	@Override
	public IResource getResource() {
		return m_res;
	}
}
