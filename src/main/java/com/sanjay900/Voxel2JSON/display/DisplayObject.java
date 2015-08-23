package com.sanjay900.Voxel2JSON.display;

import org.json.JSONArray;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DisplayObject {
	double rx,ry,rz,tx,ty,tz,sx=1,sy=1,sz=1;
	public void fromJSON(JSONObject obj) {
		if (obj.has("rotation")) {
			JSONArray arr = obj.getJSONArray("rotation");
			rx=arr.getDouble(0);
			ry=arr.getDouble(1);
			rz=arr.getDouble(2);
		}
		if (obj.has("translation")) {
			JSONArray arr = obj.getJSONArray("translation");
			tx=arr.getDouble(0);
			ty=arr.getDouble(1);
			tz=arr.getDouble(2);
		}
		if (obj.has("scale")) {
			JSONArray arr = obj.getJSONArray("scale");
			sx=arr.getDouble(0);
			sy=arr.getDouble(1);
			sz=arr.getDouble(2);
		}
	}
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		if (rx !=0 || ry !=0 || rz!=0) {
			obj.put("rotation", new Double[]{rx,ry,rz});
		}
		if (tx !=0 || ty !=0 || tz!=0) {
			obj.put("translation", new Double[]{tx,ty,tz});
		}
		if (sx !=1 || sy !=1 || sz!=1) {
			obj.put("scale", new Double[]{sx,sy,sz});
		}
		return obj;
	}
}
