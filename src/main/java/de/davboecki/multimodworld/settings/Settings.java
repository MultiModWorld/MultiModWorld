package de.davboecki.multimodworld.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.logging.Logger;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.UnicodeReader;

import de.davboecki.multimodworld.MultiModWorld;

public abstract class Settings {

	private final File file;
	private static final Yaml yaml = new Yaml();
	protected Logger log = MultiModWorld.getInstance().getLogger();

	protected enum ErrorType {
		Unknown, Cast, FileNotFound, IO
	};

	protected Settings(File file) {
		this.file = file;
	}

	/*
	 * Parse the settings from input parameter.
	 */
	abstract protected void loadparse(SettingsParser parser);

	/*
	 * Returns every setting as a HashMap.
	 */
	abstract protected HashMap<String, Object> saveparse();

	/*
	 * Returns a error message as String.
	 */
	abstract protected String error(ErrorType et);

	protected File getFile() {
		return file;
	}

	@SuppressWarnings("unchecked")
	public void load() {
		try {
			final Object omap = yaml.load(new UnicodeReader(new FileInputStream(file)));
			final HashMap<String, Object> map = (HashMap<String, Object>) omap;
			loadparse(new SettingsParser(map));
		} catch (final FileNotFoundException e) {
			log.warning(error(ErrorType.FileNotFound));
			save();
		} catch (final ClassCastException e) {
			log.severe(error(ErrorType.Cast));
			BrokenSettingsFile();
		} catch (final Exception e) {
			log.severe(error(ErrorType.Unknown));
			BrokenSettingsFile();
		}
	}

	public void save() {
		final HashMap<String, Object> map = saveparse();
		if (map == null) {
			return;
		}
		try {
			yaml.dump(filterHashMap(map), new OutputStreamWriter(new FileOutputStream(file)));
		} catch (final IOException e) {
			log.severe(error(ErrorType.IO));
		}
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Object> filterHashMap(HashMap<String, Object> map) {
		final HashMap<String, Object> newmap = new HashMap<String, Object>();
		if (map == null) {
			return null;
		}
		for (final String key : map.keySet()) {
			final Object value = map.get(key);
			if (value instanceof HashMap) {
				newmap.put(key, filterHashMap((HashMap<String, Object>) value));
			} else if (value instanceof Hashmapable) {
				newmap.put(key, filterHashMap(((Hashmapable) value).toHashMap()));
			} else {
				newmap.put(key, value);
			}
		}
		return newmap;
	}

	private void BrokenSettingsFile() {
		save();
	}
}
