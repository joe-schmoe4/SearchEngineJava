package cecs429.indexing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PositionalInvertedIndex implements Index{
    private HashMap<String, List<Posting>> positionalIndex = new HashMap<>();

    /**
     * Adds term to hashmap in O(1) by appending any new document id's at the end of List<Posting>
     */
    public void addTerm(String term, int documentId, int position) {
        List<Posting> list = new ArrayList<>();
        // Checks if hashmap is empty and will initialize it
        if (positionalIndex.isEmpty() || positionalIndex.get(term) == null){
            Posting p = new Posting(documentId, position);
            list.add(p);
            positionalIndex.put(term, list);
        }
        // Checks the last index of list of postings to see if the document id matches
        // If the document ids don't match, then add new posting in index O(1).
        else if (positionalIndex.get(term).get(positionalIndex.get(term).size() - 1).getDocumentId() != documentId){
            Posting p = new Posting(documentId, position);
            positionalIndex.get(term).add(p);
        }

        // Document id and term match, therefore append current position to posting's position list. O(1)
        else{
            positionalIndex.get(term).get(positionalIndex.get(term).size() - 1).getPositionList().add(position);
        }

    }

    /**
     *  Gets hashmap's value at term
     */
    @Override
    public List<Posting> getPostings(String term) {
        return positionalIndex.get(term);
    }

    /**
     * Creates new vocabulary list from hashmap's keys and sorts them
     */
    public List<String> getVocabulary() {
        List<String> vocabulary = new ArrayList<>(positionalIndex.keySet());
        Collections.sort(vocabulary);
        return Collections.unmodifiableList(vocabulary);
    }
}
