import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class HelpDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextArea txtArInstructions;
	private JButton btnOK;

	/**
	 * Create the dialog.
	 */
	public HelpDialog() {
		setBounds(10, 50, 700, 310);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				txtArInstructions = new JTextArea();
				txtArInstructions.setEditable(false);
				txtArInstructions.setText("Instructions: \n \n"
						+ "Grow the snake as long as you can! \n \n"
						+ "Use the arrow keys to control the snake (and you can pause at any time with the top button)\n \n"
						+ "Each level has a certain number of points to get until you can proceed to the next one \n \n"
						+ "Watch out though, the snake gets faster and faster with each stage! \n \n"
						+ "Make sure you don't collide into walls, else GAME OVER \n \n"
						+ "Have fun and thanks for playing!");
				
				txtArInstructions.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 13));
				contentPanel.add(txtArInstructions);
			}
			{
				JButton btnOK = new JButton("OK");
				btnOK.setActionCommand("OK");
				buttonPane.add(btnOK);
				getRootPane().setDefaultButton(btnOK);
				btnOK.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						btnOK_mouseClicked(arg0);
					}
				});
			}
		}
	}
	
	protected void btnOK_mouseClicked(MouseEvent e) {
		this.setVisible(false);
	}

}
