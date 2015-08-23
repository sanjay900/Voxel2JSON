package com.sanjay900.Voxel2JSON.display;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ViewType {
	IN_HAND("thirdperson"),ON_HEAD("head"),ON_FLOOR("ground"),ON_WALL("fixed"),FIRST_PERSON("firstperson"),GUI("gui");
	@Getter
	String modelName;
}
