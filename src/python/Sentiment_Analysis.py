import cbor
import trec_car.read_data as dat
import spacy
import csv
import time
import os
import nltk
from textblob import TextBlob
from textblob.sentiments import NaiveBayesAnalyzer
from collections import Counter
from spacy import displacy

nltk.download('all')
names = []
nlp = spacy.load('en_core_web_sm')
f = open(str(os.path.dirname(os.path.dirname(os.getcwd()))) + "/data/got.cbor", "rb")
pages = list(dat.iter_pages(f))
f.close()

#Reads the character names from names.txt file
def Read_Names():
    file = open(str(os.path.dirname(os.path.dirname(os.getcwd()))) + "/data/names.txt","r")
    for line in file:
        names.append(line.strip())

#Analyzes avg. positive and avg. negative sentiment for each character
def Analyze_Sentiment( name ):
    pos = 0
    neg = 0
    counter = 0
    flag = True
    for page in pages:
        if page.page_name == name:
            doc = nlp(page.get_text())
            sentences = list(doc.sents)
            f_name = name.split(" ")
            for sentence in sentences:
                sen = str(sentence)
                if name in sen:
                    blob = TextBlob(sen, analyzer=NaiveBayesAnalyzer())
                    pos += blob.sentiment.p_pos
                    neg += blob.sentiment.p_neg
                    counter += 1
                    flag = False
                elif f_name[0] in sen:
                    blob = TextBlob(sen, analyzer=NaiveBayesAnalyzer())
                    pos += blob.sentiment.p_pos
                    neg += blob.sentiment.p_neg
                    counter += 1
                    flag = False
            break
    if flag:
        return [0.5,0.5]
    return [(pos/counter),(neg/counter)]



if __name__ == "__main__":
    start_time = time.time()
    Read_Names()
    csvfile = open(str(os.path.dirname(os.path.dirname(os.getcwd()))) + "/data/Sentiment.csv", "w")
    csvfile.write("Name,Positive Sentiment,Negative Sentiment\n")
    flag = True
    for name in names:
        if flag:
            flag = False
            continue
        print(name)
        sentiment = Analyze_Sentiment(name)
        print("Positive: " + str(sentiment[0]))
        print("Negative: " + str(sentiment[1]))
        csvfile.write(name + "," + str(sentiment[0]) + "," + str(sentiment[1]) + "\n")
    csvfile.close()
    end_time = time.time()
    print(end_time - start_time)