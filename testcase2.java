import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


public class testcase2 {
	public static void main(String ar[])
	{
		Collection<Integer> container = new HashSet<Integer>();
		for(int i = 0; i<10; i++)
		{
			container.add(i);
		}
		Iterator it1 = container.iterator();
		while(it1.hasNext())
		{
			it1.next();
		}
	}

}
