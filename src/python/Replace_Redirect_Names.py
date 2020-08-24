import cbor
import trec_car.read_data as dat
import spacy
import csv
import os
import time
from collections import Counter
from spacy import displacy

csvfile = str(os.path.dirname(os.path.dirname(os.getcwd()))) + '/data/character-deaths.csv'
names = []
namelist = []

f = open(str(os.path.dirname(os.path.dirname(os.getcwd()))) + '/data/got.cbor', 'rb')
pages = list(dat.iter_pages(f))
f.close()

#Replaces the names in the name list with the redirect names for characters who have a redirect link to another page in the cbor
def Rename_Redirect( namelist ):
    file = open(str(os.path.dirname(os.path.dirname(os.getcwd()))) + '/data/names.txt','w')
    file.write("Name\n")
    for n in namelist:
        flag = True
        if n == "Victarion Greyjoy":
            file.write("House Greyjoy\n")
            continue
        for page in pages:
            if page.page_name == n:
                if "REDIRECT " in page.get_text():
                    nn = str(page.get_text()).split(" ")
                    #print(str(nn[1:]))
                    file.write(' '.join(map(str, nn[1:])) + "\n")
                    flag = False
                elif "REDIRECT" in page.get_text():
                    nn = str(page.get_text())[9:]
                    #print(str(nn[1:]))
                    file.write(nn + "\n")
                    flag = False
                else:
                    file.write(n + "\n")
                    flag = False
        if flag:
            file.write(n + "\n")
    file.close()

#Reads the character-deaths.csv file gets the character names
def Read_Csv():
    with open(csvfile, 'r') as file:
        reader = csv.reader(file)
        fields = next(reader)
        for nam in reader:
            names.append(nam[0])

if __name__ == "__main__":
    Read_Csv()
    for char_name in names:
        fname = str(char_name).split(" ")
        if len(fname) == 1:
            namelist.append(fname[0])
        elif len(fname) == 2:
            if "(" in fname[1]:
                namelist.append(fname[0])
            else:
                namelist.append(fname[0] + " " + fname[1])
        else:
            if "(" in fname[1]:
                namelist.append(fname[0])
            elif "(" in fname[2]:
                namelist.append(fname[0] + " " + fname[1])
            else:
                namelist.append(fname[0] + " " + fname[1] + " " + fname[2])
Rename_Redirect(namelist)