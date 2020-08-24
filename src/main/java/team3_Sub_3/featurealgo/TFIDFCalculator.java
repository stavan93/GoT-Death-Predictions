/**
 * 
 */
package team3_Sub_3.featurealgo;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Gayathri
 *
 */
	public class TFIDFCalculator {
	    /**
	     * @param words  list of strings
	     * @param term String represents a term
	     * @return term frequency of term in document
	     */
	    public double tf(HashSet<String> words, String term) {
	        double result = 0;
	        for (String word : words) {
	            if (term.equalsIgnoreCase(word))
	                result++;
	        }
	        return result / words.size();
	    }

	    /**
	     * @param documents list of list of strings represents the dataset
	     * @param term String represents a term
	     * @return the inverse term frequency of term in documents
	     */
	    public double idf(HashSet<Set<String>> documents, String term) {
	        double n = 0;
	        for (Set<String> doc : documents) {
	            for (String word : doc) {
	                if (term.equalsIgnoreCase(word)) {
	                    n++;
	                    break;
	               }
	            }
	        }
	        if(n==0)
	        {
	        	return n;
	        }
	        return Math.log(documents.size() / n);
	        }

	    /**
	     * @param words  a text document
	     * @param documents all documents
	     * @param term term
	     * @return the TF-IDF of term
	     */
	    public double tfIdf(HashSet<String> words, HashSet<Set<String>> documents, String term) {
	        return tf(words, term) * idf(documents, term);

	    }

//	    public static void main(String[] args) {
//
//	        List<String> doc1 = Arrays.asList("Lorem", "ipsum", "dolor", "ipsum", "sit", "ipsum");
//	        List<String> doc2 = Arrays.asList("Vituperata", "incorrupte", "at", "ipsum", "pro", "quo");
//	        List<String> doc3 = Arrays.asList("Has", "persius", "disputationi", "id", "simul");
//	        List<List<String>> documents = Arrays.asList(doc1, doc2, doc3);
//
//	        TFIDFCalculator calculator = new TFIDFCalculator();
//	        double tfidf = calculator.tfIdf(doc1, documents, "ipsum");
//	        System.out.println("TF-IDF (ipsum) = " + tfidf);
//	    }
	}

