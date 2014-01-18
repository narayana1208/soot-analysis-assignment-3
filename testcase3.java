import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


public class testcase3 {
	public static void main(String ar[])
	{
		Collection<Integer> container = new HashSet<Integer>();
		for(int i = 0; i<10; i++)
		{
			container.add(i);
		}
		Iterator it2 = container.iterator();
		while(it2.hasNext())
		{
			it2.next();
			it2.next();
		}
	}

}
