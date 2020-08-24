#!/bin/sh

cd src/python

python3 Replace_Redirect_Names.py

cd ../../

java -Xmx50g -cp target/team3_Sub_3-0.0.1-SNAPSHOT-jar-with-dependencies.jar team3_Sub_3.Construct_Knowledge_Graph

cd src/python

python3 Mention_Stats.py

cd ../../

java -Xmx50g -cp target/team3_Sub_3-0.0.1-SNAPSHOT-jar-with-dependencies.jar team3_Sub_3.featurealgo.EntityLinking pages ../common-group-data/got.cbor

cd src/main/java/team3_Sub_3/ml/analysis

python LGGender.py

cd ../../../../../../

chmod 777 data/GenderFeatureFinal2.csv

java -Xmx50g -cp target/team3_Sub_3-0.0.1-SNAPSHOT-jar-with-dependencies.jar team3_Sub_3.featurealgo.FeatureGenderPrediction

java -Xmx50g -cp target/team3_Sub_3-0.0.1-SNAPSHOT-jar-with-dependencies.jar team3_Sub_3.runner.FeatureRunner

java -Xmx50g -cp target/team3_Sub_3-0.0.1-SNAPSHOT-jar-with-dependencies.jar team3_Sub_3.MergeCSV

java -Xmx50g -cp target/team3_Sub_3-0.0.1-SNAPSHOT-jar-with-dependencies.jar team3_Sub_3.Final_MergeCSV