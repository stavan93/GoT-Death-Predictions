#!/usr/bin/env python
# coding: utf-8
#!/usr/bin/env python
# coding: utf-8
# In[364]:
import pandas as pd
import os
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression, LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.ensemble import GradientBoostingClassifier
from sklearn.ensemble import BaggingClassifier
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression, LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import recall_score
from sklearn.metrics import precision_score
from sklearn.metrics import accuracy_score
from sklearn.metrics import f1_score
import numpy as np
#mathematical operations
#data_test = pd.read_csv('deaths-test.csv')
#data_train = pd.read_csv('deaths-train.csv')
#data_test
#data1_train=data_train.drop([458])
#data1_train
#data = pd.read_csv('~/submission_3/data/GenderFeatureFinal2.csv')
data = pd.read_csv(str(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.getcwd()))))))) + "/data/GenderFeatureFinal2.csv", index=None, header=True)
data
data.info()
data1_train = data.iloc[::2]
data_test  = data.iloc[1::2]

#train = data_train.iloc[::2]
#test = data_test.iloc[1::2]
Y_train=data1_train['Gender']
X_train= data1_train[['Character Name','TFIDF']]
X_test= data_test[['Character Name','TFIDF']]
Y_test= data_test['Gender']
# In[ ]:
# In[370]:
s = pd.get_dummies(X_train['Character Name'])
# In[371]:
u = pd.get_dummies(X_test['Character Name'])
# In[372]:
X_train.drop(['Character Name'], axis=1)
# In[373]:
X_test.drop(['Character Name'], axis=1)
a = X_train.join(s)
a
#rows = [0, 1]
#X_train.drop(rows, axis=0, inplace=True)
# In[377]:
c = a.drop(['Character Name'], axis=1)
c
X_train = c
X_train
d = X_test.join(u)
d
f = d.drop(['Character Name'], axis=1)
f
X_test= f
X_test
X_train.shape
X_test.shape
X_test
X_train.shape
Y_train.shape
X_test.shape
Y_test.shape

# # LOGISTIC REGRESSION

from sklearn.linear_model import LogisticRegression
logreg = LogisticRegression(C=1e10)
fit = logreg.fit(X_train, Y_train)
Y_pred_LR = logreg.predict(X_test)
#df = pd.DataFrame(Y_pred_LR, columns = ['Death_year'])
df = pd.DataFrame(data_test['Character Name'],columns=['Character Name'])
df['Gender'] = Y_pred_LR
#export_csv = df.to_csv (r'~/submission_3/data/LoGenderRe.csv', index = None, header=True)
export_csv = df.to_csv(str(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.path.dirname(os.getcwd()))))))) + "/data/LoGenderRe.csv", index=None, header=True)
accuracy = accuracy_score(Y_test, Y_pred_LR)
print('accuracy',accuracy)
precision = precision_score(Y_test, Y_pred_LR)
print ('precision',precision)
f1=f1_score(Y_test, Y_pred_LR)
print ('f1 =',f1)
