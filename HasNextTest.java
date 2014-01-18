import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class HasNextTest {
	
	public Iterator<Integer> git;

	public static void main(String[] args) {
		Collection<Integer> container = new HashSet<Integer>();
		Collection<Integer> copyContainer = new HashSet<Integer>();

		for(int i = 0; i<10; i++)
		{
			container.add(i);
			copyContainer.add(i);
		}	
		
		HasNextTest hnt = new HasNextTest();
		hnt.f1(container);
		hnt.f5(container, copyContainer);
		hnt.f6();
	}
	
	private void f1(Collection<Integer> c){
		Iterator<Integer> it1 = c.iterator();
		int x = it1.next();
		while(it1.hasNext()){
			x = it1.next();
			if(it1.hasNext()){
				int y = it1.next();
				if(x > y){
					f2(it1, x);
				}else{
					f3(it1, y);
				}
			}
		}
	}
	
	private void f2(Iterator<Integer> it2, int i){
		int j = it2.next();
		if(i > j){
			if(it2.hasNext())
				System.out.println(it2.next());
		} else {
			System.out.println(it2.next());
		}				
	}
	
	private void f3(Iterator<Integer> it3, int i){
		if(i > 100){
			it3.hasNext();
		}
		System.out.println(it3.next());
		if(it3.hasNext()){
			f4(it3);
			it3.next();
		}
	}
	
	private void f4(Iterator<Integer> it4){
		while(it4.hasNext()){
			if(it4.next()>200)
				System.out.println("Greater than 200");
		}
	}
	
	
	private void f5(Collection<Integer> c1, Collection<Integer> c2){
		Iterator<Integer> it5 = c1.iterator();
		Iterator<Integer> it6 = c2.iterator();

		while(it5.hasNext() && it6.hasNext()){
			if(it5.next() > it6.next())
				System.out.println("First collection greater than the second.");
			else 
				System.out.println("Second collection greater than the first.");
		}
	}
	
	private void f6(){
		int i = git.next();
		if(i>100)
				System.out.println("Greater than 100");
	}
}
