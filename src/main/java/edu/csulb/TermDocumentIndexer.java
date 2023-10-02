package edu.csulb;

import cecs429.documents.DirectoryCorpus;
import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.indexing.Index;
import cecs429.indexing.Posting;
import cecs429.indexing.TermDocumentIndex;
import cecs429.text.BasicTokenProcessor;
import cecs429.text.EnglishTokenStream;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;

public class TermDocumentIndexer {
	public static void main(String[] args) {
		// Create a DocumentCorpus to load .txt documents from the project directory.
		DocumentCorpus corpus = DirectoryCorpus.loadTextDirectory(Paths.get("").toAbsolutePath(), ".txt");
		// Index the documents of the corpus.
		Index index = indexCorpus(corpus) ;

		// We aren't ready to use a full query parser; for now, we'll only support single-term queries.
//		String query = "whale"; // hard-coded search for "whale"


		// Takes user input and queries for word. Enter "quit" to stop.
		Scanner obj = new Scanner(System.in);
		while (true) {
			System.out.print("Enter a word in lowercase to be queried: ");
			String query = obj.nextLine();

			if (query.equals("quit")){
				break;
			}

			for (Posting p : index.getPostings(query)) {
				System.out.println(query + " found in " + corpus.getDocument(p.getDocumentId()).getTitle());
			}
		}

		// TODO: fix this application so the user is asked for a term to search.

	}
	
	private static Index indexCorpus(DocumentCorpus corpus) {
		HashSet<String> vocabulary = new HashSet<>();
		BasicTokenProcessor processor = new BasicTokenProcessor();
		
		// First, build the vocabulary hash set.
		for (Document d : corpus.getDocuments()) {
			System.out.println("Found document " + d.getTitle());
			// TODO:
			// Tokenize the document's content by constructing an EnglishTokenStream around the document's content.
			// Iterate through the tokens in the document, processing them using a BasicTokenProcessor,
			//		and adding them to the HashSet vocabulary.
			EnglishTokenStream tokens = new EnglishTokenStream(d.getContent());

			// Iterates through each token and processes them while adding to vocabulary
			for (String s : tokens.getTokens()){
				vocabulary.add(processor.processToken(s));
			}

		}
		
		// TODO:
		// Constuct a TermDocumentMatrix once you know the size of the vocabulary.
		// THEN, do the loop again! But instead of inserting into the HashSet, add terms to the index with addPosting.
		TermDocumentIndex matrix = new TermDocumentIndex(vocabulary, corpus.getCorpusSize());
		for (Document d : corpus.getDocuments()) {
			EnglishTokenStream tokens = new EnglishTokenStream(d.getContent());

			// Iterates through each processed token and adds them to the matrix
			for (String s : tokens.getTokens()){
				matrix.addTerm(processor.processToken(s), d.getId());
			}

		}
		
		return matrix;
	}
}
