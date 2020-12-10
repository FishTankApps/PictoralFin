

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.fishtankapps.pictoralfin.jComponents.JIntField;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditor;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditorTool;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.LayerButton;
import com.fishtankapps.pictoralfin.mainFrame.StatusLogger;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.BufferedImageUtil;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;

public class ImageResizingTool extends ImageEditorTool {

	private static final long serialVersionUID = -2297726745512899997L;

	private JLabel sizeLabel, widthLabel, heightLabel;
	private JTextField width, height;
	private JComboBox<String> scaleTypes;
	private JButton resize;
	
	private boolean hasValidImage = false;

	public ImageResizingTool(ImageEditor editor, Theme theme) {
		super("Image Resizer", editor, theme, true);

		this.setLayout(new GridBagLayout());

		sizeLabel = new JLabel("Size: No Image Selected", JLabel.CENTER);
		widthLabel = new JLabel("Width:", JLabel.RIGHT);		
		heightLabel = new JLabel("Height:", JLabel.RIGHT);
		
		widthLabel.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 10));
		heightLabel.setFont(widthLabel.getFont());
		
		sizeLabel.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 15));

		KeyListener onEnterPressedListener = new KeyListener() {

			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)	
					new Thread(()->resizeImage(editor)).start();
			}
			public void keyReleased(KeyEvent e) {}			
		};
		
		width = new JIntField();
		width.addKeyListener(onEnterPressedListener);
		height = new JIntField();
		height.addKeyListener(onEnterPressedListener);
		
		scaleTypes = new JComboBox<>();
		scaleTypes.addItem("Default Scaling");
		scaleTypes.addItem("Area Averaging Algorithm");
		scaleTypes.addItem("Replicate Algorithm");		
		scaleTypes.addItem("Smooth");
		scaleTypes.addItem("Fast");

		resize = new JButton("Resize Image");
		resize.addActionListener(e -> new Thread(()->resizeImage(editor)).start());
		resize.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 12));
		
		width.setVisible(false);
		height.setVisible(false);
		resize.setVisible(false);
		scaleTypes.setVisible(false);
		widthLabel.setVisible(false);
		heightLabel.setVisible(false);

		add(sizeLabel,   new ChainGBC(0, 0).setFill(true,  false).setPadding(10).setWidthAndHeight(2, 1));
		add(widthLabel,  new ChainGBC(0, 1).setFill(false, false).setPadding(5) .setWidthAndHeight(1, 1));
		add(width,       new ChainGBC(1, 1).setFill(true,  false).setPadding(5) .setWidthAndHeight(1, 1));
		add(heightLabel, new ChainGBC(0, 2).setFill(false, false).setPadding(5) .setWidthAndHeight(1, 1));
		add(height,      new ChainGBC(1, 2).setFill(true,  false).setPadding(5) .setWidthAndHeight(1, 1));
		add(scaleTypes,  new ChainGBC(0, 3).setFill(true,  false).setPadding(5) .setWidthAndHeight(2, 1));
		add(resize,      new ChainGBC(0, 4).setFill(true,  false).setPadding(10).setWidthAndHeight(2, 1));
		
		updateCollapsedState(true);
	}

	protected void onMouseReleased(int clickX, int clickY, BufferedImage layer, Frame frame) {
	}

	protected void onMousePressed(int clickX, int clickY, BufferedImage layer, Frame frame) {
	}

	protected void onLayerSelectionChanged(LayerButton oldFrame, LayerButton newFrame) {
		if (newFrame != null) {
			hasValidImage = true;
			sizeLabel.setText("Size: " + newFrame.getLayer().getWidth() + " by " + newFrame.getLayer().getHeight());

			width.setText("" + newFrame.getLayer().getWidth());
			height.setText("" + newFrame.getLayer().getHeight());

			updateCollapsedState(collapsed);
		} else {
			hasValidImage = false;
			sizeLabel.setText("Size: No Image Selected");

			updateCollapsedState(collapsed);
		}
	}

	protected void updateCollapsedState(boolean collapsed) {
		width.setVisible(hasValidImage && !collapsed);
		height.setVisible(hasValidImage && !collapsed);
		resize.setVisible(hasValidImage && !collapsed);
		scaleTypes.setVisible(hasValidImage && !collapsed);
		widthLabel.setVisible(hasValidImage && !collapsed);
		heightLabel.setVisible(hasValidImage && !collapsed);
	}
	
	private void resizeImage(ImageEditor editor) {
		try {
			Frame frame = editor.getSelectedFrame();
			int index = editor.getLayerSelecter().getSelectedLayerButton().getLayerIndex();
			
			int width = Integer.parseInt(this.width.getText());
			int height = Integer.parseInt(this.height.getText());

			BufferedImage image = frame.getLayer(index);
			
			StatusLogger.logPrimaryStatus("Resizing...");
			
			int scaleType;
			
			if(scaleTypes.getSelectedItem().equals("Default Scaling"))
				scaleType = BufferedImage.SCALE_DEFAULT;
			else if(scaleTypes.getSelectedItem().equals("Area Averaging Algorithm"))
				scaleType = BufferedImage.SCALE_AREA_AVERAGING;
			else if(scaleTypes.getSelectedItem().equals("Replicate Algorithm"))
				scaleType = BufferedImage.SCALE_REPLICATE;
			else if(scaleTypes.getSelectedItem().equals("Smooth"))
				scaleType = BufferedImage.SCALE_SMOOTH;
			else
				scaleType = BufferedImage.SCALE_FAST;
			
			image = BufferedImageUtil.resizeBufferedImage(image, width, height, scaleType);
			
			StatusLogger.logPrimaryStatus("Replacing Layer...");
			
			frame.removeLayer(index, false);
			frame.addLayerAtIndex(image, index);
			
			StatusLogger.logPrimaryStatus("Layer Resized!");
			
			editor.getLayerSelecter().refresh();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
					"Please enter numbers into both fields.", "Number Format Error",
					JOptionPane.ERROR_MESSAGE);

		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null,
					"Both field must be > 0\n(Positive and non-zero)", "Illegal Argument Error",
					JOptionPane.ERROR_MESSAGE);

		}
	}
}
