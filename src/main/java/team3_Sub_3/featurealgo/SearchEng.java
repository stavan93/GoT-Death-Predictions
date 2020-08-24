package team3_Sub_3.featurealgo;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;

import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

public class SearchEng {
	// Using indexsearcher the index is searched and using queryparser the user
	// entered queries
	// are converted to lucene queries
	private static IndexSearcher searcher = null;
	private QueryParser parsetext = null;
	Scanner scan = new Scanner(System.in);

	public SearchEng() throws IOException {
		searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get("data/paragraph.lucene"))));
		parsetext = new QueryParser(Constants.FIELD_PAGE_CONTENT, new StandardAnalyzer());
	}

	// Performs search on the index and returns the search results
	public TopDocs performsearch(String str, int num) throws IOException, ParseException {
		Query query = parsetext.parse(str);
		return searcher.search(query, num);
	}

	// custom similarity function
	public void customsimilarity() {
		searcher.setSimilarity(new SimilarityBase() {
			public String toString() {
				return "Similarity Base";
			}

			public float score(BasicStats stats, float freq, float doclen) {
				return freq;
			}
		});
	}

	// BM25Similarity function
	public void bm25similarity() {
		searcher.setSimilarity(new BM25Similarity());
	}

	public Document getDocument(int docId) throws IOException {
		return searcher.doc(docId);
	}

	public Map<String, List<String>> displayresults(String s, Map<String, Entity> entityMap)
			throws ParseException, IOException {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		br = new BufferedReader(new FileReader("data/character-deaths.csv"));
		Map<String, List<String>> pageIdCharacterNamesMap = new HashMap<>();
		int lines_count = 0;
		while ((line = br.readLine()) != null) {

			if (lines_count > 0) {
				// use comma as separator
				String[] deaths_file = line.split(cvsSplitBy);
				// System.out.println("Character " + deaths_file[0]);

				// String searchString = deaths_file[0];
				String searchString = deaths_file[0];
				ArrayList<String> charName = new ArrayList<>();
				// LNCLTNSimilarity(searchString);
				// Using ScoreDocs we score the top 10 Documents from the search results
				BufferedWriter writer = new BufferedWriter(new FileWriter(s));
				// for(Data.Page page: DeserializeData.iterableAnnotations(fileInputStream3)) {
				// LNCLTNSimilarity(page.getPageName());
//        	System.out.println(page.getPageName());
				// System.out.println(searchString);
				TopDocs topDocs = performsearch(searchString, 10);
				// System.out.println("Total Hits: " + topDocs.totalHits);
				// System.out.println("Top 100 Results: "+page.getPageName());
				ScoreDoc[] hits = topDocs.scoreDocs;
				int count = 0;
				//System.out.println();
				if (hits.length > 0) {
					String pageID = getDocument(hits[0].doc).getField("pageid").stringValue();
					if (!pageIdCharacterNamesMap.containsKey(pageID)) {
						pageIdCharacterNamesMap.put(pageID, new ArrayList<>());
					}
				 // count++;
					pageIdCharacterNamesMap.get(pageID).add(searchString);
				} else {
					
				for(String pageId : entityMap.keySet())
				{
				if(pageId.contains(searchString)) {
					List<String> redirect = entityMap.get(pageId).getRedirectNames();
					List<String> anchor = entityMap.get(pageId).getAnchorTexts();
					if(!redirect.isEmpty() || !anchor.isEmpty())
						{
						if (!pageIdCharacterNamesMap.containsKey(pageId)) {
							pageIdCharacterNamesMap.put(pageId, new ArrayList<>());
						}
					 // count++;
						pageIdCharacterNamesMap.get(pageId).add(searchString);
						}
				}
				}
				}
				writer.close();
			}
			lines_count = lines_count + 1;
		}
//		Map<String, String> pageIdCharacterMap = new HashMap<>();
//		for (Map.Entry<String, List<String>> pageIdCharacter : pageIdCharacterNamesMap.entrySet()) {
//			List<String> names = entityMap.get(pageIdCharacter.getKey()).getRedirectNames();
//			String newSearchRedirectNames = "";
//			for (String name : names) {
//				newSearchRedirectNames = newSearchRedirectNames + name;
//			}
//			String searchQuery = pageIdCharacter.getValue() + " " + newSearchRedirectNames;
//			System.out.println("Searc" + " " + newSearchRedirectNames);
//			// + " " + entityMap.get(pageIdCharacter.getKey()).getAnchorTexts().get(0);
//
//			TopDocs topDocs = performsearch(searchQuery, 10);
//			// System.out.println("Total Hits: " + topDocs.totalHits);
//			// System.out.println("Top 100 Results: "+page.getPageName());
//			ScoreDoc[] hits = topDocs.scoreDocs;
//
//			if (hits.length > 0) {
//				String pageID = getDocument(hits[0].doc).getField("pageid").stringValue();
//				if (!pageIdCharacterMap.containsKey(pageID)) {
//					pageIdCharacterMap.put(pageID, pageIdCharacter.getValue());
//				}
//
//			}
//		}
		return pageIdCharacterNamesMap;
	}

	public String choosesimilarity() {

		bm25similarity();
		return "Runfile-BM25Similarity.txt";
	}

}
