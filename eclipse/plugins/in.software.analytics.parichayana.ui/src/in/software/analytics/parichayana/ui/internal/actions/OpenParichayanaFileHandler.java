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

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class OpenParichayanaFileHandler extends ParichayanaHandler {
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		openParichayanaFile(getProject(event));
		return null;
	}

	/**
	 * @param project
	 */
	private void openParichayanaFile(IProject project) {
		if (project == null) {
			return;
		}
		File file = ParichayanaActivator.getDestinationFile(project.getName());
		if (file.isFile()) {
		    IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.toURI());
		    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		 
		    try {
		        IDE.openEditorOnFileStore( page, fileStore );
		    } catch ( PartInitException e ) {
		        ParichayanaActivator.log(e);
		    }
		} else {
		    ParichayanaActivator.logInfo("Parichayana output file doesn't exist");
		}
	}
	
}
