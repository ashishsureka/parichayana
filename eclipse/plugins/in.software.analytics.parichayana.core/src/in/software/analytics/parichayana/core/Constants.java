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

import org.eclipse.core.resources.IMarker;

public interface Constants {

	public static final String ANTI_PATTERN_TNPE_MESSAGE = "ANTI-PATTERN TNPE : NullPointerException should not be thrown by the program as it is expected that it is thrown by the virtual machine";
	public static final String ANTI_PATTERN_CNPE_MESSAGE = "ANTI-PATTERN CNPE : NullPointerException is a logical or programming error in the code (result of a bug) and should be eliminated rather than catching";
	public static final String ANTI_PATTERN_LGFT_MESSAGE = "ANTI-PATTERN LGFT : Fatal condition, log.fatal is the only line of code in the catch clause � method should abort and notify the caller with an exception";
	public static final String ANTI_PATTERN_INEE_MESSAGE = "ANTI-PATTERN INEE : Ignoring or suppressing InterruptedException with an empty catch-clause is an anti-pattern, empty catch block prevents in determining that an interrupted exception occurred or knowing that the thread was interrupted";
	public static final String ANTI_PATTERN_RRGC_MESSAGE = "ANTI-PATTERN RRGC : relying on the result of getCause makes the code fragile, use org.apache.commons.lang.exception.ExceptionUtils.getRootCause(Throwable throwable)";
	public static final String ANTI_PATTERN_WEPG_MESSAGE = "ANTI-PATTERN WEPG : Wrapping the exception and passing getMessage() destroys the stack trace of original exception";
	public static final String ANTI_PATTERN_THGE_MESSAGE = "ANTI-PATTERN THGE : Throws generic Exception, defeats the purpose of using a checked exception, declare the specific checked exceptions that your method can throw";
	public static final String ANTI_PATTERN_RNHR_MESSAGE = "ANTI-PATTERN RNHR : just returns null instead of handling or re-throwing the exception, swallows the exception, losing the information forever";
	public static final String ANTI_PATTERN_MLLM_MESSAGE = "ANTI-PATTERN MLLM : Using multi-line log messages causes problems when multiple threads are running in parallel, two log messages may end up spaced-out multiple lines apart in the log file,  group together all log messages, regardless of the level";
	public static final String ANTI_PATTERN_PSRN_MESSAGE = "ANTI-PATTERN PSRN : Print stack-trace and return null is wrong, instead of returning null, throw the exception, and let the caller deal with it";
	public static final String ANTI_PATTERN_LGRN_MESSAGE = "ANTI-PATTERN LGRN : Log and return null is wrong, instead of returning null, throw the exception, and let the caller deal with it";
	public static final String ANTI_PATTERN_CTGE_MESSAGE = "ANTI-PATTERN CTGE : catching generic Exception, catch the specific exception that can be thrown";
	public static final String ANTI_PATTERN_LGTE_MESSAGE = "ANTI-PATTERN LGTE : logging and throwing Exception, choose one otherwise it results in multiple log messages (multiple-entries, duplication)";
	public static final String ANTI_PATTERN_PSTE_MESSAGE = "ANTI-PATTERN PSTE : printing stack-trace and throwing Exception, choose one otherwise it results in multiple log messages (multiple-entries, duplication)";
	

	public final static String[] ATTRIBUTE_NAMES = {
    	IMarker.MESSAGE,
    	IMarker.SEVERITY,
    	IMarker.CHAR_START,
    	IMarker.CHAR_END
    };
	
	public static final String ERROR = "Error";
	public static final String WARNING = "Warning";
	public static final String IGNORE = "Ignore";

	public static final String BASE_MARKER_ID = "in.software.analytics.parichayana.core.problem";
	public static final String PSTE_MARKER_ID = "in.software.analytics.parichayana.core.problem.pste";
	public static final String LGTE_MARKER_ID = "in.software.analytics.parichayana.core.problem.lgte";
	public static final String CTGE_MARKER_ID = "in.software.analytics.parichayana.core.problem.ctge";
	public static final String LGRN_MARKER_ID = "in.software.analytics.parichayana.core.problem.lgrn";
	public static final String PSRN_MARKER_ID = "in.software.analytics.parichayana.core.problem.psrn";
	public static final String MLLM_MARKER_ID = "in.software.analytics.parichayana.core.problem.mllm";
	public static final String RNHR_MARKER_ID = "in.software.analytics.parichayana.core.problem.rnhr";
	public static final String THGE_MARKER_ID = "in.software.analytics.parichayana.core.problem.thge";
	public static final String WEPG_MARKER_ID = "in.software.analytics.parichayana.core.problem.wepg";
	public static final String RRGC_MARKER_ID = "in.software.analytics.parichayana.core.problem.rrgc";
	public static final String INEE_MARKER_ID = "in.software.analytics.parichayana.core.problem.inee";
	public static final String LGFT_MARKER_ID = "in.software.analytics.parichayana.core.problem.lgft";
	public static final String CNPE_MARKER_ID = "in.software.analytics.parichayana.core.problem.cnpe";
	public static final String TNPE_MARKER_ID = "in.software.analytics.parichayana.core.problem.tnpe";
	
	public static final String TEST_PSTE = ParichayanaActivator.PLUGIN_ID + ".testPSTE";
	public static final String TEST_LGTE = ParichayanaActivator.PLUGIN_ID + ".testLGTE";
	public static final String TEST_CTGE = ParichayanaActivator.PLUGIN_ID + ".testCTGE";
	public static final String TEST_LGRN = ParichayanaActivator.PLUGIN_ID + ".testLGRN";
	public static final String TEST_PSRN = ParichayanaActivator.PLUGIN_ID + ".testPSRN";
	public static final String TEST_MLLM = ParichayanaActivator.PLUGIN_ID + ".testMLLM";
	public static final String TEST_RNHR = ParichayanaActivator.PLUGIN_ID + ".testRNHR";
	public static final String TEST_THGE = ParichayanaActivator.PLUGIN_ID + ".testTHGE";
	public static final String TEST_WEPG = ParichayanaActivator.PLUGIN_ID + ".testWEPG";
	public static final String TEST_RRGC = ParichayanaActivator.PLUGIN_ID + ".testRRGC";
	public static final String TEST_INEE = ParichayanaActivator.PLUGIN_ID + ".testINEE";
	public static final String TEST_LGFT = ParichayanaActivator.PLUGIN_ID + ".testLGFT";
	public static final String TEST_CNPE = ParichayanaActivator.PLUGIN_ID + ".testCNPE";
	public static final String TEST_TNPE = ParichayanaActivator.PLUGIN_ID + ".testTNPE";
	
	public static final String INCLUDE_EXPRESSION = ParichayanaActivator.PLUGIN_ID + ".includeExpression";
	public static final String EXCLUDE_EXPRESSION = ParichayanaActivator.PLUGIN_ID + ".excludeExpression";
	
	public static final String ENABLE_PARICHAYANA = "enableParichayana";
	
}
