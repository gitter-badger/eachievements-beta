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
package de.tud.eclipse_achievements.ui.popup;

import org.eclipse.mylyn.commons.ui.dialogs.AbstractNotificationPopup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import de.tud.eclipse_achievements.ui.EclipseAchievementsUiPlugin;

public class AchievementNotificationPopup extends AbstractNotificationPopup {
	
	private String m_msg;
	@SuppressWarnings("unused")
	private Display m_display;
	
	public AchievementNotificationPopup(Display display, String message) {
		super(display);
		this.m_display = display;
	}
 
	@Override
	protected void createContentArea(Composite composite) {
		composite.setLayout(new GridLayout(1, true));
		Label testLabel = new Label(composite, SWT.WRAP);
		testLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
 
		testLabel.setText(m_msg);
		testLabel.setBackground(composite.getBackground());
	}
 
	@Override
	protected String getPopupShellTitle() {
		// Return a custom title
		return "Achievement!";
	}
 
	@Override
	protected Image getPopupShellImage(int maximumHeight) {
		// Use createResource to use a shared Image instance of the ImageDescriptor
		return (Image) EclipseAchievementsUiPlugin.getImageDescriptor("/icons/thumb_up.png")
				.createResource(Display.getDefault());
	}	
}
