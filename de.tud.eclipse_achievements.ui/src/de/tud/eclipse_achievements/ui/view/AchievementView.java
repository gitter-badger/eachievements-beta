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
package de.tud.eclipse_achievements.ui.view;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.tud.eclipse_achievements.core.AchievementCorePlugin;
import de.tud.eclipse_achievements.core.api.IAchievementChangedListener;
import de.tud.eclipse_achievements.index.AchievementIndex;
import de.tud.eclipse_achievements.index.entities.Achievement;

public class AchievementView extends ViewPart implements
		IAchievementChangedListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "de.tud.eclipse_achievements.views.AchievementView";

	private TableViewer viewer;

	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			List<Achievement> list = AchievementIndex.getInstance()
					.getCompletedAchievements();
			
			System.out.println("completed achievements: " + list.size());
			return list.toArray();
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			Achievement a = (Achievement) obj;
			return a.getName() + " (" + a.getCompletionCounter() + "/" + a.getNumberOfRequiredCompletion()
					+ ")";
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public AchievementView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),
		// "achievement-for-eclipse.viewer");
		
		AchievementCorePlugin.getDefault().addAchievementChangedListener(this);
	}

	public void dispose() {
		super.dispose();
		
		AchievementCorePlugin.getDefault().removeAchievementChangedListener(this);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void achievementChanged(Achievement achievement) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				System.out.println("call");
				viewer.refresh();
			}
		});
	}
}
