package cecs429.indexing;

import java.util.ArrayList;
import java.util.List;

/**
 * A Posting encapulates a document ID associated with a search query component.
 */
public class Posting {
	private int mDocumentId;
	private List<Integer> positionList = new ArrayList<>();
	public Posting(int documentId, int position) {
		mDocumentId = documentId;
		positionList.add(position);
	}
	
	public int getDocumentId() {
		return mDocumentId;
	}

	// Returns position list (Used for appending position at end of posting's list in PositionalInvertedIndex)
	public List<Integer> getPositionList() {
		return positionList;
	}
}
