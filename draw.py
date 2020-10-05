import matplotlib.pyplot as plt
import numpy as np

file1 = open('graphic.txt', 'r')
count = 0
lines = []
while True:
    count += 1
    # Get next line from file
    line = file1.readline()
    lines.append(line.rstrip())
    # if line is empty
    # end of file is reached
    if not line:
        break
    print("Line{}: {}".format(count, line.strip()))
file1.close()


separator = "    "
x = [float(x) for x in lines[0].split(separator)]
y = [float(x) for x in lines[1].split(separator)]
point_1 = [float(x) for x in lines[2].split(separator)]




plt.plot(x, y, )
plt.plot(point_1[0], point_1[1], '--or')

plt.title('A tale of 2 subplots')
plt.ylabel('Damped oscillation')

plt.show()
print("")