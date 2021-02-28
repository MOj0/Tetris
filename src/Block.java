public class Block
{
	private int[][] block = new int[4][4];
	private int x, y;
	private int rgb;

	final public int size = 64;

	public Block(int width, int height)
	{
		y = 0;
		x = (int) (Math.random() * width / size);
		rgb = (int) (Math.random() * 256 * 256 * 256);

		selectRandomBlock();
	}

	private void selectRandomBlock()
	{
		int r = (int) (Math.random() * 5);

		if(r == 0) // I shape
		{
			block = new int[][]{{1, 0, 0, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}};
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

	public void setCoords(int[] coords)
	{
		x = coords[0];
		y = coords[1];
	}
}