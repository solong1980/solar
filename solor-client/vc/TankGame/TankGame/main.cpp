#include "stdafx.h"
#include <iostream>
#include <graphics.h>
#include <time.h>
#include <conio.h>
#include "Shape.h"
#include "Graphic.h"
#include "MainTank.h"
#include "EnemyTank.h"
#include "Bomb.h"
#include "star.h"
#include <typeinfo>
#define MAXSTAR 200 // ��������


Star stars[MAXSTAR];
MainTank mainTank;
list<Tank*> lstEnemys;
list<Object*> lstMainBullets;
list<Object*> lstEnemyBullets;
list<Object*> lstBombs;

// ��ʼ����������
void InitStar()
{
	for (int i = 0; i < MAXSTAR; i++)
	{
		stars[i].InitStar();
		stars[i].x = rand() % Graphic::GetScreenWidth();
	}
}

// �ƶ���������
void MoveStar()
{
	for (int i = 0; i < MAXSTAR; i++)
		stars[i].MoveStar();

}

#define MAX_TANKS 10  
//EnemyTank enemyTanks[MAX_TANKS];
void InitEnemy(list<Tank*>& lstEnemys)
{
	for (int i = 0; i < MAX_TANKS; i++)
	{
		Tank* pTank = new EnemyTank();
		pTank->Display();
		lstEnemys.push_back(pTank);
	}
}

void MoveEnemy(list<Tank*>& lstEnemys) {
	for (list<Tank*>::iterator it = lstEnemys.begin(); it != lstEnemys.end();) {
		if ((*it)->IsDisappear()) {
			//���һ����ը����
			(*it)->Boom(lstBombs);
			delete (*it);
			//�ӵ��˶����Ƴ�
			it = lstEnemys.erase(it);
			continue;
		}
		(*it)->Move();
		if ((*it)->NeedShoot()) {
			((EnemyTank*)(*it))->Shoot(lstEnemyBullets);
		}
		(*it)->Display();
		it++;
	} 
}

bool CheckCrash(Object* bullet, Tank* tank) {
	if (Shape::CheckIntersect(bullet->GetSphere(), tank->GetSphere())) {
		return true;
	}
	return false;
}

void CheckCrash() {
	//�����ײ,������ս̹���ӵ�
	for (list<Object*>::iterator itt = lstMainBullets.begin(); itt != lstMainBullets.end();) {
		//��������
		for (list<Tank*>::iterator it = lstEnemys.begin(); it != lstEnemys.end(); ) {
			//Rect rectB = (*it)->GetSphere();
			//bool intersect = Shape::CheckIntersect(rectA, rectB);
			if (CheckCrash((*itt), (*it))) {
				//���õ���̹����ʧ
				(*it)->SetDisappear();
			}
			it++;
		}
		itt++;
	}
}

void CheckMainCrash() {
	//�����ײ,��������̹���ӵ�
	for (list<Object*>::iterator itt = lstEnemyBullets.begin(); itt != lstEnemyBullets.end();) {
		//�����ս̹��
		Rect rectA = (*itt)->GetSphere();
		Rect rectB = mainTank.GetSphere();
		bool intersect = Shape::CheckIntersect(rectA, rectB);
		if (intersect) {
			//��ս̹�˱�����,����״̬Ϊ��ʧ̬
			mainTank.SetDisappear();
		}
		itt++;
	}
}

int main(int argc, char** argv) {

	srand((unsigned)time(NULL));    // �������

	Graphic::Create();
 

	lstMainBullets.clear();
	lstEnemyBullets.clear();
	lstBombs.clear();
	lstEnemys.clear();

	InitStar();
	InitEnemy(lstEnemys);

	
	bool loop = true;
	bool skip = false;
	while (loop) {
		if (_kbhit()) {
			int key = _getch();
			switch (key) {
				// Up  
			case 72:
				mainTank.SetDir(Dir::UP);
				break;
				// Down  
			case 80:
				mainTank.SetDir(Dir::DOWN);
				break;
				// Left  
			case 75:
				mainTank.SetDir(Dir::LEFT);
				break;
				// Right  
			case 77:
				mainTank.SetDir(Dir::RIGHT);
				break;
			case 224: // �������8λ  
				break;
				// Esc  
			case 27:
				loop = false;
				break;
				// Space  
			case 32:
				mainTank.Shoot(lstMainBullets);
				break;
				// Enter  
			case 13:
				if (skip)
					skip = false;
				else
					skip = true;
				break;
			default:
				break;

			}
		}
		if (!skip) {
			cleardevice();

			Graphic::DrawBattleGround();
			Graphic::ShowScore();
			CheckCrash();
			CheckMainCrash();

			//������ը���ж���
			for (list<Object*>::iterator it = lstBombs.begin(); it != lstBombs.end();) {
				(*it)->Move();
				/*
				string oType = typeid((**it)).name();
				
				std::cout << oType.c_str() << std::endl;

				if (strcmp(oType.c_str(), "Tank")) {
					EnemyTank* tank = dynamic_cast<EnemyTank*>(*it);
				}
				*/
				if ((*it)->IsDisappear()) {
					(*it)->Boom(lstBombs);
					delete (*it);
					it = lstBombs.erase(it);
					continue;
				}
				(*it)->Display();
				it++;
			}
			//������ս�ӵ�����
			for (list<Object*>::iterator it = lstMainBullets.begin(); it != lstMainBullets.end();) {
				(*it)->Move();
				if ((*it)->IsDisappear()) {
					(*it)->Boom(lstBombs);
					delete (*it);
					it = lstMainBullets.erase(it);
					continue;
				}
				(*it)->Display();
				it++;
			}

			for (list<Object*>::iterator it = lstEnemyBullets.begin(); it != lstEnemyBullets.end();) {
				(*it)->Move();
				if ((*it)->IsDisappear()) {
					(*it)->Boom(lstBombs);
					delete (*it);
					it = lstEnemyBullets.erase(it);
					continue;
				}
				(*it)->Display();
				it++;
			}

			if (mainTank.IsDisappear()) {
				//��ս̹��Bomb����
				mainTank.Boom(lstBombs);
				//break;
			}else{
				mainTank.Move();
				mainTank.Display();
			}
			//�ƶ�����
			MoveEnemy(lstEnemys);
			// �����ǿ�
			MoveStar();

			Sleep(40);
		}

	}

	/*
	while (!_kbhit())
	{

	}

	for (size_t i = 0; i < MAX_TANKS; i++)
	{
		delete pTank[i];
	}
	*/
	for (list<Tank*>::iterator it = lstEnemys.begin(); it != lstEnemys.end();) {
		delete (*it);
		it++;
	}
	lstEnemys.clear();
	for (list<Object*>::iterator it = lstMainBullets.begin(); it != lstMainBullets.end();) {
		delete (*it);
		it++;
	}
	lstMainBullets.clear();

	for (list<Object*>::iterator it = lstBombs.begin(); it != lstBombs.end();) {
		delete (*it);
		it++;
	}
	lstBombs.clear();

	Graphic::Destroy();
}
