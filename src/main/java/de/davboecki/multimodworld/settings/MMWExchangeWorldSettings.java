package de.davboecki.multimodworld.settings;

import java.util.HashMap;

import de.davboecki.multimodworld.utils.MMWExchangeWorld;

public class MMWExchangeWorldSettings extends MMWWorldSettings {

	public MMWExchangeWorldSettings(MMWExchangeWorld world) {
		super(world);
	}

	@Override
	protected void loadparse(SettingsParser parser) {
		super.loadparse(parser);
		// TODO Auto-generated method stub
	}

	@Override
	protected HashMap<String, Object> saveparse() {
		HashMap<String, Object> answer = super.saveparse();
		// TODO Auto-generated method stub
		return answer;
	}

	@Override
	protected String error(ErrorType et) {
		// TODO Auto-generated method stub
		return "";
	}
}
