package cecs429.indexing;

import java.io.*;
import java.util.List;
import java.sql.*;

public class DiskIndexWriter {
    public void writeIndex(String filePath, Index index) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");

        // Initialize database
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:diskWriter.db");
        } catch ( Exception e ) {
            System.exit(0);
        }
        System.out.println("Db connected.");

        // Create table if it was not already created
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS vocabularyTable (term TEXT PRIMARY KEY, diskPosition LONG)";
            stmt.executeUpdate(sql);
        }catch ( Exception e ) {
            System.exit(0);
        }

        try {
            randomAccessFile.seek(0);

            List<String> vocabulary = index.getVocabulary();
            for (String s : vocabulary) {
                //Records term and the current 8 byte position
                try {
                    long bytePosition = randomAccessFile.getFilePointer();
                    insertTable(c, s, bytePosition);
                }
                catch ( Exception e ) {
                    System.exit(0);
                }


            // Writes Dft
                randomAccessFile.writeInt(index.getPostings(s).size());

                for (int i = 0; i < index.getPostings(s).size(); i++) {
                    // Writes id
                    if (i == 0) {
                        //writes first doc id
                        randomAccessFile.writeInt(index.getPostings(s).get(i).getDocumentId());
                    }
                    else{
                        //writes doc id in gap form
                        randomAccessFile.writeInt(index.getPostings(s).get(i).getDocumentId() - index.getPostings(s).get(i-1).getDocumentId());
                    }
                    // Writes tf t,d
                    randomAccessFile.writeInt(index.getPostings(s).get(i).getPositionList().size());
                    for (int j = 0; j < index.getPostings(s).get(i).getPositionList().size(); j++) {
                        // Writes first position of t
                        if (j == 0){
                            randomAccessFile.writeInt(index.getPostings(s).get(i).getPositionList().get(j));
                        }
                        // Writes jth position of t in gap form
                        else{
                            randomAccessFile.writeInt(index.getPostings(s).get(i).getPositionList().get(j) - index.getPostings(s).get(i).getPositionList().get(j-1));
                        }

                    }
                }

            }
            try {
                randomAccessFile.close();
                stmt.close();
                c.close();
            }
            catch ( Exception e ) {
                System.exit(0);
            }
        }
        catch (IOException error){
            System.out.println("Error writing file");
        }

    }

    // Inserts a term and disk position into table
    public void insertTable(Connection c, String term, long diskPosition) throws SQLException {
        String sql = "INSERT INTO vocabularyTable (term, diskPosition) VALUES (?, ?)";
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, term);
            stmt.setLong(2, diskPosition);
            stmt.executeUpdate();
        }
        catch ( Exception e ) {
            System.exit(0);
        }
    }
}
