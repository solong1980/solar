#pragma once
#ifndef _GRAPHIC_H_
#define _GRAPHIC_H_

#define SCREEN_WIDHT 512
#define SCREEN_HEIGHT 480

#include <graphics.h>

class Graphic {
public:
	static void Create();
	static void Destroy();
	static int GetScreenWidth();
	static int GetScreenHeight();
private:
	static int m_screen_width;
	static int m_screen_height;
};

#endif