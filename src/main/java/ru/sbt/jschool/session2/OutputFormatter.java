/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.sbt.jschool.session2;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class OutputFormatter {
    private PrintStream out;

    public OutputFormatter(PrintStream out) {
        this.out = out;
    }

    public void output(String[] names, Object[][] data) {
        int namesLength = names.length;
        int dataWidth = data.length;

        /*приведение каждого элемента в нужный вид и перенос в массив String*/
        String[][] dateToString = new String[dataWidth][namesLength];
        for (int i = 0; i < dataWidth; i++) {
            for (int j = 0; j < namesLength; j++) {
                if (data[i][j] instanceof Date) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    dateToString[i][j] = dateFormat.format(data[i][j]);
                } else if (data[i][j] instanceof String) {
                    dateToString[i][j] = ((String)data[i][j]).replace('\n', ' ');
                } else if (data[i][j] instanceof Float || data[i][j] instanceof Double) {
                    DecimalFormat dataFormat = new DecimalFormat("###,##0.00");
                    dateToString[i][j] = dataFormat.format(data[i][j]);
                } else if (data[i][j] instanceof Number) {
                    DecimalFormat dataFormat = new DecimalFormat("###,###");
                    dateToString[i][j] = dataFormat.format(data[i][j]);
                } else if (data[i][j] == null) {
                    dateToString[i][j] = "-";
                }
            }
        }

        /*нахождение максимальной ширины каждого столбца*/
        int[] tableWidth = new int[namesLength];
        for (int i = 0; i < namesLength; i++) {
            tableWidth[i] = names[i].length();
            for (int j = 0; j < dataWidth; j++) {
                if (tableWidth[i] < dateToString[j][i].length()) {
                    tableWidth[i] = dateToString[j][i].length();
                }
            }
        }

        /*вывод верхней части таблицы*/
        for (int i = 0; i < namesLength; i++) {
            out.print('+');
            for (int j = 0; j < tableWidth[i]; j++) {
                out.print('-');
            }
        }
        out.println('+');

        /*вывод массива имён*/
        for (int i = 0; i < namesLength; i++) {
            out.print("|");
            for (int j = 0; j < ((tableWidth[i] - names[i].length()) / 2); j++) {
                out.print(' ');
            }
            out.print(names[i]);
            for (int j = 0; j < (((tableWidth[i] - names[i].length()) / 2) + ((tableWidth[i] - names[i].length()) % 2)); j++) {
                out.print(' ');
            }
        }
        out.println('|');

        /*вывод массива данных */
        for (int i = 0; i < dataWidth; i++) {
            for (int k = 0; k < namesLength; k++) {
                out.print('+');
                for (int e = 0; e < tableWidth[k]; e++) {
                    out.print('-');
                }
            }
            out.println('+');
            for (int j = 0; j < namesLength; j++) {
                out.print('|');
                if (data[i][j] instanceof String) {
                    out.print(dateToString[i][j]);
                    for (int k = 0; k < (tableWidth[j] - dateToString[i][j].length()); k++) {
                        out.print(' ');
                    }
                } else if(data[i][j] == null) {
                    int m = 0;
                    do {
                        if (data[m][j] == null) {
                            ++m;
                            continue;
                        }
                        if (data[m][j] instanceof String) {
                            out.print(dateToString[i][j]);
                            for (int k = 0; k < (tableWidth[j] - dateToString[i][j].length()); k++) {
                                out.print(' ');
                            }
                            break;
                        } else {
                            for (int k = 0; k < (tableWidth[j] - dateToString[i][j].length()); k++) {
                                out.print(' ');
                            }
                            out.print(dateToString[i][j]);
                            break;
                        }
                    }while (m < namesLength && data[i][m] == null);
                } else {
                    for (int k = 0; k < (tableWidth[j] - dateToString[i][j].length()); k++) {
                        out.print(' ');
                    }
                    out.print(dateToString[i][j]);
                }
            }
            out.println('|');
        }

         /*вывод нижней части таблицы*/
        for (int i = 0; i < namesLength; i++) {
            out.print('+');
            for (int j = 0; j < tableWidth[i]; j++) {
                out.print('-');
            }
        }
        out.println('+');
    }
}
