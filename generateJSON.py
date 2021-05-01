#import numpy as np
import random
from mpl_toolkits import mplot3d
import matplotlib.pyplot as plt

N=1000
D=3 + 1 # empty dimension


import json
label1_num = random.randrange(100, 700)
label2_num = N - label1_num


fig = plt.figure()
ax = plt.axes(projection='3d')
data = []


def generate_data(size, min_, max_):
    global data
    global ax
    for i in range(size):
        v1 = random.uniform(min_, max_)*10
        v2 = random.uniform(min_, max_)*10
        v3 = random.uniform(min_, max_)*10
        label = 0
        color = 'red'
        
        if v1*v1 > (v2*v2 + v3*v3)/5-2:
            label = 1
            color = 'blue'
            
        data.append({
            'label': label,
            'account1': v1,
            'account2': v2,
            'account3': v3,
            'account4':  0
        })
        ax.scatter(v1, v2, v3, c=color, s = 1)

def change_entry(v):
    vocab = "abcdefghijklmnopq"
    index = int(v)%len(vocab)
    #print(str(vocab[index])) 
    return str(vocab[index])
    
# type of noises:
# - label is fliiped
# - entries is missing
def add_noise(data):
    distorted_data = []
    counter = 0
    for d in data:
        rand = random.uniform(0, 1)
        interval = 0.02
        start_percent = 0.05
        d['account1'] + random.uniform(-1, 1)
        d['account2'] + random.uniform(-1, 1)
        d['account3'] + random.uniform(-1, 1)
        d['account4'] + random.uniform(-1, 1)

        if (rand < start_percent): # flip labels
            d['label'] = 1 - d['label']
            counter += 1
        elif (start_percent <= rand <= start_percent + interval):
            d['account1'] = change_entry(d['account1'])  
            counter += 1
        elif (start_percent + interval <= rand < start_percent+interval*2):
            d['account2'] = change_entry(d['account2'])  
            counter += 1
        elif (start_percent + interval*2 <= rand <= start_percent + interval*3):
            d['account3'] = change_entry(d['account3'])  
            counter += 1
        elif (start_percent + interval*3 <= rand <= start_percent + interval*4):
            d['account3'] = None
            d['account2'] = float('inf')
            counter += 1
        distorted_data.append(d)
    print("Number of noises: " + str(counter))
    return distorted_data
    
generate_data(N, 0, 10)

 
#plt.show()

with open('data.json', 'w') as outfile:
    #json.dump(data, outfile)
    json.dump(add_noise(data), outfile)

   

