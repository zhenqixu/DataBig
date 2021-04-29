import numpy as np
import random

N=5000
D=3 + 1 # empty dimension
R = np.random.rand(N,D)
R = R*10
R = R-5

noise_precentage = 0.005

f = open("data.txt", "w")

# print(R)
def Printlsvm(label,R,D):
    f.write(str(label)+" ")
    for j in range(D):
        f.write(str(j+1)+":"+str(R[j])+" ")
    f.write("\n")

def Printlsvm2(label,R,D):
    print(str(label)+" ", end='')
    for j in range(D):
        print(str(j+1)+":"+str(R[j])+" ",end='')
    print("")


res = []
for i in range(N):

    R[i][3] = 0 # set the empty dimesion
    x = R[i][0]
    y = R[i][1]
    z = R[i][2]
    if random.random() <= noise_precentage:
        res.append(-1)
        if z > ((x*x+y*y)/5.0)-2.0:
            Printlsvm(1.0,R[i],D)
            #res.append(2)
        elif z > ((x*x+y*y)/5.0)-5.0:
            Printlsvm(0.0,R[i],D)
            #res.append(1)
        else:
            Printlsvm(2.0,R[i],D)
            #res.append(0)
    else:
        if z > ((x*x+y*y)/5.0)-2.0:
            Printlsvm(2.0,R[i],D)
            res.append(2)
        elif z > ((x*x+y*y)/5.0)-5.0:
            Printlsvm(1.0,R[i],D)
            res.append(1)
        else:
            Printlsvm(0.0,R[i],D)
            res.append(0)

print("2:" + str(res.count(2)))
print("1:" + str(res.count(1)))
print("0:" + str(res.count(0)))
print("Noise:" + str(res.count(-1)))
