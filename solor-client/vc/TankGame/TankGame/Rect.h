#pragma once
#include "Point.h"
class Rect {
public:
	Rect(int x1 = 0, int y1 = 1, int x2 = 0, int y2 = 0):m_startPoint(x1,y1),m_endPoint(x2,y2){};
	Rect(const Point& startPoint, const Point& endPoint) :m_startPoint(startPoint), m_endPoint(endPoint) {};
	~Rect() {};
	Rect& operator=(const Rect& rect) {
		this->m_startPoint = rect.GetStartPoint();
		this->m_endPoint = rect.GetEndPoint();
	};
	void Set(int x1 = 0, int y1 = 0, int x2 = 0, int y2 = 0);
	void Set(const Point  startPoint, const Point  endPoint);
	void SetStartPoint(const Point startPoint);
	void SetEndPoint(const Point endPoint);
	Point GetStartPoint() const;
	Point GetEndPoint() const;
	Point GetTRPoint() const;// Get Top Right Point  
	Point GetBLPoint() const; // Get Bottom Left Point  
	int getWidth();
	int getHeight();
private:
	void check();
	Point m_startPoint;
	Point m_endPoint;
};