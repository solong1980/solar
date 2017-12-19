#include <iostream>
#include <winsock2.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "json/json.h"
#include <fstream>
#include <cassert>
using namespace std;
#pragma comment(lib, "json/json_reader.o")

namespace JsonCall {
	int a;

	void readFromFile() {
		ifstream ifs;
		ifs.open("WeaponConfiguration.json");
		assert(ifs.is_open());

		Json::Reader reader;
		Json::Value root;
		if (!reader.parse(ifs, root, false)) {
			printf("Parse error");
			return;
		}

		assert(root.isArray());

		int size = root.size();

		cout << size << endl;

		for (int i = 0; i < size; i++) {
			Json::Value &current = root[i];

			string name = root[i]["Name"].asString();
			cout << name << endl;

			Json::Value &attribute = root[i]["WeaponAttribute"];
			int lenght = attribute.size();

			int id = attribute["ID"].asInt();
			int attackValue = attribute["AttackValue"].asInt();
			int defense = attribute["Defense"].asInt();

			cout << id << endl;
			cout << attackValue << endl;
			cout << defense << endl;
		}

		//cin >> a;
	}

	void readFromStr() {
		Json::Reader reader;
		std::string json="{\"devId\":\"000000\",\"port\":10122,\"serverIP\":\"123.56.76.77\",\"array\":[{\"key2\":\"value2\"},{\"key2\":\"value3\"},{\"key2\":\"value4\"}]}";

		Json::Value root;
		if (reader.parse(json, root)) {
			std::string out = root["devId"].asString();
			std::cout << out << std::endl;
			const Json::Value arrayObj = root["array"];
			for (int i=0; i<arrayObj.size(); i++) {
				out = arrayObj[i]["key2"].asString();
				std::cout << out << std::endl;
			}
		}
	}
}


