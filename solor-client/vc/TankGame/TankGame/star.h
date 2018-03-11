#pragma once
#include <graphics.h>
#include <time.h>
#include <conio.h>
#include "Graphic.h"

#define MAXSTAR 200 // ÐÇÐÇ×ÜÊý

class STAR
{
public:
	double  x;
	int     y;
	double  step;
	int     color;
public:
	void InitStar();
	void MoveStar();
};
