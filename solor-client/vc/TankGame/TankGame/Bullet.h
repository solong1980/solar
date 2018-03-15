#pragma once
#include "Object.h"
#include "Bomb.h"
class Bullet:public Object {
public:
		Bullet();
		Bullet(Point pos, Dir dir, COLORREF color);
		~Bullet(){}
		void Display();
		void Move();
		Rect GetSphere() {
			return m_rectSphere;
		}
		void Boom(list<Object*>& lstBombs) {
			//������ը����
			Bomb* pBomb = new Bomb(m_pos, BombType::SMALL);
			lstBombs.push_back(pBomb);
		};
		bool IsDisappear() {
			return m_bDisappear;
		}
		void SetDisappear() {
			m_bDisappear = true;
		}
protected:
	void CalculateSphere();
};
