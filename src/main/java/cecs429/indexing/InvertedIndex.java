package cecs429.indexing;
import java.util.*;

public class InvertedIndex implements Index{
    private HashMap<String,List<Posting>> invertedIndex = new HashMap<>();

    public InvertedIndex() {
    }

    /**
     * Adds term to hashmap in O(1) by appending any new document id's at the end of List<Posting>
     */
    public void addTerm(String term, int documentId) {
        List<Posting> list = new ArrayList<>();
        // Checks if hashmap is empty and will initialize it
        if (invertedIndex.isEmpty() || invertedIndex.get(term) == null){
            Posting p = new Posting(documentId);
            list.add(p);
            invertedIndex.put(term, list);
        }

        // Checks the last index of list of postings to see if the document id matches
        // If the document ids don't match, then that means we aren't adding a duplicate document id to achieve O(1)
        else if (invertedIndex.get(term).get(invertedIndex.get(term).size() - 1).getDocumentId() != documentId){
            Posting p = new Posting(documentId);
            invertedIndex.get(term).add(p);
        }

    }

    /**
     *  Gets hashmap's value at term
     */
    @Override
    public List<Posting> getPostings(String term) {
        return invertedIndex.get(term);
    }

    /**
     * Creates new vocabulary list from hashmap's keys and sorts them
     */
    public List<String> getVocabulary() {
        List<String> vocabulary = new ArrayList<>(invertedIndex.keySet());
        Collections.sort(vocabulary);
        return Collections.unmodifiableList(vocabulary);
    }

}
