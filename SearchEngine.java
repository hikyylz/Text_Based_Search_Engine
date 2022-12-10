import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SearchEngine {
	
	ArrayList<String> puncs = new ArrayList<String>();
	File stopwordsFile = new File("stop_words_en.txt");
	Scanner stopwordsFilereader;
	ArrayList<String> stopWords= new ArrayList<String>();
	HashTable<String, String> myHashTable = new HashTable<String, String>();
	static HashedDictionary<String> filesWordsHDtable = new HashedDictionary<String>(); // hangi file da kaç kelime var bu bilgiyi tutacağım bu değişkende.
	String[] searchWords = new String[1000];
	long[] searchTimeArr = new long[1000];
	
	
	SearchEngine(){
		
		prepareStopWords();
		preparePuncs();
		fillHT();
		serachEngine();
		
		//prepareSearchWords();
		//measureTimeForSearchWords();
		//totalCollisionCount();
		
		
		
	}
	
	void totalCollisionCount(){
		int collisionCount=0;
		for(String searchWord: searchWords) {
			collisionCount += myHashTable.getCollisionNbr(searchWord);
		}
		System.out.println("Collision count: " + collisionCount);
	}
	
	
	void measureTimeForSearchWords() {
		
		int i=0;
		for(String searchWord: searchWords) {
			long startTime=System.nanoTime();
			myHashTable.findSearchWord(searchWord);
			long searchTime = System.nanoTime() - startTime;
			searchTimeArr[i]=searchTime;
			i++;
		}
		
		long maxST=0;
		long minST=9999999;
		long averageST=0;
		
		//averageST
		for(long t:searchTimeArr) {
			averageST +=t;
		}
		averageST = averageST/1000;
		System.out.println("averageST: " + averageST);
		
		
		// minST and maxST
		for(long t:searchTimeArr) {
			if(t<minST) {
				minST = t;
			}
			
			if(t>maxST) {
				maxST = t;
			}
		}
		System.out.println("minST: " + minST);
		System.out.println("maxST: " + maxST);
		
		
	}
	
	void prepareSearchWords() {
		File searchF = new File("search.txt");
		Scanner Fscn=null;
		try {
			 Fscn = new Scanner( searchF );
		} catch (FileNotFoundException e) {
			System.out.println("there is no left file can be read.");
			e.printStackTrace();
		}
		
		int i=0;
		while(Fscn.hasNextLine()) {
			searchWords[i]= Fscn.nextLine();
			i++;
		}
		
	}
	
	
	private void serachEngine() {
		String query = GetQuery();
		
		String[] queryWords = query.split(" ");
		ArrayList<String> commonFile = myHashTable.getCommonFiles(queryWords[0], queryWords[1], queryWords[2]);
		// common files da girilen 3 kelimenin geçtiği file isimlerini cillop gibi tutuyorum.
		
		double frequancyOfQuery;
		double maxFreq=0;
		Object mostRelevantFile="NONE";
		
		for(String fileName:commonFile) {
			int totalNbrOfWords = filesWordsHDtable.getValueOf( fileName );
			double pay=0;
			for(String word:queryWords) {
				pay += myHashTable.getOccuranceInthisFile(word ,fileName );
			}
			frequancyOfQuery= (pay/totalNbrOfWords);
			
			if(frequancyOfQuery > maxFreq) {
				maxFreq = frequancyOfQuery;
				mostRelevantFile = fileName;
			}
		}
		
		System.out.println("The most relevent file is "+mostRelevantFile);
		
	}
	
	@SuppressWarnings("resource")
	private String GetQuery() {
		Scanner scn = new Scanner(System.in); 
	    System.out.print("Please type your query that has three word :");
	    return scn.nextLine();  // Read user input
		
	}
	
	
	
	private void fillHT() {
		String fileName;
		
		for(int i=1; i<=100; i++) {
			if(i<10) {
				fileName= "00"+i+".txt";
			}else if(i<100) {
				fileName= "0"+i+".txt";
			}else {
				fileName= i+".txt";
			}
			
			readTxtFile(fileName);
			
		}
		
	}
	
	
	public void readTxtFile(String fileName) {
		
		String data=null;
	    Scanner fileReader = null;
	    
		try {
			fileReader = new Scanner( new File(fileName) );
		} catch (FileNotFoundException e) {
			System.out.println("there is no left file can be read.");
			e.printStackTrace();
		}
	    
		while (fileReader.hasNext()) {
			try {
				data = fileReader.next();
			}catch(NoSuchElementException e) {
				break;
			}
			// burda data kelimesinden puncs uzaklaştır.
			data = clearPuncFrom(data);
			if(data!="") {		
				//komple sayılardan oluşan kelimeler hiçlik oluyordu. Onları engelledim bu if yapısıyla.
				data=data.toLowerCase();
				
				if(!isDataStopWords(data)) {		
					// stopword değilse data mı HT a yerleştireceğim. burada
					
					filesWordsHDtable.add(fileName);
					myHashTable.add(data , fileName);
					
				}
			}
	    }
		
	    fileReader.close();
	}
	
	
	public void prepareStopWords() {
		
		try {
			stopwordsFilereader = new Scanner(stopwordsFile);
		} catch (FileNotFoundException e) {
			System.out.println("a problem occured while stopWordstxt processing");
			e.printStackTrace();
		}
		
		while(stopwordsFilereader.hasNextLine()) {
			stopWords.add( stopwordsFilereader.nextLine() );
			
			try {
	        	stopwordsFilereader.nextLine();  //iki satırda bir hiçlik var ya stopwordstxt sinde. o boş satırlardan atlamak için var bu satır.
	        }catch(NoSuchElementException e) {
	        	
	        }
		}
		stopwordsFilereader.close();
		
	}

	
	public boolean isDataStopWords(String data) {
		return stopWords.contains(data);
	}
	
	
	public String clearPuncFrom(String data) {
		for(String item:puncs) {
			if(data.contains(item)) { // true demek o punc silinmeli datadan.
				data=data.replace(item, "");
			}
		}
		return data;
	}
	
	
	public void preparePuncs() {
		puncs.add(",");
		puncs.add("[");
		puncs.add("]");
		puncs.add("{");
		puncs.add("}");
		puncs.add("(");
		puncs.add(")");
		puncs.add("!");
		puncs.add("'");
		puncs.add("+");
		puncs.add("%");
		puncs.add("&");
		puncs.add("/");
		puncs.add("*");
		puncs.add("?");
		puncs.add("=");
		puncs.add("|");
		puncs.add(":");
		puncs.add(".");
		puncs.add(";");
		puncs.add("÷");
		puncs.add("<");
		puncs.add(">");
		puncs.add("@");
		puncs.add("1");
		puncs.add("2");
		puncs.add("3");
		puncs.add("4");
		puncs.add("5");
		puncs.add("6");
		puncs.add("7");
		puncs.add("8");
		puncs.add("9");
		puncs.add("0");
		puncs.add("£");
		puncs.add("#");
		puncs.add("$");
		puncs.add("½");
		puncs.add("§");
		puncs.add("-");
		puncs.add("_");
		puncs.add("\\");
		puncs.add("\"");
	}

}
