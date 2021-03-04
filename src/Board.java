import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//TODO Clear line if its full
public class Board extends JPanel implements ActionListener, KeyListener
{
	Timer timer = new Timer(1000, this);
	Block block;
	int[][] map;
	int mapWidth, mapHeight;
	int key;

	public Board(int width, int height)
	{
		// JPanel stuff
		setSize(width, height);
		setBackground(Color.black);
		setFocusable(true);
		requestFocus();
		addKeyListener(this);

		// Create a tetrominoe
		block = new Block(width);

		// Variables
		mapWidth = width / block.size;
		mapHeight = height / block.size;
		map = new int[mapHeight][mapWidth];
		for(int i = 0; i < map[0].length; i++)
		{
			map[map.length - 1][i] = 1;
		}

		//Start a 1 second timer
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		update();
		repaint();
	}

	private void update()
	{
		if(collisionCheck(0)) // move the block
		{
			block.move(0);
		}

//		// Print map TODO DELETE
//		for(int i = 0; i < map.length; i++)
//		{
//			for(int j = 0; j < map[i].length; j++)
//			{
//				System.out.print(map[i][j] + " ");
//			}
//			System.out.println();
//		}
//		System.out.println("=========================================================");
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		requestFocus(true);
		render(g);
	}

	private void render(Graphics g)
	{
		g.setColor(Color.white);
		// Testing....
		for(int i = 0; i < mapHeight; i++)
		{
			g.drawLine(0, i * block.size, mapWidth * block.size, i * block.size);
		}
		for(int j = 0; j <= mapWidth; j++)
		{
			g.drawLine(j * block.size, 0, j * block.size, mapHeight * block.size);
		}

		//Draw floor
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{
				if(map[i][j] != 0)
				{
					int[] coords = convertIndexToCartesian(i, j);
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
					g.fillRect(coords[0] + j * size, coords[1] + i * size, size, size);
				}
			}
		}
	}

	public int[] convertCartesianToIndex(int[] coords) // coords = {x, y}
	{
		int column = coords[0] / block.size;
		int row = coords[1] / block.size;
		return new int[]{row, column};
	}

	public int[] convertIndexToCartesian(int row, int column)
	{
		int x = column * block.size;
		int y = row * block.size;
		return new int[]{x, y};
	}

	public boolean collisionCheck(int direction)
	{
		int[] rowColumn = convertCartesianToIndex(block.getCoords()); // top left y, x indices
		int[][] piece = block.getPiece();

		int leftMost = 4; // set it to max value
		int rightMost = 0; // set it to min value

		for(int i = 0; i < piece.length; i++)
		{
			for(int j = 0; j < piece[i].length; j++)
			{
				if(piece[i][j] == 1)
				{
					if(j < leftMost)
					{
						leftMost = j;
					}
					if(j > rightMost)
					{
						rightMost = j;
					}
				}
			}
		}

		if(direction == -1)
		{
			int checkLeft = rowColumn[1] + leftMost - 1;
			return checkLeft >= 0;
		}
		else if(direction == 1)
		{
			int checkRight = rowColumn[1] + rightMost + 1;
			return checkRight < mapWidth;
		}


		// Ground check
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				if(piece[i][j] == 1)
				{
					int mapY = rowColumn[0] + 1 + i;
					int mapX = rowColumn[1] + j;

					if(mapY > 0 && mapY < mapHeight && mapX >= 0 && mapX < mapWidth)
					{
						if(map[mapY][mapX] != 0)
						{
							for(int x = 0; x < 4; x++)
							{
								for(int y = 0; y < 4; y++)
								{
									if(piece[y][x] == 1)
									{
										map[rowColumn[0] + y][rowColumn[1] + x] = 1;
									}
								}
							}
							// Create a tetromino
							block = new Block(getSize().width);
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		key = e.getKeyCode();

		if(key == 37 && collisionCheck(-1)) // move left
		{
			block.move(-1);
		}
		else if(key == 39 && collisionCheck(1)) // move right
		{
			block.move(1);
		}
		else if(key == 40) // move down
		{
			update();
		}
		else if(key == 38 && collisionCheck(0)) // rotate
		{
			block.rotate();
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}
}