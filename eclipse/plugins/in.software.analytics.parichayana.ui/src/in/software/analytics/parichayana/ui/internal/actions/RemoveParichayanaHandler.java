/*******************************************************************************
 * Copyright (c) 2014 Software Analytics and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License, version 2 
 * (GPL-2.0) which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-2.0.txt
 *
 * Contributors:
 *     Haris Peco - initial API and implementation
 *******************************************************************************/
package in.software.analytics.parichayana.ui.internal.actions;

import in.software.analytics.parichayana.core.ParichayanaActivator;
import in.software.analytics.parichayana.core.internal.builder.ParichayanaBuilder;
import in.software.analytics.parichayana.core.internal.nature.ParichayanaNature;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

public class RemoveParichayanaHandler extends ParichayanaHandler {
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			removeParichayanaNature(getProject(event));
		} catch (CoreException e) {
			ParichayanaActivator.log(e);
		}
		return null;
	}
	
	public static void removeParichayanaNature(IProject project) throws CoreException {
		if (project == null || !project.hasNature(ParichayanaNature.NATURE_ID)) {
			return;
		}
		IProjectDescription description = project.getDescription();
		String[] prevNatures = description.getNatureIds();
		String[] newNatures = new String[prevNatures.length - 1];
		int i = 0;
		for (String prevNature : prevNatures) {
			if (!ParichayanaNature.NATURE_ID.equals(prevNature)) {
				newNatures[i] = prevNature;
				i++;
			}
		}
		description.setNatureIds(newNatures);
		project.setDescription(description, new NullProgressMonitor());
		removeBuilder(project);
	}
	
	private static void removeBuilder(IProject project) {
		try {
			IProjectDescription description = project.getDescription();
			ICommand[] commands = description.getBuildSpec();
			if (commands != null) {
				List<ICommand> newCommands = new ArrayList<ICommand>();
				boolean exists = false;
				for (int i = 0; i < commands.length; i++) {
					ICommand command = commands[i];
					String builderName = command.getBuilderName();
					if (builderName.equals(ParichayanaBuilder.ID)) {
						exists = true;
						continue;
					}
					newCommands.add(command);
				}
				if (exists) {
					description.setBuildSpec(newCommands.toArray(new ICommand[0]));
					project.setDescription(description, null);
				}
			}
		} catch (CoreException e) {
			// ignore
		}
		
	}

}
