package cecs429.querying;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cecs429.indexing.Index;
import cecs429.indexing.Posting;

/**
 * An OrQuery composes other QueryComponents and merges their postings with a union-type operation.
 */
public class OrQuery implements QueryComponent {
	// The components of the Or query.
	private List<QueryComponent> mComponents;
	
	public OrQuery(List<QueryComponent> components) {
		mComponents = components;
	}
	
	@Override
	public List<Posting> getPostings(Index index) {
		List<Posting> result =  new ArrayList<>();

		for (int i=0; i<mComponents.size()-1; i++){
			List<Posting> list1 = index.getPostings(mComponents.get(i).toString());
			List<Posting> list2 = index.getPostings(mComponents.get(i+1).toString());


			int index1 = 0;
			int index2 = 0;
			while (true){
				if (list1.get(index1).getDocumentId() > list2.get(index2).getDocumentId()){
					index2 += 1;
				}
				else if (list1.get(index1).getDocumentId() < list2.get(index2).getDocumentId()){
					index1 += 1;
				}
				else{
					result.add(list1.get(index1));
					index1 += 1;
					index2 += 1;
					if (index1 == list1.size() || index2 == list2.size()){
						break;
					}
				}
			}
		}


		return result;
	}
	
	@Override
	public String toString() {
		// Returns a string of the form "[SUBQUERY] + [SUBQUERY] + [SUBQUERY]"
		return "(" +
		 String.join(" OR ", mComponents.stream().map(c -> c.toString()).collect(Collectors.toList()))
		 + " )";
	}
}
