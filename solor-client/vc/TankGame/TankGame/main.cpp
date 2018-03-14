#include "stdafx.h"
#include <graphics.h>
#include <time.h>
#include <conio.h>
#include "Graphic.h"
#include "MainTank.h"
#include "EnemyTank.h"
#include "Bomb.h"
#include "star.h"

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
EnemyTank enemyTanks[MAX_TANKS];
void InitEnemy()
{
	for (int i = 0; i < MAX_TANKS; i++)
	{
		enemyTanks[i].Display();
	}
}

void MoveEnemy() {
	for (int i = 0; i < MAX_TANKS; i++)
	{
		enemyTanks[i].Move();
		enemyTanks[i].Display();
	}
}



int main(int argc, char** argv) {

	srand((unsigned)time(NULL));    // 随机种子

	Graphic::Create();
	/*
	Tank* pTank[MAX_TANKS];
	for (int i = 0; i < MAX_TANKS; i++) {
		pTank[i] = new EnemyTank();
	}*/
	list<Tank*> lstEnemys;
	lstEnemys.clear();
	for (int i = 0; i < MAX_TANKS; i++) {
		lstEnemys.push_back(new EnemyTank());
	}

	list<Object*> lstBullets;
	list<Object*> lstBombs;
	lstBullets.clear();
	lstBombs.clear();

	InitStar();

	MainTank mainTank;
	InitEnemy();
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
			//检查碰撞,遍历主战坦克子弹
			for (list<Object*>::iterator it = lstBullets.begin(); it != lstBullets.end();it++) {
				Rect rectA = (*it)->GetSphere();
				//遍历敌人
				//如果碰撞
				//添加子弹Bomb对象,坦克Bomb对象
			}


			cleardevice();
			Graphic::DrawBattleGround();
			mainTank.Move();
			mainTank.Display();

			//移动敌人
			MoveEnemy();
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
