#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <vector>
#pragma comment(lib, "json/json_reader.o")

using namespace std;
namespace JsonCall {
	void readFromFile();
	void readFromStr();
}
namespace NetSend {
	void SplitString(const string& s, vector<string>& v, const string& c);
	void sendrev();
}
int main(int argc, char** argv) {
	JsonCall::readFromFile();
	char a[] = "12,1,a";
	string st = string(a);
	printf("%s",st.data());
	printf("%s","\r\n");
	vector<string> v;
	NetSend::SplitString(st,v,string(","));

	for(vector<string>::size_type i = 0; i != v.size(); ++i) {
		cout<<v[i]<<endl;
	}

	NetSend::sendrev();
	return 0;
}
