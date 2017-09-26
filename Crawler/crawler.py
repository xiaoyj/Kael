#code='utf-8'


import requests
from bs4 import BeautifulSoup

getUrl='http://ctf5.shiyanbar.com/jia/index.php'
postUrl='http://ctf5.shiyanbar.com/jia/index.php?action=check_pass'
s1=requests.session();
r=s1.get(getUrl)
soup = BeautifulSoup(r.content,"html.parser")
#print soup.prettify()
noSpace =''
#print soup.p.text[1]
for i in soup.p.contents[1].text:
    if ord(i) != 32:
        noSpace += i
string = noSpace.encode('ascii')
string = string.replace('x','*')
payload = {'pass_key':str(eval(string))}
result = s1.post(postUrl,payload)
soup1 = BeautifulSoup(result.content,"html.parser")
print soup1.html.body.text.encode("GBK",'ignore')
#print r.text.encode("GBK",'ignore').body