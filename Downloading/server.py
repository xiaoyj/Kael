import socket
import os
import json
import threading

class MyServer:
	def  __init__(self,host,port):
		self.host = host
		self.port = port
	def connect(self):
		s = socket.socket()
		s.bind((self.host,self.port))
		s.listen(5)
		isDownloading = False
		fileToSendDict ={}
		connection,adress = s.accept()
		while True:
			receiveData = connection.recv(1024)
			filelist = self.getFilesList('/home/shaw/share')
			if not receiveData:
				continue
			elif receiveData == 'hi,server!\n':
				connection.sendall('hi,client!')
			elif receiveData == 'ls\n':
				json_filelist = json.dumps(filelist)
				connection.sendall(json_filelist)
			elif receiveData.startswith('download'):
				try:
					fileNumber = int(receiveData[9])
					print filelist[fileNumber]
					dict1 = self.getFilesDict(filelist[fileNumber])
					print dict1
					json_filesDict = json.dumps(dict1)
					connection.sendall(json_filesDict)
				except:
					connection.sendall('files not exist')
			else:
				connection.sendall('wrong order')
		sock.close()
	def getFilesDict(self,path):
		filesDict = {}
		if os.path.isdir(path):
			for root,dirs,files in os.walk(path):
				for fn in files:
					filesDict[fn] = self.readFiles(path+'/' +fn)
		else:
			filesDict[path.split('/')[-1]] = self.readFiles(path)
		return filesDict

	def getFilesList(self,findPath):
		fileList = []
		fileNames = os.listdir(findPath)
		if(len(fileNames) > 0):
			for fn in fileNames:
				fullFileNames = os.path.join(findPath,fn)
				fileList.append(fullFileNames)
		return fileList

	def readFiles(self,path):
		fileTxt = {}
		f = open(path,'r')
		length = len(f.read())
		f.close()
		f = open(path,'r')
		for i in range(0,length,100000):
			fileTxt[int(i/100000)] = f.read(100000)
		f.close()
		return fileTxt


if __name__ == '__main__':
	print 'Server is in processing'
	ser = MyServer('127.0.0.1',5000)
	ser.connect()
