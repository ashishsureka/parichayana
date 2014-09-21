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
package in.software.analytics.parichayana.core.internal.preferences;

import in.software.analytics.parichayana.core.Constants;
import in.software.analytics.parichayana.core.ParichayanaActivator;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jdt.core.JavaCore;

public class ParichayanaPreferencesInitializer extends
		AbstractPreferenceInitializer {

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences preferences = DefaultScope.INSTANCE.getNode(ParichayanaActivator.PLUGIN_ID);
		preferences.putBoolean(Constants.ENABLE_PARICHAYANA, true);
		preferences.put(Constants.TEST_PSTE, JavaCore.WARNING);
		preferences.put(Constants.TEST_LGTE, JavaCore.WARNING);
		preferences.put(Constants.TEST_LGRN, JavaCore.WARNING);
		preferences.put(Constants.TEST_PSRN, JavaCore.WARNING);
		preferences.put(Constants.TEST_MLLM, JavaCore.WARNING);
		preferences.put(Constants.TEST_RNHR, JavaCore.WARNING);
		preferences.put(Constants.TEST_THGE, JavaCore.WARNING);
		preferences.put(Constants.TEST_WEPG, JavaCore.WARNING);
		preferences.put(Constants.TEST_RRGC, JavaCore.WARNING);
		preferences.put(Constants.TEST_INEE, JavaCore.WARNING);
		preferences.put(Constants.TEST_LGFT, JavaCore.WARNING);
		preferences.put(Constants.TEST_CNPE, JavaCore.WARNING);
		preferences.put(Constants.TEST_TNPE, JavaCore.WARNING);
		preferences.put(Constants.TEST_CTGE, JavaCore.WARNING);
		preferences.put(Constants.INCLUDE_EXPRESSION, "");
		preferences.put(Constants.EXCLUDE_EXPRESSION, "");
	}

}
