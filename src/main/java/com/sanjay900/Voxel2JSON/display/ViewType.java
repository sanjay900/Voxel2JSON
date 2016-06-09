package com.sanjay900.Voxel2JSON.display;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ViewType {
	IN_HAND("thirdperson_right"),ON_HEAD("head"),ON_FLOOR("ground"),ON_WALL("fixed"),FIRST_PERSON("firstperson_right"),GUI("gui");
	@Getter
	String modelName;
}
