#include <iostream>
#include <winsock2.h>
#include <windows.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <vector>
#include <sstream>
#include <iomanip>
#include <thread>
using namespace std;

#pragma comment(lib, "ws2_32")

#define MSG_CODE_DATA_UPLOAD_RES 0x10008


namespace NetSend {
	int count=0;

	class NetWork {
		private:
			string ip;
			int port;
			SOCKET socketClient;
		public:
			//复制构造
			//赋值构造
			//构造
			NetWork(const string& ip,const int port) {
				this->ip = ip;
				this->port = port;

			}
			NetWork(const NetWork& network) {
				this->ip = network.ip;
				this->port = network.port;
			}

			bool connect();
			void send();
			void rev();
			void close();
	};

	//int转byte
	void  intToByte(int i,byte *bytes,int size = 4) {
		//byte[] bytes = new byte[4];
		memset(bytes,0,sizeof(byte) *  size);
		bytes[0] = (byte) (0xff & i);
		bytes[1] = (byte) ((0xff00 & i) >> 8);
		bytes[2] = (byte) ((0xff0000 & i) >> 16);
		bytes[3] = (byte) ((0xff000000 & i) >> 24);
		return ;
	}

	//byte转int
	int bytesToInt(byte* bytes,int size = 4) {
		int addr = bytes[0] & 0xFF;
		addr |= ((bytes[1] << 8) & 0xFF00);
		addr |= ((bytes[2] << 16) & 0xFF0000);
		addr |= ((bytes[3] << 24) & 0xFF000000);
		return addr;
	}

	//byte转short
	short bytesToShort(byte* bytes,int size = 2) {
		short addr = bytes[0] & 0xFF;
		addr |= ((bytes[1] << 8) & 0xFF00);
		return addr;
	}

	string bytes_to_hexstr(unsigned char* first, unsigned char* last) {
		ostringstream oss;
		oss << hex << setfill('0');
		while(first<last) oss << setw(2) << int(*first++);
		return oss.str();
	}

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

	FILE* create_file() {
		FILE *fp = fopen("testBin.jpg","wb");   //二进制模式
		return fp;
	}

	void write_file(FILE* fp,char* buf,int len) {
		int successCont=fwrite(buf,sizeof(char),len,fp);
		if(successCont!=len)
			cout<<"error"<<endl;
	}

	void close_file(FILE* fp) {
		fclose(fp);
	}

	string receive_block_data(SOCKET& sockClient,FILE* fp) {
		char recvBuf[1024*4];
		int retLen = recv(sockClient,recvBuf,1024 * 4,0);
		cout<<"retLen="<<retLen<<endl;
		if(retLen <= 0) {
			return "";
		}
		recvBuf[retLen] = '\0';

		//响应码
		char no[3];
		memcpy(no ,recvBuf , 2);
		no[2] = '\0';
		cout << "msgCode:"<<string(no) << endl;
		//逗号
		memcpy(no ,recvBuf+2 , 1);
		no[1] = '\0';
		cout << "dot:"<<string(no) << endl;
		//包号
		memcpy(no ,recvBuf+3 , 2);
		no[2] = '\0';
		short blockNo = ntohs(bytesToShort((byte*)no));

		cout <<"package no:"<< blockNo<< endl;
		if(blockNo==(short)0x7FFF) {
			string hexStr = bytes_to_hexstr((byte*)(recvBuf+5),(byte*)(recvBuf+retLen));
			cout << hexStr << endl;
			return "";
		} else {
			string hexStr = bytes_to_hexstr((byte*)(recvBuf+5),(byte*)(recvBuf+retLen));
			cout << hexStr << endl;
			write_file(fp,recvBuf+5,retLen-5);
		}

		return to_string(blockNo);
	}

	int send_upgrade() {
		WORD wVersionRequested;
		WSADATA wsaData;
		int err;
		wVersionRequested = MAKEWORD(1,1);
		err = WSAStartup(wVersionRequested,&wsaData);
		cout<<"startup: "+err<<endl;
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

		addrSrv.sin_port=htons(10124);
		connect(sockClient,(SOCKADDR*)&addrSrv,sizeof(SOCKADDR));

		char* res="04,01,-1";
		//res = "04,01,0";
		char deli  = '\n';
		send(sockClient,res,strlen(res),0);
		send(sockClient,&deli,1,0);

		FILE* fp = create_file();
		string packageNo = receive_block_data(sockClient,fp);
		int c = 0;
		while(!packageNo.empty()) {
			//cout<<"package no:"<<a<<endl;
//			c++;
//			if(c==22) {
//				break;
//			}

			string resp= "04,01,"+packageNo;
//			if( 20<=c&&c<=22) {
//				resp = "04,"+packageNo+",00";
//			}

			cout<<"resp:"<<resp<<endl;
			send(sockClient,resp.data(),resp.length(),0);
			send(sockClient,&deli,1,0);
			cout<<"rev-----start"<<endl;
			packageNo = receive_block_data(sockClient,fp);
			cout<<"rev-----end"<<endl;
		}
		close_file(fp);

		cout<<"close-----"<<endl;
		closesocket(sockClient);
		WSACleanup();
		return 0;
	}

	int dataupload(SOCKET& sockClient,const char *data) {
		NetSend::count++;
		cout<<"send data"<<NetSend::count<<endl;
		char deli  = '\n';
		data = G2U(data);
		send(sockClient,data,strlen(data),0);
		send(sockClient,&deli,1,0);
		return 1;
	}
	int dataupload1(SOCKET& sockClient) {
		return 1;
	}
	/**
	* 循环发送数据
	*/
	int send_data() {
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

		addrSrv.sin_port=htons(10124);
		connect(sockClient,(SOCKADDR*)&addrSrv,sizeof(SOCKADDR));


		char *data= "01,17DD5E6E,1001,233,6,225,15,0,0,0,0,0,17,0,0,0,0,20171224080052,a,b";
        data="01,199E804C,03,0,5,224,14,55236,0,0,0,0,0,19,0,0,0,C000,20180115022957,30.477093,114.414081";
		std::thread t1(dataupload,std::ref(sockClient),data);
	//  std::thread t2(dataupload,std::ref(sockClient),data);
	//	std::thread t3(dataupload,std::ref(sockClient),data);
		
	//	std::thread t4(dataupload,std::ref(sockClient),data);
	//	std::thread t5(dataupload,std::ref(sockClient),data);
	//	std::thread t6(dataupload,std::ref(sockClient),data);
		
		
		/**
		Sleep(10*1000);
		//data= "01,17DD5E6E,1,233,6,225,15,0,0,0,0,0,17,0,0,0,0,20171224080052,a,b";
		dataupload(sockClient,data);
		Sleep(10*1000);
		//data= "01,17DD5E6E,1,233,6,225,15,0,0,0,0,0,17,0,0,0,0,20171224080052,a,b";
		dataupload(sockClient,data);
		Sleep(10*1000);
		//data= "01,17DD5E6E,1,233,6,225,15,0,0,0,0,0,17,0,0,0,0,20171224080052,a,b";
		dataupload(sockClient,data);
		*/
		Sleep(1000);
		closesocket(sockClient);
		WSACleanup();
		return 0;
	}

	int sendmsg() {
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

		addrSrv.sin_port=htons(10124);
		connect(sockClient,(SOCKADDR*)&addrSrv,sizeof(SOCKADDR));


		char *data= "01,17DD5E6E,1,233,6,225,15,0,0,0,0,0,17,0,0,0,0,20171224080052,a,b";
		char deli  = '\n';
		data = G2U(data);
		send(sockClient,data,strlen(data),0);
		//send(sockClient,d,strlen(d),0);
		send(sockClient,&deli,1,0);

		char recvBuf[1024 * 1024+11];
		u_long retLen = recv(sockClient,recvBuf,1024 * 1024+11,0);
		recvBuf[retLen] = '\0';
		//printf("%s",recvBuf);
		cout<<string(recvBuf)<<endl;

		retLen = recv(sockClient,recvBuf,1024 * 1024+11,0);
		cout<<"retLen="<<retLen<<endl;
		recvBuf[retLen] = '\0';

		char no[3];
		memcpy(no ,recvBuf , 2);
		no[2] = '\0';
		//响应码
		cout << string(no) << endl;
		//包号
		memcpy(no ,recvBuf+2 , 1);
		no[1] = '\0';
		cout << string(no) << endl;

		memcpy(no ,recvBuf+3 , 2);
		cout << ntohs(bytesToShort((byte*)no))<< endl;
		no[2] = '\0';
		//printf("%s",recvBuf);

		string hexStr = bytes_to_hexstr((byte*)(recvBuf+5),(byte*)(recvBuf+1024+5));
		cout << hexStr << endl;

		Sleep(10*1000);

		send(sockClient,data,strlen(data),0);
		send(sockClient,&deli,1,0);
		Sleep(100*1000);
		/*
		retLen = recv(sockClient,recvBuf,1024 * 5,0);
		recvBuf[retLen] = '\0';
		//printf("%s",recvBuf);
		cout<<string(recvBuf)<<endl;
		*/
		closesocket(sockClient);
		WSACleanup();
		return 0;
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
		addrSrv.sin_addr.S_un.S_addr=inet_addr("123.56.76.77");
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
		char *data= "g,23.0,45.39,200,1,110";//
		data = G2U(data);
		u_long msgCode= 0x000002;

		//协议
		u_long flagSize=1;
		u_long lengthSize=4;
		u_long msgCodeSize=4;

		u_short datalen = strlen(data);
		u_long length = flagSize+lengthSize+msgCodeSize+strlen(data)+2;//总长

		int bufSize = length + (length % 4 == 0)?0:(4 - length % 4);//对齐
		char buf[bufSize];

		u_long length_n = htonl(msgCodeSize + strlen(data) + 2);//主机字节序转网络字节序
		u_long msgCode_n = htonl(msgCode);
		u_long datalen_n = htons(datalen);

		memcpy(buf , &flag , flagSize);
		memcpy(buf + flagSize, &length_n , lengthSize);
		memcpy(buf + flagSize+ lengthSize, &msgCode_n , msgCodeSize);
		memcpy(buf + flagSize+ lengthSize + msgCodeSize, &datalen_n , 2);//写utf字符串长度
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
