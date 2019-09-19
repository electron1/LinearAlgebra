
//This class represents a N by N matrix
//Jack Smalligan
//1 October 2018

public class SquareMatrix extends Matrix {

    public SquareMatrix(double[][] matrix) {
        for (int r = 0; r <= matrix.length; r++) {
            assert matrix.length == matrix[r].length : "Matrix must be square";
        }

        super.setMatrix(matrix);
    }// constructor (matrix arg)

    public SquareMatrix(int n) {
        //if no data is given, construct a zero matrix
        super(n,n);
    }// constructor (int arg)

    public SquareMatrix() {
        // if no size is used, construct a zero matrix of size 1
        this(1);
    }// constructor (no args)

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
    }// getIdentity

    public static SquareMatrix getInverse(SquareMatrix m) {
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

    public int getN() {
        return super.getCols();
    }

    public static SquareMatrix add(SquareMatrix one, SquareMatrix two) {
        int myN = one.getN();
        SquareMatrix result = new SquareMatrix(myN);
        for (int r = 1; r <= myN; r++) {
            for (int c = 1; c <= myN; c++) {
                result.setElement(r, c, one.getElement(r, c) + two.getElement(r, c));
            }
        }
        return result;
    }// add (any two arbitrary matrices of the same size)

    public void add(SquareMatrix other) {
        this.setMatrix(add(this, other).getMatrix());
    }// add (updating this matrix)

    public static SquareMatrix multiplyByScalar(SquareMatrix mat, int scalar) {
        int myN = mat.getN();
        SquareMatrix result = new SquareMatrix(myN);
        for (int r = 1; r <= myN; r++) {
            for (int c = 1; c <= myN; c++) {
                result.setElement(r, c, mat.getElement(r, c) * scalar);
            }
        }
        return result;
    }// multiplyByScalar (an arbitrary matrix)

    public void multiplyByScalar(int scalar) {
        this.setMatrix(multiplyByScalar(this, scalar).getMatrix());
    }// multiplyByScalar (updating this matrix)

    public static double getDeterminant(SquareMatrix mat) {
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
                sum += Math.pow(-1, i - 1) * mat.getElement(1, i) * getDeterminant(getMinor(1, i, mat));
            }
            return sum;
        }
    }// getDeterminant (an arbitrary matrix)

    public double getDeterminant() {
        return getDeterminant(this);
    }// getDeterminant (this instance)

    public static SquareMatrix getMinor(int i, int j, SquareMatrix mat) {
        // The (i,j) minor of a matrix is the matrix without row i and column j

        double[] arr = new double[(mat.getN() - 1) * (mat.getN() - 1)];
        // z keeps track of position of inserting elements in the 1D array
        int z = 0;
        for (int r = 1; r <= mat.getN(); r++) {
            for (int c = 1; c <= mat.getN(); c++) {
                if (r == i || c == j) {
                } // If we are in the i-th row or the j-th column:
                // Do not insert into the new matrix
                else {
                    arr[z] = mat.getElement(r, c);
                    z++;
                }
            }
        }
        return new SquareMatrix(arrayToMatrix(mat.getN() - 1, arr));
    }// getMinor

    public static double[][] arrayToMatrix(int n, double[] arr) {
        double[][] result = new double[n][n];
        int i = 0;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                result[r][c] = arr[i];
                i++;
            }
        }
        return result;
    }

    public static void main(String[] args) {

    }
}