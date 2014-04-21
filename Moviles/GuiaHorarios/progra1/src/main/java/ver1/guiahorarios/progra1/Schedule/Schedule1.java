package ver1.guiahorarios.progra1.Schedule;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by sanchosv on 21/04/14.
 */

public class Schedule1
{

    private static Schedule1 _instance;

    public static Schedule1 getInstance()
    {
        if(_instance==null)
        {
            _instance = new Schedule1();
        }
        return _instance;
    }


    int cells[][];
    ArrayList<String> cellsInformation[][];
    boolean pressing = false;
    int cellPressed[] = {0,0,0};

    private Schedule1()
    {


    }

    public void initialize()
    {
        cells = new int[15][7];
        for(int fila = 0 ; fila < cells.length ; fila++)
            for(int col = 0 ; col < cells[fila].length ; col++)
            {
                if(col==0 || fila==0)
                    cells[fila][col] = 2 ;
                else
                    cells[fila][col] = 0 ;
            }

        cellsInformation = new ArrayList[15][7];
        for(int fila = 0 ; fila < cellsInformation.length ; fila++)
            for(int col = 0 ; col < cellsInformation[fila].length ; col++)
            {
                ArrayList<String> auxiliar = new ArrayList<String>();
                auxiliar.add("");
                auxiliar.add("");
                cellsInformation[fila][col] = auxiliar ;
            }
    }

    public void printCellsState()
    {
        for(int i = 0 ; i < cells.length ; i++)
        {
            int row[] = cells[i];
            Log.e("-> ", "" + row[0] + row[1] + row[2] + row[3] + row[4] + row[5] + row[6]);
        }

    }

    public boolean isPressing()
    {
        return pressing;
    }

    public void pressed(int row, int col)
    {
        cellPressed[0] = row;
        cellPressed[1] = row;
        cellPressed[2] = col;
        pressing = true;
    }

    public void dropped()
    {
        pressing = false;
    }

    public boolean isOcuppy(int row, int col)
    {
        if(cells[row][col] == 1)
            return true;
        else
            return false;
    }


    public void ocuppy(int row, int column,String course,int number)
    {
        cells[row][column] = 1 ;
        cellsInformation[row][column].set(0,course);
        cellsInformation[row][column].set(1,""+number);
    }





    // LLENA CON CEROS UN RANGE
    public void evacuateRange(int range[])
    {
        int begin = range[0];
        int end = range[1];
        int col = range[2];

        for(int aux = begin ; aux <= end ; aux++)
        {
            evacuate(aux,col);
        }
    }


    public void evacuate(int row, int column)
    {
        cells[row][column] = 0;
    }





    // VE SI UNA CELDA PERTENCE A UN RANGO
    public boolean isCellInRange(int cell[] , int range[])
    {
        int begin = range[0];
        int end = range[1];
        int col = range[2];

        int cellColumn = cell[1];
        int cellRow = cell[0];

        if(cellColumn==col)
        {
            for(int aux = begin ; aux <= end ; aux++)
            {
                if(aux == cellRow)
                    return true;
            }

            return false;
        }
        else
            return false;

    }



    // CALCULA SI UN RANGO DE CELDAS ESTA VACIO
    public boolean areEmpty(int rowsColumn[])
    {
        int begin = rowsColumn[0];
        int end = rowsColumn[1];
        int col = rowsColumn[2];

        for(int aux = begin ; aux <= end ; aux++)
        {
            if(isOcuppy(aux,col))
                return false;
        }

        return true;
    }



    public void fillRange(ArrayList<ArrayList<String>> horario)
    {
        for(int day = 0 ; day < horario.size() ; day++)
        {
            ArrayList<String> dia = horario.get(day);
            int rowsCol[] = calcularFilaCol(dia.get(2),dia.get(1));

            for(int aux = rowsCol[0] ; aux <= rowsCol[1] ; aux++)
            {
                ocuppy(aux,rowsCol[2],"",0);
            }
        }
    }

    public void evacuateAll()
    {
        for(int fila = 0 ; fila < cells.length ; fila++)
            for(int col = 0 ; col < cells[fila].length ; col++)
            {
                if(col==0 || fila==0)
                    cells[fila][col] = 2 ;
                else
                    cells[fila][col] = 0 ;
            }
    }


    public boolean tryToFillRange(ArrayList<ArrayList<String>> horario)
    {
        boolean allEmpty = true;
        for(int day = 0 ; day < horario.size() ; day++)
        {
            ArrayList<String> dia = horario.get(day);
            int rowsCol[] = calcularFilaCol(dia.get(2),dia.get(1));

            if(!areEmpty(rowsCol))
                allEmpty = false;
        }

        if(allEmpty)
        {
            for(int day = 0 ; day < horario.size() ; day++)
            {
                ArrayList<String> dia = horario.get(day);
                int rowsCol[] = calcularFilaCol(dia.get(2),dia.get(1));

                for(int aux = rowsCol[0] ; aux <= rowsCol[1] ; aux++)
                {
                    ocuppy(aux,rowsCol[2],"",0);
                }
            }
            return true;
        }
        else
            return false;
    }

    public int[] calcularFilaCol(String letraDia,String horarioClase)
    {
        int arreglo[] = new int[3];

        int diaCol = 0;

        if(letraDia.equals("L"))
            diaCol = 1;
        if(letraDia.equals("K"))
            diaCol = 2;
        if(letraDia.equals("M"))
            diaCol = 3;
        if(letraDia.equals("J"))
            diaCol = 4;
        if(letraDia.equals("V"))
            diaCol = 5;
        if(letraDia.equals("S"))
            diaCol = 6;

        arreglo[2] = diaCol;

        String[] horas = {"7:30 - 8:20","8:30 - 9:20", "9:30 - 10:20","10:30 - 11:20","11:30 - 12:20","13:00 - 13:50","14:00 - 14:50","15:00 - 15:50","16:00 - 16:50","17:00 - 17:50","18:00 - 18:50","19:00 - 19:50","20:00 - 20:50","21:00 - 21:50"};
        String inicioFinal[] = horarioClase.split(" ");

        for(int h = 0 ; h < horas.length ; h++)
        {
            String[] hs = horas[h].split(" ");
            if(inicioFinal[0].equals(hs[0]))
                arreglo[0] = h;
            if(inicioFinal[2].equals(hs[2]))
                arreglo[1] = h;

        }

        arreglo[0]++;
        arreglo[1]++;

        return arreglo;
    }




}
