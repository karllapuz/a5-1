import java.util.*;

public class a5_1 {

	public static void main(String[] args) {
		
		int[] key = { 1, 0, 1, 0, 1, 0, 1, 0,
					  1, 0, 1, 0, 1, 1, 1, 0,
					  1, 0, 1, 1, 1, 0, 0, 1,
					  1, 0, 0, 1, 1, 0, 0, 1,
					  1, 0, 0, 1, 1, 0, 0, 1,
					  1, 1, 1, 1, 0, 0, 0, 0,
					  1, 1, 1, 1, 0, 0, 0, 0,
					  1, 1, 1, 1, 1, 0, 0, 1 };
		int[] result = new int[64];
		System.out.println(Arrays.toString(runA5_1(key, result, 0)));
	}
	
	public static int[] runA5_1(int[] key, int[] result, int start) {
		
		// Set up the constants and variables
		final int X_MAJORITY_POSITION = 8;
		final int Y_MAJORITY_POSITION = 10;
		final int Z_MAJORITY_POSITION = 10;
		
		final int X_XOR_POSITION1 = 13;
		final int X_XOR_POSITION2 = 16;
		final int X_XOR_POSITION3 = 17;
		final int X_XOR_POSITION4 = 18;
		
		final int Y_XOR_POSITION1 = 20;
		final int Y_XOR_POSITION2 = 21;
		
		final int Z_XOR_POSITION1 = 7;
		final int Z_XOR_POSITION2 = 20;
		final int Z_XOR_POSITION3 = 21;
		final int Z_XOR_POSITION4 = 22;
		
		boolean xShift = false;
		boolean yShift = false;
		boolean zShift = false;
		
		// Check if the array key is 64 bits
		if (key.length != 64) {
			return null;
		}
		
		// Splice the array into three different array registers
		int[] xReg = Arrays.copyOfRange(key, 0, 19);
		int[] yReg = Arrays.copyOfRange(key, 19, 41);
		int[] zReg = Arrays.copyOfRange(key, 41, key.length);
		
		// Find the majority one each register
		int majority = majority(xReg[X_MAJORITY_POSITION], yReg[Y_MAJORITY_POSITION], zReg[Z_MAJORITY_POSITION]);
		if (xReg[X_MAJORITY_POSITION] == majority) { xShift = true; }
		if (yReg[Y_MAJORITY_POSITION] == majority) { yShift = true; }
		if (zReg[Z_MAJORITY_POSITION] == majority) { zShift = true; }
		
		// XOR the values on registers to be shifted, replace the first bits of the registers to be shifted, & shift
		if (xShift) {
			int xFirstBit = xReg[X_XOR_POSITION1] ^ xReg[X_XOR_POSITION2] ^ xReg[X_XOR_POSITION3] ^ xReg[X_XOR_POSITION4];
			xReg = shift(xFirstBit, xReg);
		}
		if (yShift) {
			int yFirstBit = yReg[Y_XOR_POSITION1] ^ yReg[Y_XOR_POSITION2];
			yReg = shift(yFirstBit, yReg);
		}
		if (zShift) {
			int zFirstBit = zReg[Z_XOR_POSITION1] ^ zReg[Z_XOR_POSITION2] ^ zReg[Z_XOR_POSITION3] ^ zReg[Z_XOR_POSITION4];
			zReg = shift(zFirstBit, zReg);
		}
		
		// XOR the outputs of the last index on each register
		int xOutput = xReg[xReg.length - 1];
		int yOutput = yReg[yReg.length - 1];
		int zOutput = zReg[zReg.length - 1];
		
		int output = xOutput ^ yOutput ^ zOutput;
		result[start] = output;
		
		// Concatenate all three arrays into one
		int[] newKey = new int[64];
		newKey = Arrays.copyOf(xReg, newKey.length);
		System.arraycopy(yReg, 0, newKey, 19, yReg.length);
		System.arraycopy(zReg, 0, newKey, 41, zReg.length);
		
		if (start + 1 == 64) {
			return result;
		}
		else {
			return runA5_1(newKey, result, start + 1);
		}
	}
	
	private static int majority(int x, int y, int z) {
		
		int zeroCounter = 0;
		if (x == 0) { zeroCounter++; }
		if (y == 0) { zeroCounter++; }
		if (z == 0) { zeroCounter++; }
		
		if (zeroCounter >=2) { return 0; }
		else { return 1; }

	}
	
	private static int[] shift(int append, int[] original) {
		
		int[] first = {append};
		int[] result = Arrays.copyOf(first, original.length);
		System.arraycopy(original, 0, result, 1, original.length - 1);
		
		return result;
	}
	
}
