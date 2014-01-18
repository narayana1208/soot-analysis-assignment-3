
import soot.*;
import soot.util.*;

import java.util.*;

import soot.JastAddJ.IfStmt;
import soot.jimple.*;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.*;


class HasNextMonitor_1 implements Cloneable {
	public Object clone() {
		try {
			HasNextMonitor_1 ret = (HasNextMonitor_1) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
	int state;
	int event;
	ArrayList state_list=null;

	boolean MOP_unsafe = false;

	public HasNextMonitor_1 () {
		state = 0;
		event = -1;
		state_list = new ArrayList<>();
	}
	synchronized public final void hasnext(Value lhs) {
		event = 1;

		switch(state) {
			case 0:
			switch(event) {
				case 1 : state = 2; break;
				case 2 : state = 1; break;
				default : state = -1; break;
			}
			break;
			case 1:
			switch(event) {
				case 1 : state = 2; break;
				case 2 : state = 1; break;
				default : state = -1; break;
			}
			break;
			case 2:
			switch(event) {
				case 1 : state = 2; break;
				case 2 : state = 0; break;
				default : state = -1; break;
			}
			break;
			default : state = -1;
		}

		MOP_unsafe = state == 1;
	}
	synchronized public final void next(Value lhs) {
		event = 2;

		switch(state) {
			case 0:
			switch(event) {
				case 1 : state = 2; break;
				case 2 : state = 1; break;
				default : state = -1; break;
			}
			break;
			case 1:
			switch(event) {
				case 1 : state = 2; break;
				case 2 : state = 1; break;
				default : state = -1; break;
			}
			break;
			case 2:
			switch(event) {
				case 1 : state = 2; break;
				case 2 : state = 0; break;
				default : state = -1; break;
			}
			break;
			default : state = -1;
		}
		MOP_unsafe = state == 1;
	}
	synchronized public final boolean MOP_unsafe() {
		return MOP_unsafe;
	}
	synchronized public final void reset() {
		state = 0;
		event = -1;

		MOP_unsafe = false;
	}
}

class HasNextAnalysis extends ForwardFlowAnalysis
{
	private UnitGraph graph;
	private ArraySparseSet emptySet=null;
	static public HashMap track = new HashMap();
	//HasNextMonitor_1 monitor = null;
	
	
	HasNextAnalysis(UnitGraph graph)
	{
		super(graph);
		this.graph = graph;
		doAnalysis();
	}

	protected void merge(Object in1, Object in2, Object out)
	{
		FlowSet inSet1 = (FlowSet) in1;
		FlowSet inSet2 = (FlowSet) in2;
		FlowSet outSet = (FlowSet) out;

		inSet1.union(inSet2, outSet);
	}

	protected void flowThrough(Object inValue, Object unit,
			Object outValue)
	{
		FlowSet in  = (FlowSet) inValue;
		FlowSet out = (FlowSet) outValue;
		Stmt    stm   = (Stmt)    unit;
		ArraySparseSet gen = new ArraySparseSet();
		
		if(stm instanceof AssignStmt){
			Value lhs = ((AssignStmt) stm).getLeftOp();
			Value rhs = ((AssignStmt) stm).getRightOp();
			
			if(rhs instanceof InterfaceInvokeExpr)
			{
				if(((InterfaceInvokeExpr)rhs).getType().toString().equals("java.util.Iterator"))
				{
					if(track.containsKey(((InterfaceInvokeExpr)rhs).getBase()));
					else
					{
						HasNextMonitor_1 hm1 = new HasNextMonitor_1();
						track.put(lhs, hm1);
						gen.add(hm1.state);
					}
				}
				else if(((InterfaceInvokeExpr)rhs).getMethod().getName().equals("hasNext"))
				{
					if(track.containsKey(((InterfaceInvokeExpr)rhs).getBase()))
							{
								HasNextMonitor_1 get =  (HasNextMonitor_1) track.get(((InterfaceInvokeExpr)rhs).getBase());
								get.hasnext(lhs);
								get.state_list.clear();
								gen.add(get.state);	
							}
				}
				else if(((InterfaceInvokeExpr)rhs).getMethod().getName().equals("next"))
				{
					if(track.containsKey(((InterfaceInvokeExpr)rhs).getBase()))
							{
								HasNextMonitor_1 get =  (HasNextMonitor_1) track.get(((InterfaceInvokeExpr)rhs).getBase());
								get.next(lhs);
								if(get.state_list.size()>=3)
								{
									gen.add(0);
									gen.add(1);
									gen.add(2);
								}
								else
								{
									gen.add(get.state);
									//System.out.println(((InterfaceInvokeExpr)rhs).getBase()+""+get.state);
								}
								
							}
					else
					{
						HasNextMonitor_1 hm1 = new HasNextMonitor_1();
						track.put(((InterfaceInvokeExpr)rhs).getBase(), hm1);
						hm1.next(((InterfaceInvokeExpr)rhs).getBase());
						gen.add(hm1.state);
					}
					//System.out.println("1"+stm+gen);
				}
				else
				{
					//write code for any other function
				}
			}
				
			else
			{
				//do nothing
			}					
			gen.union(out, out);
			//System.out.println("1"+gen);
		 }	//end complete assignment statement
		
		
		else if(stm instanceof IdentityStmt){
			//System.out.println("Identity "+stm);
			Value lhs = ((IdentityStmt) stm).getLeftOp();
			Value rhs = ((IdentityStmt) stm).getRightOp();
			
			if( rhs.getType().toString().equals("java.util.Iterator"))
				{
					if(track.containsKey(lhs));
					else
					{
						HasNextMonitor_1 hm1=new HasNextMonitor_1();
						hm1.state_list.add(0);
						hm1.state_list.add(1);
						hm1.state_list.add(2);
						track.put(lhs, hm1);
						gen.add(0);
						gen.add(1);
						gen.add(2);
					}
				}
			gen.union(out, out);
			//System.out.println("2"+gen);
		}	//end identity statement
				
		else if(stm instanceof InvokeStmt){
			InvokeExpr val = stm.getInvokeExpr();
			if(val.getMethod().getName().equals("next"))
			{
				if(track.containsKey(((InterfaceInvokeExpr)val).getBase()))
					if(track.containsKey(((InterfaceInvokeExpr)val).getBase()))
					{
						HasNextMonitor_1 get =  (HasNextMonitor_1) track.get(((InterfaceInvokeExpr)val).getBase());
						get.next(((InterfaceInvokeExpr)val).getBase());
						if(get.state_list.size()>=3)
						{
							gen.add(0);
							gen.add(1);
							gen.add(2);
						}
						else
						{
							gen.add(get.state);
						}
						
					}
				//System.out.println("2"+stm+gen);
			}
			else if(val.getMethod().getName().equals("hasNext"))
			{
				
				if(track.containsKey(((InterfaceInvokeExpr)val).getBase()))
					{
						HasNextMonitor_1 get =  (HasNextMonitor_1) track.get(((InterfaceInvokeExpr)val).getBase());
						get.hasnext(((InterfaceInvokeExpr)val).getBase());
						get.state_list.clear();
						gen.add(get.state);	
					}
			}
			else
			{
				Iterator iter2 = val.getArgs().iterator();
				while(iter2.hasNext())
				{
					Value value = (Value) iter2.next();
					if( value.getType().toString().equals("java.util.Iterator"))
					{
						if(track.containsKey(value))
						{
							HasNextMonitor_1 hm1 = (HasNextMonitor_1) track.get(value);
							hm1.state_list.add(0);
							hm1.state_list.add(1);
							hm1.state_list.add(2);
						}	//end inner if
					}	//end if
				}	//end while
			}	//end else
			gen.union(out, out);
			//System.out.println("3"+gen);
		}
		
		gen.union(out,out);
		//System.out.println(track);
	}	//end function flowThrough

	protected void copy(Object source, Object dest)
	{
		FlowSet sourceSet = (FlowSet) source;
		FlowSet destSet   = (FlowSet) dest;

		sourceSet.copy(destSet);
	}

	protected Object entryInitialFlow()
	{
		return new ArraySparseSet();
	}

	protected Object newInitialFlow()
	{
		return new ArraySparseSet(); 		
	}
}
