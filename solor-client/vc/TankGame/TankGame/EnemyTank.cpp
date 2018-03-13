#include "stdafx.h"
#include "EnemyTank.h"

void EnemyTank::Display() {
	COLORREF fill_color_save = getfillcolor();
	COLORREF color_save = getcolor();

	setfillcolor(m_color);
	setcolor(m_color);
	//绘制线条
	switch (m_dir) {
	case UP:
		line(m_pos.GetX(), m_pos.GetY(), m_pos.GetX(), m_pos.GetY() - 15);
		break;
	case DOWN:
		line(m_pos.GetX(), m_pos.GetY(), m_pos.GetX(), m_pos.GetY() + 15);
		break;
	case LEFT:
		line(m_pos.GetX(), m_pos.GetY(), m_pos.GetX() - 15, m_pos.GetY());
		break;
	case RIGHT:
		line(m_pos.GetX(), m_pos.GetY(), m_pos.GetX() + 15, m_pos.GetY());
		break;
	default:
		break;
	}
	//绘制坦克
	RandomTank();
	setfillcolor(fill_color_save);
	setcolor(color_save);
}
void EnemyTank::Move() {
	//计算位置
	switch (m_dir) {
	case UP:
		m_pos.SetY(m_pos.GetY() - m_step);
		if (m_rectSphere.GetStartPoint().GetY() < Graphic::GetBattleGround().GetStartPoint().GetY()) {
			//m_pos.SetY(Graphic::GetBattleGround().GetEndPoint().GetY()-1);
			m_pos.SetY(m_pos.GetY() + m_step);
			this->RandomDir(1);
		}
		break;
	case DOWN:
		m_pos.SetY(m_pos.GetY() + m_step);
		if (m_rectSphere.GetEndPoint().GetY()  > Graphic::GetBattleGround().GetEndPoint().GetY()) {
			//m_pos.SetY(Graphic::GetBattleGround().GetStartPoint().GetY()+1);
			m_pos.SetY(m_pos.GetY() - m_step);
			this->RandomDir(1);
		}
		break;
	case LEFT:
		m_pos.SetX(m_pos.GetX() - m_step);
		if (m_rectSphere.GetStartPoint().GetX() < Graphic::GetBattleGround().GetStartPoint().GetX()) {
			//m_pos.SetX(Graphic::GetBattleGround().GetEndPoint().GetX()-1);
			m_pos.SetX(m_pos.GetX() + m_step);
			this->RandomDir(1);
		}
		break;
	case RIGHT:
		m_pos.SetX(m_pos.GetX() + m_step);
		if (m_rectSphere.GetEndPoint().GetX() > Graphic::GetBattleGround().GetEndPoint().GetX()) {
			//m_pos.SetX(Graphic::GetBattleGround().GetStartPoint().GetX()+1);
			m_pos.SetX(m_pos.GetX() - m_step);
			this->RandomDir(1);
		}
		break;
	default:
		break;
	}

	m_stepCnt++;

	if (m_stepCnt > MAX_STEP) {
		m_stepCnt = 0;
		this->RandomDir(0);
	}

	//计算势力范围
	CalculateSphere();
}

//计算势力范围
void EnemyTank::CalculateSphere() {
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
}
//绘制坦克
void EnemyTank::RandomTank() {
	fillrectangle(m_pos.GetX() - 6, m_pos.GetY() - 6, m_pos.GetX() + 6, m_pos.GetY() + 6);
	//setfillcolor(RED);
	//setcolor(RED);
	fillrectangle(m_rectSphere.GetStartPoint().GetX(), m_rectSphere.GetStartPoint().GetY(),
		m_pos.GetX() - 6, m_pos.GetY() - 6);

	fillrectangle(m_pos.GetX() + 6, m_rectSphere.GetStartPoint().GetY(),
		m_rectSphere.GetEndPoint().GetX(), m_pos.GetY() - 6);

	fillrectangle(m_rectSphere.GetStartPoint().GetX(), m_pos.GetY() + 6,
		m_pos.GetX() - 6, m_rectSphere.GetEndPoint().GetY());

	fillrectangle(m_pos.GetX() + 6, m_pos.GetY() + 6,
		m_rectSphere.GetEndPoint().GetX(), m_rectSphere.GetEndPoint().GetY());

	setfillcolor(m_color);
	setcolor(m_color);
	/*
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
	*/
}

void EnemyTank::RandomDir(int style) {
	if (style == 1) {
		Dir dir = Dir(Dir::UP + (rand() % 4));
		while ((dir = Dir(Dir::UP + (rand() % 4))) == m_dir) {
		}
		m_dir = dir;
	}
	else {
		m_dir = (Dir)(Dir::UP + (rand() % 4));
	}
}