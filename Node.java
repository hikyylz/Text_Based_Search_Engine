
public class Node<T,V> {
	
	private T keyWord;
	private HashedDictionary<V> myHDtable;
	
	Node(T key, V fileName){
		this.keyWord = key;
		myHDtable = new HashedDictionary<V>();
		myHDtable.add(fileName);
	}
	

	public HashedDictionary<V> getMyHDtable() {
		return myHDtable;
	}

	public void setMyHDtable(HashedDictionary<V> myHDtable) {
		this.myHDtable = myHDtable;
	}

	public T getKeyWord() {
		return keyWord;
	}

	
	
	
	
	
}
