#pragma once
#include <graphics.h>
#include <time.h>
#include <conio.h>
#include "Graphic.h"

class Star
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
