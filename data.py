import numpy as np
N=1000
D=3
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
# if R[i][2] > R[i][0]*0.3+R[i][1]*0.6 : # 484 above
# if R[i][2] > (R[i][0]*R[i][0]/5.0)-2.5+ R[i][1]/2.0 : # 572 above
    if R[i][2] > ((R[i][0]*R[i][0]+R[i][1]*R[i][1])/5.0)-5.0: # 665 above
        Printlsvm(1.0,R[i],D)
    else:
        Printlsvm(0.0,R[i],D)
