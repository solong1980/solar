#include "stdafx.h"
#include "Graphic.h"
#include "Point.h"
#include "Setting.h"
 
int Graphic::m_screen_width = SCREEN_WIDTH;
int Graphic::m_screen_height = SCREEN_HEIGHT;
TCHAR Graphic::m_pArray[1024];

Rect Graphic::m_rectScreen(Point(0, 0), Point(SCREEN_WIDTH, SCREEN_HEIGHT));
Rect Graphic::m_rectBattleGround(Point(BATTLE_GROUND_X1, BATTLE_GROUND_Y1), Point(BATTLE_GROUND_X2, BATTLE_GROUND_Y2));

void Graphic::Create() {
	//m_rectScreen.Set(0, 0, SCREEN_WIDHT, SCREEN_HEIGHT);
	initgraph(m_screen_width, m_screen_height);
	setbkcolor(DARKGRAY);
	//m_rectBattleGround.Set(BATTLE_GROUND_X1, BATTLE_GROUND_Y1, BATTLE_GROUND_X2, BATTLE_GROUND_Y2);
}

void Graphic::ShowGameLevel(int nLevel) {
	//������ɫ,����
	COLORREF fill_color_save = getfillcolor();
	COLORREF color_save = getcolor();
	LOGFONT fontBak;
	gettextstyle(&fontBak);
	LOGFONT f = fontBak;
	f.lfHeight = 48;                      // ��������߶�Ϊ 48  
	_tcscpy_s(f.lfFaceName, _T("����"));  // ��������Ϊ�����塱  
	f.lfQuality = ANTIALIASED_QUALITY;    // �������Ч��Ϊ�����    
	settextstyle(&f);                     // ����������ʽ  
	wsprintf(m_pArray, _T("�� %d ��"), nLevel);

	outtextxy(BATTLE_GROUND_X1+150, BATTLE_GROUND_Y1+100, m_pArray);

	f.lfHeight = 18;
	settextstyle(&f);
	wsprintf(m_pArray, _T("�� Enter ����ʼ"), nLevel);
	outtextxy(BATTLE_GROUND_X1 + 200, BATTLE_GROUND_Y1 + 250, m_pArray);

	settextstyle(&fontBak);

	setcolor(color_save);
	setfillcolor(fill_color_save);
}

void Graphic::ShowScore() {
	COLORREF fill_color_save = getfillcolor();
	COLORREF color_save = getcolor();

	rectangle(SCORE_LEFT, SCORE_TOP, SCREEN_WIDTH -5, SCORE_TOP + 40);
	RECT r = { SCORE_LEFT, SCORE_TOP, SCREEN_WIDTH -5, SCORE_TOP + 40 };

	wsprintf(m_pArray, _T("��%d��"), Setting::GetGameLevel());
	drawtext(m_pArray, &r, DT_CENTER | DT_VCENTER | DT_SINGLELINE);

	r.top += 50;
	r.bottom += 50;
	wsprintf(m_pArray, _T("��  ��  :  %d"), Setting::GetSumScore());
	drawtext(m_pArray, &r, DT_VCENTER | DT_SINGLELINE);

	r.top += 50;
	r.bottom += 50;
	wsprintf(m_pArray, _T("��  ��  :  %d"), Setting::GetTankLevel());
	drawtext(m_pArray, &r, DT_VCENTER | DT_SINGLELINE);

	r.top += 50;
	r.bottom += 50;
	wsprintf(m_pArray, _T("��  ��  :  %d"), Setting::GetLife());
	drawtext(m_pArray, &r, DT_VCENTER | DT_SINGLELINE);

	r.top += 50;
	r.bottom += 50;
	wsprintf(m_pArray, _T("������  :  %d"), Setting::GetTankNum());
	drawtext(m_pArray, &r, DT_VCENTER | DT_SINGLELINE);

	r.top += 50;
	r.bottom += 50;

	line(SCORE_LEFT, r.bottom, SCREEN_WIDTH - 5, r.bottom);

	r.top += 50;
	r.bottom += 50;
	wsprintf(m_pArray, _T("�����ٵ�����  :  %d"), Setting::GetTankSum());
	drawtext(m_pArray, &r, DT_VCENTER | DT_SINGLELINE);

	setcolor(color_save);
	setfillcolor(fill_color_save);
}

void Graphic::ShowGameOver()
{
	COLORREF fill_color_save = getfillcolor();
	COLORREF color_save = getcolor();

	rectangle(BATTLE_GROUND_X1, BATTLE_GROUND_Y1, SCREEN_WIDTH - BATTLE_GROUND_X1, SCREEN_HEIGHT - BATTLE_GROUND_Y1);

	LOGFONT fontBak;
	gettextstyle(&fontBak);               // ��ȡ��ǰ��������  

	LOGFONT f = fontBak;
	f.lfHeight = 48;                      // ��������߶�Ϊ 48  
	_tcscpy_s(f.lfFaceName, _T("����"));  // ��������Ϊ�����塱  
	f.lfQuality = ANTIALIASED_QUALITY;    // �������Ч��Ϊ�����    
	settextstyle(&f);                     // ����������ʽ  
	wsprintf(m_pArray, _T("GAME OVER"));
	outtextxy(BATTLE_GROUND_X1 + 150, BATTLE_GROUND_Y1 + 100, m_pArray);

	f.lfHeight = 18;
	settextstyle(&f);
	wsprintf(m_pArray, _T("�� Enter ���˳�"));
	outtextxy(BATTLE_GROUND_X1 + 200, BATTLE_GROUND_Y1 + 250, m_pArray);

	settextstyle(&fontBak);

	setcolor(color_save);
	setfillcolor(fill_color_save);
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

