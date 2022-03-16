cols = ['a', 'b', 'c', 'd']
rows = ['1', '2', '3', '4']

u = []

for col in cols:
    for row in rows:
        u.append(col + row)

print(u, len(u))

p = []

for col in cols:
    for row in ['1', '2']:
        p.append(col + row)

print("p: ", p, len(p))

q = []

for col in cols:
    for row in ['2', '3']:
        q.append(col + row)

print("q: ", q, len(q))

r = []

for col in ['a', 'b']:
    for row in rows:
        r.append(col + row)

print("r: ", r, len(r))

s = []

for col in ['b', 'c']:
    for row in rows:
        s.append(col + row)

print("s: ", s, len(s))

dic = {}

for ele in u:
    str = ""
    if ele in p:
        str += "1"
    else:
        str += "0"
    
    if ele in q:
        str += "1"
    else:
        str += "0"

    if ele in r:
        str += "1"
    else:
        str += "0"

    if ele in s:
        str += "1"
    else:
        str += "0"

    dic[ele] = str

print("dic: ", dic)
        

