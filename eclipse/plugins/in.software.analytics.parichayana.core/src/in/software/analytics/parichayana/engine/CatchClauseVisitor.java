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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
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

	/**
	 * 
	 */
	private static final String ANTI_PATTERN_TNPE_MESSAGE = "ANTI-PATTERN TNPE : NullPointerException should not be thrown by the program as it is expected that it is thrown by the virtual machine";
	private static final String ANTI_PATTERN_CNPE_MESSAGE = "ANTI-PATTERN CNPE : NullPointerException is a logical or programming error in the code (result of a bug) and should be eliminated rather than catching";
	private static final String ANTI_PATTERN_LGFT_MESSAGE = "ANTI-PATTERN LGFT : Fatal condition, log.fatal is the only line of code in the catch clause � method should abort and notify the caller with an exception";
	private static final String ANTI_PATTERN_INEE_MESSAGE = "ANTI-PATTERN INEE : Ignoring or suppressing InterruptedException with an empty catch-clause is an anti-pattern, empty catch block prevents in determining that an interrupted exception occurred or knowing that the thread was interrupted";
	private static final String ANTI_PATTERN_RRGC_MESSAGE = "ANTI-PATTERN RRGC : relying on the result of getCause makes the code fragile, use org.apache.commons.lang.exception.ExceptionUtils.getRootCause(Throwable throwable)";
	private static final String ANTI_PATTERN_WEPG_MESSAGE = "ANTI-PATTERN WEPG : Wrapping the exception and passing getMessage() destroys the stack trace of original exception";
	private static final String ANTI_PATTERN_THGE_MESSAGE = "ANTI-PATTERN THGE : Throws generic Exception, defeats the purpose of using a checked exception, declare the specific checked exceptions that your method can throw";
	private static final String ANTI_PATTERN_RNHR_MESSAGE = "ANTI-PATTERN RNHR : just returns null instead of handling or re-throwing the exception, swallows the exception, losing the information forever";
	private static final String ANTI_PATTERN_MLLM_MESSAGE = "ANTI-PATTERN MLLM : Using multi-line log messages causes problems when multiple threads are running in parallel, two log messages may end up spaced-out multiple lines apart in the log file,  group together all log messages, regardless of the level";
	private static final String ANTI_PATTERN_PSRN_MESSAGE = "ANTI-PATTERN PSRN : Print stack-trace and return null is wrong, instead of returning null, throw the exception, and let the caller deal with it";
	private static final String ANTI_PATTERN_LGRN_MESSAGE = "ANTI-PATTERN LGRN : Log and return null is wrong, instead of returning null, throw the exception, and let the caller deal with it";
	private static final String ANTI_PATTERN_CTGE_MESSAGE = "ANTI-PATTERN CTGE : catching generic Exception, catch the specific exception that can be thrown";
	private static final String ANTI_PATTERN_LGTE_MESSAGE = "ANTI-PATTERN LGTE : logging and throwing Exception, choose one otherwise it results in multiple log messages (multiple-entries, duplication)";
	private static final String ANTI_PATTERN_PSTE_MESSAGE = "ANTI-PATTERN PSTE : printing stack-trace and throwing Exception, choose one otherwise it results in multiple log messages (multiple-entries, duplication)";
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
		if(throwflag && printstacktraceflag){
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
			ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
			ParichayanaActivator.logInfo(ANTI_PATTERN_PSTE_MESSAGE);
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
			//writer.println("_________________________________________________________________________________");
	    	try {
				createMarker(unit, ANTI_PATTERN_PSTE_MESSAGE, IMarker.SEVERITY_WARNING, Constants.PSTE_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException e) {
				ParichayanaActivator.log(e);
			}
			writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
			linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + " " + ANTI_PATTERN_PSTE_MESSAGE);
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
		if(throwflag && logflag){
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
			ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
			ParichayanaActivator.logInfo(ANTI_PATTERN_LGTE_MESSAGE);
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
			try {
				createMarker(unit, ANTI_PATTERN_LGTE_MESSAGE, IMarker.SEVERITY_WARNING, Constants.LGTE_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException e) {
				ParichayanaActivator.log(e);
			}
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
			linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + " " + ANTI_PATTERN_LGTE_MESSAGE);
			linenumber++;
	    	writer.println("_________________________________________________________________________________");
			numthrowlog++;
			
		}
	    /////////////////LOG FATAL/////////////////////////////////////
		List<Statement> lis = node.getBody().statements();
		int lssize = lis.size();		
		if(lssize==1){
			if( (body.contains(new StringBuffer("log.fatal"))) ||
				(body.contains(new StringBuffer("logger.fatal"))) ) {
					ParichayanaActivator.logInfo("LOG FATAL");
					numLogFatal++;
					ParichayanaActivator.logInfo("_________________________________________________________________________________");
			    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
					ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
			    	ParichayanaActivator.logInfo(ANTI_PATTERN_LGFT_MESSAGE);
			    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
			    	try {
						createMarker(unit, ANTI_PATTERN_LGFT_MESSAGE, IMarker.SEVERITY_WARNING, Constants.LGFT_MARKER_ID, node.getStartPosition(), node.getLength());
					} catch (CoreException ex) {
						ParichayanaActivator.log(ex);
					}
			    	//writer.println("_________________________________________________________________________________");
			    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
					linenumber++;
			    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			    	linenumber++;
			    	writer.println(linenumber + " " + ANTI_PATTERN_LGFT_MESSAGE);
			    	linenumber++;
			    	writer.println("_________________________________________________________________________________");
			}
		}		
		/////////////////////////////////////////////////////////////////
		
		
		//////////////////////DESTRUCTIVE WRAPPING/////////////////////
		lis = node.getBody().statements();
		lssize = lis.size();
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
			    	ParichayanaActivator.logInfo(ANTI_PATTERN_WEPG_MESSAGE);
			    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
			    	try {
						createMarker(unit, ANTI_PATTERN_WEPG_MESSAGE, IMarker.SEVERITY_WARNING, Constants.WEPG_MARKER_ID, node.getStartPosition(), node.getLength());
					} catch (CoreException ex) {
						ParichayanaActivator.log(ex);
					}
			    	//writer.println("_________________________________________________________________________________");
			    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
					linenumber++;
			    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			    	linenumber++;
			    	writer.println(linenumber + " " + ANTI_PATTERN_WEPG_MESSAGE);
			    	linenumber++;
			    	writer.println("_________________________________________________________________________________");
			    	numdestwrap++;
				}
			}
		}
		///////////////////////////////////////////////////////////////
		
		//////////////////RELYING ON GETCLAUSE////////////////////////////
		List<Statement> lists = node.getBody().statements();
		int lssizee = lists.size();
		for(int i=0;i<lssizee;i++){
			if( (lists.get(i).toString().trim().contains("if")) &&
				(lists.get(i).toString().trim().contains("getCause")) &&
				(lists.get(i).toString().trim().contains("instanceof")) )
					 {
				//ParichayanaActivator.logInfo("****** CONTAINS IF GETCAUSE INSTANCE ******");
				ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
				ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
				ParichayanaActivator.logInfo(ANTI_PATTERN_RRGC_MESSAGE);
				ParichayanaActivator.logInfo("_________________________________________________________________________________");
				try {
					createMarker(unit, ANTI_PATTERN_RRGC_MESSAGE, IMarker.SEVERITY_WARNING, Constants.RRGC_MARKER_ID, node.getStartPosition(), node.getLength());
				} catch (CoreException ex) {
					ParichayanaActivator.log(ex);
				}
				//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
				linenumber++;
		    	writer.println(linenumber + " CATCH CLAUSE : " + node);
				linenumber++;
		    	writer.println(linenumber + " " + ANTI_PATTERN_RRGC_MESSAGE);
				linenumber++;
		    	writer.println("_________________________________________________________________________________");
				numreplyingcause++;
			}
		}
		/////////////////////////////////////////////////////////////
		
		
		//////////////////CATCH AND IGNORE////////////////////////////
		List<Statement> ls = node.getBody().statements();
		lssize = ls.size();
		if(lssize==1){
			if((body.trim().contains("return null;")) || (body.trim().contains("return (null);"))) {
		    	//ParichayanaActivator.logInfo("****** CONTAINS SINGLE RETURN NULL ******");
				ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
		    	ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
		    	ParichayanaActivator.logInfo(ANTI_PATTERN_RNHR_MESSAGE);
		    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	try {
					createMarker(unit, ANTI_PATTERN_RNHR_MESSAGE, IMarker.SEVERITY_WARNING, Constants.RNHR_MARKER_ID, node.getStartPosition(), node.getLength());
				} catch (CoreException e) {
					ParichayanaActivator.log(e);
				}
		    	//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
				linenumber++;
		    	writer.println(linenumber + " CATCH CLAUSE : " + node);
		    	linenumber++;
		    	writer.println(linenumber + " " + ANTI_PATTERN_RNHR_MESSAGE);
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
		if(logcounter>1){
			nummultilinelog++;
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
			ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
			ParichayanaActivator.logInfo(ANTI_PATTERN_MLLM_MESSAGE);
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
			try {
				createMarker(unit, ANTI_PATTERN_MLLM_MESSAGE, IMarker.SEVERITY_WARNING, Constants.MLLM_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException e) {
				ParichayanaActivator.log(e);
			}
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
			linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + " " + ANTI_PATTERN_MLLM_MESSAGE);
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
			if(emptyStatement){
				numInterruptedException++;
				//ParichayanaActivator.logInfo("INTERRUPTED EXCEPTION : " + node);
				ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
		    	ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
		    	ParichayanaActivator.logInfo(ANTI_PATTERN_INEE_MESSAGE);
		    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	try {
					createMarker(unit, ANTI_PATTERN_INEE_MESSAGE, IMarker.SEVERITY_WARNING, Constants.INEE_MARKER_ID, node.getStartPosition(), node.getLength());
				} catch (CoreException ex) {
					ParichayanaActivator.log(ex);
				}
		    	//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
		    	linenumber++;
		    	writer.println(linenumber + " CATCH CLAUSE : " + node);
		    	linenumber++;
		    	writer.println(linenumber + " " + ANTI_PATTERN_INEE_MESSAGE);
		    	linenumber++;
		    	writer.println("_________________________________________________________________________________");
		    
			}
	    }
		
	    ///////////////////CATCH ALL///////////////////////////////////
	    nodeType = node.getException().getType().toString();
	    if( (nodeType.equalsIgnoreCase("Exception")) || (nodeType.equalsIgnoreCase("Throwable")) ){
	    	List<Statement> lstate = node.getBody().statements();
            int lstatesize = lstate.size();
			Statement laststatement = (Statement)lstate.get(lstatesize-1);
			if(!(laststatement.toString().contains(new StringBuffer("throw")))){
				catchall++;
		    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
		    	ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
		    	ParichayanaActivator.logInfo(ANTI_PATTERN_CTGE_MESSAGE);
		    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
		    	try {
					createMarker(unit, ANTI_PATTERN_CTGE_MESSAGE, IMarker.SEVERITY_WARNING, Constants.CTGE_MARKER_ID, node.getStartPosition(), node.getLength());
				} catch (CoreException e) {
					ParichayanaActivator.log(e);
				}
		    	//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
		    	linenumber++;
		    	writer.println(linenumber + " CATCH CLAUSE : " + node);
		    	linenumber++;
		    	writer.println(linenumber + " " + ANTI_PATTERN_CTGE_MESSAGE);
		    	linenumber++;
		    	writer.println("_________________________________________________________________________________");
			}
	    }
	    ///////////////////////////////////////////////////////////////
	    
	    ///////////////////CATCH NULL POINTER EXCEPTION///////////////////////////////////
	    nodeType = node.getException().getType().toString();
	    if( (nodeType.equalsIgnoreCase("NullPointerException"))) {
	    	catchnpe++;
	    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
	    	ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
	    	ParichayanaActivator.logInfo(ANTI_PATTERN_CNPE_MESSAGE);
	    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	try {
				createMarker(unit, ANTI_PATTERN_CNPE_MESSAGE, IMarker.SEVERITY_WARNING, Constants.CNPE_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException ex) {
				ParichayanaActivator.log(ex);
			}
	    	//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
	    	linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
	    	linenumber++;
	    	writer.println(linenumber + " " + ANTI_PATTERN_CNPE_MESSAGE);
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
	    if(returnnullflag && logflag){
	    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
	    	ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
			ParichayanaActivator.logInfo(ANTI_PATTERN_LGRN_MESSAGE);
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
			//writer.println("_________________________________________________________________________________");
			try {
				createMarker(unit, ANTI_PATTERN_LGRN_MESSAGE, IMarker.SEVERITY_WARNING, Constants.LGRN_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException e) {
				ParichayanaActivator.log(e);
			}
			writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
	    	linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + ANTI_PATTERN_LGRN_MESSAGE);
			linenumber++;
	    	writer.println("_________________________________________________________________________________");
			numlogreturnnul++;
		}
	    if(returnnullflag && printstacktraceflag){
	    	ParichayanaActivator.logInfo("_________________________________________________________________________________");
	    	ParichayanaActivator.logInfo("FILE NAME : " + getName(unit) + "\n");
	    	ParichayanaActivator.logInfo("CATCH CLAUSE : " + node);
			ParichayanaActivator.logInfo(ANTI_PATTERN_PSRN_MESSAGE);
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
			try {
				createMarker(unit, ANTI_PATTERN_PSRN_MESSAGE, IMarker.SEVERITY_WARNING, Constants.PSRN_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException e) {
				ParichayanaActivator.log(e);
			}
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
	    	linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + " " + ANTI_PATTERN_PSRN_MESSAGE);
			linenumber++;
	    	writer.println("_________________________________________________________________________________");
			numprintstackreturnnull++;
		}
        ///////////////////////////////////////////////////////////////
		return true;
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
			ParichayanaActivator.logInfo(ANTI_PATTERN_TNPE_MESSAGE);
			ParichayanaActivator.logInfo("_________________________________________________________________________________");
			try {
				createMarker(unit, ANTI_PATTERN_TNPE_MESSAGE, IMarker.SEVERITY_WARNING, Constants.TNPE_MARKER_ID, node.getStartPosition(), node.getLength());
			} catch (CoreException ex) {
				ParichayanaActivator.log(ex);
			}
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + getName(unit) + "\n");
	    	linenumber++;
	    	writer.println(linenumber + " THROW CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + ANTI_PATTERN_TNPE_MESSAGE);
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
				ParichayanaActivator.logInfo(ANTI_PATTERN_THGE_MESSAGE);
				ParichayanaActivator.logInfo("_________________________________________________________________________________");
				writer.println(linenumber + " METHOD NAME : " + node.toString().substring(ind2, ind));
				try {
					createMarker(unit, ANTI_PATTERN_THGE_MESSAGE, IMarker.SEVERITY_WARNING, Constants.THGE_MARKER_ID, node.getStartPosition(), node.getLength());
				} catch (CoreException ex) {
					ParichayanaActivator.log(ex);
				}
				linenumber++;
				writer.println();
				writer.println(linenumber + " " + ANTI_PATTERN_THGE_MESSAGE);
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