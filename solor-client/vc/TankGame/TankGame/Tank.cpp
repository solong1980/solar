#include "stdafx.h"
#include "Tank.h"
#include "Graphic.h"



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