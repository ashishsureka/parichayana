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
package in.software.analytics.parichayana.ui.internal.preferences;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import in.software.analytics.parichayana.core.Constants;
import in.software.analytics.parichayana.core.ParichayanaActivator;
import in.software.analytics.parichayana.ui.ParichayanaUIActivator;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.preferences.OptionsConfigurationBlock;
import org.eclipse.jdt.internal.ui.preferences.ScrolledPageContent;
import org.eclipse.jdt.internal.ui.wizards.IStatusChangeListener;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class ParichayanaBlock extends OptionsConfigurationBlock {

	private static final String SETTINGS_SECTION_NAME= "ParichayanaBlock";
	
	private static final Key TEST_PSTE = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_PSTE);
	private static final Key TEST_LGTE = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_LGTE);
	private static final Key TEST_CTGE = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_CTGE);
	private static final Key TEST_LGRN = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_LGRN);
	private static final Key TEST_PSRN = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_PSRN);
	private static final Key TEST_MLLM = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_MLLM);
	private static final Key TEST_RNHR = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_RNHR);
	private static final Key TEST_THGE = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_THGE);
	private static final Key TEST_WEPG = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_WEPG);
	private static final Key TEST_RRGC = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_RRGC);
	private static final Key TEST_INEE = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_INEE);
	private static final Key TEST_LGFT = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_LGFT);
	private static final Key TEST_CNPE = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_CNPE);
	private static final Key TEST_TNPE = getKey(ParichayanaActivator.PLUGIN_ID, Constants.TEST_TNPE);
	
	private static final Key INCLUDE_EXPRESSION = getKey(ParichayanaActivator.PLUGIN_ID, Constants.INCLUDE_EXPRESSION);
	private static final Key EXCLUDE_EXPRESSION = getKey(ParichayanaActivator.PLUGIN_ID, Constants.EXCLUDE_EXPRESSION);
	
	private static final Key ENABLE_PARICHAYANA = getKey(ParichayanaActivator.PLUGIN_ID, Constants.ENABLE_PARICHAYANA);
	
	private static final String ERROR= JavaCore.ERROR;
	private static final String WARNING= JavaCore.WARNING;
	private static final String IGNORE= JavaCore.IGNORE;

	private static final String ENABLED= JavaCore.ENABLED;
	private static final String DISABLED= JavaCore.DISABLED;

	private PixelConverter fPixelConverter;

	private FilteredPreferenceTree fFilteredPrefTree;

	private Button enableValidation;

	public ParichayanaBlock(IStatusChangeListener context, IProject project, IWorkbenchPreferenceContainer container) {
		super(context, project, getKeys(), container);
		
	}

	public static Key[] getKeys() {
		return new Key[] {
				 INCLUDE_EXPRESSION,
				 EXCLUDE_EXPRESSION,
				 TEST_PSTE,
				 TEST_LGTE,
				 TEST_CTGE,
				 TEST_LGRN,
				 TEST_PSRN,
				 TEST_MLLM,
				 TEST_RNHR,
				 TEST_THGE,
				 TEST_WEPG,
				 TEST_RRGC,
				 TEST_INEE,
				 TEST_LGFT,
				 TEST_CNPE,
				 TEST_TNPE,
				 ENABLE_PARICHAYANA
			};
	}

	/*
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		fPixelConverter= new PixelConverter(parent);
		setShell(parent.getShell());

		Composite mainComp= new Composite(parent, SWT.NONE);
		mainComp.setFont(parent.getFont());
		GridLayout layout= new GridLayout();
		layout.marginHeight= 0;
		layout.marginWidth= 0;
		mainComp.setLayout(layout);

		String[] enabledDisabled= new String[] { "true", "false" };
		
		enableValidation = addCheckBox(mainComp, "Enable Parichayana", ENABLE_PARICHAYANA, enabledDisabled, 0);
		enableValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setEnableStates();
			}
		
		});
		
		createIgnoreOptionalProblemsLink(mainComp);
		
		Composite spacer= new Composite(mainComp, SWT.NONE);
		spacer.setLayoutData(new GridData(0, 0));
		
		Composite commonComposite= createStyleTabContent(mainComp);
		GridData gridData= new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint= fPixelConverter.convertHeightInCharsToPixels(30);
		commonComposite.setLayoutData(gridData);

		validateSettings(null, null, null);

		setEnableStates();
		
		return mainComp;
	}

	private Composite createStyleTabContent(Composite folder) {
		String[] errorWarningIgnore= new String[] { ERROR, WARNING, IGNORE };
		String[] errorWarningIgnoreLabels= new String[] {
			Constants.ERROR,
			Constants.WARNING,
			Constants.IGNORE
		};

		String[] errorWarning= new String[] { ERROR, WARNING };
		String[] errorWarningLabels= new String[] {
				Constants.ERROR,
				Constants.WARNING,
		};
		
		fFilteredPrefTree= new FilteredPreferenceTree(this, folder, "&Select the severity level for the following optional problems:");
		final ScrolledPageContent sc1= fFilteredPrefTree.getScrolledPageContent();
		
		int nColumns= 3;

		Composite composite= sc1.getBody();
		GridLayout layout= new GridLayout(nColumns, false);
		layout.marginHeight= 0;
		layout.marginWidth= 0;
		composite.setLayout(layout);

		int indentStep=  fPixelConverter.convertWidthInCharsToPixels(1);

		int defaultIndent= indentStep * 0;
		int extraIndent= indentStep * 3;
		String label;
		ExpandableComposite excomposite;
		Composite inner;
		PreferenceTreeNode section;
		PreferenceTreeNode node;
		Key twistieKey;

		label= "Analyzing";
		twistieKey= OptionsConfigurationBlock.getLocalKey("ParichayanaBlock_potential_problems"); //$NON-NLS-1$
		section= fFilteredPrefTree.addExpandableComposite(composite, label, nColumns, twistieKey, null, false);
		excomposite= getExpandableComposite(twistieKey);
		
		inner= createInnerComposite(excomposite, nColumns, composite.getFont());
		
		label = "Include expression";
		fFilteredPrefTree.addTextField(inner, label, INCLUDE_EXPRESSION, defaultIndent, 150, section);
		
		label = "Exclude expression";
		fFilteredPrefTree.addTextField(inner, label, EXCLUDE_EXPRESSION, defaultIndent, 150, section);
		
		
		label= "PSTE:printing stack-trace and throwing Exception";
		fFilteredPrefTree.addComboBox(inner, label, TEST_PSTE, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);
		
		label= "LGTE:logging and throwing Exception";
		fFilteredPrefTree.addComboBox(inner, label, TEST_LGTE, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);
		
		label= "CTGE:catching generic Exception";
		fFilteredPrefTree.addComboBox(inner, label, TEST_CTGE, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);
		
		label= "LGRN:Log and return null is wrong";
		fFilteredPrefTree.addComboBox(inner, label, TEST_LGRN, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);
		
		label= "PSRN:Print stack-trace and return null is wrong";
		fFilteredPrefTree.addComboBox(inner, label, TEST_PSRN, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);
		
		label= "MLLM:Using multi-line log messages ";
		fFilteredPrefTree.addComboBox(inner, label, TEST_MLLM, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);
		
		label= "RNHR:Handling or re-throwing the exception";
		fFilteredPrefTree.addComboBox(inner, label, TEST_RNHR, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);
		
		label= "THGE:Throws generic Exception";
		fFilteredPrefTree.addComboBox(inner, label, TEST_THGE, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);
		
		label= "WEPG:Wrapping the exception and passing getMessage()";
		fFilteredPrefTree.addComboBox(inner, label, TEST_WEPG, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);

		label= "RRGC:relying on the result of getCause";
		fFilteredPrefTree.addComboBox(inner, label, TEST_RRGC, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);

		label= "INEE:Ignoring or suppressing InterruptedException";
		fFilteredPrefTree.addComboBox(inner, label, TEST_INEE, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);

		label= "LGFT:Log.fatal is the only line of code in the catch clause";
		fFilteredPrefTree.addComboBox(inner, label, TEST_LGFT, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);

		label= "CNPE:Catching NullPointerException";
		fFilteredPrefTree.addComboBox(inner, label, TEST_CNPE, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);

		label= "TNPE:Throwing NullPointerException";
		fFilteredPrefTree.addComboBox(inner, label, TEST_TNPE, errorWarningIgnore, errorWarningIgnoreLabels, defaultIndent, section);		
		
		IDialogSettings settingsSection= ParichayanaUIActivator.getDefault().getDialogSettings().getSection(SETTINGS_SECTION_NAME);
		restoreSectionExpansionStates(settingsSection);

		return sc1;
	}

	private Composite createInnerComposite(ExpandableComposite excomposite, int nColumns, Font font) {
		Composite inner= new Composite(excomposite, SWT.NONE);
		inner.setFont(font);
		inner.setLayout(new GridLayout(nColumns, false));
		excomposite.setClient(inner);
		return inner;
	}

	/* (non-javadoc)
	 * Update fields and validate.
	 * @param changedKey Key that changed, or null, if all changed.
	 */
	@Override
	protected void validateSettings(Key changedKey, String oldValue, String newValue) {
		fContext.statusChanged(new StatusInfo());
		validateExpression(changedKey, newValue, INCLUDE_EXPRESSION, "Invalid include expression");
		validateExpression(changedKey, newValue, EXCLUDE_EXPRESSION, "Invalid exclude expression");
	}

	/**
	 * @param changedKey
	 * @param newValue
	 * @param Expression 
	 */
	private void validateExpression(Key changedKey, String newValue, Key expression, String message) {
		if (changedKey == expression) {
			if (newValue != null && !newValue.isEmpty()) {
				try {
					Pattern.compile(newValue);
				} catch (PatternSyntaxException e) {
					final StatusInfo status= new StatusInfo();
					status.setError(message);
					fContext.statusChanged(status);
				}
			}
		}
	}

	private static boolean lessSevere(String errorWarningIgnore, String errorWarningIgnore2) {
		if (IGNORE.equals(errorWarningIgnore))
			return ! IGNORE.equals(errorWarningIgnore2);
		else if (WARNING.equals(errorWarningIgnore))
			return ERROR.equals(errorWarningIgnore2);
		else
			return false;
	}
	
	@Override
	protected String[] getFullBuildDialogStrings(boolean workspaceSettings) {
		String title= "Parichayana settings Changed";
		String message;
		if (workspaceSettings) {
			message= "The Parichayana settings have changed. A full rebuild is required for changes to take effect. Do the full build now?";
		} else {
			message= "The Parichayana settings have changed. A rebuild of the project is required for changes to take effect. Build the project now?";
		}
		return new String[] { title, message };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.preferences.OptionsConfigurationBlock#dispose()
	 */
	@Override
	public void dispose() {
		IDialogSettings section= ParichayanaUIActivator.getDefault().getDialogSettings().addNewSection(SETTINGS_SECTION_NAME);
		storeSectionExpansionStates(section);
		super.dispose();
	}

	private void setEnableStates() {
		boolean enabled = enableValidation.getSelection();
		setComboEnabled(TEST_PSTE, enabled);
		setComboEnabled(TEST_LGTE, enabled);
		setComboEnabled(TEST_CTGE, enabled);
		setComboEnabled(TEST_LGRN, enabled);
		setComboEnabled(TEST_PSRN, enabled);
		setComboEnabled(TEST_MLLM, enabled);
		setComboEnabled(TEST_RNHR, enabled);
		setComboEnabled(TEST_THGE, enabled);
		setComboEnabled(TEST_WEPG, enabled);
		setComboEnabled(TEST_RRGC, enabled);
		setComboEnabled(TEST_INEE, enabled);
		setComboEnabled(TEST_LGFT, enabled);
		setComboEnabled(TEST_CNPE, enabled);
		setComboEnabled(TEST_TNPE, enabled);
		setTextFieldEnabled(INCLUDE_EXPRESSION, enabled);
		setTextFieldEnabled(EXCLUDE_EXPRESSION, enabled);
	}

}
