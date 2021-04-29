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

fig = plt.figure()
ax = plt.axes(projection='3d')


def generate_data(label, size, min_, max_, color):
    global data
    global ax
    for i in range(label1_num):
        v1 = random.randrange(min_, max_)*3
        v2 = random.randrange(min_, max_)*0.4
        v3 = random.randrange(min_, max_)-100

        data.append({
            'account1': v1,
            'account2': v2,
            'account3': v3,
            'account4':  0
        })
        ax.scatter(v1, v2, v3, c=color, s = 1)
        
        
generate_data(1, label1_num, 101, 605, 'red')
generate_data(2, label2_num, 400, 1205, 'green')
generate_data(3, label3_num, 800, 2000, 'blue')


with open('data.json', 'w') as outfile:
    json.dump(data, outfile)
    

print("1:" + str(label1_num))
print("2:" + str(label2_num))
print("3:" + str(label3_num))



plt.show()
