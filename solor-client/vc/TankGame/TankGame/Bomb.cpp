#include "stdafx.h"
#include "Bomb.h"
#include <iostream>
bool Bomb::IsDisappear() {
	return m_bDisappear;
}

void Bomb::Display() {
	COLORREF fill_color_save = getfillcolor();
	COLORREF color_save = getcolor();

	setfillcolor(m_color);
	setcolor(RED);

	fillcircle(m_pos.GetX(), m_pos.GetY(),  (m_type==BombType::LARGE) ? 15 : 10);

	setcolor(color_save);
	setfillcolor(fill_color_save);
}
void Bomb::Move() {
	m_timer -= 2;
	if (m_timer < 0) {
		m_bDisappear = true;
	}
}

void Bomb::Boom(list<Object*>& lstBombs) {
	lstBombs.push_back(this);
}

void Bomb::CalculateSphere() {

}