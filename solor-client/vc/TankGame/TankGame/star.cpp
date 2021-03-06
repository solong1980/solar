#include "stdafx.h"
#include "star.h"
#include "Graphic.h"
// 初始化星星
void Star::InitStar()
{
	x = Graphic::GetBattleGround().GetStartPoint().GetX();
	y = rand() % Graphic::GetBattleGround().getHeight();
	step = (rand() % 5000) / 1000.0 + 1;
	color = (int)(step * 255 / 6.0 + 0.5);  // 速度越快，颜色越亮
	color = RGB(color, color, color);
}
// 移动星星
void Star::MoveStar()
{
	// 擦掉原来的星星
	//putpixel((int)x, y, 0);

	// 计算新位置
	x += step;
	if (x >  Graphic::GetBattleGround().GetEndPoint().GetX())
		InitStar();

	// 画新星星
	putpixel((int)x, y, color);
}