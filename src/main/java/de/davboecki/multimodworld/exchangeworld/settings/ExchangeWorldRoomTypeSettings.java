package de.davboecki.multimodworld.exchangeworld.settings;

import java.io.File;
import java.util.HashMap;

import de.davboecki.multimodworld.exchangeworld.rooms.RoomClipboard;
import de.davboecki.multimodworld.settings.Settings;
import de.davboecki.multimodworld.settings.SettingsParser;

public class ExchangeWorldRoomTypeSettings extends Settings {
	
	private RoomClipboard clipboard;
	
	public ExchangeWorldRoomTypeSettings(File file) {
		super(file, true);
	}
	
	@Override
	@Deprecated
	public void load() {
		if(clipboard != null) {
			super.load();
		} else {
			throw new UnsupportedOperationException("Deprecated method called. Please contact the mod authors.");
		}
	}
	
	public void load(RoomClipboard clipboard) {
		this.clipboard = clipboard;
		super.load();
	}
	
	@Override
	protected void loadparse(SettingsParser parser) {
		clipboard.parse(parser);
		
	}

	@Override
	protected HashMap<String, Object> saveparse() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("nSign", clipboard.getNormalSignPos());
		map.put("oSign", clipboard.getOtherSignPos());
		map.put("nPortal", clipboard.getNormalPortal());
		map.put("oPortal", clipboard.getOtherPortal());
		map.put("RoomPriority", clipboard.getRoomPriority());
		return null;
	}

	@Override
	protected String error(ErrorType et) {
		switch(et) {
		case Cast:
			return "Could not load ExchangeWorld-Room information. File is broken. ("+file.toString()+")";
		case FileNotFound:
			return "Could not load ExchangeWorld-Room information. Internal Error. File not found. ("+file.toString()+")";
		case IO:
			return "Could not load ExchangeWorld-Room information. Could not access file. ("+file.toString()+")";
		}
		return "Could not load ExchangeWorld-Room information. Internal Error. ("+file.toString()+")";
	}

}
