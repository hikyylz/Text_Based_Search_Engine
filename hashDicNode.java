
public class hashDicNode<T> {

	private T whichFile;
	private int occuranceNbr=1;
	
	hashDicNode(T whichFile){
		this.whichFile = whichFile;
	}
	
	int getoccuranceNbr() {
		return occuranceNbr;
	}
	
	void setOccurance() {
		occuranceNbr=occuranceNbr+1;
	}
	
	T getWhichFile() {
		return whichFile;
	}
}
