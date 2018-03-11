#include "stdafx.h"
#include "Tank.h"
#include "Graphic.h"

void MainTank::SetDir(Dir dir) {
	m_dir = dir;
}

void MainTank::DrawTankBody(int style) {
	fillrectangle(m_x - 4, m_y - 4, m_x + 4, m_y + 4);

	if (style == 1)
	{
		fillrectangle(m_x - 8, m_y - 6, m_x - 6, m_y + 6);
		fillrectangle(m_x + 6, m_y - 6, m_x + 8, m_y + 6);
	}
	else
	{
		fillrectangle(m_x - 6, m_y - 8, m_x + 6, m_y - 6);
		fillrectangle(m_x - 6, m_y + 6, m_x + 6, m_y + 8);
	}
}

void MainTank::Display() {
	COLORREF color_save = getfillcolor();
	setfillcolor(m_color);
	switch (m_dir) {
	case UP:
		DrawTankBody(1);
		line(m_x, m_y, m_x, m_y - 10);
		break;
	case DOWN:
		DrawTankBody(1);
		line(m_x, m_y, m_x, m_y + 10);
		break;
	case LEFT:
		DrawTankBody(0);
		line(m_x, m_y, m_x - 10, m_y);
		break;
	case RIGHT:
		DrawTankBody(0);
		line(m_x, m_y, m_x + 10, m_y);
	default:
		break;
	}
	setfillcolor(color_save);
}

void MainTank::Move() {
	switch (m_dir) {
	case UP:
		m_y -= m_step;
		if (m_y < 0) {
			m_y = Graphic::GetScreenHeight() - 1;
		}
		break;
	case DOWN:
		m_y += m_step;
		if (m_y > Graphic::GetScreenHeight()) {
			m_y = 1;
		}
		break;
	case LEFT:
		m_x -= m_step;
		if (m_x < 0) {
			m_x = Graphic::GetScreenWidth() - 1;
		}
		break;
	case RIGHT:
		m_x += m_step;
		if (m_x > Graphic::GetScreenWidth()) {
			m_x = 1;
		}
		break;
	default:
		break;
	}
}

/*
const int Tank::TANK_SIZE;

Tank::Tank()
{
}


Tank::~Tank()
{
}

void Tank::drawTank() {
	int start_x = this->x - TANK_SIZE / 2;
	int start_y = this->y - TANK_SIZE / 2;
	fillrectangle(
		start_x,
		start_y,
		start_x + TANK_SIZE,
		start_y + TANK_SIZE
	);
}
void Tank::calcMaskArea()
{

}
void Tank::move(int direct, int step)
{
	int start_x = this->x - TANK_SIZE / 2;
	int start_y = this->y - TANK_SIZE / 2;
	setbkcolor(RED);

	rectangle(
		start_x,
		start_y,
		start_x + TANK_SIZE,
		start_y + TANK_SIZE
	);
	setfillcolor(RGB(255, 0, 0));
	fillrectangle(
		start_x-1,
		start_y+1,
		start_x + TANK_SIZE-1,
		start_y + TANK_SIZE-1
	);

	this->x = this->x + step;
	if (this->x > 640) {
		this->x = 0;
	}
	 drawTank();
}

Bullet::Bullet()
{
}

Bullet::~Bullet() {

}
*/