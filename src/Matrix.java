import java.util.Arrays;

//This class represents any matrix with real numbers (in practice we can only represent rational numbers though)
public class Matrix {

    // By convention, rows and columns start at 1

    private double[][] mat;
    private int rows;
    private int cols;

    public static boolean checkIfArrayIsValidMatrix(double[][] arr) {
        int c = arr[0].length;
        for (double[] row : arr) {
            if (row.length != c) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkIfArrayIsValidMatrix(int[][] arr) {
        int c = arr[0].length;
        for (int[] row : arr) {
            if (row.length != c) {
                return false;
            }
        }
        return true;
    }

    public static double[][] intMatrixToDoubleMatrix(int[][] mat) {
        assert checkIfArrayIsValidMatrix(mat) : "Matrix should have the same number of elements in the same row";
        double[][] output = new double[mat.length][mat[0].length];

        for (int r = 0; r < mat.length; r++) {
            for (int c = 0; c < mat[r].length; c++) {
                output[r][c] = mat[r][c];
            }
        }
        return output;
    }

    // Counts and returns the number of leading zeros in an array
    public static int countLeadingZeros(double[] r) {

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

    // This will create an echelon form of a matrix
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

        return output;
    }

    // Given a matrix as an input, it will find that matrix's reduced row echelon
    // form, returning that as a new matrix
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
                for (int r = currentRow - 1; r >= 1; r--) {
                    int leadingEntryColumn = output.getLeadingEntryColumn(currentRow);

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

    // Constructor (no args)
    public Matrix() {
        // if there are no arguments, return a matrix containing one row, one column,
        // with the element 0.0
        this(1, 1);
    }

    // Constructor (from rows and columns)
    public Matrix(int r, int c) {
        assert r <= this.rows && r >= 1 : "Row " + r + " not possible";
        assert c <= this.rows && c >= 1 : "Column " + c + " not possible";
        // Make a matrix with r rows and c columns, filled all with 0s
        this.mat = new double[r][c];
        this.rows = r;
        this.cols = c;
    }

    // Constructor (from a double matrix)
    public Matrix(double[][] matrix) {
        assert checkIfArrayIsValidMatrix(matrix) : "Matrix should have the same number of elements in each row";

        this.mat = matrix;
        this.rows = this.mat.length;
        this.cols = this.mat[0].length;

    }

    // Constructor (from an int matrix)
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

    @Override
    public Matrix clone() {
        double[][] outputMat = new double[this.getRows()][this.getCols()];
        for (int r = 1; r <= outputMat.length; r++) {
            outputMat[r - 1] = this.getRow(r).clone();
        }
        Matrix output = new Matrix(outputMat);
        return output;
    }

    // Getter for the matrix
    public double[][] getMatrix() {
        return this.mat;
    }

    // Getter for the number of rows
    public int getRows() {
        return this.rows;
    }

    // Getter for the number of columns
    public int getCols() {
        return this.cols;
    }

    // Getter for a single row
    public double[] getRow(int row) {
        assert row <= this.rows && row >= 1 : "Row " + row + " not in matrix";

        return this.mat[row - 1];
    }

    // Getter for a single column
    public double[] getCol(int col) {
        assert col <= this.cols && col >= 1 : "Column " + col + " not in matrix";

        double[] output = new double[this.cols];
        for (int i = 0; i < this.rows; i++) {
            output[i] = this.mat[i][col - 1];
        }

        return output;
    }

    public double getElement(int r, int c) {
        assert r <= this.rows && r >= 1 : "Row " + r + " not in matrix";
        assert c <= this.cols && c >= 1 : "Column " + c + " not in matrix";
        return this.mat[r - 1][c - 1];
    }

    // Get the leading entry of row r
    // if there is no leading entry, returns 0
    public double getLeadingEntry(int r) {
        assert r >= 1 && r <= this.rows : "Row " + r + " not in matrix";

        double[] row = this.getRow(r);

        for (double d : row) {
            if (d != 0) {
                return d;
            }
        }

        return 0;
    }

    // Get the column in which the leading entry is located
    // If the row is all zeros, returns -1
    public int getLeadingEntryColumn(int r) {
        assert r >= 1 && r <= this.rows : "Row " + r + " not in matrix";

        double[] row = this.getRow(r);

        for (int i = 0; i < row.length; i++) {
            if (row[i] != 0) { // if row[i] != 0 essentially
                return i + 1; // add one to get the column number, which begins at 1
            }
        }

        return -1;
    }

    // Setter for a single element
    public void setElement(int r, int c, double value) {
        assert r >= 1 && r <= this.rows : "Row " + r + " not in matrix";
        assert c >= 1 && c <= this.cols : "Column " + c + " not in matrix";

        this.mat[r - 1][c - 1] = value;
    }

    // Setter for the matrix (double[][] input)
    public void setMatrix(double[][] matrix) {
        assert checkIfArrayIsValidMatrix(matrix) : "Matrix should have the same number of elements in each row";
        this.mat = matrix;
        this.rows = matrix.length;
        this.cols = matrix[0].length;
    }

    // Setter for the matrix (int[][] input)
    public void setMatrix(int[][] matrix) {
        assert checkIfArrayIsValidMatrix(matrix) : "Matrix should have the same number of elements in each row";

        this.mat = intMatrixToDoubleMatrix(matrix);
        this.rows = matrix.length;
        this.cols = matrix[0].length;
    }

    public void scaleRow(int row, double factor) {
        assert row <= this.rows && row >= 1 : "Row " + row + " not in matrix";
        assert factor != 0 : "Do not scale by zero";

        for (int i = 0; i < this.mat[row - 1].length; i++) {
            this.mat[row - 1][i] *= factor;
        }
    }

    public void interchangeRows(int rowOne, int rowTwo) {
        assert rowOne <= this.rows && rowOne >= 1 : "Row " + rowOne + " not in matrix";
        assert rowTwo <= this.rows && rowTwo >= 1 : "Row " + rowTwo + " not in matrix";

        double[] temp = this.mat[rowOne - 1];
        this.mat[rowOne - 1] = this.mat[rowTwo - 1];
        this.mat[rowTwo - 1] = temp;
    }

    // Scales rowOne by factor and adds it to rowTwo
    public void addRows(int rowOne, int rowTwo, double factor) {
        assert rowOne <= this.rows && rowOne >= 1 : "Row " + rowOne + " not in matrix";
        assert rowTwo <= this.rows && rowTwo >= 1 : "Row " + rowTwo + " not in matrix";
        // I will allow scaling by zero, but it has no effect on the matrix

        for (int i = 0; i < this.mat[rowOne - 1].length; i++) {
            this.mat[rowTwo - 1][i] += (this.mat[rowOne - 1][i] * factor);
        }
    }

    public void moveZerosToBottom() {
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

    // Checks if a row is made entirely of zeroes

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



    public void reduceToRREF() {
        this.setMatrix(getRREF(this).getMatrix());
    }

    public void reduceToEchelon() {
        this.setMatrix(createEchelonForm(this).getMatrix());
    }

    private int getMaxEntryLength() {
        int max = 0;

        for (int r = 1;r<=this.getRows();r++) {
            for (int c = 1; c<=this.getCols();c++) {
                if (Integer.toString((int) this.getElement(r, c)).length() > max) {
                    max = Integer.toString((int) this.getElement(r, c)).length();
                }

            }
        }

        return max;
    }

    // Override the toString method
    @Override
    public String toString() {
        String s = "";
        int charsPerEntry = this.getMaxEntryLength()+3;
        for (double[] row : this.mat) {
            s += "| ";
            for (double e : row) {
                if (e == 0) {
                    e = 0;
                }
                s += String.format("%"+charsPerEntry+".2f ", e);
            }
            s += "|\n";
        }
        return s;
    }

}