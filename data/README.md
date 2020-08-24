# README

A common place for data/resources that everyone can use.

To include this in your project, please consider doing so as a git submodule. You should be able to configure this by running

```
git submodule add https://gitlab.cs.unh.edu/cs953-spring-2020/common-group-data.git
```

Then when you update you should do `git pull --recurse-submodules`

# Trec Eval

To evaluate your result "officially" create a run file and evaluate it against the deaths.qrels file in this repo.

```
trec_eval -m set_F deaths.qrels my_predictions.run
```

An example run_file would be in the following format.  All columns are space delimited.  The first column indicates the sample set id (also thought of as the query id).  The second columns are always zero.  The third column is the name of the character from the "character-deaths.csv" file with all spaces replaced with underscores. The fourth column counts up from 1.  The fifth column is always 1.  The sixth column is the name of your team without spaces.  The file contains only people who are predicted to have died.

```
0 0 Aenys_Frey 1 1 team_X
0 0 Alayaya 2 1 team_X
0 0 Alliser_Thorne 3 1 team_X
1 0 Alliser_Thorne 1 1 team_X
1 0 John Snow 2 1 team_X
```
