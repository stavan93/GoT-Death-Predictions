/**
 * 
 */
package team3_Sub_3.featurealgo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.Data.ItemWithFrequency;
import edu.unh.cs.treccar_v2.Data.PageMetadata;
import edu.unh.cs.treccar_v2.Data.PageSkeleton;

/**
 * @author Gayathri
 *
 */
 
 /*Created entities are mapped to the page names using the character names*/
 
public class EntityGeneration {

	HashMap<String, Map<String, Integer>> characterRelationsMap = new HashMap<>();

/* Code that could be used later but not being used here at the moment*/

	public void predictCharacterApproach() throws IOException {
		Map<String, Integer> characterDetail = new HashMap<String, Integer>();
		HashMap<String, Integer> mp = new HashMap<>();
		String filepath = "data/character-predictions.csv";
		String line = "";
		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
			boolean flag = true;
			while ((line = reader.readLine()) != null) {
				if (flag) {
					flag = false;
					continue;
				}
				String[] name = line.split(",");
				String[] names = name[5].split(" ");
				if (!characterRelationsMap.containsKey(names[0])) {
					characterRelationsMap.put(names[0], new HashMap<String, Integer>());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

/* Generates entities from the skeleton of the entity */

	public Entity generateEntity(Data.Page page) {
		PageMetadata pageMetadata = page.getPageMetadata();
         List<PageSkeleton> skeleton = page.getSkeleton();
		List<String> inLinkAnchorTexts = pageMetadata.getInlinkAnchors().stream().map(ItemWithFrequency::getItem)
				.collect(Collectors.toList());
//		for(PageSkeleton pg : skeleton)
//		{
//			while(pg.toString().contains("text"))
//			{
//				int a = pg.toString().indexOf("text");                      Part that could be potentially used later.
//				String b = pg.toString().substring(a+5);
//				 Pattern p = Pattern.compile("\'.*?\'");
//				    Matcher m = p.matcher(b.toString()); 
//				    while(m.find()){
//				        System.out.println(b.toString().substring(m.start()+1,m.end()-1));
//			}
//			}
		//}
		return new Entity(page.getPageName(), pageMetadata.getRedirectNames(), pageMetadata.getInlinkIds(),
				inLinkAnchorTexts,null,page.getPageId());
	}

	/* Preprocessing the pages after indexing by removing the stop words
	and consolidating the tokens that are important(the ones except the stop words) */

	public Map<String, Set<String>> tokenizeDocuments() throws IOException {
		IndexReader reader = null;
		Map<String, Set<String>> listTokens = new HashMap<>();
		Directory indexDir = FSDirectory.open(Paths.get("paragraph.lucene"));
		reader = DirectoryReader.open(indexDir);
		Analyzer analyzer = new StandardAnalyzer();
		Set<String> hSet = new HashSet<String>();
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < reader.maxDoc(); i++) {
			Set<String> documentTokens = new HashSet<String>();
			Document doc = reader.document(i);
			String docId = doc.get(Constants.FIELD_PAGE_NAME);
			TokenStream stream = analyzer.tokenStream("text", doc.get(Constants.FIELD_CONTENTS));
			stream.reset();
			while (stream.incrementToken()) {
				String termString = stream.getAttribute(CharTermAttribute.class).toString();
				documentTokens.add(termString);
				// hSet.add(termString);
				// result.add(termString);
			}
			stream.close();
			listTokens.put(docId, documentTokens);
			// TokenSources.getTokenStream(doc, "text", new StandardAnalyzer());
		}
		analyzer.close();
		// vocabularySize = hSet.size();
		return listTokens;
	}

}
