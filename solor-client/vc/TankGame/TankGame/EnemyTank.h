#pragma once
#include "Tank.h"
#include "Graphic.h"

class EnemyTank :public Tank {
public:
	EnemyTank() {
		int rd = rand();
		m_x = rd % Graphic::GetBattleGround().getWidth();
		m_y = rd % Graphic::GetBattleGround().getHeight();
		m_pos.Set(m_x, m_y);
		m_color = WHITE;
		//随机一个方向
		m_dir = Dir(rd%4);
		m_step = 2;
		CalculateSphere();
	};
	~EnemyTank() {

	};
	void Display();
	void Move();
protected:
	void CalculateSphere();
	void RandomTank();
};