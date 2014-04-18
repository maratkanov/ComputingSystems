package edu.computing.systems;

public class Main {


    public static void main(String[] args) {
        AlgorithmRepresentation algorithmRepresentation = new AlgorithmRepresentation();
        setVersion18(algorithmRepresentation);
        algorithmRepresentation.printSequenceMatrix();
        algorithmRepresentation.printExtendedSequenceMatrix();
    }

    private static void setVersion18(AlgorithmRepresentation algorithm) {
        // настройка матрицы следования
        algorithm.setSequenceMatrixValue(4, 1, "1")
                .setSequenceMatrixValue(5, 1, "1")
                .setSequenceMatrixValue(6, 1, "1")
                .setSequenceMatrixValue(7, 1, "1")
                .setSequenceMatrixValue(8, 2, "2T")
                .setSequenceMatrixValue(9, 2, "2F")
                .setSequenceMatrixValue(9, 3, "3T")
                .setSequenceMatrixValue(10, 3, "3F")
                .setSequenceMatrixValue(11, 4, "4T")
                .setSequenceMatrixValue(11, 5, "5T")
                .setSequenceMatrixValue(12, 6, "1")
                .setSequenceMatrixValue(12, 7, "1")
                .setSequenceMatrixValue(13, 8, "8T")
                .setSequenceMatrixValue(13, 9, "9T")
                .setSequenceMatrixValue(14, 10, "1")
                .setSequenceMatrixValue(15, 11, "1")
                .setSequenceMatrixValue(16, 11, "1")
                .setSequenceMatrixValue(17, 12, "1")
                .setSequenceMatrixValue(18, 13, "1")
                .setSequenceMatrixValue(19, 14, "1")
                .setSequenceMatrixValue(20, 14, "1")
                .setSequenceMatrixValue(21, 15, "15T")
                .setSequenceMatrixValue(22, 15, "15F")
                .setSequenceMatrixValue(23, 15, "15D")
                .setSequenceMatrixValue(24, 17, "1")
                .setSequenceMatrixValue(24, 18, "1")
                .setSequenceMatrixValue(24, 19, "1")
                .setSequenceMatrixValue(25, 19, "1")
                .setSequenceMatrixValue(26, 19, "1")
                .setSequenceMatrixValue(27, 21, "1")
                .setSequenceMatrixValue(28, 21, "1")
                .setSequenceMatrixValue(29, 23, "23T")
                .setSequenceMatrixValue(30, 23, "23F")
                .setSequenceMatrixValue(30, 24, "24T");

        // настройка расширенной матрицы следования
        algorithm.setExtendedSequenceMatrixTransitionValue(4, 1, 7)
                .setExtendedSequenceMatrixTransitionValue(5, 1, 3)
                .setExtendedSequenceMatrixTransitionValue(6, 1, 2)
                .setExtendedSequenceMatrixTransitionValue(7, 1, 6)
                .setExtendedSequenceMatrixTransitionValue(8, 2, 5)
                .setExtendedSequenceMatrixTransitionValue(9, 2, 3)
                .setExtendedSequenceMatrixTransitionValue(9, 3, 4)
                .setExtendedSequenceMatrixTransitionValue(10, 3, 5)
                .setExtendedSequenceMatrixTransitionValue(11, 4, 2)
                .setExtendedSequenceMatrixTransitionValue(11, 5, 4)
                .setExtendedSequenceMatrixTransitionValue(12, 6, 2)
                .setExtendedSequenceMatrixTransitionValue(12, 7, 1)
                .setExtendedSequenceMatrixTransitionValue(13, 8, 2)
                .setExtendedSequenceMatrixTransitionValue(13, 9, 3)
                .setExtendedSequenceMatrixTransitionValue(14, 10, 2)
                .setExtendedSequenceMatrixTransitionValue(15, 11, 1)
                .setExtendedSequenceMatrixTransitionValue(16, 11, 3)
                .setExtendedSequenceMatrixTransitionValue(17, 12, 2)
                .setExtendedSequenceMatrixTransitionValue(18, 13, 4)
                .setExtendedSequenceMatrixTransitionValue(19, 14, 3)
                .setExtendedSequenceMatrixTransitionValue(20, 14, 2)
                .setExtendedSequenceMatrixTransitionValue(21, 15, 3)
                .setExtendedSequenceMatrixTransitionValue(22, 15, 4)
                .setExtendedSequenceMatrixTransitionValue(23, 15, 5)
                .setExtendedSequenceMatrixTransitionValue(24, 17, 6)
                .setExtendedSequenceMatrixTransitionValue(24, 18, 7)
                .setExtendedSequenceMatrixTransitionValue(24, 19, 3)
                .setExtendedSequenceMatrixTransitionValue(25, 19, 5)
                .setExtendedSequenceMatrixTransitionValue(26, 19, 1)
                .setExtendedSequenceMatrixTransitionValue(27, 21, 3)
                .setExtendedSequenceMatrixTransitionValue(28, 21, 1)
                .setExtendedSequenceMatrixTransitionValue(29, 23, 3)
                .setExtendedSequenceMatrixTransitionValue(30, 23, 4)
                .setExtendedSequenceMatrixTransitionValue(30, 24, 5);

        algorithm.setExtendedSequenceMatrixWeightValue(1 ,8)
                .setExtendedSequenceMatrixWeightValue(2, 2)
                .setExtendedSequenceMatrixWeightValue(3, 3)
                .setExtendedSequenceMatrixWeightValue(4, 5)
                .setExtendedSequenceMatrixWeightValue(5, 6)
                .setExtendedSequenceMatrixWeightValue(6, 6)
                .setExtendedSequenceMatrixWeightValue(7, 1)
                .setExtendedSequenceMatrixWeightValue(8, 2)
                .setExtendedSequenceMatrixWeightValue(9, 3)
                .setExtendedSequenceMatrixWeightValue(10, 4)
                .setExtendedSequenceMatrixWeightValue(11, 4)
                .setExtendedSequenceMatrixWeightValue(12, 5)
                .setExtendedSequenceMatrixWeightValue(13, 4)
                .setExtendedSequenceMatrixWeightValue(14, 3)
                .setExtendedSequenceMatrixWeightValue(15, 6)
                .setExtendedSequenceMatrixWeightValue(16, 7)
                .setExtendedSequenceMatrixWeightValue(17, 3)
                .setExtendedSequenceMatrixWeightValue(18, 2)
                .setExtendedSequenceMatrixWeightValue(19, 1)
                .setExtendedSequenceMatrixWeightValue(20, 2)
                .setExtendedSequenceMatrixWeightValue(21, 3)
                .setExtendedSequenceMatrixWeightValue(22, 4)
                .setExtendedSequenceMatrixWeightValue(23, 5)
                .setExtendedSequenceMatrixWeightValue(24, 5)
                .setExtendedSequenceMatrixWeightValue(25, 2)
                .setExtendedSequenceMatrixWeightValue(26, 3)
                .setExtendedSequenceMatrixWeightValue(27, 6)
                .setExtendedSequenceMatrixWeightValue(28, 5)
                .setExtendedSequenceMatrixWeightValue(29, 4)
                .setExtendedSequenceMatrixWeightValue(30, 3);
    }

}
