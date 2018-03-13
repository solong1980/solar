#include "stdafx.h"
#include "star.h"
#include "Graphic.h"
// ��ʼ������
void Star::InitStar()
{
	x = Graphic::GetBattleGround().GetStartPoint().GetX();
	y = rand() % Graphic::GetBattleGround().getHeight();
	step = (rand() % 5000) / 1000.0 + 1;
	color = (int)(step * 255 / 6.0 + 0.5);  // �ٶ�Խ�죬��ɫԽ��
	color = RGB(color, color, color);
}
// �ƶ�����
void Star::MoveStar()
{
	// ����ԭ��������
	//putpixel((int)x, y, 0);

	// ������λ��
	x += step;
	if (x >  Graphic::GetBattleGround().GetEndPoint().GetX())
		InitStar();

	// ��������
	putpixel((int)x, y, color);
}