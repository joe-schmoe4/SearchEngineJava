package cecs429.querying;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cecs429.indexing.Index;
import cecs429.indexing.Posting;

/**
 * An AndQuery composes other QueryComponents and merges their postings in an intersection-like operation.
 */
public class AndQuery implements QueryComponent {
	private List<QueryComponent> mComponents;
	

	public AndQuery(List<QueryComponent> components) {
		mComponents = components;
	}
	
	public List<QueryComponent> getComponents() {
		return mComponents;
	}
	
	@Override
	public List<Posting> getPostings(Index index) {
		List<Posting> result = new ArrayList<>();

		for (int i=0; i<mComponents.size()-1; i++){
			List<Posting> list1 = mComponents.get(i).getPostings(index);
			List<Posting> list2 = mComponents.get(i+1).getPostings(index);

			int index1 = 0;
			int index2 = 0;

			// If it gets past the first iteration, that means there are more query components. Clear list1 and result and have it store the previous result of intersection.
			if (i>0){
				list1.clear();
				list1.addAll(result);
				result.clear();
			}
			// Uses two pointer algorithm where an index counter will advance only if the doc id of a list is smaller than the other
			while (true){

				if (list1.get(index1).getDocumentId() > list2.get(index2).getDocumentId()){
					index2 += 1;
					// Once either index counter is equal to list size, then intersection is complete
					if (index1 == list1.size() || index2 == list2.size()){
						break;
					}
				}
				else if (list1.get(index1).getDocumentId() < list2.get(index2).getDocumentId()){
					index1 += 1;
					// Once either index counter is equal to list size, then intersection is complete
					if (index1 == list1.size() || index2 == list2.size()){
						break;
					}
				}
				// Both doc ids are equal therefore add it to result list and advance both index counters
				else{
					result.add(list1.get(index1));
					index1 += 1;
					index2 += 1;

					// Once either index counter is equal to list size, then intersection is complete
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
		return
		 String.join(" AND ", mComponents.stream().map(c -> c.toString()).collect(Collectors.toList()));
	}
}
