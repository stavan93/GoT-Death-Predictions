import cbor
import trec_car.read_data as dat
import spacy
import csv
import time
import os
from collections import Counter
from spacy import displacy

nlp = spacy.load('en_core_web_sm')

names = []
f = open(str(os.path.dirname(os.path.dirname(os.getcwd()))) + "/data/got.cbor", "rb")
pages = list(dat.iter_pages(f))
f.close()

#Read character names from the names.txt file
def Read_Names():
    file = open(str(os.path.dirname(os.path.dirname(os.getcwd()))) + "/data/names.txt","r")
    flag = True
    for line in file:
        if flag:
            flag = False
            continue
        names.append(line.strip())

#Calculates the mention count for each character
def Mention_Count( name ):
    count = 0
    for page in pages:
        if page.page_name == name:
            doc = nlp(page.get_text())
            sentences = list(doc.sents)
            f_name = name.split(" ")
            for sentence in sentences:
                sen = str(sentence)
                if name in sen:
                    count += 1
                    #print(str(count) + " 1 " + name)
                elif f_name[0] in sen:
                    count += 1
                    #print(str(count) + " 2 " + f_name[0])
            break
    return count

#Calculates the mention length for each character
def Mention_Length( name , len ):
    count = 0
    counter = 0
    for page in pages:
        if page.page_name == name:
            doc = nlp(page.get_text())
            sentences = list(doc.sents)
            f_name = name.split(" ")
            flag = False
            for sentence in sentences:
                sen = str(sentence)
                if flag:
                    count += 1
                if name in sen:
                    if counter == 0:
                        flag = True
                    counter += 1
                    continue
                elif f_name[0] in sen:
                    if counter == 0:
                        flag = True
                    counter += 1
                    continue
                if counter == len:
                    break
            break
    return count

#Calculates the mention frequency for each character
def Mention_Frequency( count , length ):
    if count == 0 or length == 0:
        return 0
    freq = count / length
    return freq


if __name__ == "__main__":
    print("Calculating Mention Stats.....")
    print("Please wait, it may take upto 3-5 minutes....")
    Read_Names()
    start_time = time.time()
    csvfile = open(str(os.path.dirname(os.path.dirname(os.getcwd()))) + "/data/Mention_Stats.csv","w")
    csvfile.write("Name,Mention_count,Mention_length,Mention_Frequency\n")
    for name in names:
        #print(name)
        #print("Mention Count:")
        mention_cou = Mention_Count(str(name))
        #print(mention_cou)

        #print("Mention Length:")
        mention_len = Mention_Length(str(name), mention_cou)
        #print(mention_len)

        #print("Mention Frequency:")
        mention_freq = Mention_Frequency(mention_cou, mention_len)
        #print(mention_freq)
        csvfile.write(name + "," + str(mention_cou) + "," + str(mention_len) + "," + str(mention_freq) + "\n")
    csvfile.close()
    end_time = time.time()
    print("Mention Stats Calculated.")
    print(end_time - start_time)