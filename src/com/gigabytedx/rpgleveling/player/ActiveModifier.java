package com.gigabytedx.rpgleveling.player;

import com.gigabytedx.rpgleveling.modifiers.Modifier;

public class ActiveModifier {
	String type;
	Modifier modifier;
	
	public ActiveModifier(String type, Modifier modifier) {
		this.type = type;
		this.modifier = modifier;
	}

	public String getType() {
		return type;
	}

	public Modifier getModifier() {
		return modifier;
	}
	
}
