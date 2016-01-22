import socket
import os
import sys
import json

class MyClient:
    def __init__(self,host,port,defaultPath):
        self.host = host
        self.port = port
        self.defaultPath = defaultPath
    def pullRequest(self):
        sock = socket.socket()
        sock.connect((self.host,self.port))
        theServerFilesList =[]
        print'input ls to get the files list in service'
        while True:
            print 'please input the order'
            order = sys.stdin.readline()
            sock.sendall(order)
            result = sock.recv(1024)
            try:
                receiveData = json.loads(result)
                if type(receiveData) == list:
                    print receiveData
                else:
                    isDownloading = True
                    for filename in receiveData.keys():
                        print filename
                        if os.path.exists(self.defaultPath + '/' + filename):
                            print 'the file '+filename+ ' is existed in your folder,do you want to update the file\n input "yes" or else to reply, \nyes refer to update the file ,others mean to cancel\n'
                            isUpdate = sys.stdin.readline()
                            if isUpdate == 'yes\n':
                                isDownloading = True
                            else:
                                isDownloading = False
                                print 'download canceled'
                        if isDownloading:
                            if os.path.exists(self.defaultPath + '/' + filename + '.part') and os.path.exists(self.defaultPath + '/' + filename + '.number'):
                                file_isDownloaded = int(self.readFiles(self.defaultPath + '/' + filename + '.number'))
                                file_part = open(self.defaultPath + '/' + filename + '.part','a')
                                for i in range(file_isDownloaded,len(receiveData[filename]),1):
                                    file_number = open(self.defaultPath + '/' + filename + '.number','w')
                                    file_part.write(receiveData[filename][i])
                                    file_number.write(str(i))
                                    file_number.close()
                                file_part.close()
                            else:
                                file_part = open(self.defaultPath + '/' + filename + '.part','a')
                                for i in range(0,len(receiveData[filename]),1):
                                    file_number = open(self.defaultPath + '/' + filename + '.number','w')
                                    dict2 = receiveData[filename]
                                    file_part.write(dict2[str(i)])
                                    file_number.write(str(i))
                                    file_number.close()
                                file_part.close()
                        if os.path.exists(self.defaultPath + '/' + filename + '.number'):
                            os.remove(self.defaultPath + '/' + filename + '.number')
                        if os.path.exists(self.defaultPath + '/' + filename + '.part'):
                            os.rename(self.defaultPath + '/' + filename + '.part',self.defaultPath + '/' + filename)
                    print 'download finished'
            except:
                print result

    def readFiles(self,path):
		f = open(path,'r')
		fileTxt = f.read(100000)
		f.close()
        if fileTxt == '':
            fileTxt = '0'
		return fileTxt

if __name__ == '__main__':
	print 'Client is in processing'
	cli = MyClient('127.0.0.1',5000,'/home/shaw/downloadByShaw')
	cli.pullRequest()
