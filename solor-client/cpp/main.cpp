#include <iostream>
#include <winsock2.h>
#include <stdio.h>
#include   <stdlib.h>             
#include   <string.h>   
using namespace std;

#pragma comment(lib, "ws2_32")
/* run this program using the console pauser or add your own getch, system("pause") or input loop */

char* G2U(const char* gb2312)
{
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
int main(int argc, char** argv) {
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
	char *data= "ÎÒÃÇg,23.0,45.39,200,1,110";//
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
	
	memcpy(&msgCode , recvBuf + flagSize+ lengthSize, msgCodeSize);
	msgCode = ntohl(msgCode);
	
	memcpy(&status, recvBuf  + flagSize + lengthSize + msgCodeSize, statusSize);
	status = ntohl(status);
	
	memcpy(&responseDataLen, recvBuf + flagSize + lengthSize + msgCodeSize + statusSize, 2);
	responseDataLen = ntohs(responseDataLen);
	
	char retdata[responseDataLen+1];
	memcpy(retdata, recvBuf  + flagSize + lengthSize + msgCodeSize+statusSize + 2, responseDataLen);
	
	retdata[responseDataLen]=0;
	printf("%s\n",retdata);
	 
	closesocket(sockClient);
	WSACleanup();	
	return 0;
}
