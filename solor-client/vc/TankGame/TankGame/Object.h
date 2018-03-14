#pragma once
#include <graphics.h>
#include <list>
#include "Point.h"
#include "Rect.h"
using namespace std;
enum Dir { UP, DOWN, LEFT, RIGHT };

class Object {
public:
	//绘图
	virtual void Display() = 0;
	//移动
	virtual void Move() = 0;
	//判断是否消失
	virtual bool IsDisappear() = 0;
	// 爆炸  
	virtual void Boom(list<Object*>& lstBombs) = 0;
	//获得势力范围
	virtual Rect GetSphere() = 0;

	Point getPos() {
		return m_pos;
	}
	Object() {};
	~Object() {};
protected:
	//计算势力范围
	virtual void CalculateSphere() = 0;
	//位置
	Point m_pos;
	//势力范围
	Rect m_rectSphere;//势力范围
	//颜色
	COLORREF m_color;
	//方向
	Dir m_dir;
	//单步前进步长
	int m_step;
	//存在状态
	bool m_bDisappear;

};