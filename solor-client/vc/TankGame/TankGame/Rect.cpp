#include "stdafx.h"
#include "Rect.h"

void Rect::Set(int x1, int y1, int x2, int y2) {
	this->m_startPoint = Point(x1, y1);
	this->m_endPoint = Point(x2, y2);
}
void Rect::Set(const Point startPoint, const Point endPoint) {
	this->m_startPoint = startPoint;
	this->m_endPoint = endPoint;
}
void Rect::SetStartPoint(const Point startPoint) {
	this->m_startPoint = startPoint;
}
void Rect::SetEndPoint(const Point endPoint) {
	this->m_endPoint = endPoint;
}
Point Rect::GetStartPoint() const {
	return this->m_startPoint;
}
Point Rect::GetEndPoint() const {
	return this->m_endPoint;
}
int Rect::getWidth() {
	return m_endPoint.GetX() - m_startPoint.GetX();
}
int Rect::getHeight() {
	return m_endPoint.GetY() - m_startPoint.GetY();
}

Point Rect::GetTRPoint() const{
	Point p = m_startPoint;
	p.SetX(m_endPoint.GetX());
	return p;
}

Point Rect::GetBLPoint() const {
	Point p = m_startPoint;
	p.SetY((m_endPoint.GetY()));
	return p;
}

void Rect::check() {
	//这里有问题
	if (m_startPoint.GetX() > m_endPoint.GetX() || m_startPoint.GetY() > m_endPoint.GetY())
	{
		Point p = m_startPoint;
		m_startPoint = m_endPoint;
		m_endPoint = m_startPoint;
	}
}