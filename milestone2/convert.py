f = open('Data/users.csv', 'r')
lines = f.readlines()
print(lines)
for i in range(len(lines)):
    lines[i] = lines[i].rstrip() + '::\n'
f.close()
f = open('Data/users.csv', 'w')
for line in lines:
    f.write(line)
f.close()