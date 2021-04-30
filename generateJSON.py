import numpy as np
import random
from mpl_toolkits import mplot3d
import matplotlib.pyplot as plt

N=100
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
    return vocab[index]
    
# type of noises:
# - label is fliiped
# - entries is missing
def add_noise(data):
    distorted_data = []
    counter = 0
    for d in data:
        rand = random.uniform(0, 1)
        interval = 0.02
        start_percent = 0.02
        if (rand < start_percent):
            d['label'] = 1 - d['label']
            counter += 1
        elif (start_percent <= rand <= start_percent + interval):
            d['account1'] = change_entry(d['account1']) if rand%2 == 0 else float('inf')
            counter += 1
        elif (start_percent + interval <= rand < start_percent+interval*2):
            d['account2'] = change_entry(d['account2']) if rand%2 == 0 else None
            counter += 1
        elif (start_percent + interval*2 <= rand <= start_percent + interval*3):
            d['account3'] = change_entry(d['account3']) if rand%2 == 0 else float('inf')
            counter += 1
        distorted_data.append(d)
    print("Number of noises: " + str(counter))
    return distorted_data
    
generate_data(N, 0, 10)

 
#plt.show()

with open('data.json', 'w') as outfile:
    #json.dump(data, outfile)
    json.dump(add_noise(data), outfile)

   

