import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO: Keyboard input
public class Board extends JPanel implements ActionListener
{
	Timer timer = new Timer(1000, this);
	Block block;
	int[][] map;
	int mapWidth, mapHeight;

	public Board(int width, int height)
	{
		setSize(width, height);
		setBackground(Color.black);

		block = new Block(width, height);
		mapWidth = width / block.size;
		mapHeight = height / block.size;
		map = new int[mapHeight][mapWidth];
		for(int i = 0; i < map[0].length; i++)
		{
			map[map.length - 1][i] = 1;
		}

		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == timer) // TODO: Not really necessary?
		{
			update();
			repaint();
		}
	}

	private void update()
	{
		moveBlock();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		render(g);
	}

	private void render(Graphics g)
	{
		//Draw map
		g.setColor(Color.white);
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{
				if(map[i][j] != 0)
				{
					int[] coords = convertIndexCartesian(i, j);
					g.fillRect(coords[0], coords[1], block.size, block.size);
				}
			}
		}

		// Draw block
		g.setColor(new Color(block.getRGB()));
		int[] coords = block.getCoords();
		int size = block.size;
		int[][] piece = block.getPiece();

		for(int i = 0; i < piece.length; i++)
		{
			for(int j = 0; j < piece[i].length; j++)
			{
				if(piece[i][j] == 1)
				{
					g.drawRect(coords[0] + j * size, coords[1] + i * size, size, size); // TODO change to fillRect
				}
			}
		}
	}

	public int[] convertCartesianIndex(int[] coords) // coords = {x, y}
	{
		int column = coords[0] / block.size;
		int row = coords[1] / block.size;
		return new int[]{row, column};
	}

	public int[] convertIndexCartesian(int row, int column)
	{
		int x = column * block.size;
		int y = row * block.size;
		return new int[]{x, y};
	}

	public void moveBlock()
	{
		int[] rowColumn = convertCartesianIndex(block.getCoords());
		int[][] piece = block.getPiece();
		int lastRow = 0;
		int[] lastColumns = new int[4];

		for(int i = 0; i < piece.length; i++)
		{
			for(int j = 0; j < piece[i].length; j++)
			{
				if(piece[i][j] == 1)
				{
					lastColumns[j] = 1;
					lastRow = i;
				}
				else
				{
					lastColumns = new int[4];
				}
			}
		}

		int checkRow = rowColumn[0] + lastRow + 1;
		boolean collision = false;
		for(int i = 0; i < 4; i++)
		{
			if(i > mapWidth - 1)
			{
				break;
			}
			if(map[checkRow][i] == 1 && lastColumns[i] == 1)
			{
				System.out.println("COLLISION!!!"); // TODO: IndexOutOfBoundsException
				collision = true;
				break;
			}
		}
		if(!collision)
		{
			int newRow = rowColumn[0] + 1;
			int[] newCoords = convertIndexCartesian(newRow, rowColumn[1]);
			block.setCoords(newCoords);
		}
	}
}