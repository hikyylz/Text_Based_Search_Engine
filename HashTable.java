import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HashTable<T,V> {
	
	private static int initial_capacity=2500;
	private static int current_capaity;
	private static int nbrOfEnrties=0;
	@SuppressWarnings("rawtypes")
	private static Node[] hashTable;
	private static double load_factor = 0.5;
	private Map<Character, Integer> ABC = new HashMap<Character, Integer>(); //kelimelere hashcode oluştuturken kullandım bunu sadece, prepareASCI() içinde.
	
	
	HashTable(){
		this(initial_capacity);
	}
	
	HashTable(int capasity){
		
		prepareASCI();
		current_capaity = capasity;
		hashTable = new Node[capasity];
		
	}
	
	ArrayList<String> getCommonFiles(T key1, T key2, T key3) {
		ArrayList<String> commonFiles= new ArrayList<String>();
		boolean isFileNameAdded;
		
		HashedDictionary<?> dic1 = hashTable[getIndex(key1)].getMyHDtable();   // wildcard ?
		HashedDictionary<?> dic2 = hashTable[getIndex(key2)].getMyHDtable();
		HashedDictionary<?> dic3 = hashTable[getIndex(key3)].getMyHDtable();
		
		int sizeOfdic1=dic1.getSizeOfHDtable();
		int sizeOfdic2=dic2.getSizeOfHDtable();
		int sizeOfdic3=dic3.getSizeOfHDtable();
		
		for(int i=1; i<sizeOfdic1 ; i++) {
			isFileNameAdded=false;
			
			if(!dic1.isIndexNull(i)) {
				Object fileName = dic1.getFileNameAt(i);
				
				for(int j=1; j<sizeOfdic2; j++) {
					
					if(!dic2.isIndexNull(j) && fileName.equals( dic2.getFileNameAt(j) )) {
						
						for(int k=1; k<sizeOfdic3; k++) {
							
							if(!dic3.isIndexNull(k) && fileName.equals( dic3.getFileNameAt(k) )) {
								commonFiles.add((String) fileName);
								isFileNameAdded=true;
								break;
							}
							
						}
						if(isFileNameAdded) {
							break;
						}
						
					}
					
				}
				
				
			}
			
		}
		return commonFiles;
	}
	
	@SuppressWarnings("unchecked")
	int getOccuranceInthisFile(T key, V whichFile) {
		int index = getIndex(key);
		return hashTable[index].getMyHDtable().getValueOf(whichFile);
	}
	
	int findSearchWord(T key) {
		int index = hashCode(key);
		int doublHashingResult = doubleHashing(index);   // keep this nbr in case we need it.
		int turn=1;
		
		do {
			if(hashTable[index]==null) {
				// aranan search word hashtable ıma hiç eklenmemiş demek.
				return -1;
			}else {
				if(hashTable[index].getKeyWord().equals(key)) {
					return index;
				}else {
					index += turn*doublHashingResult;
					index = index%current_capaity;
					turn++;
					continue;
				}
			}
		}while(true);
		
	}
	
	int getCollisionNbr(T key) {
		int collisionCount=0;
		if(key==null) {
			return 0;
		}
		int index = hashCode(key);
		int doublHashingResult = doubleHashing(index);   // keep this nbr in case we need it.
		int turn=1;
		do {
			if(hashTable[index]==null) {
				// aranan search word hashtable ıma hiç eklenmemiş demek.
				return 0;
			}else {
				if(hashTable[index].getKeyWord().equals(key)) {
					return collisionCount;
				}else {
					index += turn*doublHashingResult;
					index = index%current_capaity;
					turn++;
					collisionCount++;
					continue;
				}
			}
		}while(true);
	}
	
	int getIndex(T key) {
		int index = hashCode(key);
		int doublHashingResult = doubleHashing(index);   // keep this nbr in case we need it.
		int turn=1;
		
		do {
			if(hashTable[index]==null) {
				// sıkıntı var. hashtable da olmayan kelimeyi soruyor.
				System.out.println("please type an query which has words in hashtable ! !");
				System.exit(0);
				
			}else {
				if(hashTable[index].getKeyWord().equals(key)) {
					return index;
				}else {
					index += turn*doublHashingResult;
					index = index%current_capaity;
					turn++;
					continue;
				}
			}
		}while(true);
		
	}
	
	
	@SuppressWarnings("rawtypes")
	void display() {
		for(Node node:hashTable) {
			if(node!=null) {
				System.out.print( node.getKeyWord() + " " );
				node.getMyHDtable().display();
			}
		}
	}
	
	
	
	@SuppressWarnings("rawtypes")
	void resizeHT() {
		Node[] tempHT = new Node[current_capaity*2];
 		for(int i=0; i<current_capaity; i++) {
			tempHT[i] = hashTable[i];
		}
		hashTable = tempHT;
		current_capaity = current_capaity*2;
	}
	
	
	@SuppressWarnings("unchecked")
	void add(T word, V whichFile) {
		
		// is resizing needed?
		if(nbrOfEnrties >= (int)(current_capaity*load_factor)) {
			resizeHT();
		}
		
		boolean isWordSet = false;
		nbrOfEnrties+=1;
		int index = hashCode(word);
		int doublHashingResult = doubleHashing(index);   // keep this nbr in case we need it.
		int turn=1;
		
		do {
			if( hashTable[index]!=null) {
				
				if(hashTable[index].getKeyWord().equals( word )) {
					hashTable[index].getMyHDtable().add(whichFile);
					isWordSet=true;
				}else {
					// yeni bir index bul ekleyeceğin bu word için .
					index += turn*doublHashingResult;
					index = index%current_capaity;
					turn++;
					continue;
				}
				
			}else {
				Node<T, V> newnode=new Node<T, V>(word, whichFile);
				hashTable[index]= newnode;
				isWordSet=true;
			}
		}while(!isWordSet);
		
	}

	int hashCode(T word) {
		return simpSumFunc(word) % current_capaity;
	}
	
	
	// hash functions 2.
	int simpSumFunc(T word) {
		int index=0;
		
		CharacterIterator it = new StringCharacterIterator((String) word);
		while (it.current() != CharacterIterator.DONE) {
			index += ABC.get( it.current() );
			it.next();
		}
		
		return index ;
	}
	
	int polAccuFunc(T word) {
		int z=7;
		int index=0;
		int wordLentgh=((String) word).length();
		int constant=1;
		
		CharacterIterator it = new StringCharacterIterator((String) word);
		while (it.current() != CharacterIterator.DONE) {
			index += ( ABC.get(it.current()) * ( Math.pow(z ,(wordLentgh - constant) ) ) ); // index e long uzunlukta kelime eklerken sıkıntı çıkarsa canı cehenneme diyip...
			index = index%2500;
			constant +=1;
			it.next();
		}
		
		return index;
	}
	
	// Collision handling 2 .
	int linProbbing(int index) {
		return index+1 % current_capaity;
	}
	
	int doubleHashing(int index) {
		return ( 31 - (index%31) );
	}
	
	
	
	void prepareASCI() {
		
		ABC.put('a', 1);
		ABC.put('b', 2);
		ABC.put('c', 3);
		ABC.put('d', 4);
		ABC.put('e', 5);
		ABC.put('f', 6);
		ABC.put('g', 7);
		ABC.put('h', 8);
		ABC.put('i', 9);
		ABC.put('j', 10);
		ABC.put('k', 11);
		ABC.put('l', 12);
		ABC.put('m', 13);
		ABC.put('n', 14);
		ABC.put('o', 15);
		ABC.put('p', 16);
		ABC.put('q', 17);
		ABC.put('r', 18);
		ABC.put('s', 19);
		ABC.put('t', 20);
		ABC.put('u', 21);
		ABC.put('v', 22);
		ABC.put('w', 23);
		ABC.put('x', 24);
		ABC.put('y', 25);
		ABC.put('z', 26);
		
	}
	

}
