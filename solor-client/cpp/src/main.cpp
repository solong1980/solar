#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <vector>
#include <thread>
#pragma comment(lib, "json/json_reader.o")

using namespace std;
namespace JsonCall {
	void readFromFile();
	void readFromStr();
}
namespace NetSend {
	void SplitString(const string& s, vector<string>& v, const string& c);
	void sendrev();
	void sendmsg();
	void send_data();
	void send_upgrade();
}

void splitStr() {
	char a[] = "12,1,a";
	string st = string(a);
	printf("%s",st.data());
	printf("%s","\r\n");
	vector<string> v;
	NetSend::SplitString(st,v,string(","));
	for(vector<string>::size_type i = 0; i != v.size(); ++i) {
		cout<<v[i]<<endl;
	}
}

void hello(){
    std::cout << "Hello from thread " << std::endl;
}

int main(int argc, char** argv) {
	//JsonCall::readFromFile();
	std::thread t1(hello);
	t1.join();
	std::thread t2(NetSend::send_data);
	t2.join();
	cout<<"--"<<endl;
	return 0;
}
