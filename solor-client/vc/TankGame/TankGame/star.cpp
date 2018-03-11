#include "stdafx.h"
#include "star.h"
// ��ʼ������
void STAR::InitStar()
{
	x = 0;
	y = rand() % Graphic::GetScreenHeight();
	step = (rand() % 5000) / 1000.0 + 1;
	color = (int)(step * 255 / 6.0 + 0.5);  // �ٶ�Խ�죬��ɫԽ��
	color = RGB(color, color, color);
}
// �ƶ�����
void STAR::MoveStar()
{
	// ����ԭ��������
	putpixel((int)x, y, 0);

	// ������λ��
	x += step;
	if (x > 640)
		InitStar();

	// ��������
	putpixel((int)x, y, color);
}