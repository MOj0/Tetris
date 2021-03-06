import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Arrays;

public class Board extends JPanel implements ActionListener, KeyListener
{
	Timer timer = new Timer(1000, this);
	Block block;
	Color[][] map;
	int mapWidth, mapHeight;
	int key;
	int state; // 1 - inGame, 2 - GameOver
	int score;
	Font font;

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
		map = new Color[mapHeight][mapWidth];
		for(int i = 0; i < map[0].length; i++)
		{
			map[mapHeight - 1][i] = new Color(255, 255, 255);
		}

		font = new Font("TimesRoman", Font.BOLD, 24);
		score = 0;
		state = 1;

		playSound("Tetris 99 - Main Theme.wav");

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

		checkFullLine();

		if(score > 5000)
		{
			timer.setDelay(500);
		}
		else if(score > 2500)
		{
			timer.setDelay(600);
		}
		else if(score > 1000)
		{
			timer.setDelay(800);
		}
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
		if(state == 1)
		{
			// Draw grid
			g.setColor(new Color(255, 255, 255, 110));
			for(int i = 0; i < mapHeight; i++)
			{
				g.drawLine(0, i * block.size, mapWidth * block.size, i * block.size);
			}
			for(int j = 0; j <= mapWidth; j++)
			{
				g.drawLine(j * block.size, 0, j * block.size, mapHeight * block.size);
			}

			//Draw map
			for(int i = 0; i < map.length; i++)
			{
				for(int j = 0; j < map[i].length; j++)
				{
					if(map[i][j] != null)
					{
						int[] coords = convertIndexToCartesian(i, j);
						g.setColor(map[i][j]);
						g.fillRect(coords[0], coords[1], block.size, block.size);

						g.setColor(Color.white);
						g.drawRect(coords[0], coords[1], block.size, block.size);
					}
				}
			}

			// Draw block
			int[] coords = block.getCoords();
			int size = block.size;
			int[][] piece = block.getPiece();

			for(int i = 0; i < piece.length; i++)
			{
				for(int j = 0; j < piece[i].length; j++)
				{
					if(piece[i][j] == 1)
					{
						g.setColor(new Color(block.getRGB()));
						g.fillRect(coords[0] + j * size, coords[1] + i * size, size, size);

						g.setColor(Color.white);
						g.drawRect(coords[0] + j * size, coords[1] + i * size, size, size);
					}
				}
			}

			// Display score
			g.drawString("Score: " + score, 10, 40);
		}
		else if(state == 2)
		{
			g.setColor(Color.white);
			g.setFont(font);
			g.drawString("Game Over", getWidth() / 2 - 50, getHeight() / 2 - 30);
			g.drawString("Press R to restart", getWidth() / 2 - 75, getHeight() / 2);
			g.drawString("Score: " + score, getWidth() / 2 - 50, getHeight() / 2 + 30);
		}
	}


	public void checkFullLine()
	{
		for(int i = 0; i < mapHeight - 1; i++)
		{
			for(int l = 0; l < mapWidth; l++)
			{
				if(map[i][l] == null)
				{
					break;
				}
				if(l == mapWidth - 1)
				{
					Arrays.fill(map[i], null);

					for(int j = i - 1; j > -1; j--)
					{
						for(int k = 0; k < mapWidth; k++)
						{
							if(map[j][k] != null)
							{
								int m = j + 1;
								while(map[m][k] == null)
								{
									m++;
								}
								m--;
								map[m][k] = map[j][k];

								map[j][k] = null;
							}
						}
					}
					score += mapWidth * 10;
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

		for(int[] line : piece)
		{
			for(int j = 0; j < line.length; j++)
			{
				if(line[j] == 1)
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

		// Ground check
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				if(piece[i][j] == 1)
				{
					int mapY = rowColumn[0] + 1 + i; // + 1 to simulate another fall
					int mapX = rowColumn[1] + j;

					if(mapY > 0 && mapY < mapHeight && mapX >= 0 && mapX < mapWidth && map[mapY][mapX] != null)
					{
						for(int x = 0; x < 4; x++)
						{
							for(int y = 0; y < 4; y++)
							{
								if(piece[y][x] == 1)
								{
									if(rowColumn[0] + y <= 0) // Game Over
									{
										state = 2;
										timer.stop();
										return false;
									}
									map[rowColumn[0] + y][rowColumn[1] + x] = new Color(block.getRGB()); // Make block part of the map
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

		if(direction == -1) // left check
		{
			int checkLeft = rowColumn[1] + leftMost - 1;
			if(checkLeft < 0) // Left most position
			{
				return false;
			}

			if(rowColumn[0] < 0) // Block just spawned
			{
				return true;
			}
			for(int i = 0; i < 4; i++) // Check map to the left
			{
				if(map[rowColumn[0] + i][checkLeft] != null && piece[i][leftMost] == 1)
				{
					return false;
				}
			}
		}
		else if(direction == 1) // right check
		{
			int checkRight = rowColumn[1] + rightMost + 1;
			if(checkRight >= mapWidth) // right most position
			{
				return false;
			}

			if(rowColumn[0] < 0) // Block just spawned
			{
				return true;
			}
			for(int i = 0; i < 4; i++) // Check map to the right
			{
				if(map[rowColumn[0] + i][checkRight] != null && piece[i][rightMost] == 1)
				{
					return false;
				}
			}
		}
		else if(direction == 2) // rotate check
		{
			int[][] rotatedPiece = block.rotate();

			for(int i = 0; i < 4; i++)
			{
				for(int j = 0; j < 4; j++)
				{
					if(rotatedPiece[i][j] == 1)
					{
						int mapY = rowColumn[0] + i;
						int mapX = rowColumn[1] + j;

						if(mapY < 0 || mapX < 0 || mapX >= mapWidth || map[mapY][mapX] != null)
						{
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

		if(state == 1)
		{
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
				score++;
				update();
			}
			else if(key == 38 && collisionCheck(2)) // rotate
			{
				block.setPiece(block.rotate());
			}
		}
		if(key == 82) // R
		{
			timer.stop();

			// Create a tetrominoe
			block = new Block(getSize().width);

			for(int i = 0; i < map.length; i++)
			{
				Color value = null;
				if(i == map.length - 1)
				{
					value = new Color(255, 255, 255);
				}
				Arrays.fill(map[i], value);
			}

			score = 0;
			state = 1;
			//Start a 1 second timer
			timer.setDelay(1000);
			timer.start();
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}


	public static void playSound(String path)
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			// Volume Control
			FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volumeControl.setValue(-15.0f); // Reduce volume by 15 decibels.
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		catch(Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}
}