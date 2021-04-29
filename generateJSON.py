import numpy as np
import random
from mpl_toolkits import mplot3d
import matplotlib.pyplot as plt

N=1000
D=3 + 1 # empty dimension



import json
label1_num = random.randrange(100, 500)
label2_num = random.randrange(100, 500)
label3_num = N - label1_num - label2_num


#data['people'] = []
#with open('data.json', 'w') as outfile:
data = []
for i in range(100):
    data.append({
        'account1': random.randrange(10, 500),
        'account2': random.randrange(10, 500),
        'account3': random.randrange(10, 500),
        'account4':  0,
        'label' : 0
    })

    
for i in range(label2_num):
    data.append({
        'account1': random.randrange(500, 1000),
        'account2': random.randrange(500, 1000),
        'account3': random.randrange(500, 1000),
        'account4': 0,
        'label' : 1
    })
for i in range(label3_num):
    data.append({
        'account1': random.randrange(1000, 10000)-5,
        'account2': random.randrange(1000, 10000)-5,
        'account3': random.randrange(1000, 10000)-5,
        'account4':  0,
        'label' : 2
    })

with open('data.json', 'w') as outfile:
    json.dump(data, outfile)
    

print("1:" + str(label1_num))
print("2:" + str(label2_num))
print("3:" + str(label3_num))

fig = plt.figure()
ax = plt.axes(projection='3d')
#plt.show()
