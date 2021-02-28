import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame
{
	final static int WIDTH = 768, HEIGHT = 1024;

	public static void main(String[] args)
	{
		new Tetris();
	}

	public Tetris()
	{
		Board p = new Board(WIDTH, HEIGHT);

		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);

		setContentPane(p);
	}
}
