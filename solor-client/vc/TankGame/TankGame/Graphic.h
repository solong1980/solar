#pragma once
#ifndef _GRAPHIC_H_
#define _GRAPHIC_H_

#define SCREEN_WIDHT 612
#define SCREEN_HEIGHT 485

#define BATTLE_GROUND_X1 5  
#define BATTLE_GROUND_Y1 5  
#define BATTLE_GROUND_X2 485  
#define BATTLE_GROUND_Y2 (SCREEN_HEIGHT - BATTLE_GROUND_Y1)  

#include <graphics.h>
#include "Rect.h"

class Graphic {
public:
	static void Create();
	static void Destroy();
	static int GetScreenWidth();
	static int GetScreenHeight();
	static void DrawBattleGround();
	static Rect GetBattleGround();
private:
	static int m_screen_width;
	static int m_screen_height;
	static Rect m_rectScreen;
	static Rect m_rectBattleGround;
};

#endif