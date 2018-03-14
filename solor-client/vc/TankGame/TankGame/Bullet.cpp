#include "stdafx.h"
#include "Bullet.h"
#include "Graphic.h"

Bullet::Bullet() {
	m_bDisappear = true;
}

Bullet::Bullet(Point pos, Dir dir, COLORREF color) {
	m_pos = pos;
	m_dir = dir;
	m_color = color;
	m_bDisappear = false;
	m_step = 20;
	this->CalculateSphere();
}


void Bullet::Display() {
	COLORREF save_fill_color = getfillcolor();
	COLORREF save_color = getcolor();
	setfillcolor(m_color);
	setcolor(m_color);
	fillcircle(m_pos.GetX() - 1, m_pos.GetY() - 1, 4);
	setfillcolor(save_fill_color);
	setcolor(save_color);
}

void Bullet::Move() {
	switch (m_dir) {
	case UP:
		m_pos.SetY(m_pos.GetY() - m_step);
		CalculateSphere();
		if (m_rectSphere.GetStartPoint().GetY() < Graphic::GetBattleGround().GetStartPoint().GetY()) {
			m_bDisappear = true;
		}
		break;
	case DOWN:
		m_pos.SetY(m_pos.GetY() + m_step);
		CalculateSphere();
		if (m_rectSphere.GetEndPoint().GetY() > Graphic::GetBattleGround().GetEndPoint().GetY())
		{
			m_bDisappear = true;
		}
		break;
	case LEFT:
		m_pos.SetX(m_pos.GetX() - m_step);
		CalculateSphere();
		if (m_rectSphere.GetStartPoint().GetX() < Graphic::GetBattleGround().GetStartPoint().GetX())
		{
			m_bDisappear = true;
		}
		break;
	case RIGHT:
		m_pos.SetX(m_pos.GetX() + m_step);
		CalculateSphere();
		if (m_rectSphere.GetEndPoint().GetX() > Graphic::GetBattleGround().GetEndPoint().GetX())
		{
			m_bDisappear = true;
		}
		break;
	default:
		break;
	}
}

void Bullet::CalculateSphere() {
	m_rectSphere.Set(m_pos.GetX() - 2, m_pos.GetY() - 2, m_pos.GetX() + 2, m_pos.GetY() + 2);
}