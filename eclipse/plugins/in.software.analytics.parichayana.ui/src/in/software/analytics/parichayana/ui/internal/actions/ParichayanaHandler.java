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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class ParichayanaHandler extends AbstractHandler {

	protected IProject getProject(ExecutionEvent event) {
		ISelection sel= HandlerUtil.getCurrentSelection(event);
		IProject project = null;
		if (sel instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) sel;
			Object object = structuredSelection.getFirstElement();
			IResource resource = null;
			if (object instanceof IResource) {
				resource = (IResource) object;
			} else  if (object instanceof IAdaptable) {
				Object res= ((IAdaptable) object).getAdapter(IResource.class);
				if (res != null) {
					resource = (IResource) res;
				}
			}
			if (resource != null) {
				project = resource.getProject();
			}
			
		}
		return project;
	}

}
