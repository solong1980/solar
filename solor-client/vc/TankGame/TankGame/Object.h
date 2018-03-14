#pragma once
#include <graphics.h>
#include "Point.h"
#include "Rect.h"

enum Dir { UP, DOWN, LEFT, RIGHT };

class Object {
public:
	//��ͼ
	virtual void Display() = 0;
	//�ƶ�
	virtual void Move() = 0;
	//�ж��Ƿ���ʧ
	virtual bool IsDisappear() = 0;
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