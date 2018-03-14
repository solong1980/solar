#include "stdafx.h"

#include "Point.h"

void Point::Set(int x, int y) {
	this->m_x = x;
	this->m_y = y;
}

void Point::SetX(int x) {
	this->m_x = x;
}

void Point::SetY(int y) {
	this->m_y = y;
}

int Point::GetX() const{
	return this->m_x;
}

int Point::GetY() const{
	return this->m_y;
}