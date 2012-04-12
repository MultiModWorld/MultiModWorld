package de.davboecki.multimodworld.settings;

import java.util.HashMap;

import de.davboecki.multimodworld.MMWExchangeWorld;
import de.davboecki.multimodworld.settings.Settings.ErrorType;

public class MMWExchangeWorldSettings extends MMWWorldSettings{

	public MMWExchangeWorldSettings(MMWExchangeWorld world) {
		super(world);
	}

	@Override
	protected void loadparse(SettingsParser parser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HashMap<String, Object> saveparse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String error(ErrorType et) {
		// TODO Auto-generated method stub
		return "";
	}
}
