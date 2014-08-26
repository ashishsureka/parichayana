/**
* Mining Source Code for Automatically Discovering Exception Management Anti-Patterns and Code Smell 
*
* @author  Ashish Sureka (ashish@iiitd.ac.in)
* @version 1.0
* @since   2014-06-30 
*/

import org.eclipse.jdt.core.dom.*;
import java.util.*;
import java.io.*;

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
	
	public File f;
	public PrintWriter writer = null;
	public int linenumber = 1; 
	
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
	
	public void setFile(File f){
		this.f = f;
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
	    //System.out.println("CATCH CLAUSE : " + node.toString());
	    //System.out.println("BODY : " + node.getBody());
	    //System.out.println("NODE TYPE : " + node.getNodeType());
	    
	    ///////////////////LOG AND THROW, PRINTSTACKTRACE AND THROW///////////////////////////////
		String body = node.getBody().toString();
		
		//System.out.println("CATCH CLAUSE : " + node);
		numcatchclause++;
		
		boolean throwflag = false;
		boolean printstacktraceflag = false;
		boolean logflag = false;
		boolean returnnullflag = false;
		 
		if(body.contains(new StringBuffer("throw "))){
			//System.out.println("****** CONTAINS THROW ******");
			throwflag = true;
		}
		if(body.contains(new StringBuffer("printStackTrace"))){
			//System.out.println("****** CONTAINS PRINT STACK TRACE ******");
			printstacktraceflag = true;
		}
		if(throwflag && printstacktraceflag){
			System.out.println("_________________________________________________________________________________");
	    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
			System.out.println("CATCH CLAUSE : " + node);
			System.out.println("ANTI-PATTERN PSTE : printing stack-trace and throwing Exception, choose one otherwise it results in multiple log messages (multiple-entries, duplication)");
			System.out.println("_________________________________________________________________________________");
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
			linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + " ANTI-PATTERN PSTE : printing stack-trace and throwing Exception, choose one otherwise it results in multiple log messages (multiple-entries, duplication)");
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
			//System.out.println("****** CONTAINS LOG ******");
			logflag = true;
		
		}
		if(throwflag && logflag){
			System.out.println("_________________________________________________________________________________");
	    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
			System.out.println("CATCH CLAUSE : " + node);
			System.out.println("ANTI-PATTERN LGTE : logging and throwing Exception, choose one otherwise it results in multiple log messages (multiple-entries, duplication)");
			System.out.println("_________________________________________________________________________________");
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
			linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + " ANTI-PATTERN LGTE : logging and throwing Exception, choose one otherwise it results in multiple log messages (multiple-entries, duplication)");
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
					System.out.println("LOG FATAL");
					numLogFatal++;
					System.out.println("_________________________________________________________________________________");
			    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
					System.out.println("CATCH CLAUSE : " + node);
			    	System.out.println("ANTI-PATTERN LGFT : Fatal condition, log.fatal is the only line of code in the catch clause – method should abort and notify the caller with an exception");
			    	System.out.println("_________________________________________________________________________________");
			    	//writer.println("_________________________________________________________________________________");
			    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
					linenumber++;
			    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			    	linenumber++;
			    	writer.println(linenumber + " ANTI-PATTERN LGFT : Fatal condition, log.fatal is the only line of code in the catch clause – method should abort and notify the caller with an exception");
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
				//System.out.println("SVD : " + svd.getName());
				if(!(content.contains(","+svd.getName()+")"))){
					//System.out.println("NOT-PATTERN" + ","+svd.getName()+")");
					//System.out.println("****** CONTAINS THROW GETMESSAGE ******");
					System.out.println("_________________________________________________________________________________");
			    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
					System.out.println("CATCH CLAUSE : " + node);
			    	System.out.println("ANTI-PATTERN WEPG : Wrapping the exception and passing getMessage() destroys the stack trace of original exception");
			    	System.out.println("_________________________________________________________________________________");
			    	//writer.println("_________________________________________________________________________________");
			    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
					linenumber++;
			    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			    	linenumber++;
			    	writer.println(linenumber + " ANTI-PATTERN WEPG : Wrapping the exception and passing getMessage() destroys the stack trace of original exception");
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
				//System.out.println("****** CONTAINS IF GETCAUSE INSTANCE ******");
				System.out.println("_________________________________________________________________________________");
		    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
				System.out.println("CATCH CLAUSE : " + node);
				System.out.println("ANTI-PATTERN RRGC : relying on the result of getCause makes the code fragile, use org.apache.commons.lang.exception.ExceptionUtils.getRootCause(Throwable throwable)");
				System.out.println("_________________________________________________________________________________");
				//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
				linenumber++;
		    	writer.println(linenumber + " CATCH CLAUSE : " + node);
				linenumber++;
		    	writer.println(linenumber + " ANTI-PATTERN RRGC : relying on the result of getCause makes the code fragile, use org.apache.commons.lang.exception.ExceptionUtils.getRootCause(Throwable throwable)");
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
		    	//System.out.println("****** CONTAINS SINGLE RETURN NULL ******");
				System.out.println("_________________________________________________________________________________");
		    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
		    	System.out.println("CATCH CLAUSE : " + node);
		    	System.out.println("ANTI-PATTERN RNHR : just returns null instead of handling or re-throwing the exception, swallows the exception, losing the information forever");
		    	System.out.println("_________________________________________________________________________________");
		    	//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
				linenumber++;
		    	writer.println(linenumber + " CATCH CLAUSE : " + node);
		    	linenumber++;
		    	writer.println(linenumber + " ANTI-PATTERN RNHR : just returns null instead of handling or re-throwing the exception, swallows the exception, losing the information forever");
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
			System.out.println("_________________________________________________________________________________");
	    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
			System.out.println("CATCH CLAUSE : " + node);
			System.out.println("ANTI-PATTERN MLLM : Using multi-line log messages causes problems when multiple threads are running in parallel, two log messages may end up spaced-out multiple lines apart in the log file,  group together all log messages, regardless of the level");
			System.out.println("_________________________________________________________________________________");
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
			linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + " ANTI-PATTERN MLLM : Using multi-line log messages causes problems when multiple threads are running in parallel, two log messages may end up spaced-out multiple lines apart in the log file,  group together all log messages, regardless of the level");
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
				//System.out.println("INTERRUPTED EXCEPTION : " + node);
				System.out.println("_________________________________________________________________________________");
		    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
		    	System.out.println("CATCH CLAUSE : " + node);
		    	System.out.println("ANTI-PATTERN INEE : Ignoring or suppressing InterruptedException with an empty catch-clause is an anti-pattern, empty catch block prevents in determining that an interrupted exception occurred or knowing that the thread was interrupted");
		    	System.out.println("_________________________________________________________________________________");
		    	//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
		    	linenumber++;
		    	writer.println(linenumber + " CATCH CLAUSE : " + node);
		    	linenumber++;
		    	writer.println(linenumber + " ANTI-PATTERN INEE : Ignoring or suppressing InterruptedException with an empty catch-clause is an anti-pattern, empty catch block prevents in determining that an interrupted exception occurred or knowing that the thread was interrupted");
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
		    	System.out.println("_________________________________________________________________________________");
		    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
		    	System.out.println("CATCH CLAUSE : " + node);
		    	System.out.println("ANTI-PATTERN CTGE : catching generic Exception, catch the specific exception that can be thrown");
		    	System.out.println("_________________________________________________________________________________");
		    	//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
		    	linenumber++;
		    	writer.println(linenumber + " CATCH CLAUSE : " + node);
		    	linenumber++;
		    	writer.println(linenumber + " ANTI-PATTERN CTGE : catching generic Exception, catch the specific exception that can be thrown");
		    	linenumber++;
		    	writer.println("_________________________________________________________________________________");
			}
	    }
	    ///////////////////////////////////////////////////////////////
	    
	    ///////////////////CATCH NULL POINTER EXCEPTION///////////////////////////////////
	    nodeType = node.getException().getType().toString();
	    if( (nodeType.equalsIgnoreCase("NullPointerException"))) {
	    	catchnpe++;
	    	System.out.println("_________________________________________________________________________________");
	    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
	    	System.out.println("CATCH CLAUSE : " + node);
	    	System.out.println("ANTI-PATTERN CNPE : NullPointerException is a logical or programming error in the code (result of a bug) and should be eliminated rather than catching");
	    	System.out.println("_________________________________________________________________________________");
	    	//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
	    	linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
	    	linenumber++;
	    	writer.println(linenumber + " ANTI-PATTERN CNPE : NullPointerException is a logical or programming error in the code (result of a bug) and should be eliminated rather than catching. ");
	    	linenumber++;
	    	writer.println("_________________________________________________________________________________");
	    }
	  ///////////////////////////////////////////////////////////////

	    
	    
        ///////////////////LOG AND RETURN NULL, PRINTSTACKTRACE AND RETURN NULL///////////////////////////////////
	    if((body.trim().contains("return null;")) || (body.trim().contains("return (null);"))) {
	    	returnnull++;
	    	returnnullflag = true;
	    	//System.out.println("****** CONTAINS RETURN NULL ******");
	    }
	    if(returnnullflag && logflag){
	    	System.out.println("_________________________________________________________________________________");
	    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
	    	System.out.println("CATCH CLAUSE : " + node);
			System.out.println("ANTI-PATTERN LGRN : Log and return null is wrong, instead of returning null, throw the exception, and let the caller deal with it");
			System.out.println("_________________________________________________________________________________");
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
	    	linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + "ANTI-PATTERN LGRN : Log and return null is wrong, instead of returning null, throw the exception, and let the caller deal with it");
			linenumber++;
	    	writer.println("_________________________________________________________________________________");
			numlogreturnnul++;
		}
	    if(returnnullflag && printstacktraceflag){
	    	System.out.println("_________________________________________________________________________________");
	    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
	    	System.out.println("CATCH CLAUSE : " + node);
			System.out.println("ANTI-PATTERN PSRN : Print stack-trace and return null is wrong, instead of returning null, throw the exception, and let the caller deal with it");
			System.out.println("_________________________________________________________________________________");
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
	    	linenumber++;
	    	writer.println(linenumber + " CATCH CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + " ANTI-PATTERN PSRN : Print stack-trace and return null is wrong, instead of returning null, throw the exception, and let the caller deal with it");
			linenumber++;
	    	writer.println("_________________________________________________________________________________");
			numprintstackreturnnull++;
		}
        ///////////////////////////////////////////////////////////////
		return true;
	}
	
	public boolean visit(TryStatement node){
		//System.out.println("TRY CLAUSE : " + node.toString());
		return true;
	}
	
	public boolean visit(ThrowStatement node){
		////////////////////////////THROWS NULL-POINTER-EXCEPTION/////////////////////////////////////////////
		if(node.getExpression().toString().contains("NullPointerException")){
			//System.out.println("THROW CLAUSE : " + node.getExpression());
			System.out.println("_________________________________________________________________________________");
	    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
	    	System.out.println("THROW CLAUSE : " + node);
			System.out.println("ANTI-PATTERN TNPE : NullPointerException should not be thrown by the program as it is expected that it is thrown by the virtual machine");
			System.out.println("_________________________________________________________________________________");
			//writer.println("_________________________________________________________________________________");
	    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
	    	linenumber++;
	    	writer.println(linenumber + " THROW CLAUSE : " + node);
			linenumber++;
	    	writer.println(linenumber + "ANTI-PATTERN TNPE : NullPointerException should not be thrown by the program as it is expected that it is thrown by the virtual machine");
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
				System.out.println("_________________________________________________________________________________");
		    	System.out.println("FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
		    	//writer.println("_________________________________________________________________________________");
		    	writer.println(linenumber + " FILE NAME : " + f.getAbsolutePath().substring(30, f.getAbsolutePath().length()) + "\n");
		    	linenumber++;
		    	int ind = node.toString().indexOf("{");
		    	int ind2 = node.toString().indexOf(node.getName().toString());
				System.out.println("METHOD NAME : " + node.toString().substring(ind2, ind));
				System.out.println();
				System.out.println("ANTI-PATTERN THGE : Throws generic Exception, defeats the purpose of using a checked exception, declare the specific checked exceptions that your method can throw");
				System.out.println("_________________________________________________________________________________");
				writer.println(linenumber + " METHOD NAME : " + node.toString().substring(ind2, ind));
				linenumber++;
				writer.println();
				writer.println(linenumber + " ANTI-PATTERN THGE : Throws generic Exception, defeats the purpose of using a checked exception, declare the specific checked exceptions that your method can throw");
				linenumber++;
				writer.println("_________________________________________________________________________________");
				numthrowsexception++;
			}
		}
		
		return true;
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) {
		

	}

}
