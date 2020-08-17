/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Set;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author alper
 */
public class Solvedoku extends Application{
    
    Text[] numTrack = new Text[81] ;
    StackPane[] stackTrack = new StackPane[81] ;
    int[][] grid = new int[9][9] ;
    Button solve = new Button("SOLVE") ;
    Button clear = new Button("CLEAR") ;
    VBox paneBox = new VBox();
    
    @Override
    public void start(Stage primaryStage) {  
        FlowPane mainPane = new FlowPane() ;
        paneBox.getChildren().add(mainPane) ;
        paneBox.setStyle("-fx-background-color:#e3c39d");
        solve.setStyle("-fx-background-color:#10893e;-fx-text-fill:white");
        clear.setStyle("-fx-background-color:#10893e;-fx-text-fill:white");
        paneBox.setAlignment(Pos.TOP_CENTER);
        paneBox.setSpacing(10) ;
        paneBox.setPadding(new Insets(20));
        Scene scene = new Scene(paneBox, 500, 600);
        setScene(mainPane) ;
        actions() ;
        
               
        primaryStage.setTitle("Solvedoku");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void setScene(FlowPane fp)
    {
        HBox buts = new HBox() ;
        buts.getChildren().addAll(solve,clear) ;
        buts.setAlignment(Pos.CENTER);
        buts.setSpacing(30) ;

        for(int i=0 ;i<81 ; i++)
        {
            StackPane sp = new StackPane() ;
            Rectangle sc = new Rectangle(50,50) ;
            Text txt = new Text("-") ;
            txt.setFont(new Font("Arial",40));
            numTrack[i]=txt ;
            stackTrack[i] = sp ;
            sp.getChildren().addAll(sc,txt) ;
            sc.setFill(Color.WHITE);
            sc.setStyle("-fx-stroke:black;-fx-stroke-width:0.5");
            fp.getChildren().add(sp);
        }
        paneBox.getChildren().add(buts) ;
        buts.setPadding(new Insets(20,5,5,5));   
        
    }
    
    public void actions()
    {
       
          clear.setOnMouseClicked(e -> {
          for(int j=0 ; j<stackTrack.length ; j++)
          {   
            Text newtext = new Text("-") ;
            newtext.setFont(new Font("Arial",40));
            numTrack[j].setText("-");
          }
        });
        
        solve.setOnMouseClicked(e -> {
            fillGrid(grid,numTrack) ;
            if(checkParameter(grid))
            {
                if(solve(grid))
                {
                    fillFinal();
                }
            }
            else
            {
                //Error Window
                error_window() ;
                clear.fireEvent(e);
            }
        });
        
        for(int i=0 ; i<stackTrack.length; i++)
        {
            Text tp = numTrack[i] ;
            stackTrack[i].setOnMouseClicked(e -> {
                if(e.getButton()== MouseButton.PRIMARY)
                    increase(tp);
                else if(e.getButton() == MouseButton.SECONDARY)
                    decrease(tp);
            });
            stackTrack[i].getChildren().set(1,tp) ;
        }
        }     
    public void increase(Text txt)
    {
        if(txt.getText().equals("-"))
            txt.setText("1");
        else
        {
            int temp = Integer.parseInt(txt.getText());
            if(temp %9 == 0)
                temp = 0 ;
            if(temp == 0)
                txt.setText("-");
            else
                txt.setText(++temp + "");
        }

    }
    
    public void decrease(Text txt)
    {
        if(txt.getText().equals("-"))
            txt.setText("9");
        else
        {
            int temp = Integer.parseInt(txt.getText());
            if(temp == 1)
            {
                txt.setText("-");
            }
            else
                txt.setText(--temp + "");
        }

    }
    
    public void fillGrid(int[][] grid,Text[] numtrack)
    {
        for(int i=0 ; i<numtrack.length ; i++)
        {
            if(numtrack[i].getText() == "-")
                numtrack[i].setText("0");
            grid[i/9][i%9] = Integer.parseInt(numtrack[i].getText());
        }
        
    }
   
    public boolean solve(int[][] board)
    {
        
           for(int i=0 ; i<board.length ; i++)
          {
            for(int j=0 ; j<board[0].length ; j++)
            {
               if(board[i][j] == 0)
               {
                  for(int num =1 ; num<=board.length ; num++)
                  {
                     if (isSafe(i,j,num,board))  
                     {   
                        board[i][j] = num ;
                        
                        if(solve(board))
                           return true ;
                        else
                           board[i][j] = 0 ;                        
                     }
                  }
                  return false ;
               }             
            }
          }
       return true ;       
    }
    
   public boolean isSafe(int row,int col,int num,int[][] board)
   {
        int r = row - row%3 ;
        int c = col - col%3 ;

        for(int i = 0; i < board.length ; i++)
        {
           if(board[row][i] == num)
              return false ;
           if(board[i][col] == num)
              return false ;         
        }
        for(int i=r ; i<r+3 ; i++)
        {
            for(int j=c ; j<c+3 ; j++)
            {
               if(board[i][j] == num)
                  return false ;
            }
        }

        return true ;
     }
   
   
   public boolean checkParameter(int[][] board)
   { 
      for(int i=0 ; i<board.length ; i++)
      {
            for(int j=0 ; j<board.length ; j++)
            {
                for(int k=j+1 ; k< board.length ; k++)
                {
                    if(board[i][j] == board[i][k] && board[i][j] != 0)
                        return false ;
                }
            }
      }
      for(int i=0 ; i<board.length ; i++)
      {
            for(int j=0 ; j<board.length ; j++)
            {
                for(int k=j+1 ; k<board.length ; k++)
                {
                    if(board[j][i] == board[k][i] && board[j][i] != 0)
                        return false ;
                }
            }
      }
      for(int i=0 ; i<board.length ; i++)
      {
          for(int j=0 ; j<board.length ; j++)
          {
                int r = i - i%3 ;
                int c = j - j%3 ;
                for(int k=r ; k<r+3 ; k++)
                {
                    for(int p=c ; p<c+3 ; p++)
                    {
                        if(board[k][p] == board[i][j] && board[i][j] != 0 && k!= 0 && p!=0 && k!=i && p!=c)
                            return false ;
                    }
                }
          }     
      }
      
      return true;
   }
   
   public void fillFinal()
   {
       for(int j=0 ;j<9 ; j++)
       {
            for(int k=0 ; k<9 ; k++)
            {
                numTrack[j*9+k].setText(String.valueOf(grid[j][k])) ;
            }
       }
                    for(int j=0 ; j<81 ; j++)
                    {
                        Text newtext = numTrack[j] ;
                        newtext.setFont(new Font("Arial",40));
                        stackTrack[j].getChildren().set(1,newtext) ;
                         numTrack[j] = newtext ;
                    }
   }
   
   public void error_window()
   {
       Alert alert = new Alert(AlertType.ERROR);
       alert.setTitle("Error");
       alert.setContentText("Sudoku cannot be solved!") ;
       alert.showAndWait();
   }
   
   public void sleep(int time)
   {
       try
       {
           Thread.sleep(time);
       }
       catch(InterruptedException e)
       {
           System.out.println(e);
       }  
   }
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
