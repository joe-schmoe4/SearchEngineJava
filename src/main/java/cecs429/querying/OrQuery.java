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
		List<Posting> result = new ArrayList<>();

		for (int i=0; i<mComponents.size()-1; i++){
			List<Posting> list1 = index.getPostings(mComponents.get(i).toString());
			List<Posting> list2 = index.getPostings(mComponents.get(i+1).toString());

			int index1 = 0;
			int index2 = 0;

			// If it gets past the first iteration, that means there are more query components. Clear list1 and result and have it store the previous result of union.
			if (i>0){
				list1.clear();
				list1.addAll(result);
				result.clear();
			}
			while (true){
				if (list1.get(index1).getDocumentId() < list2.get(index2).getDocumentId()){
					result.add(list1.get(index1));
					index1 += 1;
					// Once either index counter is equal to list size, then add rest of postings from other sublist and union is complete
					if (index1 == list1.size()){
						List<Posting> subList1 = list1.subList(index1, list1.size());
						result.addAll(subList1);
						break;
					}
				}
				else if (list1.get(index1).getDocumentId() > list2.get(index2).getDocumentId()){
					result.add(list2.get(index2));
					index2 += 1;

					// Once either index counter is equal to list size, then add rest of postings from other sublist and union is complete
					if (index2 == list2.size()){
						List<Posting> subList2 = list2.subList(index2, list2.size());
						result.addAll(subList2);
						break;

					}
				}
				// Both doc ids are equal therefore add it to result list and advance both index counters
				else{
					result.add(list1.get(index1));
					index1 += 1;
					index2 += 1;

					// Once either index counter is equal to list size, then add rest of postings from other sublist and union is complete
					if (index1 == list1.size()){
						List<Posting> subList1 = list1.subList(index1, list1.size());
						result.addAll(subList1);
						break;
					}

					else if (index2 == list2.size()){
						List<Posting> subList2 = list2.subList(index2, list2.size());
						result.addAll(subList2);
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
