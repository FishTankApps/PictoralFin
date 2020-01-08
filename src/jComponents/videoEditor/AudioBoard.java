package jComponents.videoEditor;

import static globalValues.GlobalVariables.dataFile;
import static globalValues.GlobalVariables.pfk;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import objectBinders.AudioInfo;
import objectBinders.AudioSettings;

public class AudioBoard extends JPanel {
	
	private static final long serialVersionUID = 3637279690539051586L;

	
	private JButton addAudio, removeAudio;
	private JComboBox<AudioInfo> audioSelector;
	private JSpinner startingFrame, endingFrame;
	private JLabel startingFrameLabel, endingFrameLabel;
	private JPanel settingsPanel;
	private SpinnerNumberModel startingSpinnerModel, endingSpinnerModel;
	private boolean iChangedIt = false;

	
	public AudioBoard() {
		addAudio = new JButton("Add Audio");
		//addAudio.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 10));
		addAudio.setMaximumSize(new Dimension(90, 25));
		//addAudio.setBackground(settings.getTheme().getSecondaryBaseColor());
		
		removeAudio = new JButton("Remove this Audio");
		//removeAudio.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.ITALIC + Font.BOLD, 15));
		//removeAudio.setBackground(settings.getTheme().getSecondaryBaseColor());
		
		audioSelector = new JComboBox<>();
		audioSelector.getModel().addListDataListener(new OnAudioSelectionChanges());
		//audioSelector.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 10));

		startingSpinnerModel = new SpinnerNumberModel(1,1,1,1);
		endingSpinnerModel = new SpinnerNumberModel(1,1,1,1);	
		
		startingFrame = new JSpinner(startingSpinnerModel);
		startingFrameLabel = new JLabel("Starting Frame:");
		startingFrameLabel.setHorizontalTextPosition(JLabel.RIGHT);
		//startingFrameLabel.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 10));
		startingFrame.addChangeListener(new OnSpinnerChanged());
		//startingFrame.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 12));
		
		endingFrame = new JSpinner(endingSpinnerModel);
		endingFrameLabel = new JLabel("Ending Frame:");
		endingFrameLabel.setHorizontalTextPosition(JLabel.RIGHT);
		//endingFrameLabel.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 10));
		endingFrame.addChangeListener(new OnSpinnerChanged());
		//endingFrame.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 12));
		
		SpringLayout settingsPanelLayout = new SpringLayout();
		
		settingsPanel = new JPanel(settingsPanelLayout) {
			private static final long serialVersionUID = -3496625263375052480L;
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				//g.setColor(settings.getTheme().getSecondaryHighlightColor());
				g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 20);
				
			}
		};
		
		settingsPanelLayout.putConstraint(SpringLayout.WEST, startingFrameLabel, 15, SpringLayout.WEST, settingsPanel);
		settingsPanelLayout.putConstraint(SpringLayout.WEST, startingFrame, 5, SpringLayout.EAST, startingFrameLabel);
		settingsPanelLayout.putConstraint(SpringLayout.WEST, endingFrameLabel, 15, SpringLayout.EAST, startingFrame);
		settingsPanelLayout.putConstraint(SpringLayout.WEST, endingFrame, 5, SpringLayout.EAST, endingFrameLabel);
		settingsPanelLayout.putConstraint(SpringLayout.EAST, settingsPanel, 15, SpringLayout.EAST, endingFrame);
		
		settingsPanelLayout.putConstraint(SpringLayout.NORTH, startingFrameLabel, 5, SpringLayout.NORTH, settingsPanel);
		settingsPanelLayout.putConstraint(SpringLayout.NORTH, startingFrame, 4, SpringLayout.NORTH, settingsPanel);
		settingsPanelLayout.putConstraint(SpringLayout.NORTH, endingFrameLabel, 5, SpringLayout.NORTH, settingsPanel);
		settingsPanelLayout.putConstraint(SpringLayout.NORTH, endingFrame, 4, SpringLayout.NORTH, settingsPanel);
		
		settingsPanelLayout.putConstraint(SpringLayout.WEST, removeAudio, 10, SpringLayout.WEST, settingsPanel);
		settingsPanelLayout.putConstraint(SpringLayout.EAST, removeAudio, -10, SpringLayout.EAST, settingsPanel);
		settingsPanelLayout.putConstraint(SpringLayout.NORTH, removeAudio, 10, SpringLayout.SOUTH, endingFrame);
		settingsPanelLayout.putConstraint(SpringLayout.SOUTH, removeAudio, -5, SpringLayout.SOUTH, settingsPanel);
		
		//settingsPanel.setBackground(settings.getTheme().getPrimaryHighlightColor());
		
		
		
		settingsPanel.add(startingFrameLabel);
		settingsPanel.add(startingFrame);
		settingsPanel.add(endingFrameLabel);
		settingsPanel.add(endingFrame);
		settingsPanel.add(removeAudio);		
		
		
		SpringLayout springLayout = new SpringLayout();
		this.setLayout(springLayout);
		
		springLayout.putConstraint(SpringLayout.NORTH, addAudio, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.NORTH, audioSelector, 11, SpringLayout.NORTH, this);
		
		springLayout.putConstraint(SpringLayout.WEST, addAudio, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.WEST, audioSelector, 10, SpringLayout.EAST, addAudio);
		springLayout.putConstraint(SpringLayout.EAST, this, 10, SpringLayout.EAST, audioSelector);
		
		springLayout.putConstraint(SpringLayout.NORTH, settingsPanel, 10, SpringLayout.SOUTH, audioSelector);
		springLayout.putConstraint(SpringLayout.WEST, settingsPanel, 10, SpringLayout.WEST, this);		
		springLayout.putConstraint(SpringLayout.EAST, settingsPanel, -10, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, settingsPanel, -10, SpringLayout.SOUTH, this);
		
		this.add(audioSelector);		
		this.add(settingsPanel);
		this.add(addAudio);
		
		//this.setBackground(settings.getTheme().getPrimaryBaseColor());		
		
		addAudio.addActionListener(new OnButtonClicked());
		removeAudio.addActionListener(new OnButtonClicked());
	}
	
	private class OnButtonClicked implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == addAudio) {
				importAudio();
			}else if(e.getSource() == removeAudio && audioSelector.getSelectedIndex() != -1) {
				int index = audioSelector.getSelectedIndex();
				audioSelector.removeItemAt(index);				
				refresh();
			}
		}
		public void importAudio() {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Choose an audio file:");			
			jfc.setCurrentDirectory(new File(dataFile.getLastOpenAudioLocation()));
			
			if(jfc.showOpenDialog(null) == JFileChooser.CANCEL_OPTION)
				return;
			
			File audioFile = jfc.getSelectedFile();
			
			dataFile.setLastOpenAudioLocation(audioFile.getParent());
			
			audioSelector.addItem(new AudioInfo(audioFile, 0, 0));
			audioSelector.setSelectedIndex(audioSelector.getModel().getSize()-1);
			
			refresh();
		}		
	}
	private class OnSpinnerChanged implements ChangeListener{

		public void stateChanged(ChangeEvent arg0) {
			if(audioSelector.getModel().getSize() > 0 && audioSelector.getSelectedIndex() != -1 && !iChangedIt) {
				AudioInfo ai = (AudioInfo) audioSelector.getSelectedItem();
				ai.setEndingFrame((Integer) endingSpinnerModel.getValue()); 
				ai.setStartingFrame((Integer) startingSpinnerModel.getValue()); 
			}			
		}
		
	}
	private class OnAudioSelectionChanges implements ListDataListener{

		public void contentsChanged(ListDataEvent arg0) {
			if(audioSelector.getModel().getSize() > 0 && audioSelector.getSelectedIndex() != -1) {
				iChangedIt = true;
				AudioInfo ai = (AudioInfo) audioSelector.getSelectedItem();
				endingSpinnerModel.setValue(ai.getEndingFrame());
				startingSpinnerModel.setValue(ai.getStartingFrame());
				iChangedIt = false;
			}		
		}
		public void intervalAdded(ListDataEvent arg0) {}
		public void intervalRemoved(ListDataEvent arg0) {}		
	}
	
	public AudioSettings generateAudioSettings() {
		AudioSettings toReturn = new AudioSettings();

		for(int index = 0; index < audioSelector.getItemCount(); index++)
			toReturn.addAudioInfo(audioSelector.getItemAt(index));
		
		return toReturn;
	}
	
	public void refresh() {
		if(pfk != null) {
			
			startingSpinnerModel.setMaximum(pfk.getFrameTimeLine().length());
			endingSpinnerModel.setMaximum(pfk.getFrameTimeLine().length());
			endingSpinnerModel.setMinimum(1);
			endingSpinnerModel.setMinimum(1);
			settingsPanel.setVisible(audioSelector.getModel().getSize() != 0);
			this.setPreferredSize(new Dimension(this.getParent().getSize().width - 10, audioSelector.getHeight() + 20 + ((!settingsPanel.isVisible()) ? 0 : 75)));
			repaint();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//g.setColor(settings.getTheme().getPrimaryHighlightColor());
		g.fillRoundRect(0, 0, getWidth()-2, getHeight(), 10, 15);

	}
}
