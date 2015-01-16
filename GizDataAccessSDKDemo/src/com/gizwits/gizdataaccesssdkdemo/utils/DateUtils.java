/**
 * Project Name:GizDataAccessSDKDemo
 * File Name:DateUtils.java
 * Package Name:com.gizwits.gizdataaccesssdkdemo
 * Date:2015-1-12 15:30:11
 * Copyright (c) 2014~2015 Xtreme Programming Group, Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gizwits.gizdataaccesssdkdemo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 *  
 * ClassName: Class DateUtils. <br/> 
 * <br/>
 * date: 2015-1-12 15:30:11 <br/> 
 *
 * @author Lien
 */
public class DateUtils {
	
	/** The sf. */
	private static SimpleDateFormat sf = null;

	/* 获取系统时间 格式为："yyyy/MM/dd " */
	/**
	 * Gets the current date.
	 *
	 * @return the current date
	 */
	public static String getCurrentDate() {
		Date d = new Date();
		sf = new SimpleDateFormat("yyyyMMddssSSS");
		return sf.format(d);
	};

	/* 时间戳转换成字符窜 */
	/**
	 * Gets the date to string.
	 *
	 * @param time the time
	 * @return the date to string
	 */
	public static String getDateToString(long time) {
		Date d = new Date(time);
		sf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		return sf.format(d);
	};

	/* 将字符串转为时间戳 */
	/**
	 * Gets the string to date.
	 *
	 * @param time the time
	 * @return the string to date
	 */
	public static long getStringToDate(String time) {
		sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = new Date();
		try {
			date = sf.parse(time);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	};
}
