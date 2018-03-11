#pragma once
#include <graphics.h>

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
protected:
	int m_x;
	int m_y;
	COLORREF m_color;

	Dir m_dir;
	int m_step;

};


class MainTank :public Tank {
public:
	MainTank() {
		m_x = 400;
		m_y = 300;
		m_color = WHITE;
		m_dir = Dir::UP;
		m_step = 2;
	}
	~MainTank() {

	}
	void SetDir(Dir dir);
	void Display();
	void Move();
protected:
	void DrawTankBody(int style);
};