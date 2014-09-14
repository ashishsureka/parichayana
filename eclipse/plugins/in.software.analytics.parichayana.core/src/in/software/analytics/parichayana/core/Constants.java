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

	public final static String[] ATTRIBUTE_NAMES = {
    	IMarker.MESSAGE,
    	IMarker.SEVERITY,
    	IMarker.CHAR_START,
    	IMarker.CHAR_END
    };
}
