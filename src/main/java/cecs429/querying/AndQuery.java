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
			List<Posting> list1 = index.getPostings(mComponents.get(i).toString());
			List<Posting> list2 = index.getPostings(mComponents.get(i+1).toString());


			int index1 = 0;
			int index2 = 0;
			// Uses two pointer algorithm where an index counter will advance only if the doc id of a list is smaller than the other
			while (true){
				if (list1.get(index1).getDocumentId() > list2.get(index2).getDocumentId()){
					index2 += 1;
				}
				else if (list1.get(index1).getDocumentId() < list2.get(index2).getDocumentId()){
					index1 += 1;
				}
				// Both doc ids are equal therefore add it to result list and advance both index counters
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
		return
		 String.join(" AND ", mComponents.stream().map(c -> c.toString()).collect(Collectors.toList()));
	}
}
