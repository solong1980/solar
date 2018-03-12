#include "stdafx.h"
#include "Graphic.h"
#include "Point.h"

int Graphic::m_screen_width = SCREEN_WIDHT;
int Graphic::m_screen_height = SCREEN_HEIGHT;

Rect Graphic::m_rectScreen(Point(0, 0), Point(SCREEN_WIDHT, SCREEN_HEIGHT));
Rect Graphic::m_rectBattleGround(Point(BATTLE_GROUND_X1, BATTLE_GROUND_Y1), Point(BATTLE_GROUND_X2, BATTLE_GROUND_Y2));

void Graphic::Create() {
	//m_rectScreen.Set(0, 0, SCREEN_WIDHT, SCREEN_HEIGHT);
	initgraph(m_screen_width, m_screen_height);
	setbkcolor(DARKGRAY);
	//m_rectBattleGround.Set(BATTLE_GROUND_X1, BATTLE_GROUND_Y1, BATTLE_GROUND_X2, BATTLE_GROUND_Y2);
}

void Graphic::Destroy() {
	closegraph();
}

int Graphic::GetScreenWidth() {
	return m_screen_width;
}

int Graphic::GetScreenHeight() {
	return m_screen_height;
}

void Graphic::DrawBattleGround()
{
	rectangle(m_rectBattleGround.GetStartPoint().GetX(), m_rectBattleGround.GetStartPoint().GetY(),
		m_rectBattleGround.GetEndPoint().GetX(), m_rectBattleGround.GetEndPoint().GetY());
}

Rect Graphic::GetBattleGround()
{
	return m_rectBattleGround;
}

