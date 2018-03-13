#pragma once
#include "Tank.h"

class MainTank :public Tank {
public:
	MainTank() {
		m_x = 400;
		m_y = 300;
		m_pos.Set(m_x, m_y);
		this->CalculateSphere();
		m_color = YELLOW;
		m_dir = Dir::UP;
		m_step = 2; 
	};
	~MainTank() {

	};
	Tank& operator=(const MainTank& mainTank) {
		return *this;
	};
	void SetDir(Dir dir);
	void Display();
	void Move();
protected:
	//计算势力范围
	void CalculateSphere();
	// 绘制坦克主体 
	void DrawTankBody();
};