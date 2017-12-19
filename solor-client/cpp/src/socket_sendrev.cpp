#include <iostream>
#include <winsock2.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <vector>
using namespace std;

#pragma comment(lib, "ws2_32")

#define MSG_CODE_DATA_UPLOAD_RES 0x10008


namespace NetSend {

	void SplitString(const string& s, vector<string>& v, const string& c) {
		string::size_type pos1, pos2;
		pos2 = s.find(c);
		pos1 = 0;
		while(string::npos != pos2) {
			v.push_back(s.substr(pos1, pos2-pos1));

			pos1 = pos2 + c.size();
			pos2 = s.find(c, pos1);
		}
		if(pos1 != s.length())
			v.push_back(s.substr(pos1));
	}

	vector<string> split(const string &s, const string &seperator) {
		vector<string> result;
		typedef string::size_type string_size;
		string_size i = 0;

		while(i != s.size()) {
			//找到字符串中首个不等于分隔符的字母；
			int flag = 0;
			while(i != s.size() && flag == 0) {
				flag = 1;
				for(string_size x = 0; x < seperator.size(); ++x)
					if(s[i] == seperator[x]) {
							++i;
							flag = 0;
							break;
					}
			}

			//找到又一个分隔符，将两个分隔符之间的字符串取出；
			flag = 0;
			string_size j = i;
			while(j != s.size() && flag == 0) {
				for(string_size x = 0; x < seperator.size(); ++x)
					if(s[j] == seperator[x]) {
						flag = 1;
						break;
					}
				if(flag == 0)
					++j;
			}
			if(i != j) {
				result.push_back(s.substr(i, j-i));
				i = j;
			}
		}
		return result;
	}

	char* G2U(const char* gb2312) {
		int len = MultiByteToWideChar(CP_ACP, 0, gb2312, -1, NULL, 0);
		wchar_t* wstr = new wchar_t[len+1];
		memset(wstr, 0, len+1);
		MultiByteToWideChar(CP_ACP, 0, gb2312, -1, wstr, len);
		len = WideCharToMultiByte(CP_UTF8, 0, wstr, -1, NULL, 0, NULL, NULL);
		char* str = new char[len+1];
		memset(str, 0, len+1);
		WideCharToMultiByte(CP_UTF8, 0, wstr, -1, str, len, NULL, NULL);
		if(wstr) delete[] wstr;
		return str;
	}

	int sendrev() {
		WORD wVersionRequested;
		WSADATA wsaData;
		int err;

		wVersionRequested = MAKEWORD(1,1);
		err = WSAStartup(wVersionRequested,&wsaData);

		cout<<"startup:"+err<<endl;

		if ( err != 0 ) {
			return 1;
		}
		if ( LOBYTE( wsaData.wVersion ) != 1 ||
		        HIBYTE( wsaData.wVersion ) != 1 ) {
			WSACleanup( );
			return 1;
		}
		SOCKET sockClient=socket(AF_INET,SOCK_STREAM,0);
		SOCKADDR_IN addrSrv;
		addrSrv.sin_addr.S_un.S_addr=inet_addr("127.0.0.1");
		addrSrv.sin_family=AF_INET;
		addrSrv.sin_port=htons(10122);
		connect(sockClient,(SOCKADDR*)&addrSrv,sizeof(SOCKADDR));

		char *s= "g,23.0,45.39,200,1,110";
		char d[20];
		memcpy(d,s,strlen(s));
		d[strlen(s)]=0;
		printf( "%s ",d);
		int t;

		char flag=1;
		char *data= "我们g,23.0,45.39,200,1,110";//
		data = G2U(data);
		u_long msgCode= 0x000002;

		u_long flagSize=1;
		u_long lengthSize=4;
		u_long msgCodeSize=4;

		u_short datalen = strlen(data);
		u_long length = flagSize+lengthSize+msgCodeSize+strlen(data)+2;

		int bufSize = length + (length % 4 == 0)?0:(4 - length % 4);
		char buf[bufSize];

		u_long length_n = htonl(msgCodeSize + strlen(data) + 2);
		u_long msgCode_n = htonl(msgCode);
		u_long datalen_n = htons(datalen);

		memcpy(buf , &flag , flagSize);
		memcpy(buf + flagSize, &length_n , lengthSize);
		memcpy(buf + flagSize+ lengthSize, &msgCode_n , msgCodeSize);
		memcpy(buf + flagSize+ lengthSize + msgCodeSize, &datalen_n , 2);
		memcpy(buf + flagSize+ lengthSize + msgCodeSize + 2, data , length);

		send(sockClient,buf,length,0);

		//	send(sockClient,"hello",strlen("hello")+1,0);
		char recvBuf[1024 * 5];
		u_long retLen = recv(sockClient,recvBuf,1024 * 5,0);

		u_long statusSize=4;
		char defaultFlag;
		//u_long length;
		//u_long msgCode;
		u_long status;
		u_short responseDataLen;


		memcpy(&defaultFlag , recvBuf , flagSize);

		memcpy(&length, recvBuf+flagSize , lengthSize);
		length = ntohl(length);

		memcpy(&msgCode , recvBuf + flagSize + lengthSize, msgCodeSize);
		msgCode = ntohl(msgCode);

		memcpy(&status, recvBuf  + flagSize + lengthSize + msgCodeSize, statusSize);
		status = ntohl(status);

		memcpy(&responseDataLen, recvBuf + flagSize + lengthSize + msgCodeSize + statusSize, 2);
		responseDataLen = ntohs(responseDataLen);

		char retdata[responseDataLen+1];
		memcpy(retdata, recvBuf  + flagSize + lengthSize + msgCodeSize+statusSize + 2, responseDataLen);

		retdata[responseDataLen] = 0;

		printf("%s\n",retdata);

		if(status==0) {
			//Sucess. The return should be json or dot join string
			if(msgCode == MSG_CODE_DATA_UPLOAD_RES) {
				string ret = string(retdata);
				printf("%s",ret.data());
			}
		}
		closesocket(sockClient);
		WSACleanup();
		return 0;
	}
}
