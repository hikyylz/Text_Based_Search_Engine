
public class HashedDictionary<T> {

	@SuppressWarnings("rawtypes")
	public hashDicNode[] hashedDicTable;
	private static int initial_capasity=101;  // en kötü senaryoda her txt de geçen bir kelime varsa bu 100 index i de kullanırım.
	
	HashedDictionary(){
		this(initial_capasity);
	}
	
	
	HashedDictionary(int capasity){
		hashedDicTable = new hashDicNode[capasity];
	}
	
	int getValueOf(T whichFile) {
		int index = hashCode(whichFile);
		return hashedDicTable[index].getoccuranceNbr();
	}
	
	@SuppressWarnings("unchecked")
	T getFileNameAt(int index) {
		return (T) hashedDicTable[index].getWhichFile();
	}
	
	int getSizeOfHDtable() {
		return hashedDicTable.length;
	}
	
	boolean isIndexNull(int index) {
		return (hashedDicTable[index]==null);
	}
	
	void add(T whichFile) {
		int index = hashCode(whichFile);
		if(hashedDicTable[index]!=null) {
			hashedDicTable[index].setOccurance();
			
		}else {
			hashDicNode<T> newnode = new hashDicNode<T>(whichFile);
			hashedDicTable[index] = newnode;
		}
		
	}
	
	
	int hashCode(T whichFile) {
		return Integer.parseInt(((String) whichFile).substring(0, 3)); 
	}
	
	void display() {
		System.out.print("hashedDicT: ");
		for(hashDicNode<?> node: hashedDicTable) {
			if(node!=null) {
				System.out.print(node.getWhichFile()+", ");
			}
		}
        System.out.println();
	}
	
}
