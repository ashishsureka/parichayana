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
package in.software.analytics.parichayana.core.internal.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class ParichayanaNature implements IProjectNature {

	public static final String NATURE_ID = "in.software.analytics.parichayana.core.parichayanaNature"; //$NON-NLS-1$
	
	IProject project;
	
	@Override
	public void configure() throws CoreException {
		
	}

	@Override
	public void deconfigure() throws CoreException {
		
	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
	}

}
