/* TETRIS AI GAME
 * 
 * this code is a simple tetris automatic algorithm, that distributed tetramino on the field themselves
 * to start game user must enter a list of figure (tetramino)
 * every tetraminos drop down to the floor of the game field
 * algorithm select the best angle and column of tetramino
 * 
 * this algorithm can be optimized more
 */



package contest_tetris;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mainclass {
	
	 static int[][] game_field = {{0,0,0,0,0,0,0,0,0,0},
								  {0,0,0,0,0,0,0,0,0,0},
								  {0,0,0,0,0,0,0,0,0,0},
								  {0,0,0,0,0,0,0,0,0,0},
								  {0,0,0,0,0,0,0,0,0,0},
								  {0,0,0,0,0,0,0,0,0,0},
								  {0,0,0,0,0,0,0,0,0,0},
								  {0,0,0,0,0,0,0,0,0,0},
								  {0,0,0,0,0,0,0,0,0,0},
								  {0,0,0,0,0,0,0,0,0,0}};
	
	
	 static int[][][] L = {{{1,0},
		      			    {1,0},
		      			    {1,1}},
			 			   {{1,1},
					 	    {0,1},
					 	    {0,1}},
			 			   {{0,0,1},
			                {1,1,1}},
			 		   	   {{1,1,1},
					 	    {1,0,0}}};
	 
	 static int[][][] J = {{{0,1},
						    {0,1},
						    {1,1}},
						   {{1,1},
					 	    {1,0},
					 	    {1,0}},
						   {{1,0,0},
					 	    {1,1,1}},
					   	   {{1,1,1},
					 	    {0,0,1}}};
	 
	 static int[][][] T = {{{0,1},
						    {1,1},
						    {0,1}},
						   {{1,0},
					 	    {1,1},
					 	    {1,0}},
						   {{0,1,0},
					 	    {1,1,1}},
					   	   {{1,1,1},
					 	    {0,1,0}}};
 
	 static int[][][] S = {{{1,0},
		    				{1,1},
						    {0,1}},
					   	   {{0,1,1},
					 	    {1,1,0}}};
	 
	 static int[][][] Z = {{{0,1},
							{1,1},
						    {1,0}},
					   	   {{1,1,0},
					 	    {0,1,1}}};
	 
	 static int[][][] O = {{{1,1},
						    {1,1}},
			 			   {{1,1},
					        {1,1}}};
	 
	 static int[][][] I = {{{1},
						    {1},
						    {1},
						    {1}},
					   	   {{1,1,1,1}}};	 
	 
	 public static void main(String[] args) {	
		
		Scanner in = new Scanner(System.in);
		System.out.print("Enter sequence of tetramino in random order (L,S,T,O,J,Z,I): ");
		String input_str = in.next();
		
		Pattern pattern = Pattern.compile("[^,LSTOJZI]");
		Matcher matcher = pattern.matcher(input_str);
		
		if (matcher.find()) {
			System.out.println("You are typing wrong symbol!");
			System.exit(0);
		}
		
		String [] input = input_str.split(",");		
	
		//----- drop down figure -----
		for(String figure : input) {
			switch (figure) {
				 case "L": place(L); System.out.print("\n\nL is dropped"); break;
	             case "J": place(J); System.out.print("\n\nJ is dropped"); break;
	             case "T": place(T); System.out.print("\n\nT is dropped"); break;
	             case "S": place(S); System.out.print("\n\nS is dropped"); break;
	             case "Z": place(Z); System.out.print("\n\nZ is dropped"); break;
	             case "O": place(O); System.out.print("\n\nO is dropped"); break;
	             case "I": place(I); System.out.print("\n\nI is dropped"); break;
					
			}
			//---- show field------
			System.out.print("\n");
			for(int i = 0; i < game_field.length; ++i) {
				System.out.print("\n");
				for(int j = 0; j < game_field[i].length; ++j) {
					System.out.print(game_field[i][j]);
				}
			}
			
			//---- flush row -----
			for(int k = game_field.length-1; k >= 0; --k) {
				int util_row = 0;
				for(int l : game_field[k]) {
					util_row += l; 
				}
				if (util_row == game_field[0].length) { 
					System.out.print("\n\nflush row = " + k);
					for(int i = k; i > 0; --i) 
						for(int j = game_field[i].length-1; j >= 0; --j) {		
							game_field[i][j] = game_field[i-1][j];
						}
					++k;
				}
			}						
		}		
	}
	 
	 
	 public static void place(int[][][] figure) {
		int[][] fantom_fields = game_field;
		int best_row = 0, best_fig = 0, best_i = 0, best_j = 0, best_W = 0;
			
		for(int k = 0; k < figure.length; ++k) {
			for(int j = 0; j <= game_field[0].length - figure[k][0].length; ++j) {
				boolean across = false;
				//----- drop down figure -----
				for(int i = figure[k].length-1; i < game_field.length; ++i) {
					
					int w=0, W=0, best_row_1 = 0;
					
					//----- 
					for(int r = 0; r < figure[k].length; ++r)
						for(int t = 0; t < figure[k][r].length; ++t) {
							//----- weight = sum of points figure and field -----
							w = fantom_fields[i-figure[k].length+r+1][j+t] + figure[k][r][t];
							//----- summary weight of points -----
							W += w;
							//----- check across -----
							if (w==2) {across = true;} 
						}
											
					//----- calculate max utilization row-1 -----
					if ((across)||(i==game_field.length-1)) {
						if (across) {--i;}
						
						for(int r = 0; r < figure[k].length; ++r) {
							int row_fig = 0, util_row = 0;
							for(int t = 0; t < game_field[0].length; ++t) {
								util_row += game_field[i-figure[k].length+r+1][t];
							}
							for(int t = 0; t < figure[k][r].length; ++t) {
								row_fig += figure[k][r][t];
							}
							util_row += row_fig;
							//----- calculate max utilization of row ----- 
							if (best_row_1 < util_row) best_row_1 = util_row;
						}
						
							
						if ((best_row_1 > best_row)||((best_row_1 == best_row)&&(W > best_W)))  {
							best_row = best_row_1; 
							best_W = W;
							best_fig = k; 
							best_i = i; 
							best_j = j;				 
					    }
						i = game_field.length;
					}
					else {continue;}
				 }
			}
		}
		
		for(int r = 0; r < figure[best_fig].length; ++r)
			for(int t = 0; t < figure[best_fig][r].length; ++t) {
				game_field[best_i-figure[best_fig].length+r+1][best_j+t] += figure[best_fig][r][t];
				
			}
	}
}
