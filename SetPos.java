package calculator3;


public class SetPos {

 
    private int row;

    private int column;

  
    public SetPos(int row, int column) {
        this.row = row;
        this.column = column;
    }

  
    public int getRow() {
        return row;
    }

   
    public int getColumn() {
        return column;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof SetPos) {
            result = ((SetPos) obj).column == column
                    && ((SetPos) obj).row == row;
        }
        return result;
    }
}