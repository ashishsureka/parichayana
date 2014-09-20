package in.software.analytics.parichayana.engine;
/**
* Mining Source Code for Automatically Discovering Exception Management Anti-Patterns and Code Smell 
*
* @author  Ashish Sureka (ashish@iiitd.ac.in)
* @version 1.0
* @since   2014-06-30 
*/

import in.software.analytics.parichayana.core.Constants;
import in.software.analytics.parichayana.core.ParichayanaActivator;

import java.io.PrintWriter;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;

public class CatchClauseVisitor extends ASTVisitor {

	public int catchall = 0;
	public int catchnpe = 0;
	public int returnnull = 0;
	public int numcatchclause = 0;
	public int nummethoddecl = 0;
	public int numthrowprintst = 0;
	public int numthrowlog = 0;
	public int numlogreturnnul = 0;
	public int numprintstackreturnnull = 0;
	public int nummultilinelog = 0;
	public int numcatchignore = 0;
	public int numthrowsexception = 0;
	public int numdestwrap = 0;
	public int numreplyingcause = 0;
	public int numInterruptedException=0;
	public int numLogFatal = 0;
	public int numtnpe = 0;
	
	public PrintWriter writer = null;
	public int linenumber = 1;
	private ICompilationUnit unit;
	private IProject project;
	
	/**
	 * @param project
	 */
	public CatchClauseVisitor(IProject project) {
		this.project = project;
	}

	public void setLinenumber(int linenumber){
		this.linenumber = linenumber;
	}
	
	public int getNumLogFatal(){
		return numLogFatal;
	}
	
	public int getLinenumber(){
		return linenumber;
	}
	
	public void setWriter(PrintWriter pw){
		writer = pw;
	}
	
	public int getNumInterrptedException(){
		return numInterruptedException;
	}
	
	public int getNumrelyingcause(){
		return numreplyingcause;
	}
	
	public int getNumdestwrap(){
		return numdestwrap;
	}
	
	public int getNummethoddecl(){
		return nummethoddecl;
	}
	 
	public int getNumthrowsexception(){
		return numthrowsexception; 
	}
	
	public int getNumcatchignore(){
		return numcatchignore;
	}
	
	public int getNummultilinelog(){
		return nummultilinelog;
	}
	
	public int getNumlogreturnnull(){
		return numlogreturnnul;
	}
	
	public int getNumprintstackreturnnull(){
		return numprintstackreturnnull;
	}
	 
	public int getNumcatchclause(){
		return numcatchclause;
	}
	
	public int getNumthrowprintst(){
		return numthrowprintst;
	}
	
	public int getNumthrowlog(){
		return numthrowlog;
	}
	
	public int getNumtnpe(){
		return numtnpe;
	}
	
	public int getNumcatchall(){
		return catchall;
	}
	
	public int getNumcatchnpe(){
		return catchnpe; 
	}
	
	public boolean visit(CatchClause node){
	    //ParichayanaActivator.logInfo("CATCH CLAUSE : " + node.toString());
	    //ParichayanaActivator.logInfo("BODY : " + node.getBody());
	    //ParichayanaActivator.logInfo("NODE TYPE : " + node.getNodeType());
	    
	    ///////////////////LOG AND THROW, PRINTSTACKTRACE AND THROW///////////////////////////////
		String body = node.getBody().toString();
		
		//ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
		numcatchclause++;
		
		boolean throwflag = false;
		boolean printstacktraceflag = false;
		boolean logflag = false;
		boolean returnnullflag = false;
		 
		if(body.contains(new StringBuffer("throw "))){
			//ParichayanaActivator.logInfo("****** CONTAINS THROW ******");
			throwflag = true;
		}
		if(body.contains(new StringBuffer("printStackTrace"))){
			//ParichayanaActivator.logInfo("****** CONTAINS PRINT STACK TRACE ******");
			printstacktraceflag = true;
		}
		String type = ParichayanaActivator.getPreference(Constants.TEST_PSTE, project);
		if(throwflag && printstacktraceflag && !JavaCore.IGNORE.equals(type)){
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
			ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
			ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_PSTE_MESSAGE);
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
			//writer.println("_________________________________________________________________________________");
	    	try {
	    		int severity = getSeverity(type);
				createMarker(unit, Constants.ANTI_PATTERN_PSTE_MESSAGE, severity, Constants.PSTE_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException e) {
				ParichayanaActivator.log(e);
			}
			writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
			linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + " " + Constants.ANTI_PATTERN_PSTE_MESSAGE);
			linenumber++;
	    	writer.println("_________________________________________________________________________________");
			numthrowprintst++;
		}
		if( (body.contains(new StringBuffer("log.error"))) ||
		    (body.contains(new StringBuffer("log.info"))) ||
		    (body.contains(new StringBuffer("log.warn"))) ||
		    (body.contains(new StringBuffer("log.debug"))) ||
		    (body.contains(new StringBuffer("log.trace"))) ||
		    (body.contains(new StringBuffer("log.fatal"))) ||
		    (body.contains(new StringBuffer("logger.trace"))) ||
		    (body.contains(new StringBuffer("logger.fatal"))) ||
		    (body.contains(new StringBuffer("logger.error"))) ||
		    (body.contains(new StringBuffer("logger.info"))) ||
		    (body.contains(new StringBuffer("logger.warn"))) ||
		    (body.contains(new StringBuffer("logger.debug"))) ) {
			//ParichayanaActivator.logInfo("****** CONTAINS LOG ******");
			logflag = true;
		
		}
		type = ParichayanaActivator.getPreference(Constants.TEST_LGTE, project);
		if(throwflag && logflag && !JavaCore.IGNORE.equals(type)){
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
			ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
			ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_LGTE_MESSAGE);
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
			try {
				int severity = getSeverity(type);
				createMarker(unit, Constants.ANTI_PATTERN_LGTE_MESSAGE, severity, Constants.LGTE_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException e) {
				ParichayanaActivator.log(e);
			}
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
			linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + " " + Constants.ANTI_PATTERN_LGTE_MESSAGE);
			linenumber++;
	    	writer.println("_________________________________________________________________________________");
			numthrowlog++;
			
		}
	    /////////////////LOG FATAL/////////////////////////////////////
		List<Statement> lis = node.getBody().statements();
		int lssize = lis.size();		
		type = ParichayanaActivator.getPreference(Constants.TEST_LGFT, project);
		if(lssize==1 && !JavaCore.IGNORE.equals(type)){
			if( (body.contains(new StringBuffer("log.fatal"))) ||
				(body.contains(new StringBuffer("logger.fatal"))) ) {
					ParichayanaActivator.logInfo("LOG FATAL");
					numLogFatal++;
					ParichayanaActivator.logInfo("_________________________________________________________________________________");
			    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
					ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
			    	ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_LGFT_MESSAGE);
			    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
			    	try {
						createMarker(unit, Constants.ANTI_PATTERN_LGFT_MESSAGE, IMarker.SEVERITY_WARNING, Constants.LGFT_MARKER_ID, node.getStartPosition(), node.getLength());
					} catch (CoreException ex) {
						ParichayanaActivator.log(ex);
					}
			    	//writer.println("_________________________________________________________________________________");
			    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
					linenumber++;
			    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			    	linenumber++;
			    	writer.println(linenumber + " " + Constants.ANTI_PATTERN_LGFT_MESSAGE);
			    	linenumber++;
			    	writer.println("_________________________________________________________________________________");
			}
		}		
		/////////////////////////////////////////////////////////////////
		
		
		//////////////////////DESTRUCTIVE WRAPPING/////////////////////
		lis = node.getBody().statements();
		lssize = lis.size();
		type = ParichayanaActivator.getPreference(Constants.TEST_WEPG, project);
		if (!JavaCore.IGNORE.equals(type)) {
			for(int i=0;i<lssize;i++){
				Statement s = (Statement)lis.get(i);
				String content = s.toString();
				if( content.contains("throw") && content.contains("new") && content.contains("getMessage") ){
					SingleVariableDeclaration svd = node.getException();
					//ParichayanaActivator.logInfo("SVD : " + svd.getName());
					if(!(content.contains(","+svd.getName()+")"))){
						//ParichayanaActivator.logInfo("NOT-PATTERN" + ","+svd.getName()+")");
						//ParichayanaActivator.logInfo("****** CONTAINS THROW GETMESSAGE ******");
						ParichayanaActivator.logInfo("_________________________________________________________________________________");
						ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
						ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
						ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_WEPG_MESSAGE);
						ParichayanaActivator.logInfo("_________________________________________________________________________________");
						try {
							int severity = getSeverity(type);
							createMarker(unit, Constants.ANTI_PATTERN_WEPG_MESSAGE, severity, Constants.WEPG_MARKER_ID, node.getStartPosition(), node.getLength());
						} catch (CoreException ex) {
							ParichayanaActivator.log(ex);
						}
			    	//writer.println("_________________________________________________________________________________");
			    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
					linenumber++;
			    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			    	linenumber++;
			    	writer.println(linenumber + " " + Constants.ANTI_PATTERN_WEPG_MESSAGE);
			    	linenumber++;
			    	writer.println("_________________________________________________________________________________");
			    	numdestwrap++;
				}
			}
		}
		}
		///////////////////////////////////////////////////////////////
		
		//////////////////RELYING ON GETCLAUSE////////////////////////////
		List<Statement> lists = node.getBody().statements();
		int lssizee = lists.size();
		type = ParichayanaActivator.getPreference(Constants.TEST_RRGC, project);
		if (!JavaCore.IGNORE.equals(type)) {
			for(int i=0;i<lssizee;i++){
				if( (lists.get(i).toString().trim().contains("if")) &&
						(lists.get(i).toString().trim().contains("getCause")) &&
						(lists.get(i).toString().trim().contains("instanceof")) )
					 	{
					//ParichayanaActivator.logInfo("****** CONTAINS IF GETCAUSE INSTANCE ******");
					ParichayanaActivator.logInfo("_________________________________________________________________________________");
					ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
					ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
					ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_RRGC_MESSAGE);
					ParichayanaActivator.logInfo("_________________________________________________________________________________");
					try {
						int severity = getSeverity(type);
						createMarker(unit, Constants.ANTI_PATTERN_RRGC_MESSAGE, severity, Constants.RRGC_MARKER_ID, node.getStartPosition(), node.getLength());
					} catch (CoreException ex) {
						ParichayanaActivator.log(ex);
					}
					//writer.println("_________________________________________________________________________________");
					writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
					linenumber++;
					writer.println(linenumber + " CATCH CLAUSE : " + node);
					linenumber++;
					writer.println(linenumber + " " + Constants.ANTI_PATTERN_RRGC_MESSAGE);
					linenumber++;
					writer.println("_________________________________________________________________________________");
					numreplyingcause++;
					}
			}
		}
		/////////////////////////////////////////////////////////////
		
		
		//////////////////CATCH AND IGNORE////////////////////////////
		List<Statement> ls = node.getBody().statements();
		lssize = ls.size();
		type = ParichayanaActivator.getPreference(Constants.TEST_RNHR, project);
		if(lssize==1 && !JavaCore.IGNORE.equals(type)){
			if((body.trim().contains("return null;")) || (body.trim().contains("return (null);"))) {
		    	//ParichayanaActivator.logInfo("****** CONTAINS SINGLE RETURN NULL ******");
				ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
		    	ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
		    	ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_RNHR_MESSAGE);
		    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	try {
		    		int severity = getSeverity(type);
					createMarker(unit, Constants.ANTI_PATTERN_RNHR_MESSAGE, severity, Constants.RNHR_MARKER_ID, node.getStartPosition(), node.getLength());
				} catch (CoreException e) {
					ParichayanaActivator.log(e);
				}
		    	//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
				linenumber++;
		    	writer.println(linenumber + " CATCH CLAUSE : " + node);
		    	linenumber++;
		    	writer.println(linenumber + " " + Constants.ANTI_PATTERN_RNHR_MESSAGE);
		    	linenumber++;
		    	writer.println("_________________________________________________________________________________");
		    	numcatchignore++;
		    }
		}
		/////////////////////////////////////////////////////////////
	    
		//////////////////MULTI-LINE LOG-MESSAGE//////////////////////
		List<Statement> l = node.getBody().statements();
		int logcounter = 0;
		for(int i=0; i<l.size(); i++){
			Statement s = (Statement)l.get(i);
			String content = s.toString();
			if( (content.contains(new StringBuffer("log.error"))) ||
				(content.contains(new StringBuffer("log.info"))) ||
				(content.contains(new StringBuffer("log.warn"))) ||
				(content.contains(new StringBuffer("log.debug"))) ||
				(content.contains(new StringBuffer("log.trace"))) ||
				(content.contains(new StringBuffer("log.fatal"))) ||
				(content.contains(new StringBuffer("logger.trace"))) ||
				(content.contains(new StringBuffer("logger.fatal"))) ||
				(content.contains(new StringBuffer("logger.error"))) ||
				(content.contains(new StringBuffer("logger.info"))) ||
				(content.contains(new StringBuffer("logger.warn"))) ||
				(content.contains(new StringBuffer("logger.debug"))) ) {
				logcounter++;
			}
		}
		type = ParichayanaActivator.getPreference(Constants.TEST_MLLM, project);
		if(logcounter>1 && !JavaCore.IGNORE.equals(type)){
			nummultilinelog++;
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
			ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
			ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_MLLM_MESSAGE);
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
			try {
				int severity = getSeverity(type);
				createMarker(unit, Constants.ANTI_PATTERN_MLLM_MESSAGE, severity, Constants.MLLM_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException e) {
				ParichayanaActivator.log(e);
			}
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
			linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + " " + Constants.ANTI_PATTERN_MLLM_MESSAGE);
			linenumber++;
	    	writer.println("_________________________________________________________________________________");
		}
		//////////////////////////////////////////////////////////////
		
	    //////////////////////////////////////////////////////////////
		
		///////////////INTERRUPTED EXCEPTION//////////////////////////
		String nodeType = node.getException().getType().toString();
		boolean emptyStatement = true;
	    if(nodeType.equalsIgnoreCase("InterruptedException")){
	    	List<Statement> lst = node.getBody().statements();
			for(int i=0; i<lst.size(); i++){
				Statement s = (Statement)lst.get(i);
				String content = s.toString().trim();
				if(!(content.equalsIgnoreCase(""))){
					emptyStatement = false;
				}
			}
			type = ParichayanaActivator.getPreference(Constants.TEST_INEE, project);
			if(emptyStatement && !JavaCore.IGNORE.equals(type)){
				numInterruptedException++;
				//ParichayanaActivator.logInfo("INTERRUPTED EXCEPTION : " + node);
				ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
		    	ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
		    	ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_INEE_MESSAGE);
		    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	try {
		    		int severity = getSeverity(type);
					createMarker(unit, Constants.ANTI_PATTERN_INEE_MESSAGE, severity, Constants.INEE_MARKER_ID, node.getStartPosition(), node.getLength());
				} catch (CoreException ex) {
					ParichayanaActivator.log(ex);
				}
		    	//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
		    	linenumber++;
		    	writer.println(linenumber + " CATCH CLAUSE : " + node);
		    	linenumber++;
		    	writer.println(linenumber + " " + Constants.ANTI_PATTERN_INEE_MESSAGE);
		    	linenumber++;
		    	writer.println("_________________________________________________________________________________");
		    
			}
	    }
		
	    ///////////////////CATCH ALL///////////////////////////////////
	    nodeType = node.getException().getType().toString();
	    type = ParichayanaActivator.getPreference(Constants.TEST_CTGE, project);
	    boolean ignore = JavaCore.IGNORE.equals(type);
	    if( !ignore && ( (nodeType.equalsIgnoreCase("Exception")) || (nodeType.equalsIgnoreCase("Throwable")) ) ){
	    	List<Statement> lstate = node.getBody().statements();
            int lstatesize = lstate.size();
			Statement laststatement = (Statement)lstate.get(lstatesize-1);
			if(!(laststatement.toString().contains(new StringBuffer("throw")))){
				catchall++;
		    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
		    	ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
		    	ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_CTGE_MESSAGE);
		    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	try {
		    		int severity = getSeverity(type);
					createMarker(unit, Constants.ANTI_PATTERN_CTGE_MESSAGE, severity, Constants.CTGE_MARKER_ID, node.getStartPosition(), node.getLength());
				} catch (CoreException e) {
					ParichayanaActivator.log(e);
				}
		    	//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
		    	linenumber++;
		    	writer.println(linenumber + " CATCH CLAUSE : " + node);
		    	linenumber++;
		    	writer.println(linenumber + " " + Constants.ANTI_PATTERN_CTGE_MESSAGE);
		    	linenumber++;
		    	writer.println("_________________________________________________________________________________");
			}
	    }
	    ///////////////////////////////////////////////////////////////
	    
	    ///////////////////CATCH NULL POINTER EXCEPTION///////////////////////////////////
	    nodeType = node.getException().getType().toString();
	    type = ParichayanaActivator.getPreference(Constants.TEST_CNPE, project);
	    if( (nodeType.equalsIgnoreCase("NullPointerException"))) {
	    	catchnpe++;
	    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
	    	ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
	    	ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_CNPE_MESSAGE);
	    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	try {
	    		int severity = getSeverity(type);
				createMarker(unit, Constants.ANTI_PATTERN_CNPE_MESSAGE, severity, Constants.CNPE_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException ex) {
				ParichayanaActivator.log(ex);
			}
	    	//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
	    	linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
	    	linenumber++;
	    	writer.println(linenumber + " " + Constants.ANTI_PATTERN_CNPE_MESSAGE);
	    	linenumber++;
	    	writer.println("_________________________________________________________________________________");
	    }
	  ///////////////////////////////////////////////////////////////

	    
	    
        ///////////////////LOG AND RETURN NULL, PRINTSTACKTRACE AND RETURN NULL///////////////////////////////////
	    if((body.trim().contains("return null;")) || (body.trim().contains("return (null);"))) {
	    	returnnull++;
	    	returnnullflag = true;
	    	//ParichayanaActivator.logInfo("****** CONTAINS RETURN NULL ******");
	    }
	    type = ParichayanaActivator.getPreference(Constants.TEST_LGRN, project);
	    if(returnnullflag && logflag && !JavaCore.IGNORE.equals(type)){
	    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
	    	ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
			ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_LGRN_MESSAGE);
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
			//writer.println("_________________________________________________________________________________");
			try {
				int severity = getSeverity(type);
				createMarker(unit, Constants.ANTI_PATTERN_LGRN_MESSAGE, severity, Constants.LGRN_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException e) {
				ParichayanaActivator.log(e);
			}
			writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
	    	linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + Constants.ANTI_PATTERN_LGRN_MESSAGE);
			linenumber++;
	    	writer.println("_________________________________________________________________________________");
			numlogreturnnul++;
		}
	    type = ParichayanaActivator.getPreference(Constants.TEST_PSRN, project);
	    if(returnnullflag && printstacktraceflag && !JavaCore.IGNORE.equals(type)){
	    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
	    	ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
			ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_PSRN_MESSAGE);
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
			try {
				int severity = getSeverity(type);
				createMarker(unit, Constants.ANTI_PATTERN_PSRN_MESSAGE, severity, Constants.PSRN_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException e) {
				ParichayanaActivator.log(e);
			}
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
	    	linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + " " + Constants.ANTI_PATTERN_PSRN_MESSAGE);
			linenumber++;
	    	writer.println("_________________________________________________________________________________");
			numprintstackreturnnull++;
		}
        ///////////////////////////////////////////////////////////////
		return true;
	}
	
	/**
	 * @param type
	 * @return
	 */
	private int getSeverity(String type) {
		// TODO Auto-generated method stub
		if (JavaCore.ERROR.equals(type)) {
			return IMarker.SEVERITY_ERROR;
		};
		if (JavaCore.WARNING.equals(type)) {
			return IMarker.SEVERITY_WARNING;
		};
		return IMarker.SEVERITY_INFO;
	}

	/**
	 * @param unit2
	 * @return
	 */
	private String getName(ICompilationUnit unit) {
		try {
			if (unit != null && (unit.getUnderlyingResource() instanceof IFile) ) {
				return unit.getUnderlyingResource().getFullPath().toFile().toString();
			}
		} catch (JavaModelException e) {
			ParichayanaActivator.log(e);
		}
		return "?";
	}

	public boolean visit(TryStatement node){
		//ParichayanaActivator.logInfo("TRY CLAUSE : " + node.toString());
		return true;
	}
	
	public boolean visit(ThrowStatement node){
		////////////////////////////THROWS NULL-POINTER-EXCEPTION/////////////////////////////////////////////
		if(node.getExpression().toString().contains("NullPointerException")){
			//ParichayanaActivator.logInfo("THROW CLAUSE : " + node.getExpression());
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
	    	ParichayanaActivator.logInfo("THROW CLAUSE : " + node);
			ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_TNPE_MESSAGE);
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
			try {
				createMarker(unit, Constants.ANTI_PATTERN_TNPE_MESSAGE, IMarker.SEVERITY_WARNING, Constants.TNPE_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException ex) {
				ParichayanaActivator.log(ex);
			}
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
	    	linenumber++;
	    	writer.println(linenumber + " THROW CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + Constants.ANTI_PATTERN_TNPE_MESSAGE);
			linenumber++;
	    	writer.println("_________________________________________________________________________________");
			numtnpe++;
		}
		return true;
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	
	///////////////////////// THROWS EXCEPTION ///////////////////////////////////
	public boolean visit(MethodDeclaration node){
		
		nummethoddecl++;
		List<Name> l = node.thrownExceptions();
		int lssize = l.size();
		for(int i=0;i<lssize;i++){
			Expression e = (Expression)l.get(i);
			if(e.toString().trim().equals(new String("Exception"))){
				ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
		    	//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
		    	linenumber++;
		    	int ind = node.toString().indexOf("{");
		    	int ind2 = node.toString().indexOf(node.getName().toString());
				ParichayanaActivator.logInfo("METHOD NAME : " + node.toString().substring(ind2, ind));
				ParichayanaActivator.logInfo("\n");
				ParichayanaActivator.logInfo(Constants.ANTI_PATTERN_THGE_MESSAGE);
				ParichayanaActivator.logInfo("_________________________________________________________________________________");
				writer.println(linenumber + " METHOD NAME : " + node.toString().substring(ind2, ind));
				try {
					createMarker(unit, Constants.ANTI_PATTERN_THGE_MESSAGE, IMarker.SEVERITY_WARNING, Constants.THGE_MARKER_ID, node.getStartPosition(), node.getLength());
				} catch (CoreException ex) {
					ParichayanaActivator.log(ex);
				}
				linenumber++;
				writer.println();
				writer.println(linenumber + " " + Constants.ANTI_PATTERN_THGE_MESSAGE);
				linenumber++;
				writer.println("_________________________________________________________________________________");
				numthrowsexception++;
			}
		}
		
		return true;
	}

	/**
	 * @param unit
	 */
	public void setCompilationUnit(ICompilationUnit unit) {
		this.unit = unit;
	}
	
	private IMarker createMarker(ICompilationUnit unit, String message, Integer severity, String type, int startPosition, int length)
			throws CoreException {
		if (severity == null || unit == null || unit.getUnderlyingResource() == null) {
			return null;
		}
		IMarker marker = unit.getUnderlyingResource().createMarker(type);
		String[] attributeNames = Constants.ATTRIBUTE_NAMES;
		String[] allNames = attributeNames;

		Object[] allValues = new Object[allNames.length];
		int index = 0;
		StringBuffer sb = new StringBuffer();
		sb.append(message);

		allValues[index++] = sb.toString(); // message
		allValues[index++] = severity;
		
//		ISourceRange range = null;
//		IMember javaElement = unit.findPrimaryType();
//		if (javaElement != null) {
//			try {
//				range = javaElement.getNameRange();
//			} catch (JavaModelException e) {
//				if (e.getJavaModelStatus().getCode() != IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST) {
//					throw e;
//				}
//				if (!CharOperation.equals(javaElement.getElementName()
//						.toCharArray(), TypeConstants.PACKAGE_INFO_NAME)) {
//					throw e;
//				}
//
//			}
//		}
//		int start = range == null ? 0 : range.getOffset();
//		int end = range == null ? 1 : start + range.getLength();

		int start = startPosition;
		int end = startPosition + length;
		
		allValues[index++] = new Integer(start); // start

		allValues[index++] = new Integer(end > 0 ? end + 1 : end); // end

		marker.setAttributes(allNames, allValues);
		return marker;
	}


}