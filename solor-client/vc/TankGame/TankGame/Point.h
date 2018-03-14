#pragma once
class Point {
public:
	Point(int x = 0, int y = 0) :m_x(x), m_y(y) {};
	~Point() {};
	Point(const Point& point) {
		this->m_x = point.m_x;
		this->m_y = point.m_y;
	};
	Point& operator=(const Point& point) {
		this->m_x = point.m_x;
		this->m_y = point.m_y;
		return (*this);
	};
	void Set(int x, int y);
	void SetX(int x);
	void SetY(int y);
	int GetX() const;
	int GetY() const;
private:
	int m_x;
	int m_y;
};