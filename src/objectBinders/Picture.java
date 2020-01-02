package objectBinders;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utilities.BufferedImageUtil;

public class Picture {

	private ArrayList<BufferedImage> history;
	private int currentLayer = 0;

	public Picture(BufferedImage start) {
		start = BufferedImageUtil.setBufferedImageType(start, BufferedImage.TYPE_4BYTE_ABGR);

		history = new ArrayList<>();
		history.add(start);
		currentLayer = 0;
	}
	public BufferedImage getImage(boolean toEdit) {
		if (toEdit) {
			while (currentLayer + 1 < history.size())
				history.remove(currentLayer + 1);

			currentLayer++;
			history.add(BufferedImageUtil.copyBufferedImage(history.get(history.size() - 1)));
		}

		return history.get(currentLayer);
	}
	public void undo() {
		currentLayer = (currentLayer == 0) ? 0 : currentLayer - 1;
	}
	public void redo() {
		currentLayer = (currentLayer == history.size() - 1) ? history.size() - 1 : currentLayer + 1;
	}

	public int getHeight() {
		return history.get(currentLayer).getHeight();
	}
	public int getWidth() {
		return history.get(currentLayer).getWidth();
	}
	
	public static ArrayList<BufferedImage> layerArrayToBufferedImageArray(ArrayList<Picture> array) {
		ArrayList<BufferedImage> toReturn = new ArrayList<>();
		for (Picture p : array)
			toReturn.add(p.getImage(false));

		return toReturn;
	}
	public static void screenLayerForTransparentPixels(Picture toScreen) {
		int rgbClear = new Color(1, 1, 1).getRGB();
		int rgbReplacement = new Color(0, 1, 1).getRGB();
		BufferedImage i = toScreen.history.get(toScreen.currentLayer);

		int y;

		for (int x = 0; x < i.getWidth(); x++)
			for (y = 0; y < i.getHeight(); y++)
				if (i.getRGB(x, y) == rgbClear)
					i.setRGB(x, y, rgbReplacement);
	}
}
