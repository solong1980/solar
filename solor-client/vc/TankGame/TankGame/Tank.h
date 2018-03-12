#pragma once
#include <graphics.h>
#include "Point.h"
#include "Rect.h"

enum Dir { UP, DOWN, LEFT, RIGHT };
/*
class Bullet
{
public:
	Bullet();
	~Bullet();
private:
	int x;
	int y;
	int shape;
};

class Tank
{
public:
	Tank();
	~Tank();
public:
	void drawTank();
	void move(int direct, int step);
	void calcMaskArea();
	int x;
	int y;
	const static int TANK_SIZE = 80;
};
*/

class Tank {
public:
	virtual void Display() = 0;
	virtual void Move() = 0;
	Tank() {};
protected:
	virtual void CalculateSphere() = 0;
	Point m_pos;
	Rect m_rectSphere;//ÊÆÁ¦·¶Î§
	int m_x;
	int m_y;
	COLORREF m_color;

	Dir m_dir;
	int m_step;

};