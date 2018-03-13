#pragma once
#include "Tank.h"
#include "Graphic.h"

#define MAX_STEP 40

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
		m_stepCnt = rand() % MAX_STEP;
		CalculateSphere();
	};
	~EnemyTank() {

	};
	void Display();
	void Move();
protected:
	void CalculateSphere();
	void RandomTank();
	// 随机产生坦克方向 type： 1, 新方向必须与之前方向不同 2, 任意一个新方向  
	void RandomDir(int type);
	int m_stepCnt;
};