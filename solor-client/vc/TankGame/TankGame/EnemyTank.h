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
		//���һ������
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
	// �������̹�˷��� type�� 1, �·��������֮ǰ����ͬ 2, ����һ���·���  
	void RandomDir(int type);
	int m_stepCnt;
};