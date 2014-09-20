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
package in.software.analytics.parichayana.core.internal.builder;

import in.software.analytics.parichayana.core.Constants;
import in.software.analytics.parichayana.core.ParichayanaActivator;
import in.software.analytics.parichayana.core.internal.nature.ParichayanaNature;
import in.software.analytics.parichayana.engine.CatchAntiPatterns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.util.IClassFileReader;
import org.eclipse.jdt.internal.core.ClasspathEntry;
import org.eclipse.jdt.internal.core.ExternalFoldersManager;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.internal.core.JavaProject;

public class ParichayanaBuilder extends IncrementalProjectBuilder {

	public static final String ID = "in.software.analytics.parichayana.core.parichayanaBuilder";
	
	private IProject project;
	static final IProject[] ZERO_PROJECT = new IProject[0];
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] build(int kind, Map<String, String> args,
			IProgressMonitor monitor) throws CoreException {
		project = getProject();
		if (project == null || !project.isAccessible()) {
			return ZERO_PROJECT;
		}
		if (!project.hasNature(ParichayanaNature.NATURE_ID) || !ParichayanaActivator.isParichayanaEnabled(project)) {
			cleanupMarkers(project);
			return ZERO_PROJECT;
		}
		try {
			if (kind == FULL_BUILD) {
				buildAll(monitor);
			} else {
				IResourceDelta delta = getDelta(project);
				if (delta == null) {
					buildAll(monitor);
				} else {
					buildDelta(delta, monitor);
				}
			}
		} catch (CoreException e) {
			ParichayanaActivator.log(e);
		} finally {
			if (monitor != null) {
				monitor.done();
			}
		}
		IProject[] requiredProjects = getRequiredProjects(true);
		return requiredProjects;
	}

	/**
	 * @param delta
	 * @param monitor
	 * @throws CoreException 
	 */
	private void buildDelta(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		final List<ICompilationUnit> units = new ArrayList<ICompilationUnit>();
		delta.accept(new IResourceDeltaVisitor() {
			
			@Override
			public boolean visit(IResourceDelta delta) throws CoreException {
				IResource res = delta.getResource();
				switch (res.getType()) {
					case IResource.ROOT :
						return true;
					case IResource.PROJECT :
						return true;
					case IResource.FOLDER :
						return true;
					case IResource.FILE :
						IJavaElement element = JavaCore.create(res);
						ICompilationUnit cu = null;
						if (element instanceof IClassFile) {
							cu = getCompilationUnit((IClassFile)element);
						}
						if (element instanceof ICompilationUnit) {
							cu = (ICompilationUnit) element;
						}
						if (cu != null) {
							cleanupMarkers(cu.getUnderlyingResource());
							if (units.contains(cu)) {
								return false;
							}
							units.add(cu);
						}
						return false;
					default :
						return false; 
				}
			}
			
			public ICompilationUnit getCompilationUnit(IClassFile classFile) {
				IClassFileReader classFileReader = ToolFactory.createDefaultClassFileReader(classFile,
								IClassFileReader.CLASSFILE_ATTRIBUTES);
				if (classFileReader != null) {
					char[] className = classFileReader.getClassName();
					if (className != null) {
						String fqn = new String(classFileReader.getClassName())
								.replace("/", "."); //$NON-NLS-1$ //$NON-NLS-2$
						IJavaProject javaProject = classFile.getJavaProject();
						IType sourceType = null;
						try {
							sourceType = javaProject.findType(fqn);
							if (sourceType != null) {
								return sourceType.getCompilationUnit();
							}
						} catch (JavaModelException e) {
							// ignore
						}
					}
				}
				return null;
			}
		});
		build(units, monitor);	
	}

	/**
	 * @param units
	 * @param monitor 
	 */
	private void build(List<ICompilationUnit> units, IProgressMonitor monitor) {
		monitor.beginTask("Parichayana builder", units.size());
		new CatchAntiPatterns(units, project, monitor);
	}

	/**
	 * @param monitor
	 */
	private void buildAll(IProgressMonitor monitor) throws CoreException {
		checkCancel(monitor);
		List<ICompilationUnit> units = getCompilationUnits();
		build(units, monitor);
	}

	public void checkCancel(IProgressMonitor monitor) {
		if (monitor != null && monitor.isCanceled())
			throw new OperationCanceledException();
	}
	
	private void cleanupMarkers(IResource resource) {
		if (resource == null) {
			return;
		}
		try {
			resource.deleteMarkers(Constants.BASE_MARKER_ID, true,
					IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			ParichayanaActivator.log(e);
		}
	}
	
	private IProject[] getRequiredProjects(boolean includeBinaryPrerequisites) {
		JavaProject javaProject = (JavaProject) JavaCore.create(this.project);
		IWorkspaceRoot workspaceRoot = this.project.getWorkspace().getRoot();
		if (javaProject == null || workspaceRoot == null) {
			return ZERO_PROJECT;
		}

		List<IProject> projects = new ArrayList<IProject>();
		ExternalFoldersManager externalFoldersManager = JavaModelManager.getExternalManager();
		try {
			IClasspathEntry[] entries = javaProject.getExpandedClasspath();
			for (int i = 0, l = entries.length; i < l; i++) {
				IClasspathEntry entry = entries[i];
				IPath path = entry.getPath();
				IProject p = null;
				switch (entry.getEntryKind()) {
					case IClasspathEntry.CPE_PROJECT :
						p = workspaceRoot.getProject(path.lastSegment()); // missing projects are considered too
						if (((ClasspathEntry) entry).isOptional() && !JavaProject.hasJavaNature(p)) // except if entry is optional
							p = null;
						break;
					case IClasspathEntry.CPE_LIBRARY :
						if (includeBinaryPrerequisites && path.segmentCount() > 0) {
							// some binary resources on the class path can come from projects that are not included in the project references
							IResource resource = workspaceRoot.findMember(path.segment(0));
							if (resource instanceof IProject) {
								p = (IProject) resource;
							} else {
								resource = externalFoldersManager.getFolder(path);
								if (resource != null)
									p = resource.getProject();
							}
						}
				}
				if (p != null && !projects.contains(p))
					projects.add(p);
			}
		} catch(JavaModelException e) {
			return ZERO_PROJECT;
		}
		IProject[] result = new IProject[projects.size()];
		projects.toArray(result);
		return result;
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		project = getProject();
		if (project == null || !project.isAccessible()) {
			return;
		}
		cleanupMarkers(project);
	}
	
	private List<ICompilationUnit> getCompilationUnits()
			throws JavaModelException {
		List<ICompilationUnit> compilationUnits = new ArrayList<ICompilationUnit>();
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry[] entries = javaProject.getRawClasspath();
		for (IClasspathEntry entry : entries) {
			if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				IPackageFragmentRoot[] roots = javaProject
						.findPackageFragmentRoots(entry);
				for (IPackageFragmentRoot root : roots) {
					if (root.isArchive()) {
						continue;
					}
					IJavaElement[] children = root.getChildren();
					for (IJavaElement child : children) {
						if (child instanceof IPackageFragment) {
							IPackageFragment packageFragment = (IPackageFragment) child;
							ICompilationUnit[] cus = packageFragment
									.getCompilationUnits();
							for (ICompilationUnit cu : cus) {
								cleanupMarkers(cu.getUnderlyingResource());
								compilationUnits.add(cu);
							}
						}
					}
				}
			}
		}

		return compilationUnits;
	}

}