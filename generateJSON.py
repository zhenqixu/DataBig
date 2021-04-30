import numpy as np
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

def generate_data(size, min_, max_, color):
    global data
    global ax
    for i in range(size):
        v1 = random.randrange(min_, max_)*10
        v2 = random.randrange(min_, max_)*10
        v3 = random.randrange(min_, max_)*10
        if v1*v1 > (v2*v2 + v3*v3)/5-2:
            data.append({
                'label': 1,
                'account1': v1,
                'account2': v2,
                'account3': v3,
                'account4':  0
            })
            ax.scatter(v1, v2, v3, c='red', s = 1)
        else:
            data.append({
                'label': 0,
                'account1': v1,
                'account2': v2,
                'account3': v3,
                'account4':  0
            })
            ax.scatter(v1, v2, v3, c='blue', s = 1)

generate_data(N, 0, 10, 'red')


with open('data.json', 'w') as outfile:
    json.dump(data, outfile)
    



plt.show()
