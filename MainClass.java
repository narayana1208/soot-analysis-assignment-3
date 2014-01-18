import java.util.*;

import soot.*;
import soot.jimple.Stmt;
import soot.tagkit.LineNumberTag;
import soot.tagkit.StringTag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
public class MainClass {

	public MainClass() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		soot.options.Options.v().set_keep_line_number(true);
		soot.PhaseOptions.v().setPhaseOption("jb", "use-original-names");
		
		SootClass Sc=soot.Scene.v().loadClassAndSupport("testcase3");
		Sc.setApplicationClass();		
		Iterator methodIt=Sc.getMethods().iterator();
		while( methodIt.hasNext() ) {
            SootMethod method = (SootMethod) methodIt.next();
            Body b = method.retrieveActiveBody();
			UnitGraph graph = new ExceptionalUnitGraph(b); 
			HasNextAnalysis an = new HasNextAnalysis(graph); 
            Iterator It = graph.iterator();
            while( It.hasNext() ) {
                Stmt stm = (Stmt) It.next();
                LineNumberTag tag = (LineNumberTag) stm.getTag("LineNumberTag");
                FlowSet In = (FlowSet) an.getFlowBefore( stm );
                FlowSet Out = (FlowSet) an.getFlowAfter( stm );
                if(!Out.isEmpty())
                {
                	if(Out.size()==1 && Out.contains(1))
                		System.out.println(tag.getLineNumber()+" "+stm+" Error");
                	else if((Out.size()>1 && Out.contains(1)))
                	{
                		System.out.println(tag.getLineNumber()+" "+stm+" Warning");
                	}
                }
                //System.out.println(stm);
                //System.out.println("IN "+In);
                //System.out.println("Out "+Out);
                }
			}
		}
	}
