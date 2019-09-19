
/**
 * This class is a special case of the matrix class that offers a few additional
 * methods that are specific to square matrices. Most notably, inverses and
 * determinants.
 * @author Jack Smalligan
 * @version 1.0
 */
public class SquareMatrix extends Matrix {

    /**
     * Constructs a {@code SquareMatrix} with the given data
     * @param matrix a 2D array to back this Matrix
     */
    public SquareMatrix(double[][] matrix) {
        for (int r = 0; r <= matrix.length; r++) {
            assert matrix.length == matrix[r].length : "Matrix must be square";
        }

        super.setMatrix(matrix);
    }

    /**
     * Constructs a square zero matrix of the given dimension
     * @param n the number of rows and columns for this matrix
     */
    public SquareMatrix(int n) {
        super(n, n);
    }

    /**
     * If no size is specified, construct a zero matrix of size 1
     */
    public SquareMatrix() {
        this(1);
    }

    /**
     * Generate the identity matrix of the given dimension
     * @param n the number of rows and columns in the desired identity
     * @return an identity matrix of the given dimension
     */
    public static SquareMatrix getIdentity(int n) {
        SquareMatrix output = new SquareMatrix(n);

        for (int r = 1; r <= n; r++) {
            for (int c = 1; c <= n; c++) {
                if (r == c) {
                    output.setElement(r, c, 1);
                }
            }
        }
        return output;
    }

    /**
     * Construct the inverse of the given matrix
     * @param m a {@code SquareMatrix} whose inverse is to be determined
     * @return the inverse of {@code m}
     * @throws MatrixException if the inverse doesn't exist
     */
    public static SquareMatrix getInverse(SquareMatrix m) throws MatrixException {
        if (Math.abs(m.getDeterminant()) < 0.00001) {
            throw new MatrixException("Matrix is not invertible");
        }

        Matrix augment = new Matrix(m.getN(), m.getN() * 2);
        SquareMatrix ident = SquareMatrix.getIdentity(m.getN());

        for (int r = 1; r <= m.getN(); r++) {
            for (int c = 1; c <= m.getN(); c++) {
                augment.setElement(r, c, m.getElement(r, c));
            }
            for (int c = m.getN() + 1; c <= m.getN() * 2; c++) {
                augment.setElement(r, c, ident.getElement(r, c - m.getN()));
            }
        }

        Matrix rowReduced = Matrix.getRREF(augment);

        SquareMatrix output = new SquareMatrix(m.getN());
        for (int r = 1; r <= m.getN(); r++) {
            for (int c = m.getN() + 1; c <= m.getN() * 2; c++) {
                output.setElement(r, c - m.getN(), rowReduced.getElement(r, c));
            }
        }

        return output;
    }

    /**
     * Gets the dimension of this matrix
     * @return the number of rows and columns
     */
    public int getN() {
        return super.getCols();
    }

    /**
     * Calculates the determinant of a given matrix
     * @param mat the matrix to find the determinant of
     * @return the determinant of {@code mat}
     */
    public static double getDeterminant(SquareMatrix mat) {
        // Special case where n=1:
        if (mat.getN() == 1) {
            return mat.getElement(1, 1);
        }
        // Otherwise:
        // Define recursively:
        // Base case: 2 X 2 matrix, return ad-bc:
        // |a b|
        // |c d|
        // Otherwise: return the sum of the first value in each column * determinant of
        // the (1, c) minor
        // In this sum, alternate positive and negative terms, thus Math.pow(-1,i)
        if (mat.getN() == 2) {
            return (mat.getElement(1, 1) * mat.getElement(2, 2)) - (mat.getElement(2, 1) * mat.getElement(1, 2));
        } // base case
        else {
            int sum = 0;
            for (int i = 1; i <= mat.getN(); i++) {
                sum += Math.pow(-1, i - 1) * mat.getElement(1, i) * getDeterminant((SquareMatrix) getMinor(1, i, mat));
            }
            return sum;
        }
    }

    /**
     * Calculates the determinant of this matrix
     * @return the determinant of this matrix
     */
    public double getDeterminant() {
        return getDeterminant(this);
    }
}