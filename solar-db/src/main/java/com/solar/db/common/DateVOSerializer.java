package com.solar.db.common;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

/**
 * 根据.net C# 日期类型数据结构,对fastjson序列化做定制
 * 
 * @author long
 *
 */
public class DateVOSerializer implements ObjectSerializer {
	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
			throws IOException {
		// sample:
		// {"date":4,"day":2,"hours":17,"minutes":18,"month":7,"seconds":34,"time":1249377514000,"timezoneOffset":-480,"year":109}
		DateVO dateVO = new DateVO();
		Date date = (Date) object;
		dateVO.setTime(date.getTime());

		// LocalDateTime now = LocalDateTime.now();

		// ZonedDateTime now = new GregorianCalendar().toZonedDateTime();
		// 先采用过期api
		// dateVO.setDate(now.getDayOfMonth());
		// dateVO.setMonth(now.getMonthValue());
		// dateVO.setYear(now.getYear());
		// dateVO.setDay(now.getDayOfWeek().getValue());
		// dateVO.setHours(now.getHour());
		// dateVO.setMinutes(now.getMinute());
		// dateVO.setSeconds(now.getSecond());
		// dateVO.setTimezoneOffset(now.getOffset().get(ChronoField.OFFSET_SECONDS));

		// dateVO.setDate(date.getDate());
		// dateVO.setMonth(date.getMonth());
		// dateVO.setYear(date.getYear());
		// dateVO.setDay(date.getDay());
		// dateVO.setHours(date.getHours());
		// dateVO.setMinutes(date.getMinutes());
		// dateVO.setSeconds(date.getSeconds());
		// dateVO.setTimezoneOffset(date.getTimezoneOffset());

		serializer.write(dateVO);
	}
}
