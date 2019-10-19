package interfaces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestUndoable {

	public JFrame frame;
	public JPanel panel;
	public CircleInfo circleInfo;
	
	public static void main(String[] args) {
		new TestUndoable();
		
	}
	
	public TestUndoable() {
		frame = new JFrame();
		frame.setLocationRelativeTo(null);
		frame.setBackground(Color.WHITE);
		frame.setSize(500, 500);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.addMouseListener(new OnClickListener());
		
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.setColor(Color.RED);				
				g.fillOval(circleInfo.x, circleInfo.y, circleInfo.size, circleInfo.size);
			}
		};
		frame.addKeyListener(new OnKeyPressed());
		circleInfo = new CircleInfo(250, 250, 100);
		circleInfo.saveInstance();
		frame.add(panel);
		
		frame.setVisible(true);
	}
	
	class CircleInfo extends Undoable<CircleInfo> {
		private int x, y, size;
		
		public CircleInfo(int x, int y, int size) {
			this.x = x;
			this.y = y;
			this.size = size;
		}

		protected void override(CircleInfo instance) {
			x = instance.x;
			y = instance.y;
			size = instance.size;			
		}
		protected CircleInfo clone() {
			return new CircleInfo(x, y, size);
		}

		
	}
	
	public class OnClickListener implements MouseListener {

		public void mouseClicked(MouseEvent arg0) {
			circleInfo.x = frame.getMousePosition().x - (circleInfo.size/2);
			circleInfo.y = frame.getMousePosition().y - (circleInfo.size/2);
			
			circleInfo.saveInstance();
			panel.repaint();
		}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}		
	}
	public class OnKeyPressed implements KeyListener {
		public void keyPressed(KeyEvent arg0) {			
			if(arg0.getKeyCode() == KeyEvent.VK_Z) {
				System.out.println("UNDO: " + circleInfo.undo());
				
			} else if(arg0.getKeyCode() == KeyEvent.VK_Y) {
				System.out.println("REDO: " + circleInfo.redo());
			}
			
			panel.repaint();
		}
		public void keyReleased(KeyEvent arg0) {}
		public void keyTyped(KeyEvent arg0) {}
		
	}
}
