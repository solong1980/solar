#pragma once
#include "Object.h"

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
		void Boom(list<Object*>& lstBombs) {};
		bool IsDisappear() {
			return m_bDisappear;
		}
protected:
	void CalculateSphere();
};
