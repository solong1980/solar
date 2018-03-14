#pragma once
#include <graphics.h>
#include <list>
#include "Point.h"
#include "Rect.h"
using namespace std;
enum Dir { UP, DOWN, LEFT, RIGHT };

class Object {
public:
	//��ͼ
	virtual void Display() = 0;
	//�ƶ�
	virtual void Move() = 0;
	//�ж��Ƿ���ʧ
	virtual bool IsDisappear() = 0;
	// ��ը  
	virtual void Boom(list<Object*>& lstBombs) = 0;
	//���������Χ
	virtual Rect GetSphere() = 0;

	Point getPos() {
		return m_pos;
	}
	Object() {};
	~Object() {};
protected:
	//����������Χ
	virtual void CalculateSphere() = 0;
	//λ��
	Point m_pos;
	//������Χ
	Rect m_rectSphere;//������Χ
	//��ɫ
	COLORREF m_color;
	//����
	Dir m_dir;
	//����ǰ������
	int m_step;
	//����״̬
	bool m_bDisappear;

};