/**
* Mining Source Code for Automatically Discovering Exception Management Anti-Patterns and Code Smell 
*
* @author  Ashish Sureka (ashish@iiitd.ac.in)
* @version 1.0
* @since   2014-06-30 
*/
import java.io.*;

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
	public static String destinationFile = "C:\\RESEARCH\\ExceptionHandling\\output.txt";
	public static String sourceFolder = "C:\\RESEARCH\\ExceptionHandling\\Activiti-master";
	
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
			    		//System.out.println(fileCounter + " : " + f.getAbsolutePath());
			    		try{
			    			String content = readFileToString(f.getAbsolutePath());
			    			//System.out.println("_________________________________");
			    			process(content);
			    		}catch(Exception e){
			    			System.out.println("Exception condition : " + e.getMessage());
			    		}
			    		
			    	}
			        
			     }
		    }
		 }
	}
	
	public void initialize(){
		parser = ASTParser.newParser(AST.JLS3);
		ccv = new CatchClauseVisitor();
	}
	
	public void process(String content){
		//System.out.println("CONTENT : " + content);
		parser.setSource(content.toCharArray());
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
	        //          System.out.println(numRead);
	        String readData = String.valueOf(buf, 0, numRead);
	        fileData.append(readData);
	        buf = new char[1024];
	    }
	    reader.close();
	    return  fileData.toString();    
	}
	 
	public static void main(String[] args) {
		
		sourceFolder = args[0];
		destinationFile = args[1];
	
		System.out.println("Mining Source Code for Automatically Discovering Exception Management Anti-Patterns and Code Smell\n");
		
		CatchAntiPatterns cap = new CatchAntiPatterns();
		try{
			writer = new PrintWriter(destinationFile, "UTF-8");
			writer.println(linenumber + " Mining Source Code for Automatically Discovering Exception Management Anti-Patterns and Code Smell\n");
			linenumber++;
		}catch(Exception e){
			System.out.println("PRINT-WRITER : " + e);
		}
		try{
			cap.initialize();
			ccv.setWriter(writer);
			ccv.setLinenumber(linenumber);
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\JavaSyntaxHighlighter-1.2.0");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\JFreeChart");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\JUnit");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\apache-tomcat-8.0.8-src");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\apache-jmeter-2.11");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\h2");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\k-9-4-804");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\JSON-java-master");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\libgdx-master");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\aws-sdk-java-master");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\astyanax-master");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\play-authenticate-master");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\jOOQ-master");
			//File dire = new File("C:\\RESEARCH\\ExceptionHandling\\spring-data-neo4j-master");
			File dire = new File(sourceFolder);
			
			cap.listFilesInDirectory(dire);
			int numcatchclause = ccv.getNumcatchclause();
			linenumber = ccv.getLinenumber();
			System.out.println("\n\nEXPERIMENTAL RESULTS");
			System.out.println("\nNUMBER OF JAVA FILES :" + fileCounter);
			System.out.println("NUMBER OF CATCH CLAUSES :" + numcatchclause);
			System.out.println("NUMBER OF METHOD DECLARATIONS :" + ccv.getNummethoddecl());
			System.out.println();
			System.out.println("NUMBER OF THROW PRINTSTACKTRACE ANTIPATTERN : " + ccv.getNumthrowprintst());
			System.out.println("NUMBER OF THROW LOG ANTIPATTERN : " + ccv.getNumthrowlog());
			System.out.println("NUMBER OF CATCH ALL ANTIPATTERN : " + ccv.getNumcatchall());
			System.out.println("NUMBER OF RETURN NULL LOG ANTIPATTERN : " + ccv.getNumlogreturnnull());
			System.out.println("NUMBER OF RETURN NULL PRINTSTACKTRACE ANTIPATTERN : " + ccv.getNumprintstackreturnnull());
			System.out.println("NUMBER OF MULTI-LINE LOG ANTIPATTERN : " + ccv.getNummultilinelog());
			System.out.println("NUMBER OF CATCH-AND-IGNORE ANTIPATTERN : " + ccv.getNumcatchignore());
			System.out.println("NUMBER OF THROWS EXCEPTION ANTIPATTERN : " + ccv.getNumthrowsexception());
			System.out.println("NUMBER OF DESTRUCTIVE WRAPPING ANTIPATTERN : " + ccv.getNumdestwrap());
			System.out.println("NUMBER OF RELYING ON GETCAUSE ANTIPATTERN : " + ccv.getNumrelyingcause());
			
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
			writer.println(linenumber + " NUMBER OF RNHR ANTIPATTERN : " + ccv.getNumlogreturnnull());
			linenumber++;
			writer.println(linenumber + " NUMBER OF PSRN ANTIPATTERN : " + ccv.getNumprintstackreturnnull());
			linenumber++;
			writer.println(linenumber + " NUMBER OF MLLM ANTIPATTERN : " + ccv.getNummultilinelog());
			linenumber++;
			writer.println(linenumber + " NUMBER OF LGRN ANTIPATTERN : " + ccv.getNumcatchignore());
			linenumber++;
			writer.println(linenumber + " NUMBER OF THGE ANTIPATTERN : " + ccv.getNumthrowsexception());
			linenumber++;
			writer.println(linenumber + " NUMBER OF WEPG ANTIPATTERN : " + ccv.getNumdestwrap());
			linenumber++;
			writer.println(linenumber + " NUMBER OF RRGC ANTIPATTERN : " + ccv.getNumrelyingcause());
			linenumber++;
			writer.close();
		}catch(Exception e){
			System.out.println("EXCEPTION " + e);
		}

	}

}
