from time import time
from math import exp, sqrt, log
from random import gauss, seed

seed(20000)
initial_time = time()


initial_value = 100.  
Strike_Price = 107. 
Maturity = 1.0 
risk = 0.05  
volatility = 0.3  
Time_Steps= 50  
dt = Maturity / Time_Steps  
num_paths = 250000  


Sim = []
for i in range(num_paths):
    Simpath = []
    for t in range(Time_Steps + 1):
        if t == 0:
            Simpath.append(initial_value)
        else:
            z = gauss(0.0, 1.0)
            Simt = Simpath[t - 1] * exp((risk - 0.5 * volatility ** 2) * dt
                                  + volatility * sqrt(dt) * z)
            Simpath.append(Simt)
    Sim.append(Simpath)
    

Option_Value = exp(-risk * Maturity) * sum([max(path[-1] - Strike_Price, 0) for path in Sim]) / num_paths

time_taken = time() - initial_time
print(" Option Value %7.3f" % Option_Value)
print("Duration in Seconds   %7.3f" % time_taken)
