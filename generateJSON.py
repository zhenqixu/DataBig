#import numpy as np
import random
#from mpl_toolkits import mplot3d
#import matplotlib.pyplot as plt
import sys
import json

N=1000
D=3 + 1 # empty dimension


label1_num = random.randrange(100, 700)
label2_num = N - label1_num

'''
fig = plt.figure()
ax = plt.axes(projection='3d')
'''


def generate_data(size, min_, max_):
    data = []
    #global ax

    noise = True
    for i in range(size):
        v1 = random.uniform(min_, max_)*1
        v2 = random.uniform(min_, max_)*1
        v3 = random.uniform(min_, max_)*1
        v4 = 0
        label = 0
        color = 'red'

        if v1 > (v2*v2 + v3*v3)/5-6:
            label = 1
            color = 'blue'

        # add noises
        if (noise):
            v1 += random.uniform(0, 0.03)
            v2 += random.uniform(-0.05, 0)
            v3 += random.uniform(-0.05, 0.05)
            v4 += random.uniform(0.1, 0.12)

        data.append({
            'label': label,
            'account1': v1,
            'account2': v2,
            'account3': v3,
            'account4': v4
        })
        #ax.scatter(v1, v2, v3, c=color, s = 1)
    return data

def change_entry(v):
    vocab = "abcdefghijklmnopq"
    index = int(v)%len(vocab)
    #print(str(vocab[index]))
    return str(vocab[index])

# type of noises:
# - label is flipped
# - invalid entries
# - entries are missing
def add_noise(data):
    distorted_data = []
    counter = 0
    for d in data:
        rand = random.uniform(0, 1)
        interval = 0.02
        start_percent = 0.1


        if (rand < start_percent): # flip labels
            d['label'] = 1 - d['label']
            counter += 1
        elif (start_percent <= rand <= start_percent + interval): # add invalid entries - vocab
            d['account1'] = change_entry(d['account1'])
            counter += 1
        elif (start_percent + interval <= rand < start_percent+interval*2):
            d['account2'] = change_entry(d['account2'])
            counter += 1
        elif (start_percent + interval*2 <= rand <= start_percent + interval*3):
            d['account3'] = change_entry(d['account3'])
            counter += 1
        elif (start_percent + interval*3 <= rand <= start_percent + interval*4): # add Null entries
            d['account3'] = None
            d['account2'] = float('inf')
            counter += 1


        if (rand < 0.3):
            distorted_data.append(d)
        elif (rand < 0.5):
            d['account1'] = str(d['account1'])
            d['account2'] = str(d['account2'])
            d['account3'] = str(d['account3'])
            d['account4'] = str(d['account4'])
        elif (rand < 0.6):
            d['account1'] = None
            d['account2'] = None
        elif (rand < 0.7):
            d['account3'] = None
            d['account4'] = None


        distorted_data.append(d)

        
    print("Number of noises: " + str(counter))
    return distorted_data



#plt.show()

for i in range(3):
    file_name = "data" + str(i) + ".json"
    with open(file_name, 'w') as outfile:
        #json.dump(data, outfile)
        json.dump(add_noise(generate_data(N, 1, 7)), outfile)
