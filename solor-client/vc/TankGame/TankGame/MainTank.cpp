#include "stdafx.h"
#include "MainTank.h"
#include "Graphic.h"


void MainTank::SetDir(Dir dir) {
	m_dir = dir;
}

void MainTank::DrawTankBody(/*int style*/) {
	/*fillrectangle(m_x - 4, m_y - 4, m_x + 4, m_y + 4);
	if (style == 1)
	{
		fillrectangle(m_x - 8, m_y - 6, m_x - 6, m_y + 6);
		fillrectangle(m_x + 6, m_y - 6, m_x + 8, m_y + 6);
	}
	else
	{
		fillrectangle(m_x - 6, m_y - 8, m_x + 6, m_y - 6);
		fillrectangle(m_x - 6, m_y + 6, m_x + 6, m_y + 8);
	}*/
	fillrectangle(m_pos.GetX() - 6, m_pos.GetY() - 6, m_pos.GetX() + 6, m_pos.GetY() + 6);
	switch (m_dir)
	{
	case UP:
	case DOWN:
		fillrectangle(m_rectSphere.GetStartPoint().GetX(), m_rectSphere.GetStartPoint().GetY(),
			m_rectSphere.GetStartPoint().GetX() + 4, m_rectSphere.GetEndPoint().GetY());
		fillrectangle(m_rectSphere.GetEndPoint().GetX() - 4, m_rectSphere.GetStartPoint().GetY(),
			m_rectSphere.GetEndPoint().GetX(), m_rectSphere.GetEndPoint().GetY());
		break;
	case LEFT:
	case RIGHT:
		fillrectangle(m_rectSphere.GetStartPoint().GetX(), m_rectSphere.GetStartPoint().GetY(),
			m_rectSphere.GetEndPoint().GetX(), m_rectSphere.GetStartPoint().GetY() + 4);
		fillrectangle(m_rectSphere.GetStartPoint().GetX(), m_rectSphere.GetEndPoint().GetY() - 4,
			m_rectSphere.GetEndPoint().GetX(), m_rectSphere.GetEndPoint().GetY());
		break;
	default:
		break;
	}
}

void MainTank::Display() {
	COLORREF fill_color_save = getfillcolor();
	COLORREF color_save = getcolor();
	setfillcolor(m_color);
	setcolor(m_color);
	switch (m_dir) {
	case UP:
		//DrawTankBody(1);
		line(m_pos.GetX(), m_pos.GetY(), m_pos.GetX(), m_pos.GetY() - 15);
		break;
	case DOWN:
		//DrawTankBody(1);
		line(m_pos.GetX(), m_pos.GetY(), m_pos.GetX(), m_pos.GetY() + 15);
		break;
	case LEFT:
		//DrawTankBody(0);
		line(m_pos.GetX(), m_pos.GetY(), m_pos.GetX() - 15, m_pos.GetY());
		break;
	case RIGHT:
		//DrawTankBody(0);
		line(m_pos.GetX(), m_pos.GetY(), m_pos.GetX() + 15, m_pos.GetY());
	default:
		break;
	}
	DrawTankBody();
	setfillcolor(fill_color_save);
	setcolor(color_save);
}

void MainTank::Move() {
	switch (m_dir) {
	case UP:
		m_pos.SetY(	m_pos.GetY() -  m_step);
		if (m_pos.GetY() < Graphic::GetBattleGround().GetStartPoint().GetY()) {
			//m_pos.SetY(Graphic::GetBattleGround().GetEndPoint().GetY()-1);
			m_pos.SetY(m_pos.GetY()+ m_step);
		}
		break;
	case DOWN:
		m_pos.SetY(m_pos.GetY() + m_step);
		if (m_pos.GetY()  > Graphic::GetBattleGround().GetEndPoint().GetY()) {
			//m_pos.SetY(Graphic::GetBattleGround().GetStartPoint().GetY()+1);
			m_pos.SetY(m_pos.GetY() - m_step);
		}
		break;
	case LEFT:
		m_pos.SetX(m_pos.GetX()- m_step);
		if (m_pos.GetX() < Graphic::GetBattleGround().GetStartPoint().GetX()) {
			//m_pos.SetX(Graphic::GetBattleGround().GetEndPoint().GetX()-1);
			m_pos.SetX(m_pos.GetX() + m_step);
		}
		break;
	case RIGHT:
		m_pos.SetX(m_pos.GetX() + m_step);
		if (m_pos.GetX() > Graphic::GetBattleGround().GetEndPoint().GetX()) {
			//m_pos.SetX(Graphic::GetBattleGround().GetStartPoint().GetX()+1);
			m_pos.SetX(m_pos.GetX() - m_step);
		}
		break;
	default:
		break;
	}
	CalculateSphere();
}

/*
void MainTank::CalculateSphere() {
	switch (m_dir)
	{
	case UP:
	case DOWN:
		m_rectSphere.Set(m_pos.GetX() - 13, m_pos.GetY() - 10, m_pos.GetX() + 13, m_pos.GetY() + 10);
		break;
	case LEFT:
	case RIGHT:
		m_rectSphere.Set(m_pos.GetX() - 10, m_pos.GetY() - 13, m_pos.GetX() + 10, m_pos.GetY() + 13);
		break;
	default:
		break;
	}
}*/