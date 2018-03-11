#include "stdafx.h"
#include "star.h"
// 初始化星星
void STAR::InitStar()
{
	x = 0;
	y = rand() % Graphic::GetScreenHeight();
	step = (rand() % 5000) / 1000.0 + 1;
	color = (int)(step * 255 / 6.0 + 0.5);  // 速度越快，颜色越亮
	color = RGB(color, color, color);
}
// 移动星星
void STAR::MoveStar()
{
	// 擦掉原来的星星
	putpixel((int)x, y, 0);

	// 计算新位置
	x += step;
	if (x > 640)
		InitStar();

	// 画新星星
	putpixel((int)x, y, color);
}