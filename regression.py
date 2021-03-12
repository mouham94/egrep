import numpy
import re

f = open('regex.data', 'r')

x = []
y = []

lines = f.readlines()

for line in lines:
	coords = re.split("\t", line)
	x.append(float(coords[0]))
	y.append(float(coords[1]))


fit = numpy.polyfit(x, y, 1)
print(fit)
