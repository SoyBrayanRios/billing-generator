package com.jtc.app.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.json.JSONArray;
import org.json.JSONException;

@Converter
public class JSONConverter implements AttributeConverter<JSONArray, String> {

	@Override
	public String convertToDatabaseColumn(JSONArray attribute) {
		String json;
		try {
			json = attribute.toString();
		} catch (NullPointerException e) {
			json = "";
		}
		return json;
	}

	@Override
	public JSONArray convertToEntityAttribute(String jsonDataAsJson) {
		JSONArray jsonData;
		try {
			jsonData = new JSONArray(jsonDataAsJson);
		} catch (JSONException e) {
			jsonData = null;
		}
		return jsonData;
	}
}
