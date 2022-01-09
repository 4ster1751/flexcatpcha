package io.github.yaforster.flexcaptcha.imgbased.handling.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

/**
 * Helps bulk loading of image files from a given directory. File formats can be
 * specified.
 * 
 * @author Yannick Forster
 *
 */
@Getter
@Setter
public class ImageLoader {
	
	/**
	 * Log4J Logger
	 */
	Logger log = LogManager.getLogger(ImageLoader.class);

	/**
	 * The supported image file types. Will filter out all files that do not have
	 * these extensions when loading files.
	 */
	private String[] EXTENSIONS = new String[] { "gif", "png", "bmp", "jpg", "jpeg" };

	/**
	 * Gets all images with the extensions defined in the EXTENSIONS field as
	 * {@link BufferedImage}s
	 * 
	 * @param dirPath Directory path from which to load
	 * @return Array of {@link BufferedImage}s loaded from the directory
	 * @throws NotDirectoryException if the specified Path is no directory.
	 */
	public BufferedImage[] getImagesfromPath(String dirPath) throws NotDirectoryException {
		File dir = new File(dirPath);
		if (!dir.isDirectory()) {
			throw new NotDirectoryException(dirPath);
		}
		FilenameFilter filter = createNewFileFilter();
		File[] imgFiles = dir.listFiles(filter);
		BufferedImage[] images = Stream.of(imgFiles).map(file -> {
			try {
				return ImageIO.read(file);
			} catch (IOException e) {
				log.error("Error loading image:" + e.getMessage());
				return null;
			}
		}).filter(img -> img != null).toArray(BufferedImage[]::new);
		return images;
	}

	/**
	 * Creates a {@link FilenameFilter} allowing files ending with file name
	 * extensions defined in the EXTENSIONS field
	 * 
	 * @return configured {@link FilenameFilter}
	 */
	private FilenameFilter createNewFileFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				for (final String ext : EXTENSIONS) {
					if (name.endsWith("." + ext)) {
						return (true);
					}
				}
				return (false);
			}
		};
	}

}