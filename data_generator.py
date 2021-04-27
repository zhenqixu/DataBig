import numpy as np
N=1000
D=3 + 1 # empty dimension
R = np.random.rand(N,D)
R = R*10
R = R-5


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


for i in range(N):
    R[i][3] = 0 # set the empty dimesion
    x = R[i][0]
    y = R[i][1]
    z = R[i][2]
    if z > ((x*x+y*y)/5.0)-2.0:
        Printlsvm(2.0,R[i],D)
    elif z > ((x*x+y*y)/5.0)-7.0:
        Printlsvm(1.0,R[i],D)
    else:
        Printlsvm(0.0,R[i],D)

