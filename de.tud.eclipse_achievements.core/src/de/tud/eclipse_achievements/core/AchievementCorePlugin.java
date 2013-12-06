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
package de.tud.eclipse_achievements.core;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.tud.eclipse_achievements.core.api.IAchievementChangedListener;
import de.tud.eclipse_achievements.core.parser.StaticAnalysisVisitor;
import de.tud.eclipse_achievements.core.reader.IAchievementReader;
import de.tud.eclipse_achievements.core.reader.XMLAchievementReader;
import de.tud.eclipse_achievements.index.AchievementIndex;
import de.tud.eclipse_achievements.index.entities.Achievement;

/**
 * The activator class controls the plug-in life cycle
 */
public class AchievementCorePlugin extends AbstractUIPlugin implements IResourceChangeListener, IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.tud.eclipse_achievements.core";

	// The shared instance
	private static AchievementCorePlugin plugin;

	// Resource bundle.
	private ResourceBundle m_resourceBundle;
	
	private List<IAchievementChangedListener> achievementChangedListener;

	public String getPluginId() {
		return getBundle().getSymbolicName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		achievementChangedListener = new LinkedList<IAchievementChangedListener>();
		
		plugin = this;
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static AchievementCorePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * Open the alert dialog.
	 * 
	 * @param message
	 *            message
	 */
	public static void openAlertDialog(String message) {
		MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(),
				SWT.NULL | SWT.ICON_ERROR);
		box.setMessage(message);
		box.setText(getResourceString("ErrorDialog.Caption"));
		box.open();
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return m_resourceBundle;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = AchievementCorePlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
				event.getDelta().accept(new StaticAnalysisVisitor());
				
				for(IAchievementChangedListener listener : achievementChangedListener) {
					listener.achievementChanged(null);
				}
			}
		} catch (CoreException e) {
			// log it
			e.printStackTrace();
		}		
	}

	@Override
	public void earlyStartup() {
		IAchievementReader reader = new XMLAchievementReader();
		Collection<Achievement> achievements = reader.read();
		AchievementIndex.getInstance().addAchievements(achievements);
	}
	
	public void addAchievementChangedListener(IAchievementChangedListener listener) {
		if(listener != null) {
			achievementChangedListener.add(listener);
		}
	}
	
	public void removeAchievementChangedListener(IAchievementChangedListener listener) {
		if(listener != null) {
			achievementChangedListener.remove(listener);
		}
	}
}
