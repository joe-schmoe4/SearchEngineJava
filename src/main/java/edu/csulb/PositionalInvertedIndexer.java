package edu.csulb;

import cecs429.documents.DirectoryCorpus;
import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.indexing.DiskIndexWriter;
import cecs429.indexing.Index;
import cecs429.indexing.PositionalInvertedIndex;
import cecs429.indexing.Posting;
import cecs429.querying.BooleanQueryParser;
import cecs429.querying.QueryComponent;
import cecs429.text.EnglishTokenStream;
import cecs429.text.TokensProcessor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class PositionalInvertedIndexer {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Scanner obj = new Scanner(System.in);

        long start = System.currentTimeMillis();
        // Create a DocumentCorpus to load .txt or .json documents from the project directory.


        System.out.print("Input: \"txt\" or \"json\": ");
        String input = obj.nextLine();

        DocumentCorpus corpus = null;
        if (input.equals("txt")){
            corpus = DirectoryCorpus.loadTextDirectory(Paths.get("").toAbsolutePath(), ".txt");
        }
        else if (input.equals("json")){
            corpus = DirectoryCorpus.loadTextDirectory(Paths.get("/home/joe/IdeaProjects/searchengine/nps-sites").toAbsolutePath(), ".json");
        }

        // Index the documents of the corpus.
        Index index = indexCorpus(corpus);

        long finish = System.currentTimeMillis();
        System.out.println("Finished processing!");
        System.out.println("Time took: " + (finish - start)*.001 + " seconds");
        // Takes user input and queries for word. Enter "quit" to stop.
        while (true) {
            BooleanQueryParser parser = new BooleanQueryParser();
            System.out.print("Enter a word in lowercase to be queried: ");
            String query = obj.nextLine();

            if (query.equals("quit")){
                break;
            }

            query = query.toLowerCase();

            QueryComponent parsed = parser.parseQuery(query);

            int foundCount = 0;
            for (Posting p : parsed.getPostings(index)) {
                System.out.println(query + " found in Title: " + corpus.getDocument(p.getDocumentId()).getTitle() + ",   Doc id: " +  p.getDocumentId());
                foundCount += 1;
            }
            System.out.println(query + " found " + foundCount + " times!");

        }
    }

    private static Index indexCorpus(DocumentCorpus corpus) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        TokensProcessor processor = new TokensProcessor();

        PositionalInvertedIndex index = new PositionalInvertedIndex();
        int position = 1;

        for (Document d : corpus.getDocuments()) {

            System.out.println("Found document " + d.getTitle());
            // Tokenize the document's content by constructing an EnglishTokenStream around the document's content.
            // Iterate through the tokens in the document, processing them using the new tokens processor
            EnglishTokenStream tokens = new EnglishTokenStream(d.getContent());

            // Iterates through each token, process them and add to the index while increasing the position of each term
            for (String s : tokens.getTokens()){
                for (String p: processor.processToken(s)) {
                    index.addTerm(processor.normalizeType(p), d.getId(), position);
                    position += 1;
                }
            }
            // Reset position counter for next document
            position = 1;

        }
        return index;
    }

}
