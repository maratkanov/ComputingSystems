package edu.computing.systems;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmRepresentation {

    public static final int NODE_AMOUNT = 30;       // узлы вычислительной сети
    private static final int STRING_LENGTH = 5;     // длина строки для формирования таблиц
    private static final int TIME_AXIS_MAX_VALUE = 35; // максимальное значение по временной оси


    private String[][] sequenceMatrix;      // матрица следования
    private int[][] extendedSequenceMatrix; // расширенная матрица следования (с весами)
    private int[][] extendedSequenceMatrixCopy; // расширенная матрица следования (с весами)
    private int[][] threadInterconnections;     // матрица взаимодействия нитей

    private boolean[] initialVertexArray;   // массив начальных вершин для каждой итерации
    private boolean[] deletedVertexArray;   // массив вершин исклченных из рассмотрения

    private int threadAmount;
    private List<List<Operation>> threads;

    private int[] startTimeOfOperationsInThread;
    private int[] endTimeOfOperationsInThread;

    private int[] threadStartTimes;
    private int[] threadEndTimes;

    private int[][] timeCharts;     // временная диаграмма исполнения нитей на процессорах
    private int processorAmount;

    public AlgorithmRepresentation() {
        sequenceMatrix = new String[NODE_AMOUNT][NODE_AMOUNT];
        for (int i=0; i< NODE_AMOUNT; i++) {
            for (int j=0; j< NODE_AMOUNT; j++) {
                sequenceMatrix[i][j] = "0";
            }
        }
        // в последнем столбце дополнительно представлены веса
        extendedSequenceMatrix = new int[NODE_AMOUNT][NODE_AMOUNT +1];
        for (int i=0; i< NODE_AMOUNT; i++) {
            for (int j=0; j< NODE_AMOUNT +1; j++) {
                extendedSequenceMatrix[i][j] = 0;
            }
        }

        initialVertexArray = new boolean[NODE_AMOUNT];
        for (int i=0; i< NODE_AMOUNT; i++) {
            initialVertexArray[i] = false;
        }
        deletedVertexArray = new boolean[NODE_AMOUNT];
        for (int i=0; i< NODE_AMOUNT; i++) {
            deletedVertexArray[i] = false;
        }

        threadAmount = 0;
        threads = new ArrayList<List<Operation>>(NODE_AMOUNT);  // элементов будет не более NODE_AMOUNT

        startTimeOfOperationsInThread = new int[NODE_AMOUNT];
        endTimeOfOperationsInThread = new int[NODE_AMOUNT];

        threadInterconnections = new int[NODE_AMOUNT][NODE_AMOUNT];
    }

    private class Operation {
        public int operationNumber;
        public int operationWeight;

        private Operation(int operationNumber, int operationWeight) {
            this.operationNumber = operationNumber;
            this.operationWeight = operationWeight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Operation operation = (Operation) o;

            if (operationWeight != operation.operationWeight) return false;
            if (operationNumber != operation.operationNumber) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = operationNumber;
            result = 31 * result + operationWeight;
            return result;
        }
    }

    /**
     * Функция создаёт копию копию массива
     * @return копию массива
     */
    private int[][] getExtendedSequenceMatrixCopy() {
        int[][] copy = new int[NODE_AMOUNT][NODE_AMOUNT +1];
        for (int i=0; i< NODE_AMOUNT; i++)
            System.arraycopy(extendedSequenceMatrix[i], 0, copy[i], 0, NODE_AMOUNT + 1);
        return copy;
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
        for (int i=0; i< NODE_AMOUNT; i++) {
            // convolution - свёртка
            boolean hasNoConvolution = true;
            for (int j=0; j< NODE_AMOUNT; j++) {
                if (extendedSequenceMatrixCopy[i][j] != 0)
                    hasNoConvolution = false;
            }
            if (hasNoConvolution && !deletedVertexArray[i]) {
                initialVertexArray[i] = true;
                isNotEmpty = true;
            }
        }
        return !isNotEmpty;
    }

    /**
     * Функция возвращает следующий доступный узел
     */
    private int getNextNode(int previousNode) {
        int nextNode = -1;  // TODO: check here equals 0
        int maxTransitionWeight = 0;
        for (int i=0; i< NODE_AMOUNT; i++) {
            if (extendedSequenceMatrixCopy[i][previousNode] > maxTransitionWeight) {
                maxTransitionWeight = extendedSequenceMatrixCopy[i][previousNode];
                nextNode = i;
            }
        }
        return nextNode;
    }

    /**
     * Функция рассчитывает времена начала и конца для каждой операции
     * с учётом принадлежности к конкретной нити
     *
     */
    private void calculateTimesForOperationsInThreads() {
        for (int operationNumber=0; operationNumber< NODE_AMOUNT; operationNumber++) {
            int endTime = getOperationWeight(operationNumber);
            boolean isStarted = true;
            int time = 0;
            for (int i=0; i< NODE_AMOUNT; i++) {
                if (extendedSequenceMatrix[operationNumber][i] != 0 && endTimeOfOperationsInThread[i] > time) {
                    time = endTimeOfOperationsInThread[i];
                    isStarted = false;
                }
            }
            if (!isStarted) {
                startTimeOfOperationsInThread[operationNumber] = time;
                endTime += time;
            }
            endTimeOfOperationsInThread[operationNumber] = endTime;
        }
    }

    /**
     * Функция рассчитывает времена начала и конца для каждой нити
     */
    private void calculateTimesForThreads() {
        threadStartTimes = new int[threadAmount];
        threadEndTimes = new int[threadAmount];

        for (int i=0; i<threadAmount; i++) {
            List<Operation> thread = threads.get(i);
            int firstThreadOperation = thread.get(0).operationNumber;
            threadStartTimes[i] = startTimeOfOperationsInThread[firstThreadOperation];
            int lastThreadOperation = thread.get(thread.size() - 1).operationNumber;
            threadEndTimes[i] = endTimeOfOperationsInThread[lastThreadOperation];
        }
    }

    /**
     *
     * @param operationNumber - номер операции, для которой ищется вес
     * @return вес текущей операции в одной из нитей или 0, если операция не найдена
     */
    private int getOperationWeight(int operationNumber) {
        for (int i=0; i< NODE_AMOUNT; i++) {
            List<Operation> thread = threads.get(i);
            if (thread != null) {
                for (Operation operation : thread) {
                    if (operation.operationNumber == operationNumber)
                        return operation.operationWeight;
                }
            }
        }
        return 0;
    }

    private boolean isOperationInTheThread(List<Operation> thread, int operationNumber) {
        for (Operation operation : thread) {
            if (operation.operationNumber == operationNumber)
                return true;
        }
        return false;
    }

    /**
     * Функция возвращает номер нити, которой принадлежит операция operation
     * @param operation - операция
     * @return номер нити или -1, если не найдено
     */
    private int getThreadByOperation(int operation) {
        for (int i = 0; i < threadAmount; i++) {
            List<Operation> thread = threads.get(i);
            for (Operation threadOperation : thread) {
                if (threadOperation.operationNumber == operation)
                    return i;
            }
        }
        return -1;
    }

    private void arrangeThreadsOnProcessors() {
        processorAmount = threadAmount; // TODO: create function
        timeCharts = new int[processorAmount][TIME_AXIS_MAX_VALUE];
        for (int time = 0; time < TIME_AXIS_MAX_VALUE; time++) {
            for (int i = 0; i < threadAmount; i++) {
                if (threadStartTimes[i] == time) {
                    int j = 1;
                    while (timeCharts[j][time+1] != 0)
                        j++;
                    for (int t = time+1; t < threadEndTimes[i]; t++)
                        timeCharts[j][t] = i + 1;   // для наглядного представления на графике
                }
            }
        }
    }


    public void computeThreads() {
        extendedSequenceMatrixCopy = getExtendedSequenceMatrixCopy();
        while (!isGraphEmpty()) {
            for (int i=0; i< NODE_AMOUNT; i++) {
                if (initialVertexArray[i]) {
                    boolean hasConvolution = false;
                    deletedVertexArray[i] = true;
                    initialVertexArray[i] = false;
                    threads.add(new ArrayList<Operation>());
                    threads.get(threadAmount).add(new Operation(i, extendedSequenceMatrixCopy[i][NODE_AMOUNT]));
                    int nextNode;
                    int previousNode = i;
                    do {
                        nextNode = getNextNode(previousNode);
                        for (int j=0; j< NODE_AMOUNT; j++) {
                            if (extendedSequenceMatrixCopy[j][previousNode] != 0 && j != nextNode) {
                                extendedSequenceMatrixCopy[j][NODE_AMOUNT] += extendedSequenceMatrixCopy[j][previousNode];
                                extendedSequenceMatrixCopy[j][previousNode] = 0;
                            }
                            if (extendedSequenceMatrixCopy[previousNode][j] != 0 && j!= nextNode && !hasConvolution) {
                                if (!sequenceMatrix[previousNode][j].contains(".")) {
                                    extendedSequenceMatrixCopy[j][NODE_AMOUNT] += extendedSequenceMatrixCopy[previousNode][j];
                                    extendedSequenceMatrixCopy[previousNode][j] = 0;
                                } else {
                                    // свёртка по исключающему ИЛИ
                                    if (isOperationInTheThread(threads.get(threadAmount), j)) {
                                        extendedSequenceMatrixCopy[j][NODE_AMOUNT] += extendedSequenceMatrixCopy[previousNode][j];
                                        extendedSequenceMatrixCopy[previousNode][j] = 0;
                                    }
                                    hasConvolution = true;
                                }
                            }
                        }
                        if (nextNode >= 0) {
                            threads.get(threadAmount).add(new Operation(nextNode, extendedSequenceMatrixCopy[nextNode][NODE_AMOUNT]));
                            deletedVertexArray[nextNode] = true;
                            previousNode = nextNode;
                        } else {
                            List<Operation> currentThread = threads.get(threadAmount);
                            threadAmount++;
                            hasConvolution = false;
                        }
                    }
                    while (nextNode >= 0);
                }
            }
        }

        calculateTimesForOperationsInThreads();
        calculateTimesForThreads();

        for (int i=0; i<threadAmount; i++) {
            int next = threads.get(i).get(0).operationNumber;   // операция, с которой начинается нить
            for (int j = 0; j < NODE_AMOUNT; j++) {
                if (extendedSequenceMatrix[next][j] != 0) {
                    int prev = getThreadByOperation(j);
                    threadInterconnections[i][prev] = 1;
                }
            }
        }


        for (int i=0; i<threadAmount; i++) {
            List<Operation> thread = threads.get(i);
            int next = thread.get(thread.size() - 1).operationNumber; // операция, которой заканчивается нить
            for (int j = 0; j < NODE_AMOUNT; j++) {
                if (extendedSequenceMatrix[j][next] != 0) {
                    int prev = getThreadByOperation(j);
                    threadInterconnections[prev][i] = 1;
                }
            }
        }
        arrangeThreadsOnProcessors();

        System.out.println("DONE");
        printThreads();
        printThreadInterconnectionTable();
        printTimeCharts();
    }



    /**
     * Печать на каждой строке очередной нити в формате
     * [номер операции],[вес]
     *
     * если операция i, то выводится i+1 для удобного восприятия
     */
    private void printThreads() {
        System.out.println("Нити:");
        for (int i=0; i<threadAmount; i++) {
            List<Operation> thread = threads.get(i);
            for (Operation operation : thread) {
                System.out.print(fixedLengthString(operation.operationNumber + 1 + "," + operation.operationWeight + " ", STRING_LENGTH));
            }
            System.out.println(fixedLengthString(threadStartTimes[i] + "-" + threadEndTimes[i], 3*STRING_LENGTH));
        }
    }

    /**
     * Функция печати матрицы взаимодействия нитей
     */
    private void printThreadInterconnectionTable() {
        System.out.println("Матрица взаимосвязей:");
        System.out.print(fixedLengthString(" ", STRING_LENGTH));
        for (int i = 0; i < threadAmount; i++) {
            System.out.print(fixedLengthString(String.valueOf(i +1), STRING_LENGTH));
        }
        System.out.println();
        for (int i = 0; i < threadAmount; i++) {
            System.out.print(fixedLengthString(String.valueOf(i +1), STRING_LENGTH));
            for (int j = 0; j < threadAmount; j++) {
                System.out.print(fixedLengthString(String.valueOf(threadInterconnections[i][j]), STRING_LENGTH));
            }
            System.out.println();
        }
    }

    /**
     * Функция печати временной диаграммы исполнения нитей на процессорах
     *
     * если операция i, то выводится i+1 для удобного восприятия
     */
    private void printTimeCharts() {
        for (int i = 0; i < processorAmount; i++) {
            for (int j = 0; j < TIME_AXIS_MAX_VALUE; j++) {
                System.out.print(fixedLengthString(String.valueOf(timeCharts[i][j]), 3));
            }
            System.out.println();
        }
    }

    /**
     * значения i и j вводятся начиная с 1, а не с 0
     * функция это учитывает
     */
    public AlgorithmRepresentation setSequenceMatrixValue(int i, int j, String value) {
        assert i > 0 : "Значение должно быть положительно";
        assert j > 0 : "Значение должно быть положительно";
        assert i <= NODE_AMOUNT : "Значение не должно превышать " + String.valueOf(NODE_AMOUNT);
        assert j <= NODE_AMOUNT : "Значение не должно превышать " + String.valueOf(NODE_AMOUNT);
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
        assert i <= NODE_AMOUNT : "Значение не должно превышать " + String.valueOf(NODE_AMOUNT);
        assert j <= NODE_AMOUNT : "Значение не должно превышать " + String.valueOf(NODE_AMOUNT);
        extendedSequenceMatrix[i-1][j-1] = value;
        return this;
    }

    /**
     * значение nodeNumber вводится начиная с 1, а не с 0
     * функция это учитывает
     */
    public AlgorithmRepresentation setExtendedSequenceMatrixWeightValue(int nodeNumber, int value) {
        assert nodeNumber > 0 : "Значение должно быть положительно";
        assert nodeNumber < NODE_AMOUNT : "Значение не должно превышать " + String.valueOf(NODE_AMOUNT);
        extendedSequenceMatrix[nodeNumber-1][NODE_AMOUNT] = value;
        return this;
    }

    private static String fixedLengthString(String string, int length) {
        return String.format("%1$"+length+ "s", string);
    }

    public void printSequenceMatrix() {
        System.out.println("Матрица следования:");

        // шапка
        System.out.print(fixedLengthString(" ", STRING_LENGTH));
        for (int i=0; i< NODE_AMOUNT; i++) {
            System.out.print(fixedLengthString(String.valueOf(i+1), STRING_LENGTH));
        }
        System.out.println();

        for (int i=0; i< NODE_AMOUNT; i++) {
            System.out.print(fixedLengthString(String.valueOf(i+1), STRING_LENGTH));
            for (int j=0; j< NODE_AMOUNT; j++) {
                System.out.print(fixedLengthString(sequenceMatrix[i][j], STRING_LENGTH));
            }
            System.out.println();
        }
    }


    public void printExtendedSequenceMatrix() {
        System.out.println("Расширенная Матрица следования:");

        // шапка
        System.out.print(fixedLengthString(" ", STRING_LENGTH));
        for (int i=0; i< NODE_AMOUNT; i++) {
            System.out.print(fixedLengthString(String.valueOf(i+1), STRING_LENGTH));
        }
        System.out.println(" Weight");

        for (int i=0; i< NODE_AMOUNT; i++) {
            System.out.print(fixedLengthString(String.valueOf(i+1), STRING_LENGTH));
            for (int j=0; j< NODE_AMOUNT +1; j++) {
                System.out.print(fixedLengthString(String.valueOf(extendedSequenceMatrix[i][j]), STRING_LENGTH));
            }
            System.out.println();
        }
    }
}
