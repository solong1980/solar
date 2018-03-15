#include "stdafx.h"
#include "Graphic.h"
#include "Point.h"
#include "Setting.h"
 
int Graphic::m_screen_width = SCREEN_WIDTH;
int Graphic::m_screen_height = SCREEN_HEIGHT;

Rect Graphic::m_rectScreen(Point(0, 0), Point(SCREEN_WIDTH, SCREEN_HEIGHT));
Rect Graphic::m_rectBattleGround(Point(BATTLE_GROUND_X1, BATTLE_GROUND_Y1), Point(BATTLE_GROUND_X2, BATTLE_GROUND_Y2));

void Graphic::Create() {
	//m_rectScreen.Set(0, 0, SCREEN_WIDHT, SCREEN_HEIGHT);
	initgraph(m_screen_width, m_screen_height);
	setbkcolor(DARKGRAY);
	//m_rectBattleGround.Set(BATTLE_GROUND_X1, BATTLE_GROUND_Y1, BATTLE_GROUND_X2, BATTLE_GROUND_Y2);
}

void Graphic::ShowScore() {
	COLORREF fill_color_save = getfillcolor();
	COLORREF color_save = getcolor();

	rectangle(SCORE_LEFT, SCORE_TOP, SCREEN_WIDTH -5, SCORE_TOP + 40);
	RECT r = { SCORE_LEFT, SCORE_TOP, SCREEN_WIDTH -5, SCORE_TOP + 40 };

	LPSTR m_pArray = new TCHAR[1024];
	wsprintf(m_pArray, _T("第%d关"), Setting::GetGameLevel());
	drawtext((LPSTR)m_pArray, &r, DT_CENTER | DT_VCENTER | DT_SINGLELINE);

	r.top += 50;
	r.bottom += 50;
	wsprintf(m_pArray, _T("分  数  :  %d"), Setting::GetSumScore());
	drawtext(m_pArray, &r, DT_VCENTER | DT_SINGLELINE);

	r.top += 50;
	r.bottom += 50;
	wsprintf(m_pArray, _T("级  别  :  %d"), Setting::GetTankLevel());
	drawtext(m_pArray, &r, DT_VCENTER | DT_SINGLELINE);

	r.top += 50;
	r.bottom += 50;
	wsprintf(m_pArray, _T("生  命  :  %d"), Setting::GetLife());
	drawtext(m_pArray, &r, DT_VCENTER | DT_SINGLELINE);

	r.top += 50;
	r.bottom += 50;
	wsprintf(m_pArray, _T("敌人数  :  %d"), Setting::GetTankNum());
	drawtext(m_pArray, &r, DT_VCENTER | DT_SINGLELINE);

	r.top += 50;
	r.bottom += 50;

	line(SCORE_LEFT, r.bottom, SCREEN_WIDTH - 5, r.bottom);

	r.top += 50;
	r.bottom += 50;
	wsprintf(m_pArray, _T("共击毁敌人数  :  %d"), Setting::GetTankSum());
	drawtext(m_pArray, &r, DT_VCENTER | DT_SINGLELINE);

	setcolor(color_save);
	setfillcolor(fill_color_save);

	delete m_pArray;
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

