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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

public class AddParichayanaHandler extends ParichayanaHandler {
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			addParichayanaNature(getProject(event));
		} catch (CoreException e) {
			ParichayanaActivator.log(e);
		}
		return null;
	}
	
	private static void addParichayanaNature(IProject project) throws CoreException {
		if (project == null) {
			return;
		}
		IProjectDescription description = project.getDescription();
		String[] prevNatures = description.getNatureIds();
		String[] newNatures = new String[prevNatures.length + 1];
		System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
		newNatures[prevNatures.length] = ParichayanaNature.NATURE_ID;
		description.setNatureIds(newNatures);
		project.setDescription(description, new NullProgressMonitor());
		addParichayanaBuilder(project);
	}
	
	private static void addParichayanaBuilder(IProject project) {
		if (project == null) {
			return;
		}
		try {
			IProjectDescription description = project.getDescription();
			ICommand[] commands = description.getBuildSpec();
			if (commands != null) {
				for (int i = 0; i < commands.length; i++) {
					ICommand command = commands[i];
					String builderName = command.getBuilderName();
					if (builderName == null) {
						continue;
					}

					if (builderName.equals(ParichayanaBuilder.ID)) {
						return;
					}
				}
			}
			ICommand newCommand = description.newCommand();
			newCommand.setBuilderName(ParichayanaBuilder.ID);
			ICommand[] newCommands = new ICommand[commands.length + 1];
			System.arraycopy(commands, 0, newCommands, 0, commands.length);
			newCommands[commands.length] = newCommand;
			description.setBuildSpec(newCommands);

			project.setDescription(description, null);

		} catch (CoreException e) {
			// ignore
		}
	}

}
