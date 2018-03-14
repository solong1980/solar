#pragma once

#include <list>
#include "Object.h"
#include "Bullet.h"

using namespace std;

class Tank :public Object{
public:
	//virtual void Display() = 0;
	//virtual void Move() = 0;
 
	Rect GetSphere() {
		return m_rectSphere;
	};

	void Boom(list<Object*>& lstBombs) {
		if (m_step == 0)
			return;
		lstBombs.push_back(this);
		m_step = 0;
	};
	
	virtual void Shoot(list<Object*>& lstBullets){
		Bullet* pBullet = new Bullet(m_pos, m_dir, m_color);
		lstBullets.push_back(pBullet);
	};

	void SetDisappear() {
		m_bDisappear = true;
	};

	Tank() {
		m_pos.Set(300, 400);
		m_dir = Dir::UP;
		m_color = YELLOW;
		m_step = 4;
		m_timer = 3;
		m_bombSize = 26;
		m_bDisappear = false;
		this->CalculateSphere();
	};
	~Tank() {};
protected:
	void CalculateSphere() {
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
	};
	bool IsDisappear() {
		return m_bDisappear;
	};
	int m_timer;
	int m_bombSize;
};