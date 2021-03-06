#pragma once
#include "Tank.h"
#include "Graphic.h"

#define MAX_STEP 150
#define MAX_STEP_SHOOT 30

class EnemyTank :public Tank {
public:
	EnemyTank():Tank() {
		int rd = rand();
		int m_x = rd % Graphic::GetBattleGround().getWidth();
		int m_y = rd % Graphic::GetBattleGround().getHeight();
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
	virtual void Shoot(list<Object*>& lstBombs) {
		Tank::Shoot(lstBombs);
		m_bNeedShoot = false;
	}
protected:
	void RandomTank();
	// 随机产生坦克方向 type： 1, 新方向必须与之前方向不同 2, 任意一个新方向  
	void RandomDir(int type);
	int m_stepCnt;
};