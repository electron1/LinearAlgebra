import java.util.Arrays;

/**
 * This class represents any matrix with real numbers (in practice we can only
 * represent rational numbers). The class is backed by a two-dimensional array
 * of doubles.
 * @author Jack Smalligan
 * @version 1.0
 */
public class Matrix {

    // By convention in this class, rows and columns start at 1

    /**
     * This is used as how close a double has to be to be considered equal to zero
     * for purposes of double equality comparison
     */
    private final static double DELTA = 0.00000001;

    private double determinant;
    private double[][] mat;
    private int rows;
    private int cols;

    /**
     * Constructs a Matrix with 1 row, one column, and the element 0.0
     */
    public Matrix() {
        this(1, 1);
    }

    /**
     * Constructs the zero matrix with {@code r} rows and {@code c} columns
     * @param r number of rows
     * @param c number of columns
     */
    public Matrix(int r, int c) {
        assert r <= this.rows && r >= 1 : r + "rows is not possible";
        assert c <= this.rows && c >= 1 : c + "columns is not possible";

        this.mat = new double[r][c];
        this.rows = r;
        this.cols = c;
    }

    /**
     * Constructs a matrix with the given data in it
     * @param matrix a 2D array of doubles to be copied into the matrix
     */
    public Matrix(double[][] matrix) {
        assert checkIfArrayIsValidMatrix(matrix) : "Matrix should have the same number of elements in each row";

        this.mat = matrix;
        this.rows = this.mat.length;
        this.cols = this.mat[0].length;

    }

    /**
     * Constructs a matrix with the given data in it
     * @param matrix a 2D array of ints to be copied into the matrix
     */
    public Matrix(int[][] matrix) {
        assert checkIfArrayIsValidMatrix(matrix) : "Matrix should have the same number of elements in each row";

        this.mat = new double[matrix.length][matrix[0].length];
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[r].length; c++) {
                this.mat[r][c] = matrix[r][c];
            }
        }

        this.rows = this.mat.length;
        this.cols = this.mat[0].length;
    }

    /**
     * A 2D array is a valid matrix for the purposes of this method if it has the
     * same number of elements in each row
     * @param arr a 2 dimensional array of doubles
     * @return whether this array is valid as a matrix or not
     */
    private static boolean checkIfArrayIsValidMatrix(double[][] arr) {
        int c = arr[0].length;
        for (double[] row : arr) {
            if (row.length != c) {
                return false;
            }
        }
        return true;
    }

    /**
     * A 2D array is a valid matrix for the purposes of this method if it has the
     * same number of elements in each row
     * @param arr a 2 dimensional array of ints
     * @return whether this array is valid as a matrix or not
     */
    private static boolean checkIfArrayIsValidMatrix(int[][] arr) {
        int c = arr[0].length;
        for (int[] row : arr) {
            if (row.length != c) {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts a 2D array of ints to a 2D array of doubles
     * @param mat a 2D array of ints
     * @return a 2D array of doubles with values copied from {@code mat}
     */
    private static double[][] intMatrixToDoubleMatrix(int[][] mat) {
        assert checkIfArrayIsValidMatrix(mat) : "Matrix should have the same number of elements in the same row";
        double[][] output = new double[mat.length][mat[0].length];

        for (int r = 0; r < mat.length; r++) {
            for (int c = 0; c < mat[r].length; c++) {
                output[r][c] = mat[r][c];
            }
        }
        return output;
    }

    /**
     * Counts and returns the number of leading zeros in an array
     * @param r an array to find the number of leading zeros
     * @return the number of leading zeros in this array
     */
    private static int countLeadingZeros(double[] r) {

        int count = 0;
        for (double d : r) {
            if (d == 0.0) {
                count++;
            } else {
                return count;
            }
        }
        return count;
    }

    /**
     * This will create and return an echelon form of the given matrix, without
     * modifying the input's contents
     * @param input a {@code Matrix} whose echelon form is to be found
     * @return an echelon form of {@code input}
     */
    public static Matrix createEchelonForm(Matrix input) {
        Matrix output = input.clone();

        double[][] matrix = output.getMatrix();

        // Sort by the number of leading zeros in each row
        Arrays.sort(matrix, (rowOne, rowTwo) -> countLeadingZeros(rowOne) - countLeadingZeros(rowTwo));

        output.setMatrix(matrix);

        int currentRow = 1;

        while (currentRow <= output.rows - 1) {
            if (output.isAllZeros(currentRow)) {
                currentRow += 1;
                continue;
            } else {
                for (int r = currentRow + 1; r <= output.getRows(); r++) {
                    int leadingEntryColumn = output.getLeadingEntryColumn(currentRow);
                    double factor = -output.getElement(r, leadingEntryColumn) / output.getLeadingEntry(currentRow);
                    output.addRows(currentRow, r, factor);
                }
                currentRow += 1;
            }
        }

        output.moveZerosToBottom();
        return output;
    }

    /**
     * Given a matrix as an input, it will find that matrix's reduced row echelon
     * form, returning that as a new matrix
     * @param input a {@code Matrix} whose reduced row echelon form is to be found
     * @return the reduced row echelon form of {@code input}
     */
    public static Matrix getRREF(Matrix input) {
        Matrix output = createEchelonForm(input);

        // create a leading 1 in each nonzero row
        for (int i = 1; i <= output.getRows(); i++) {
            double leadingEntry = output.getLeadingEntry(i);
            if (leadingEntry != 0) {
                output.scaleRow(i, 1 / leadingEntry);
            }
        }

        int currentRow = output.getRows();

        while (currentRow >= 1) {
            if (output.isAllZeros(currentRow)) {
                currentRow -= 1;
                continue;
            } else {
                int leadingEntryColumn = output.getLeadingEntryColumn(currentRow);
                for (int r = currentRow - 1; r >= 1; r--) {

                    if (leadingEntryColumn != -1) { // if that row is not all zeros
                        double factor = -output.getElement(r, leadingEntryColumn);
                        output.addRows(currentRow, r, factor);
                    }

                }
                currentRow -= 1;
            }
        }

        output.moveZerosToBottom();

        return output;
    }

    /**
     * @return a new {@code Matrix} with the same entries as this one
     */
    @Override
    public Matrix clone() {
        double[][] outputMat = new double[this.getRows()][this.getCols()];
        for (int r = 1; r <= outputMat.length; r++) {
            outputMat[r - 1] = this.getRow(r).clone();
        }
        Matrix output = new Matrix(outputMat);
        return output;
    }

    /**
     * Two matrices are equal if and only if all of their elements are the same
     */
    @Override
    public boolean equals(Object other) {
        if (other != null) {
            try {
                if (this.getRows() != ((Matrix) other).getRows() || this.getCols() != ((Matrix) other).getCols()) {
                    return false;
                }
                for (int r = 1; r <= this.getRows(); r++) {
                    for (int c = 1; c <= this.getCols(); c++) {
                        if (Math.abs(((Matrix) other).getElement(r, c) - this.getElement(r, c)) > DELTA) {
                            return false;
                        }
                    }
                }
            } catch (ClassCastException e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Getter for the 2D array that backs this matrix
     * @return the 2D array for this matrix
     */
    public double[][] getMatrix() {
        return this.mat;
    }

    /**
     * Getter for the number of rows
     * @return the number of rows in this matrix
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * Getter for the number of columns
     * @return the number of columns in this matrix
     */
    public int getCols() {
        return this.cols;
    }

    /**
     * Getter for a single row
     * @param row the row to be accessed
     * @return the requested row as an array of doubles
     */
    public double[] getRow(int row) {
        assert row <= this.rows && row >= 1 : "Row " + row + " not in matrix";

        return this.mat[row - 1];
    }

    /**
     * Getter for a single column
     * @param col the column to be accessed
     * @return the requested column as an array of doubles
     */
    public double[] getCol(int col) {
        assert col <= this.cols && col >= 1 : "Column " + col + " not in matrix";

        double[] output = new double[this.cols];
        for (int i = 0; i < this.rows; i++) {
            output[i] = this.mat[i][col - 1];
        }

        return output;
    }

    /**
     * Getter for the element in a particular row and column
     * @param r the row of the desired element. {@code 1 <= r <= this.getRows()}
     * @param c the column of the desired element. {@code 1 <= c <= this.getCols()}
     * @return the element in row {@code r} and column {@code c}
     */
    public double getElement(int r, int c) {
        assert r <= this.rows && r >= 1 : "Row " + r + " not in matrix";
        assert c <= this.cols && c >= 1 : "Column " + c + " not in matrix";
        return this.mat[r - 1][c - 1];
    }

    /**
     * Gets the leading entry of row r
     * @param r the row in which to find the leading entry
     * @return the leading entry if one exists, 0 otherwise
     */
    public double getLeadingEntry(int r) {
        assert r >= 1 && r <= this.rows : "Row " + r + " not in matrix";

        double[] row = this.getRow(r);

        for (double d : row) {
            if (Math.abs(d) > DELTA) {
                return d;
            }
        }

        return 0;
    }

    /**
     * Get the column in which the leading entry is located
     * @param r the row in which to find the leading entry's column
     * @return the column where the leading entry occurs in this row, if one exists,
     *         -1 otherwise.
     */
    public int getLeadingEntryColumn(int r) {
        assert r >= 1 && r <= this.rows : "Row " + r + " not in matrix";

        double[] row = this.getRow(r);

        for (int i = 0; i < row.length; i++) {
            if (Math.abs(row[i]) > DELTA) { // if row[i] != 0 essentially
                return i + 1; // add one to get the column number, which begins at 1
            }
        }

        return -1;
    }

    /**
     * Setter for a single element of
     * @param r     the row of the element to be set
     * @param c     the column of the element to be set
     * @param value the new value to be set in row {@code r} and column {@code c}
     */
    public void setElement(int r, int c, double value) {
        assert r >= 1 && r <= this.rows : "Row " + r + " not in matrix";
        assert c >= 1 && c <= this.cols : "Column " + c + " not in matrix";

        this.mat[r - 1][c - 1] = value;
    }

    /**
     * Setter for the 2D array backing this matrix
     * @param matrix the new array to back this matrix
     */
    public void setMatrix(double[][] matrix) {
        assert checkIfArrayIsValidMatrix(matrix) : "Matrix should have the same number of elements in each row";
        this.mat = matrix;
        this.rows = matrix.length;
        this.cols = matrix[0].length;
    }

    /**
     * Setter for the 2D array backing this matrix
     * @param matrix the new array to back this matrix
     */
    public void setMatrix(int[][] matrix) {
        assert checkIfArrayIsValidMatrix(matrix) : "Matrix should have the same number of elements in each row";

        this.mat = intMatrixToDoubleMatrix(matrix);
        this.rows = matrix.length;
        this.cols = matrix[0].length;
    }

    /**
     * Creates the (i, j) minor of a given matrix
     * @param i   the row to be excluded
     * @param j   the column to be excluded
     * @param mat the matrix to find the minor of
     * @return the (i, j) minor of {@code mat}. Note that this method will return a
     *         {@code SquareMatrix} if rows = columns
     */
    public static Matrix getMinor(int i, int j, Matrix mat) {
        // The (i,j) minor of a matrix is the matrix without row i and column j

        double[] arr = new double[(mat.getRows() - 1) * (mat.getCols() - 1)];
        // z keeps track of position of inserting elements in the 1D array
        int z = 0;
        for (int r = 1; r <= mat.getRows(); r++) {
            for (int c = 1; c <= mat.getCols(); c++) {
                if (r == i || c == j) {
                } // If we are in the i-th row or the j-th column:
                // Do not insert into the new matrix
                else {
                    arr[z] = mat.getElement(r, c);
                    z++;
                }
            }
        }
        return mat.getRows() == mat.getCols()
                ? new SquareMatrix(arrayToMatrix(mat.getRows() - 1, mat.getCols() - 1, arr))
                        : new Matrix(arrayToMatrix(mat.getRows() - 1, mat.getCols(), arr));
    }

    /**
     * Converts a one dimensional array of doubles to a two-dimensional array that
     * could be used to back a {@code Matrix}.
     * @param rows the number of rows to have in the 2D array
     * @param cols the number of columns to have in the 2D array
     * @param arr  the 1D array that is being made into a 2D array
     * @return a 2D array with the elements of {@code arr} copied into it
     */
    private static double[][] arrayToMatrix(int rows, int cols, double[] arr) {
        assert rows * cols == arr.length : "Incorrect number of elements or incorrect parameter for rows or cols";

        double[][] result = new double[rows][cols];
        int i = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = arr[i];
                i++;
            }
        }
        return result;
    }

    /**
     * Adds two arbitrary matrices together and returns a new matrix storing the
     * result
     * @param one first matrix
     * @param two second matrix
     * @return the sum of {@code one} and {@code two}
     */
    public static Matrix add(Matrix one, Matrix two) {
        assert one.getRows() == two.getRows()
                && one.getCols() == two.getCols() : "Matrices must have the same dimension to add them";

                Matrix result = new Matrix(one.getRows(), one.getCols());
                for (int r = 1; r <= one.getCols(); r++) {
                    for (int c = 1; c <= two.getCols(); c++) {
                        result.setElement(r, c, one.getElement(r, c) + two.getElement(r, c));
                    }
                }
                return result;
    }

    /**
     * Adds {@code other} to this matrix, updating this one
     * @param other a matrix to be added
     */
    public void add(Matrix other) {
        this.setMatrix(add(this, other).getMatrix());
    }

    /**
     * Multiply a given matrix by a scalar
     * @param mat    the matrix to be scaled
     * @param scalar the scalar to scale by
     * @return a new matrix that is {@code mat} scaled by {@code scalar}
     */
    public static Matrix multiplyByScalar(Matrix mat, double scalar) {
        int rows = mat.getRows();
        int cols = mat.getCols();
        Matrix result = new Matrix(rows, cols);
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                result.setElement(r, c, mat.getElement(r, c) * scalar);
            }
        }
        return result;
    }

    /**
     * Scales this matrix by {@code scalar}
     * @param scalar a value to scale the matrix by
     */
    public void multiplyByScalar(int scalar) {
        this.setMatrix(multiplyByScalar(this, scalar).getMatrix());
    }

    /**
     * Scales {@code row} by {@code factor}
     * @param row    the row to be scaled
     * @param factor the factor to be scaled by
     */
    public void scaleRow(int row, double factor) {
        assert row <= this.rows && row >= 1 : "Row " + row + " not in matrix";
        assert factor != 0 : "Do not scale by zero";

        for (int i = 0; i < this.mat[row - 1].length; i++) {
            this.mat[row - 1][i] *= factor;
        }
    }

    /**
     * Exchanges {@code rowOne} with {@code rowTwo}
     * @param rowOne first row to be interchanged
     * @param rowTwo second row to be interchanged
     */
    public void interchangeRows(int rowOne, int rowTwo) {
        assert rowOne <= this.rows && rowOne >= 1 : "Row " + rowOne + " not in matrix";
        assert rowTwo <= this.rows && rowTwo >= 1 : "Row " + rowTwo + " not in matrix";

        double[] temp = this.mat[rowOne - 1];
        this.mat[rowOne - 1] = this.mat[rowTwo - 1];
        this.mat[rowTwo - 1] = temp;
    }

    /**
     * Scales {@code rowOne} by {@code factor} and adds it to {@code rowTwo}
     * @param rowOne the row to be scaled
     * @param rowTwo the row being modified
     * @param factor the scale factor for {@code rowOne}
     */
    public void addRows(int rowOne, int rowTwo, double factor) {
        assert rowOne <= this.rows && rowOne >= 1 : "Row " + rowOne + " not in matrix";
        assert rowTwo <= this.rows && rowTwo >= 1 : "Row " + rowTwo + " not in matrix";
        // I will allow scaling by zero, but it has no effect on the matrix

        for (int i = 0; i < this.mat[rowOne - 1].length; i++) {
            this.mat[rowTwo - 1][i] += (this.mat[rowOne - 1][i] * factor);
        }
    }

    /**
     * Moves all rows of all zeros to the bottom of this matrix
     */
    private void moveZerosToBottom() {
        double[][] output = new double[this.rows][this.cols];
        int index = 0;
        for (int i = 1; i <= this.rows; i++) {
            if (!this.isAllZeros(i)) {
                output[index] = this.getRow(i);
                index++;
            }
        }

        this.setMatrix(output);
    }

    /**
     * Checks if a row contains only zeros
     * @param row the row to be checked
     * @return whether the row is all zeros
     */
    public boolean isAllZeros(int row) {
        assert row <= this.rows && row >= 1 : "Row " + row + " not in matrix";
        double[] rowArr = this.getRow(row);
        for (double d : rowArr) {
            if (d != 0.0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Row reduces this matrix to its RREF form
     */
    public void reduceToRREF() {
        this.setMatrix(getRREF(this).getMatrix());
    }

    /**
     * Row reduces this matrix to an echelon form
     */
    public void reduceToEchelon() {
        this.setMatrix(createEchelonForm(this).getMatrix());
    }

    /**
     * This method is used when making a string representation of a {@code Matrix}
     * for formatting reasons. In order to make it so the entries are always
     * properly lined up in the string representation, we need to know the maximum
     * length of the content to the left of the decimal point among all of the
     * entries. This method determines that maximum length.
     * @return the maximum number of characters to the left of the decimal point in
     *         a string representation of each entry
     */
    private int getMaxEntryLength() {
        int max = 0;

        for (int r = 1; r <= this.getRows(); r++) {
            for (int c = 1; c <= this.getCols(); c++) {
                String asString = Double.toString(this.getElement(r, c));
                asString = asString.substring(0, asString.indexOf('.'));
                if (asString.length() > max) {
                    max = asString.length();
                }

            }
        }

        return max;
    }

    /**
     * Create a string representation of this matrix
     * @return a string representation of this matrix
     */
    @Override
    public String toString() {
        String s = "";
        int charsPerEntry = this.getMaxEntryLength() + 3; // add 1 for the decimal point and 2 for the digits after it
        for (double[] row : this.mat) {
            s += "| ";
            for (double e : row) {
                if (Math.abs(e) < DELTA) {
                    e = 0;
                }
                s += String.format("%" + charsPerEntry + ".2f ", e);
            }
            s += "|\n";
        }
        return s;
    }

}