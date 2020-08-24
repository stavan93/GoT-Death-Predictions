# Submission_3

## clone the repository using the following command:

git clone https://gitlab.cs.unh.edu/cs953-spring-2020/team_3/submission_3.git

git clone https://gitlab.cs.unh.edu/cs953-spring-2020/common-group-data.git

## go to the working directory:

cd submission_3

## set permissions for the shell scripts

chmod 777 install.sh

chmod 777 run.sh

chmod 777 evaluate.sh

## To install the software execute the following script

./install.sh

## To run the software and perform feature extraction execute the following script

./run.sh

## To get the best evaluation results execute the following script

./evaluate.sh

### Sentiment Features take a very long time to be generated, which is why we have made a separate script to run Sentiment_Analysis.py file, the feature CSV has already been uploaded inside "data" folder as Sentiment.csv, however if you would still like to run the code, you can run the following command

chmod 777 Sentiment_Analysis.sh

./Sentiment_Analysis.sh