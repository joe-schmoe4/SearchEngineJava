//package cecs429.indexing;
//
//import java.io.*;
//import java.util.List;
//
//public class DiskIndexWriter {
//    public void writeIndex(String filePath, Index index) throws IOException {
//        RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
//        FileOutputStream fileOutput = new FileOutputStream(filePath);
//        DataOutputStream dataOutput = new DataOutputStream(fileOutput);
//
//        List<String> vocabulary = index.getVocabulary();
//        for (String s : vocabulary){
//            // writes first Dft
//            dataOutput.writeInt(index.getPostings(s).size());
//
//            for (int i=0; i<index.getPostings(s).size(); i++) {
//                dataOutput.writeInt(index.getPostings(s).get(i).getDocumentId());
//                dataOutput.writeInt(index.getPostings(s).get(i).getPositionList().size());
//                dataOutput.writeInt(index.getPostings(s).get(i).getPositionList().get());
//
//
//            }
//        }
//
//    }
//}
