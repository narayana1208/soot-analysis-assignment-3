import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class testcase1 {
public static void main(String[] args) {
Collection<String> c = new ArrayList<String>();
c.add("lalala");
c.add("tatata");
Iterator it1 = c.iterator();
while(it1.hasNext())
{
	it1.next();
	it1.next();
}
removeLalala(c);
System.err.println(c);
System.exit(0);
}

private static void removeLalala(Collection<String> c) {
for (Iterator<String> i = c.iterator(); i.hasNext();) {
String s = i.next();
if(s.equals("lalala")) {
c.remove(s);
}
}
}
}