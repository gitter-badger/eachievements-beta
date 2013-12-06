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
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;

import de.tud.eclipse_achievements.index.AchievementIndex;
import de.tud.eclipse_achievements.index.entities.Achievement;

public class StaticAnalysisVisitor implements IResourceDeltaVisitor {

	@Override
	public boolean visit(IResourceDelta delta) {
		System.out.println("NEW EVENT");
		System.out.println("================================");
		System.out.println(delta.toString());
		
		IResource res = delta.getResource();
		switch(res.getType()) {
		case IResource.FILE:
			System.out.println("file resource");
			switch(delta.getKind()) {
			case IResourceDelta.ADDED:
				System.out.println("added");
				System.out.println("type: ");
				break;
			case IResourceDelta.REMOVED:
				System.out.println("removed");
				break;
			case IResourceDelta.CHANGED:
				System.out.println("changed");
				break;
			case IResourceDelta.CONTENT:
				System.out.println("content");
				break;
			}
		}		
		
		
		if ((delta.getFlags() | IResourceDelta.CONTENT) == IResourceDelta.CONTENT) {
			ResourceAdapter adapter = new ResourceAdapter(res);
			AchievementIndex store = AchievementIndex.getInstance();
			for (final Achievement achievement : store.getAllAchievements()) {

				if (!achievement.isCompleted()) {
					// TODO: implement Achievement#analyze(ResourceAdapter) method
					/*try {
						if (achievement.analyze(adapter)) {
							achievement.complete();
							UIJob uiJob = new UIJob("Update UI") {
								public IStatus runInUIThread(
										IProgressMonitor monitor) {
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									NotifierDialog.notify("Achievement",
											achievement.getName());
									return Status.OK_STATUS;
								}
							};
							uiJob.schedule();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}*/
				}
			}
		}
		return true;
	}
}
