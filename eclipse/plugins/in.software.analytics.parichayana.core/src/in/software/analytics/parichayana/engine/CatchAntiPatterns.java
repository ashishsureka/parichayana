/**
* Mining Source Code for Automatically Discovering Exception Management Anti-Patterns and Code Smell 
*
* @author  Ashish Sureka (ashish@iiitd.ac.in)
* @version 1.0
* @since   2014-06-30 
*/
package in.software.analytics.parichayana.engine;

import in.software.analytics.parichayana.core.ParichayanaActivator;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class CatchAntiPatterns {

	private int fileCounter = 0;
	private ASTParser parser;
	public CatchClauseVisitor ccv;
	public PrintWriter writer = null;
	public int linenumber = 1;
	public File destinationFile;
	private IProject project;
	
	public CatchAntiPatterns(List<ICompilationUnit> units, IProject project, IProgressMonitor monitor) {
		this.project = project;
		String projectName = project.getName();
		destinationFile = ParichayanaActivator.getDestinationFile(projectName);
		
		ParichayanaActivator.logInfo("Mining Source Code for Automatically Discovering Exception Management Anti-Patterns and Code Smell\n");
		
		try{
			writer = new PrintWriter(destinationFile, "UTF-8");
			writer.println(linenumber + " Mining Source Code for Automatically Discovering Exception Management Anti-Patterns and Code Smell\n");
			linenumber++;
		}catch(Exception e){
			ParichayanaActivator.logInfo("PRINT-WRITER : " + e);
		}
		try{
			initialize();
			ccv.setWriter(writer);
			ccv.setLinenumber(linenumber);
			
			process(units, monitor);
			int numcatchclause = ccv.getNumcatchclause();
			linenumber = ccv.getLinenumber();
			ParichayanaActivator.logInfo("\n\nEXPERIMENTAL RESULTS");
			ParichayanaActivator.logInfo("\nNUMBER OF JAVA FILES :" + fileCounter);
			ParichayanaActivator.logInfo("NUMBER OF CATCH CLAUSES :" + numcatchclause);
			ParichayanaActivator.logInfo("NUMBER OF METHOD DECLARATIONS :" + ccv.getNummethoddecl());
			ParichayanaActivator.logInfo("\n");
			ParichayanaActivator.logInfo("NUMBER OF THROW PRINTSTACKTRACE ANTIPATTERN : " + ccv.getNumthrowprintst());
			ParichayanaActivator.logInfo("NUMBER OF THROW LOG ANTIPATTERN : " + ccv.getNumthrowlog());
			ParichayanaActivator.logInfo("NUMBER OF CATCH ALL ANTIPATTERN : " + ccv.getNumcatchall());
			ParichayanaActivator.logInfo("NUMBER OF RETURN NULL LOG ANTIPATTERN : " + ccv.getNumlogreturnnull());
			ParichayanaActivator.logInfo("NUMBER OF RETURN NULL PRINTSTACKTRACE ANTIPATTERN : " + ccv.getNumprintstackreturnnull());
			ParichayanaActivator.logInfo("NUMBER OF MULTI-LINE LOG ANTIPATTERN : " + ccv.getNummultilinelog());
			ParichayanaActivator.logInfo("NUMBER OF CATCH-AND-IGNORE ANTIPATTERN : " + ccv.getNumcatchignore());
			ParichayanaActivator.logInfo("NUMBER OF THROWS EXCEPTION ANTIPATTERN : " + ccv.getNumthrowsexception());
			ParichayanaActivator.logInfo("NUMBER OF DESTRUCTIVE WRAPPING ANTIPATTERN : " + ccv.getNumdestwrap());
			ParichayanaActivator.logInfo("NUMBER OF RELYING ON GETCAUSE ANTIPATTERN : " + ccv.getNumrelyingcause());
			ParichayanaActivator.logInfo("NUMBER OF EMPTY CATCH INTERRUPTED EXCEPTION : " + ccv.getNumInterrptedException());
			ParichayanaActivator.logInfo("NUMBER OF LOG FATAL EXCEPTION : " + ccv.getNumLogFatal());
			ParichayanaActivator.logInfo("NUMBER OF CATCH NULL POINTER EXCEPTION : " + ccv.getNumcatchnpe());
			ParichayanaActivator.logInfo("NUMBER OF THROW NULL POINTER EXCEPTION : " + ccv.getNumtnpe());
			
			writer.println("\n\n" + linenumber + " EXPERIMENTAL RESULTS");
			linenumber++;
			writer.println("\n" + linenumber + " NUMBER OF JAVA FILES IN THE APPLICATION : " + fileCounter);
			linenumber++;
			writer.println(linenumber + " NUMBER OF CATCH CLAUSES IN THE APPLICATION :" + numcatchclause);
			linenumber++;
			writer.println(linenumber + " NUMBER OF METHOD DECLARATIONS IN THE APPLICATION :" + ccv.getNummethoddecl());
			linenumber++;
			writer.println();
			writer.println(linenumber + " NUMBER OF PSTE ANTIPATTERN : " + ccv.getNumthrowprintst());
			linenumber++;
			writer.println(linenumber + " NUMBER OF LGTE ANTIPATTERN : " + ccv.getNumthrowlog());
			linenumber++;
			writer.println(linenumber + " NUMBER OF CTGE ANTIPATTERN : " + ccv.getNumcatchall());
			linenumber++;
			writer.println(linenumber + " NUMBER OF LGRN ANTIPATTERN : " + ccv.getNumlogreturnnull());
			linenumber++;
			writer.println(linenumber + " NUMBER OF PSRN ANTIPATTERN : " + ccv.getNumprintstackreturnnull());
			linenumber++;
			writer.println(linenumber + " NUMBER OF MLLM ANTIPATTERN : " + ccv.getNummultilinelog());
			linenumber++;
			writer.println(linenumber + " NUMBER OF RNHR ANTIPATTERN : " + ccv.getNumcatchignore());
			linenumber++;
			writer.println(linenumber + " NUMBER OF THGE ANTIPATTERN : " + ccv.getNumthrowsexception());
			linenumber++;
			writer.println(linenumber + " NUMBER OF WEPG ANTIPATTERN : " + ccv.getNumdestwrap());
			linenumber++;
			writer.println(linenumber + " NUMBER OF RRGC ANTIPATTERN : " + ccv.getNumrelyingcause());
			linenumber++;
			writer.println(linenumber + " NUMBER OF INEE ANTIPATTERN : " + ccv.getNumInterrptedException());
			linenumber++;
			writer.println(linenumber + " NUMBER OF LGFT ANTIPATTERN : " + ccv.getNumLogFatal());
			linenumber++;
			writer.println(linenumber + " NUMBER OF CNPE ANTIPATTERN : " + ccv.getNumcatchnpe());
			linenumber++;
			writer.println(linenumber + " NUMBER OF TNPE ANTIPATTERN : " + ccv.getNumtnpe());
			linenumber++;
			
			writer.close();
		}catch(Exception e){
			ParichayanaActivator.logInfo("EXCEPTION " + e);
		}

	}
	
	public void process(List<ICompilationUnit> units, IProgressMonitor monitor) {
		int size = units.size();
		for (ICompilationUnit unit:units) {
			fileCounter++;
			if (monitor != null) {
				monitor.subTask("Analyzing " + unit.getElementName() + " (" + fileCounter + "/" + size + ")" );
				monitor.worked(1);
			}
			ccv.setCompilationUnit(unit);
			process(unit);
		}
	}
	
	public void initialize(){
		parser = ASTParser.newParser(AST.JLS3);
		ccv = new CatchClauseVisitor(project);
	}
	
	public void process(ICompilationUnit unit){
		parser.setSource(unit);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
	    cu.accept(ccv);
	}	 
	
}