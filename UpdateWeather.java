package com.niit.iweather;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.weixvn.wae.webpage.WebPage;

import android.os.Bundle;
import android.os.Message;

/**
 * 
 * @author hfutqy
 *
 */

public class UpdateWeather extends WebPage {

	@Override
	public void onStart() {
		String city = getHtmlValue("city");
		// OXUgvi2T7TR87uQw6RF1HqpuBUNBdRjo这个API是自己申请的http://lbsyun.baidu.com/apiconsole/key
		//一开始选取Android发现有问题，查看车联网API介绍发现需要Web API就选择浏览器端发现是可以使用的
		//备用API：Py3C1WGl0o2GDrulfPFuZYEy
		final String ak = "OXUgvi2T7TR87uQw6RF1HqpuBUNBdRjo";
		this.uri = "http://api.map.baidu.com/telematics/v3/weather";
		this.type = RequestType.GET;
		this.params = getParams();
		params.put("location", city);
		params.put("ak", ak);
	}

	@Override
	public void onSuccess(Document doc) {
		analyze(doc);
	}

	private void analyze(Document doc) {
		Message msg = Weather.handler.obtainMessage();
		String status = doc.getElementsByTag("status").get(0).text();
		if ("success".equals(status)) {
			// 查询成功
			msg.what = 1;
			String city = doc.getElementsByTag("currentcity").get(0).text();
			Element weatherDataElem = doc.getElementsByTag("weather_data").get(
					0);
			Elements dateElem = weatherDataElem.getElementsByTag("date");
			Elements weatherElem = weatherDataElem.getElementsByTag("weather");
			Elements windElem = weatherDataElem.getElementsByTag("wind");
			Elements temperatureElem = weatherDataElem
					.getElementsByTag("temperature");
			String[] dateArray = new String[4];
			String[] weatherArray = new String[4];
			String[] windArray = new String[4];
			String[] temperatureArray = new String[4];
			String currentTemperature = null;
			for (int i = 0; i < 4; i++) {
				String date = dateElem.get(i).text();
				if (i == 0) {
					if (date.contains("实时")) {
						currentTemperature = date.substring(
								date.indexOf("：") + 1, date.indexOf("℃")) + "°";
					}
					date = date.substring(0, 2);
				}
				dateArray[i] = date;
				weatherArray[i] = weatherElem.get(i).text();
				windArray[i] = windElem.get(i).text();
				String temperature = temperatureElem.get(i).text();
				if (temperature.contains("~")) {
					String highTem = temperature.substring(0,
							temperature.indexOf(" "));
					String lowTem = temperature.substring(
							temperature.lastIndexOf(" ") + 1,
							temperature.indexOf("℃"));
					temperature = lowTem + "~" + highTem + "°";
				} else {
					temperature = temperature.replace("℃", "°");
				}
				temperatureArray[i] = temperature;
			}
			if (currentTemperature == null) {
				currentTemperature = temperatureArray[0];
			}
			Bundle bundle = new Bundle();
			bundle.putStringArray("date", dateArray);
			bundle.putStringArray("weather", weatherArray);
			bundle.putStringArray("wind", windArray);
			bundle.putStringArray("temperature", temperatureArray);
			bundle.putString("city", city);
			bundle.putString("current_temperature", currentTemperature);
			msg.setData(bundle);
		} else if ("No result available".equals(status)) {
			// 没有天气信息
			msg.what = 2;
		} else {
			// 其他错误
			msg.what = 0;
		}
		Weather.handler.sendMessage(msg);
	}
}
