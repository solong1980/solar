#pragma once

#include <list>
#include "Object.h"

using namespace std;

enum BombType{LARGE,SMALL};

class Bomb :public Object {
public:
	Bomb() {
		m_bDisappear = false;
		this->m_color = YELLOW;
	};
	Bomb(Point pos, BombType type) : Bomb() {
		m_pos = pos;
		m_type = type;
		switch (m_type)
		{
		case LARGE:
			m_timer = 40;
			break;
		case SMALL:
			m_timer = 20;
			break;
		default:
			break;
		}
	}
	void Display();
	void Move();
	bool IsDisappear();
	void SetDisappear() {
		m_bDisappear = true;
	};
	Rect GetSphere() {
		return m_rectSphere;
	};
	void Boom(list<Object*>& lstBombs);
protected:
	void CalculateSphere();
	BombType m_type;
	int m_timer;
};
