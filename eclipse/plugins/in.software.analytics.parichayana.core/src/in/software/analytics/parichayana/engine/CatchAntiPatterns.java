package in.software.analytics.parichayana.engine;
/**
* Mining Source Code for Automatically Discovering Exception Management Anti-Patterns and Code Smell 
*
* @author  Ashish Sureka (ashish@iiitd.ac.in)
* @version 1.0
* @since   2014-06-30 
*/
import in.software.analytics.parichayana.core.ParichayanaActivator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class CatchAntiPatterns {

	private static int fileCounter = 0;
	private ASTParser parser;
	public static CatchClauseVisitor ccv;
	public File f1 = null;
	public static PrintWriter writer = null;
	public static int linenumber = 1;
	public static File destinationFile;
	public static File sourceFolder;
	
	public CatchAntiPatterns(List<ICompilationUnit> units, String projectName) {
		//sourceFolder = args[0];
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
			
			process(units);
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
	
	/**
	 * 
	 */
	public CatchAntiPatterns() {
		// TODO Auto-generated constructor stub
	}

	public void listFilesInDirectory(File dir) {
		File[] files = dir.listFiles();
		   if (files != null) {
		      for (File f : files) {
		         if (f.isDirectory()) {
			        listFilesInDirectory(f);
			     } else {
			    	f1 = f;
			    	ccv.setFile(f1);
			    	String fileName = f.getName();
			    	if(fileName.endsWith("java")){
			    		fileCounter++;
			    		//ParichayanaActivator.logInfo(fileCounter + " : " + f.getAbsolutePath());
			    		try{
			    			String content = readFileToString(f.getAbsolutePath());
			    			//ParichayanaActivator.logInfo("_________________________________");
			    			process(content);
			    		}catch(Exception e){
			    			ParichayanaActivator.logInfo("Exception condition : " + e.getMessage());
			    		}
			    		
			    	}
			        
			     }
		    }
		 }
	}
	
	public void process(List<ICompilationUnit> units) {
		for (ICompilationUnit unit:units) {
			fileCounter++;
			IResource resource;
			try {
				resource = unit.getUnderlyingResource();
				if (resource instanceof IFile) {
					IFile file = (IFile) resource;
					
					File javaFile = getJavaFile(file);
					ccv.setFile(javaFile);
					process(unit);
				}
			} catch (CoreException e) {
				ParichayanaActivator.log(e);
			}
			
		}
	}
	
	/**
	 * @param file
	 * @return
	 * @throws CoreException
	 */
	private File getJavaFile(IFile file) throws CoreException {
		URI uri = file.getLocationURI();
		if(file.isLinked()){
		   uri = file.getRawLocationURI();
		}
		File javaFile = EFS.getStore(uri).toLocalFile(0, new NullProgressMonitor());
		return javaFile;
	}
	
	public void initialize(){
		parser = ASTParser.newParser(AST.JLS3);
		ccv = new CatchClauseVisitor();
	}
	
	public void process(String content){
		//ParichayanaActivator.logInfo("CONTENT : " + content);
		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
	    cu.accept(ccv);
	}
	
	public void process(ICompilationUnit unit){
		parser.setSource(unit);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
	    cu.accept(ccv);
	}
	
	
	public static String readFileToString(String filePath) throws IOException {
	    StringBuilder fileData = new StringBuilder(1000);
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));

	    char[] buf = new char[10];
	    int numRead = 0;
	    while ((numRead = reader.read(buf)) != -1) {
	        //          ParichayanaActivator.logInfo(numRead);
	        String readData = String.valueOf(buf, 0, numRead);
	        fileData.append(readData);
	        buf = new char[1024];
	    }
	    reader.close();
	    return  fileData.toString();    
	}
	 
	public static void main(String[] args) {
		
		//sourceFolder = args[0];
		//destinationFile = new File(args[1];
	
		ParichayanaActivator.logInfo("Mining Source Code for Automatically Discovering Exception Management Anti-Patterns and Code Smell\n");
		
		CatchAntiPatterns cap = new CatchAntiPatterns();
		try{
			writer = new PrintWriter(destinationFile, "UTF-8");
			writer.println(linenumber + " Mining Source Code for Automatically Discovering Exception Management Anti-Patterns and Code Smell\n");
			linenumber++;
		}catch(Exception e){
			ParichayanaActivator.logInfo("PRINT-WRITER : " + e);
		}
		try{
			cap.initialize();
			ccv.setWriter(writer);
			ccv.setLinenumber(linenumber);
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\JavaSyntaxHighlighter-1.2.0");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\JFreeChart");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\JUnit");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\apache-tomcat-8.0.8-src");
			File dire = new File("C:\\RESEARCH\\ExceptionHandling\\apache-jmeter-2.11");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\h2");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\k-9-4-804");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\JSON-java-master");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\libgdx-master");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\aws-sdk-java-master");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\astyanax-master");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\play-authenticate-master");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\jOOQ-master");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\spring-data-neo4j-master");
			//File dire = new File(sourceFolder);
			
			cap.listFilesInDirectory(dire);
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

}