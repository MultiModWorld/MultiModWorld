package de.davboecki.multimodworld.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.yaml.snakeyaml.Yaml;

import de.davboecki.multimodworld.MultiModWorld;

public abstract class Settings {

	protected final File file;
	private static final Yaml yaml = new Yaml();
	protected Logger log = MultiModWorld.getInstance().getLogger();
	private final boolean activateGZIP;

	protected enum ErrorType {
		Unknown, Cast, FileNotFound, IO
	};

	protected Settings(File file) {
		this(file, false);
	}

	protected Settings(File file,boolean gzipflag) {
		this.file = file;
		activateGZIP = gzipflag;
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
	
	private String getError(ErrorType et) {
		String msg = error(et);
		if(msg == null && et.equals(ErrorType.FileNotFound)) return null;
		if(msg == null || msg == "") {
			if(et == ErrorType.IO) {
				msg = "IOException";
			} else if(et == ErrorType.FileNotFound) {
				msg = "FileNotFoundException";
			} else if(et == ErrorType.Cast) {
				msg = "ClasCastException";
			} else {
				msg = "Error";
			}
		}
		return msg;
	}
	
	protected File getFile() {
		return file;
	}

	@SuppressWarnings("unchecked")
	public void load() {
		try {
			final Object omap;
			if(activateGZIP) {
				omap = yaml.load(new GZIPInputStream(new FileInputStream(file)));
			} else {
				omap = yaml.load(new FileInputStream(file));
			}
			final HashMap<String, Object> map = (HashMap<String, Object>) omap;
			loadparse(new SettingsParser(map));
		} catch (final FileNotFoundException e) {
			String msg = getError(ErrorType.FileNotFound);
			if(msg != null) log.warning(msg);
			save();
		} catch (final ClassCastException e) {
			log.severe(getError(ErrorType.Cast));
			BrokenSettingsFile();
		} catch (final Exception e) {
			log.severe(getError(ErrorType.Unknown));
			BrokenSettingsFile();
		}
	}

	public void save() {
		final HashMap<String, Object> map = saveparse();
		if (map == null) {
			return;
		}
		try {
			new File(file.getParent()).mkdirs();
			if(activateGZIP) {
				OutputStreamWriter OutputStream = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(file)));
				yaml.dump(filterHashMap(map), OutputStream);
				OutputStream.close();
			} else {
				yaml.dump(filterHashMap(map), new OutputStreamWriter(new FileOutputStream(file)));
			}
		} catch (final IOException e) {
			log.severe(getError(ErrorType.IO));
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
