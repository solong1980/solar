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
void InitEnemy(list<Object*>& lstEnemys)
{
	for (int i = 0; i < MAX_TANKS; i++)
	{
		EnemyTank* pTank = new EnemyTank();
		pTank->Display();
		lstEnemys.push_back(pTank);
	}
}

void MoveEnemy(list<Object*>& lstEnemys) {
	for (list<Object*>::iterator it = lstEnemys.begin(); it != lstEnemys.end();) {
		(*it)->Move();
		(*it)->Display();
		it++;
	} 
}



int main(int argc, char** argv) {

	srand((unsigned)time(NULL));    // 随机种子

	Graphic::Create();
 
	list<Object*> lstEnemys;
	list<Object*> lstBullets;
	list<Object*> lstBombs;
	lstBullets.clear();
	lstBombs.clear();
	lstEnemys.clear();

	InitStar();
	InitEnemy(lstEnemys);
	MainTank mainTank;
	
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
				mainTank.Shoot(lstBullets);
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
					delete (*it);
					it = lstBombs.erase(it);
					continue;
				}
				(*it)->Display();
				it++;
			}
			for (list<Object*>::iterator it = lstBullets.begin(); it != lstBullets.end();) {
				(*it)->Move();
				if ((*it)->IsDisappear()) {
					Bomb* bomb = new Bomb((*it)->getPos(), BombType::SMALL);
					//创建爆炸对象
					bomb->Boom(lstBombs);
					delete (*it);
					it = lstBullets.erase(it);
					continue;
				}

				(*it)->Display();
				it++;
			}
			//检查碰撞,遍历子弹
			for (list<Object*>::iterator it = lstBullets.begin(); it != lstBullets.end();) {
				Rect rectA = (*it)->GetSphere();
				//遍历敌人
				for (list<Object*>::iterator it = lstEnemys.begin(); it != lstEnemys.end(); ) {
					Rect rectB = (*it)->GetSphere();
					bool intersect = Shape::CheckIntersect(rectA, rectB);
					if (intersect) {
						//敌人被击中,放入bomb list
						(*it)->Boom(lstBombs);
						//从敌人队列移出
						it = lstEnemys.erase(it);
						continue;
					}
					it++;
				}
				Rect rectB = mainTank.GetSphere();
				bool intersect = Shape::CheckIntersect(rectA, rectB);
				if (intersect) {
					//敌人被击中,放入bomb list
					mainTank.Boom(lstBombs);
					continue;
				}

				//如果碰撞
				//添加子弹Bomb对象,坦克Bomb对象
				it++;
			}


			cleardevice();
			Graphic::DrawBattleGround();
			mainTank.Move();
			mainTank.Display();

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
	for (list<Object*>::iterator it = lstEnemys.begin(); it != lstEnemys.end();) {
		delete (*it);
		it++;
	}
	lstEnemys.clear();
	for (list<Object*>::iterator it = lstBullets.begin(); it != lstBullets.end();) {
		delete (*it);
		it++;
	}
	lstBullets.clear();

	for (list<Object*>::iterator it = lstBombs.begin(); it != lstBombs.end();) {
		delete (*it);
		it++;
	}
	lstBombs.clear();

	Graphic::Destroy();
}
