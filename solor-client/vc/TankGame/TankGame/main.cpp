#include "stdafx.h"
#include <graphics.h>
#include <time.h>
#include <conio.h>
#include "MainTank.h"
#include "Graphic.h"
#include "star.h"


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



int main(int argc, char** argv) {

	srand((unsigned)time(NULL));    // 随机种子

	Graphic::Create();

	MainTank mainTank;
	InitStar();
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
			mainTank.Move();
			mainTank.Display();
			// 绘制星空
			MoveStar();
			Sleep(20);
		}
		

	}

	/*
	while (!_kbhit())
	{
		
	}*/
	Graphic::Destroy();
}
