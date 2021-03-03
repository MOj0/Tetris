public class Block
{
	private int[][] block = new int[4][4];
	private int x, y, r;
	private final int rgb;
	final public int size = 64;

	public Block(int width)
	{
		y = -1 * size;
		x = ((int) ((Math.random() * (width - size * 4)) / size)) * size; // Yikes
		rgb = (int) (Math.random() * 256 * 256 * 256);

		selectRandomBlock();
	}

	private void selectRandomBlock()
	{
		int r = (int) (Math.random() * 5);

		if(r == 0) // I shape
		{
			block = new int[][]{{0, 0, 0, 1}, {0, 0, 0, 1}, {0, 0, 0, 1}, {0, 0, 0, 1}};
		}
		else if(r == 1) // L shape
		{
			block = new int[][]{{0, 0, 0, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}, {1, 1, 0, 0}};
		}
		else if(r == 2) // O shape
		{
			block = new int[][]{{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}};
		}
		else if(r == 3) // Z shape
		{
			block = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 1, 1, 0}, {0, 0, 1, 1}};
		}
		else // T shape
		{
			block = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 1, 0, 0}, {1, 1, 1, 0}};
		}
	}

	public int[][] getPiece()
	{
		return block;
	}

	public int[] getCoords()
	{
		return new int[]{x, y};
	}

	public int getRGB()
	{
		return rgb;
	}

	public void move(int direction)
	{
		if(direction == 0) // Move down
		{
			y += size;
		}
		else
		{
			x += size * direction; // direction = -1 -> left, direction = 1 -> right
		}
	}

	public void rotate() // Rotate for -90 deg
	{
		int[][] newArray = new int[4][4];
		for(int j = 0; j < 4; j++)
		{
			int[] temp = new int[4];
			for(int i = 3; i > -1; i--)
			{
				temp[3 - i] = block[i][j];
			}
			newArray[j] = temp;
		}
		block = newArray;
	}
}