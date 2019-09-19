import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This class can be used to make the {@code Matrix} and {@code SquareMatrix} classes
 * useful. The main method of this program will collect the data from the user to
 * create a matrix, and then offer a number of functions to the user.
 * @author Jack Smalligan
 * @version 1.0
 *
 */
public class MatrixRunner {

    private static Scanner s = new Scanner(System.in);
    private static Matrix mat;

    private static int collectData(String fieldName) {
        System.out.printf("Enter the number of %s for your matrix: ", fieldName);

        int data;

        try {
            data = s.nextInt();
        } catch (NoSuchElementException e) {
            System.out.printf("The number of %s must be an integer greater than or equal to 1\n", fieldName);
            return collectData(fieldName);
        }
        if (data<1) {
            System.out.printf("The number of %s must be an integer greater than 1\n", fieldName);
            return collectData(fieldName);
        }
        return data;
    }

    private static void collectMatrix(int rows, int cols, Matrix mat) {
        System.out.println("Enter your matrix: ");
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                double element  = s.nextDouble();
                mat.setElement(r, c, element);
            }
        }
    }

    public static void performOperation(String function) {
        switch (function) {
        case "DISPLAY":
            System.out.println(mat);
            break;
        case "ECHELON":
            System.out.println(Matrix.createEchelonForm(mat));
            break;
        case "RREF":
            System.out.println(Matrix.getRREF(mat));
            break;
        case "INVERSE":
            if (mat.getRows() != mat.getCols()) {
                System.out.println("Cannot create an inverse of a non-square matrix");
            } else {
                if (((SquareMatrix) mat).getDeterminant() == 0) {
                    System.out.println("No inverse exists for this matrix");
                } else {
                    System.out.println(SquareMatrix.getInverse((SquareMatrix) mat));
                }
            }
            break;
        case "DETERMINANT":
            if (mat.getRows() != mat.getCols()) {
                System.out.println("Cannot calculate the determinant of a non-square matrix");
            } else {
                System.out.println(SquareMatrix.getDeterminant((SquareMatrix) mat));
            }
            break;
        case "NEW":
            System.out.println("\n\n");
            fullProcess();
            break;
        case "EXIT":
            s.close();
            System.exit(0);
            break; //won't reach here, but break anyway
        default:
            System.out.println("Invalid operation");
        }
    }

    private static void fullProcess() {
        int rows = collectData("rows");
        int cols = collectData("columns");

        if (rows == cols) {
            mat = new SquareMatrix(rows);
        } else {
            mat = new Matrix(rows, cols);
        }

        collectMatrix(rows, cols, mat);

        while (true) {
            System.out.println();
            System.out.print("Enter your function (DISPLAY, ECHELON, RREF, INVERSE, DETERMINANT, NEW, EXIT): ");
            String function = s.next();
            performOperation(function);
        }
    }

    public static void main(String[] args) {
        fullProcess();
    }

}
