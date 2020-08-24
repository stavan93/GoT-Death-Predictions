#!/bin/sh

mvn clean compile assembly:single

cd src/main/java/team3_Sub_3/ml/analysis

python Svmfeatures.py

cd ../../../../../../

java -Xmx50g -cp target/team3_Sub_3-0.0.1-SNAPSHOT-jar-with-dependencies.jar team3_Sub_3.featurealgo.FinalSubsetGenerateFile

cd data

trec_eval -m set_F deaths.qrels subsetfile9.run

trec_eval -m set_F deaths.qrels subsetfile8.run

trec_eval -m set_F deaths.qrels subsetfile7.run

trec_eval -m set_F deaths.qrels subsetfile6.run

trec_eval -m set_F deaths.qrels subsetfile5.run

trec_eval -m set_F deaths.qrels subsetfile4.run

trec_eval -m set_F deaths.qrels subsetfile3.run

trec_eval -m set_F deaths.qrels subsetfile2.run

trec_eval -m set_F deaths.qrels subsetfile1.run

trec_eval -m set_F deaths.qrels subsetfile0.run