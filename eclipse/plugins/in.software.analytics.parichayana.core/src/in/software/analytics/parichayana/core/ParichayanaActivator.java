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
package in.software.analytics.parichayana.core;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ParichayanaActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "in.software.analytics.parichayana.core"; //$NON-NLS-1$

	// The shared instance
	private static ParichayanaActivator plugin;
	
	/**
	 * The constructor
	 */
	public ParichayanaActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ParichayanaActivator getDefault() {
		return plugin;
	}
	
	public static void log(Throwable e) {
		plugin.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e ));
	}
	
	public static void logInfo(String message) {
		if (plugin.isDebugging()) {
			plugin.getLog().log(new Status(IStatus.INFO, PLUGIN_ID, message));
		}
	}

	public static File getDestinationFile(String projectName) {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		IPath location = Platform.getStateLocation(bundle);
		File root = location.toFile();
		File projectFile = new File(root, projectName);
		projectFile.mkdirs();
		File file = new File(projectFile, "parichyana.txt");
		return file;
	}
	
	public static String getPreference(String name) {
		return getPreference(name, null, null);
	}
	
	public static String getPreference(String name, String defaultValue) {
		return getPreference(name, defaultValue, null);
	}
	
	public static String getPreference(String name, IProject project) {
		return getPreference(name, null, project);
	}
	
	public static String getPreference(String name, String defaultValue, IProject project) {
		IEclipsePreferences[] preferencesLookup;
		if (project != null) {
			preferencesLookup = new IEclipsePreferences[] {
					new ProjectScope(project).getNode(PLUGIN_ID),
					InstanceScope.INSTANCE.getNode(PLUGIN_ID),
					DefaultScope.INSTANCE.getNode(PLUGIN_ID)
			};
		} else {
			preferencesLookup = new IEclipsePreferences[] {
					InstanceScope.INSTANCE.getNode(PLUGIN_ID),
					DefaultScope.INSTANCE.getNode(PLUGIN_ID)
			};
		}
		IPreferencesService service = Platform.getPreferencesService();
		String value = service.get(name, defaultValue, preferencesLookup);
		return value;
	}

	public static boolean isParichayanaEnabled(IProject project) {
		String preference = getPreference(Constants.ENABLE_PARICHAYANA, project);
		return Boolean.TRUE.toString().equals(preference);
	}
}
