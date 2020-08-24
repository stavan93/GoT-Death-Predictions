#!/usr/bin/env python
# coding: utf-8

# In[364]:


import pandas as pd
import os
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression, LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import recall_score

from sklearn.metrics import precision_score
from sklearn.metrics import accuracy_score
from sklearn.metrics import f1_score

import numpy as np

# Split data into 1 :@

# data = pd.read_csv('~/submission_2/data/finalCSV.csv')
# data
# data.info()
#data1_train = pd.read_csv('~/submission_3/data/Final_Predictions/All_FeatureSet_Train_final.csv')
#data_test = pd.read_csv('~/submission_3/data/Final_Predictions/All_FeatureSet_Test_final.csv')
data1_train = pd.read_csv(str(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.getcwd()))))))) + "/data/Final_Predictions/All_FeatureSet_Train_final.csv")
data_test = pd.read_csv(str(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.getcwd()))))))) + "/data/Final_Predictions/All_FeatureSet_Test_final.csv")
# data_test = pd.read_csv('C:\\Users\\Vivek\\Documents\\DSbooks\\FinalCSV_TEST.csv')
# data1_train = p.read_csv('C:\\Users\\Vivek\\Documents\\DSbooks\\FinalCSV_train.csv')
# data_test
data1_train = data1_train.drop([458])
# data1_train
Y_train = data1_train[['Death']]
X_train = data1_train[
    ['Character Name','Mention Count','Mention Length','Mention Frequency','Avg. Positive Sentiment','Avg. Negative Sentiment','Gender','Weapon','Longevity','Killer','Married','Nobility','Battle Winner','Gender2','Popularity','Gender1']]
X_test = data_test[['Character Name','Mention Count','Mention Length','Mention Frequency','Avg. Positive Sentiment','Avg. Negative Sentiment','Gender','Weapon','Longevity','Killer','Married','Nobility','Battle Winner','Gender2','Popularity','Gender1']]
Y_test = data_test['Death']
s = pd.get_dummies(X_train['Character Name'])
u = pd.get_dummies(X_test['Character Name'])
X_train.drop(['Character Name'], axis=1)
X_test.drop(['Character Name'], axis=1)
a = X_train.join(s)
a
c = a.drop(['Character Name'], axis=1)
c
X_train = c
X_train
d = X_test.join(u)
d
f = d.drop(['Character Name'], axis=1)
f
X_test = f
X_test
X_train.shape
X_test.shape
X_test
X_train.shape
Y_train.shape
X_test.shape
Y_test.shape
from sklearn import svm
from sklearn.metrics import precision_score
from sklearn.metrics import accuracy_score
from sklearn.metrics import f1_score

lsvm = svm.SVC(kernel='linear', C=1)
lsvm.fit(X_train, Y_train)
print("training classification rate = ", lsvm.score(X_train, Y_train))
print("testing classification rate = ", lsvm.score(X_test, Y_test))
print("training classification rate = ", lsvm.score(X_train, Y_train))
print("testing classification rate = ", lsvm.score(X_test, Y_test))
Y_pred_svm = lsvm.predict(X_test)

Y_pred_svm
df = pd.DataFrame(data_test['Character Name'], columns=['Character Name'])
df['Death'] = Y_pred_svm
# df1.append(df, ignore_index = True)
# df1['Character Name']= df['Death'].astype(str)
#export_csv = df.to_csv(r'~/submission_3/data/svm_prediction1.csv', index=None, header=True)
export_csv = df.to_csv(str(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.getcwd()))))))) + "/data/svm_prediction1.csv", index=None, header=True)
accuracy = accuracy_score(Y_test, Y_pred_svm)
print('Accuracy = ', accuracy)
precision = precision_score(Y_test, Y_pred_svm, average='micro')
print('Precision = ', precision)
f1 = f1_score(Y_test, Y_pred_svm, average='micro')
print('F1 score = ', f1)

psvm = svm.SVC(kernel='poly', degree=2)
psvm.fit(X_train, Y_train)
print("training classification rate = ", psvm.score(X_train, Y_train))
print("testing classification rate = ", psvm.score(X_test, Y_test))

