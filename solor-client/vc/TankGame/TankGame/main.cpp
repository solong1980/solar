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
#define MAXSTAR 200 // 星星总数


Star stars[MAXSTAR];
MainTank mainTank;
list<Tank*> lstEnemys;
list<Object*> lstMainBullets;
list<Object*> lstEnemyBullets;
list<Object*> lstBombs;

// 初始化所有星星
void InitStar()
{
	for (int i = 0; i < MAXSTAR; i++)
	{
		stars[i].InitStar();
		stars[i].x = rand() % Graphic::GetScreenWidth();
	}
}

// 移动所有星星
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
			//添加一个爆炸对象
			(*it)->Boom(lstBombs);
			delete (*it);
			//从敌人队列移出
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
	//检查碰撞,遍历主战坦克子弹
	for (list<Object*>::iterator itt = lstMainBullets.begin(); itt != lstMainBullets.end();) {
		//遍历敌人
		for (list<Tank*>::iterator it = lstEnemys.begin(); it != lstEnemys.end(); ) {
			//Rect rectB = (*it)->GetSphere();
			//bool intersect = Shape::CheckIntersect(rectA, rectB);
			if (CheckCrash((*itt), (*it))) {
				//设置敌人坦克消失
				(*it)->SetDisappear();
			}
			it++;
		}
		itt++;
	}
}

void CheckMainCrash() {
	//检查碰撞,遍历敌人坦克子弹
	for (list<Object*>::iterator itt = lstEnemyBullets.begin(); itt != lstEnemyBullets.end();) {
		//检查主战坦克
		Rect rectA = (*itt)->GetSphere();
		Rect rectB = mainTank.GetSphere();
		bool intersect = Shape::CheckIntersect(rectA, rectB);
		if (intersect) {
			//主战坦克被击中,设置状态为消失态
			mainTank.SetDisappear();
		}
		itt++;
	}
}

int main(int argc, char** argv) {

	srand((unsigned)time(NULL));    // 随机种子

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
			case 224: // 方向键高8位  
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

			//遍历爆炸对列对象
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
			//遍历主战子弹对象
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
				//主战坦克Bomb对象
				mainTank.Boom(lstBombs);
				//break;
			}else{
				mainTank.Move();
				mainTank.Display();
			}
			//移动敌人
			MoveEnemy(lstEnemys);
			// 绘制星空
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
