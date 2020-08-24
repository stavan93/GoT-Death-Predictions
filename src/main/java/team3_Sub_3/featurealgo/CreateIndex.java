package team3_Sub_3.featurealgo;
import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreateIndex {

	public Map<String, Map<String,String>> pageParaMap = new HashMap<>();
	
	/**
	 * @return the pageParaMap
	 */
	public Map<String, Map<String, String>> getPageParaMap() {
		return pageParaMap;
	}

	/**
	 * @param pageParaMap the pageParaMap to set
	 */
	public void setPageParaMap(Map<String, Map<String, String>> pageParaMap) {
		this.pageParaMap = pageParaMap;
	}

	public Map<String, List<String>> createIndex() throws IOException {

		Map<String, List<String>> pageMap = new HashMap<String, List<String>>();
		
		String path = "../common-group-data/got.cbor";
		// String path = Constants.PARAGRAPHS_FILE;
		IndexWriter indexer = null;

		// Path indexLocation = Constants.INDEX_DIRECTORY_PATH;
		Path indexLocation = FileSystems.getDefault()
				.getPath("data", "paragraph.lucene");
		try {

			FSDirectory dir = FSDirectory.open(indexLocation);
			IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			indexer = new IndexWriter(dir, config);
		} catch (IOException e) {
			System.out.println("Index Writer Creation Failed " + e.getMessage());
			System.exit(10);
		}

		File f = new File(path);
		InputStream is = null;

		try {
			is = new BufferedInputStream(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			System.out.println("Cannot open  Test Data Directory " + e.getMessage());
			System.exit(11);
		}

		Iterable<Data.Page> iterableAnnotations = DeserializeData.iterableAnnotations(is);
		int docCount = 0;
		List<String> paraText;
		int total_docs = 0;
		for (Data.Page p : iterableAnnotations) {
			Map<String,String> paragraphMap = new HashMap<String,String>();
			paraText = new ArrayList<String>();
			String paragraph = "";
			Document doc = new Document();
			doc.add(new StringField(Constants.FIELD_PAGE_ID, p.getPageId(), Field.Store.YES));

			
			for (Data.Page.SectionPathParagraphs flatSectionPathsParagraphs : p.flatSectionPathsParagraphs()) {
//				Document doc = new Document();
//				doc.add(new StringField(Constants.FIELD_PARA_ID, flatSectionPathsParagraphs.getParagraph().getParaId(),
//						Field.Store.YES));
//				doc.add(new TextField(Constants.FIELD_CONTENTS, flatSectionPathsParagraphs.getParagraph().getTextOnly(),
//						Field.Store.YES));
//				indexer.addDocument(doc);
				paraText.add(flatSectionPathsParagraphs.getParagraph().getTextOnly());
				//System.out.println(flatSectionPathsParagraphs.getParagraph().getTextOnly());
				paragraph = paragraph.concat(flatSectionPathsParagraphs.getParagraph().getTextOnly());
				paragraphMap.put(flatSectionPathsParagraphs.getParagraph().getParaId(), flatSectionPathsParagraphs.getParagraph().getTextOnly());
			}
			doc.add(new TextField(Constants.FIELD_PAGE_CONTENT, paragraph, Field.Store.YES));
			indexer.addDocument(doc);
			GetCharacterName character = new GetCharacterName();
			List<String> characterNames = character.getActualNamesMap1();
			if (!pageMap.containsKey(p.getPageId())) {
				pageMap.put(p.getPageId(), paraText);
				pageParaMap.put(p.getPageId(), paragraphMap);
			}
		}
		if (indexer != null) {
			indexer.close();
		}
		System.out.println("Indexing Complete");
		return pageMap;
	}

	/**
	 * @param pageParaMap
	 * @param p
	 * @param paragraph
	 */
	public void pageParagraph(  Map<String,String> paragraphMap, Data.Page p) {
		pageParaMap.put(p.getPageId(), paragraphMap);
	}
}
