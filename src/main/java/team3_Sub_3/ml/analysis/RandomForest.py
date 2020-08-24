#!/usr/bin/env python
# coding: utf-8
#!/usr/bin/env python
# coding: utf-8
# In[364]:
import pandas as pd
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
data = pd.read_csv('~/submission_3/data/finalCSV.csv')
data
data.info()
data1_train = data.iloc[::2]
data_test  = data.iloc[1::2]

#train = data_train.iloc[::2]
#test = data_test.iloc[1::2]
Y_train=data1_train['Death']
X_train= data1_train[['Character Name','Gender','Popularity']]
X_test= data_test[['Character Name','Gender','Popularity']]
Y_test= data_test['Death']
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

# # Random Forest

from sklearn.ensemble import RandomForestClassifier
rf = RandomForestClassifier(n_estimators=1, max_depth=100, random_state=345)
fit = rf.fit(X_train, Y_train)
Y_predDT = fit.predict(X_test)
#df = pd.DataFrame(Y_pred_LR, columns = ['Death_year'])
df = pd.DataFrame(data_test['Character Name'],columns=['Character Name'])
df['Death'] = Y_predDT
export_csv = df.to_csv (r'~/submission_3/data/RandomForest.csv', index = None, header=True)
accuracy = accuracy_score(Y_test, Y_predDT)
print('accuracy',accuracy)
precision = precision_score(Y_test, Y_predDT)
print ('precision',precision)
f1=f1_score(Y_test, Y_predDT)
print ('f1 =',f1)
