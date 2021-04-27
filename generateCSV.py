import numpy as np
N=1000
D=3 + 1 # empty dimension
R = np.random.rand(N,D)
R = R*10
R = R-5

res = []
for i in range(N):
    R[i][3] = 0 # set the empty dimesion
    x = R[i][0]
    y = R[i][1]
    z = R[i][2]
    if z > ((x*x+y*y)/5.0)-2.0:
        res.append(2)
    elif z > ((x*x+y*y)/5.0)-5.0:
        res.append(1)
    else:
        res.append(0)

csv = np.asarray(R)
np.savetxt("data.csv",csv,delimiter=" ",fmt="%.15f")

print("2:" + str(res.count(2)))
print("1:" + str(res.count(1)))
print("0:" + str(res.count(0)))
