

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class MatrixTest {
	
	private final Matrix mat = new Matrix();
	
	private void assertThatMatricesAreEqual(double[][] expected, double[][] actual) {
		double delta = 0.000001;
		for (int r=0; r<expected.length;r++) {
			for (int c=0; c<expected[r].length;c++) {
				assertEquals(expected[r][c], actual[r][c], delta);
			}
		}
	}
	
	@Disabled
	void testCheckIfArrayIsValidMatrixDoubleArrayArray() {
		double[][] matrix1 = {
				{0.0},
				{0.0},
				{0.0},
				{0.0},
				};
		assertEquals(true, Matrix.checkIfArrayIsValidMatrix(matrix1));
		
		double[][] matrix2 = {
				{0.0},
				{0.0},
				{0.0},
				{0.0, 0.0},
				};
		assertEquals(false, Matrix.checkIfArrayIsValidMatrix(matrix2));
	}

	@Disabled
	void testScaleRow() {
		int[][] matrix = {
								{1,2,3},
								{2,4,6},
								{3,6,9}
							};
		
		mat.setMatrix(matrix);
		
		mat.scaleRow(1, 2);
		mat.scaleRow(2, 0.5);	
		mat.scaleRow(3, (1.0/3));	
		
		double[] expectedRowOne = {2.0, 4.0, 6.0};
		double[] expectedRowTwo = {1.0, 2.0, 3.0};
		double[] expectedRowThree = {1.0, 2.0, 3.0};
				
		assertArrayEquals(expectedRowOne, mat.getRow(1));
		assertArrayEquals(expectedRowTwo, mat.getRow(2));
		assertArrayEquals(expectedRowThree, mat.getRow(3));
	}

	@Disabled
	void testInterchangeRows() {
		int[][] matrix = {
				{1,2,3},
				{2,4,6},
				{3,6,9}
			};

		mat.setMatrix(matrix);
		
		mat.interchangeRows(1,2);
		
		double[][] expectedMat = {
									{2.0, 4.0, 6.0},
									{1.0, 2.0, 3.0},
									{3.0, 6.0, 9.0}
								 };
		assertArrayEquals(expectedMat[0],mat.getRow(1));
		assertArrayEquals(expectedMat[1],mat.getRow(2));
		assertArrayEquals(expectedMat[2],mat.getRow(3));

	}

	@Disabled
	void testAddRows() {
		int[][] matrix = {
				{1,2,3},
				{2,4,6},
				{3,6,9}
			};

		mat.setMatrix(matrix);
		
		mat.addRows(1, 2, -2);
		mat.addRows(1, 3, 1);
		
		double[][] expectedMat = {
				{1.0, 2.0, 3.0},
				{0.0, 0.0, 0.0},
				{4.0, 8.0, 12.0}
			 };
		
		assertArrayEquals(expectedMat[0],mat.getRow(1));
		assertArrayEquals(expectedMat[1],mat.getRow(2));
		assertArrayEquals(expectedMat[2],mat.getRow(3));
	}
	
	@Disabled
	void testCreateEchelonForm() {
		int[][] matrix = {
				{0,0,3,0,10},
				{2,4,0,6,9},
				{0,0,3,5,8},
				{0,0,0,0,0},
				{0,3,6,9,7},
				{0,0,0,0,1},
				{0,1,1,0,5},
				{0,0,0,5,3}
			};

		mat.setMatrix(matrix);
		
		double[][] expectedMat = {
				{2,4,0,6,9},
				{0,3,6,9,7},
				{0,1,1,0,5},
				{0,0,3,0,10},
				{0,0,3,5,8},
				{0,0,0,5,3},
				{0,0,0,0,1},
				{0,0,0,0,0}
			};
		
		mat.setMatrix(Matrix.createEchelonForm(mat).getMatrix());
		
		assertArrayEquals(expectedMat[0],mat.getRow(1));
		assertArrayEquals(expectedMat[1],mat.getRow(2));
		assertArrayEquals(expectedMat[2],mat.getRow(3));
		assertArrayEquals(expectedMat[3],mat.getRow(4));
		assertArrayEquals(expectedMat[4],mat.getRow(5));
		assertArrayEquals(expectedMat[5],mat.getRow(6));
		assertArrayEquals(expectedMat[6],mat.getRow(7));
		assertArrayEquals(expectedMat[7],mat.getRow(8));
	}
	
	@Test
	void testGetRREF() {
		double[][] matrix = {
				{1,1,1,12},
				{1,2,4,15},
				{1,3,9,16}
			};

		mat.setMatrix(matrix);
		
		double[][] expectedMat = {
				{1,0,0,7},
				{0,1,0,6},
				{0,0,1,-1}
			};
		
		mat.reduceToRREF();
		assertThatMatricesAreEqual(expectedMat, mat.getMatrix());
		
	}

}
