// Jay Doshi
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Button extends JButton implements ActionListener{
	private ImageIcon X = new ImageIcon(this.getClass().getResource("X.png"));
	private ImageIcon O = new ImageIcon(this.getClass().getResource("O.png"));
	public static boolean turn = true;
	public static boolean done = false;
	public Button() {
		this.addActionListener(this);
	}
	
	public void setIconO() {
		setIcon(O);
	}
	
	public boolean IconIsX() {
		if (this.getIcon() == null) return false;
		return this.getIcon().equals(X);
	}
	
	public boolean IconIsO() {
		if (this.getIcon() == null) return false;
		return this.getIcon().equals(O);
	}
	
	public void setIconX() {
		setIcon(X);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (turn && !done) {
			setIconX();
			turn = !turn;
			System.out.println(turn + " " + IconIsX() + " " + IconIsO());
		}
	}
}

public class TicTacToe{
  private JFrame frame = new JFrame();
  private JFrame frameLabel = new JFrame();
  private JPanel panel = new JPanel();
  private Button[] buttons = new Button[9];
  private int[] board;
  private int person;
  private int computer;

  public TicTacToe(Scanner in){
    frame.setSize(800, 800);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    panel.setLayout(new GridLayout(3, 3));
    
    for (int i = 0; i < 9; i++) {
    	buttons[i] = new Button();
    	panel.add(buttons[i]);
    }
    frame.add(panel);
    frame.setVisible(true);
    person = 1;
    computer = -1;
    board = new int[9];
    
    printBoard();
    playGame(in);
  }
  
  private void setFrameLabel(String str) {
    frameLabel.setLayout(new GridBagLayout());
    JPanel p = new JPanel();
    JLabel label = new JLabel(str);
    label.setFont(new Font("Verdana", 30 ,20));
    p.add(label);
    p.setBorder(new LineBorder(Color.BLACK)); // make it easy to see
    frameLabel.add(p);
    frameLabel.setSize(100, 100);
    frameLabel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frameLabel.setVisible(true);
  }
  
  private void playGame(Scanner in){
    while (!Button.done){
      for (int i = 0; i < 9; i++) {
    	  if (buttons[i].IconIsX())
    		  board[i] = 1;
      }
      if (win(person)){
        Button.done = true;
        setFrameLabel("X Wins");
        System.out.println("X Wins");
        break;
      }
      // printBoard();
      if (!Button.turn) {
    	  computerMove();
    	  Button.turn = !Button.turn;
      }
      
      if (win(computer)){
        Button.done = true;
        setFrameLabel("O Wins");
        System.out.println("O Wins");
        printBoard();
        break;
      }
      
      if (tie()){
        Button.done = false;
        setFrameLabel("Tie");
        System.out.println("Tie");
        break;
      }
    }
    
  }

  private void printBoard(){
    System.out.println(Arrays.toString(board));
    for (int i = 0; i < 3; i++){
      for (int j = i * 3; j < 3 * (i + 1); j++){
        if (board[j] == person) System.out.print("X ");
        else if (board[j] == computer) System.out.print("O ");
        else System.out.print("- ");
      }
      System.out.print("\n");
    }
    System.out.println();
  }

  private void playerMove(Scanner in){
    System.out.print("Enter in form: (x y): ");
    int x = in.nextInt();
    int y = in.nextInt();
    int index = 3 * x + y;
    while (index < 0 || index >= 9 || (index >= 0 && index < 9 && board[index] != 0)){
      System.out.println("Invalid, Please Try Again!");
      System.out.print("Enter in form: (x y): ");
      x = in.nextInt();
      y = in.nextInt();
      index = 3 * x + y;
    }
    board[index] = person;
  }

  private void computerMove(){
    int best = 10000;
    int place = -1;
    for (int i = 0; i < 9; i++){
      if (board[i] == 0){
        board[i] = computer;
        int score = minimax(0, person);
        board[i] = 0;
        if (score < best){
          best = score;
          place = i;
        }
      }
    }
    if (place >= 0) {
      board[place] = computer;
      buttons[place].setIconO();
    }
  }

  private int evaluate(){
    if (win(person))
      return person;
    if (win(computer))
      return computer;
    return 0;
  }

  private int minimax(int depth, int player){
    int total = evaluate();

    if (total == person)
      return total;
    if (total == computer)
      return total;

    if (tie())
      return 0;

    if (player == person){
      int val = -10000;
      for (int i = 0; i < 9; i++){
        if (board[i] != 0) continue;
        board[i] = player;
        val = Math.max(val, minimax(depth + 1, computer));
        board[i] = 0;
      }
      return val;
    }
    else {
      int val = 10000;
      for (var i = 0; i < 9; i++){
        if (board[i] != 0) continue;
        board[i] = computer;
        val = Math.min(val, minimax(depth + 1, person));
        board[i] = 0;
      }
      return val;
    }
  }

  private boolean win(int player){
    for (int i = 0; i < 9; i += 3){
      if (board[i] == player && board[i + 1] == player && board[i + 2] == player){
        return true;
      }
    }

    for (int i = 0; i < 3; i++){
      if (board[i] == player && board[i + 3] == player && board[i + 6] == player){
        return true;
      }
    }

    if (board[0] == player && board[4] == player && board[8] == player)
      return true;

    if (board[2] == player && board[4] == player && board[6] == player)
      return true;

    return false;
  }

  private boolean tie(){
    for (int i = 0; i < 9; i++)
      if (board[i] == 0)
        return false;
    return true;
  }

  public static void main(String[] args){
    Scanner in = new Scanner(System.in);
    TicTacToe game = new TicTacToe(in);
  }
}
