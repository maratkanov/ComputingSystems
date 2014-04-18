package edu.computing.systems;

public class AlgorithmRepresentation {

    public static final int nodeAmount = 30;    // узлы вычислительной сети

    private String[][] sequenceMatrix;      // матрица следования
    private int[][] extendedSequenceMatrix; // расширенная матрица следования (с весами)

    private boolean[] initialVertexArray;   // массив начальных вершин для каждой итерации
    private boolean[] deletedVertexArray;   // массив вершин исклченных из рассмотрения

    public AlgorithmRepresentation() {
        sequenceMatrix = new String[nodeAmount][nodeAmount];
        for (int i=0; i<nodeAmount; i++) {
            for (int j=0; j<nodeAmount; j++) {
                sequenceMatrix[i][j] = "0";
            }
        }
        // в последнем столбце дополнительно представлены веса
        extendedSequenceMatrix = new int[nodeAmount][nodeAmount+1];
        for (int i=0; i<nodeAmount; i++) {
            for (int j=0; j<nodeAmount+1; j++) {
                extendedSequenceMatrix[i][j] = 0;
            }
        }

        initialVertexArray = new boolean[nodeAmount];
        for (int i=0; i<nodeAmount; i++) {
            initialVertexArray[i] = false;
        }
        deletedVertexArray = new boolean[nodeAmount];
        for (int i=0; i<nodeAmount; i++) {
            deletedVertexArray[i] = false;
        }
    }

    /**
     * Функция проверяет, является ли граф пустым
     * Попутно определяется массив начальных вершин,
     * т.е. тех, у которых нет свёрток (входящих дуг)
     *
     * @return есть ли еще вершины, с которыми можно работать,
     * т.е. не были ли они помещены в массив "удалённых" вершин
     */
    private boolean isGraphEmpty() {
        boolean isNotEmpty = false;
        for (int i=0; i<nodeAmount; i++) {
            // convolution - свёртка
            boolean hasNoConvolution = true;
            for (int j=0; j<nodeAmount; j++) {
                if (extendedSequenceMatrix[i][j] != 0)
                    hasNoConvolution = false;
                if (hasNoConvolution && !deletedVertexArray[i]) {
                    initialVertexArray[i] = true;
                    isNotEmpty = true;
                }
            }
        }
        return !isNotEmpty;
    }








    /**
     * значения i и j вводятся начиная с 1, а не с 0
     * функция это учитывает
     */
    public AlgorithmRepresentation setSequenceMatrixValue(int i, int j, String value) {
        assert i > 0 : "Значение должно быть положительно";
        assert j > 0 : "Значение должно быть положительно";
        assert i <= nodeAmount : "Значение не должно превышать " + String.valueOf(nodeAmount);
        assert j <= nodeAmount : "Значение не должно превышать " + String.valueOf(nodeAmount);
        sequenceMatrix[i-1][j-1] = value;
        return this;
    }

    /**
     * значения i и j вводятся начиная с 1, а не с 0
     * функция это учитывает
     */
    public AlgorithmRepresentation setExtendedSequenceMatrixTransitionValue(int i, int j, int value) {
        assert i > 0 : "Значение должно быть положительно";
        assert j > 0 : "Значение должно быть положительно";
        assert i <= nodeAmount : "Значение не должно превышать " + String.valueOf(nodeAmount);
        assert j <= nodeAmount : "Значение не должно превышать " + String.valueOf(nodeAmount);
        extendedSequenceMatrix[i-1][j-1] = value;
        return this;
    }

    /**
     * значение nodeNumber вводится начиная с 1, а не с 0
     * функция это учитывает
     */
    public AlgorithmRepresentation setExtendedSequenceMatrixWeightValue(int nodeNumber, int value) {
        assert nodeNumber > 0 : "Значение должно быть положительно";
        assert nodeNumber < nodeAmount : "Значение не должно превышать " + String.valueOf(nodeAmount);
        extendedSequenceMatrix[nodeNumber-1][nodeAmount] = value;
        return this;
    }

    private static String fixedLengthString(String string, int length) {
        return String.format("%1$"+length+ "s", string);
    }

    public void printSequenceMatrix() {
        int stringLength = 4;
        System.out.println("Матрица следования:");

        // шапка
        System.out.print(fixedLengthString(" ", stringLength));
        for (int i=0; i<nodeAmount; i++) {
            System.out.print(fixedLengthString(String.valueOf(i+1), stringLength));
        }
        System.out.println();

        for (int i=0; i<nodeAmount; i++) {
            System.out.print(fixedLengthString(String.valueOf(i+1), stringLength));
            for (int j=0; j<nodeAmount; j++) {
                System.out.print(fixedLengthString(sequenceMatrix[i][j], stringLength));
            }
            System.out.println();
        }
    }


    public void printExtendedSequenceMatrix() {
        int stringLength = 4;
        System.out.println("Расширенная Матрица следования:");

        // шапка
        System.out.print(fixedLengthString(" ", stringLength));
        for (int i=0; i<nodeAmount; i++) {
            System.out.print(fixedLengthString(String.valueOf(i+1), stringLength));
        }
        System.out.println(" Weight");

        for (int i=0; i<nodeAmount; i++) {
            System.out.print(fixedLengthString(String.valueOf(i+1), stringLength));
            for (int j=0; j<nodeAmount+1; j++) {
                System.out.print(fixedLengthString(String.valueOf(extendedSequenceMatrix[i][j]), stringLength));
            }
            System.out.println();
        }
    }
}
