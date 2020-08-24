package team3_Sub_3.featurealgo;

import org.apache.lucene.analysis.Analyzer;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Constants {

    public static final String FIELD_PARA_ID = "paragraphid";
    public static final String FIELD_PAGE_NAME = "pageid";
    public static final String FIELD_PAGE_ID = "pageid";
    public static final String FIELD_PAGE_CONTENT = "pagetext";
    public static final String FIELD_CONTENTS = "text";

    public static Analyzer ANALYZER;
    public static String PARAGRAPHS_FILE;
    public static Path INDEX_DIRECTORY_PATH;
    public static String CHARACTERS_DEATH_FILE;

    public static String QRELS_FILE_PATH;


    public static void setPaths(/*String dataFile,*/String paraFile, String indexDirectoryPath, String characters_death_file){
        CHARACTERS_DEATH_FILE = characters_death_file;
        PARAGRAPHS_FILE = paraFile;
        INDEX_DIRECTORY_PATH = FileSystems.getDefault().getPath(indexDirectoryPath, "paragraph.lucene");
    }

    public static void setAnalyzer(Analyzer analyzer){
        ANALYZER = analyzer;
    }

}
