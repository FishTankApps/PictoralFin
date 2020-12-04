package com.fishtankapps.pictoralfin.utilities;

import java.util.ArrayList;
import java.util.Scanner;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.objectBinders.DataFile;

public class JokeFactory {

	private static ArrayList<String> jokes;
	private static int jokeIndex;
	private static DataFile dataFile = null;
	
	private JokeFactory() {}
	
	public static void initialize(PictoralFin pictoralFin) {
		Scanner input = new Scanner(JokeFactory.class.getResourceAsStream("jokes.txt"));
		jokes = new ArrayList<>();
		
		dataFile = pictoralFin.getConfiguration().getDataFile();
		
		jokeIndex = dataFile.getJokeIndex();
		
		while(input.hasNextLine())
			jokes.add(input.nextLine().replace("$", "\n\n"));		
		
		input.close();
	}
	
	public static String getJoke() {		
		dataFile.setJokeIndex(++jokeIndex);		
		return jokes.get((jokeIndex % (jokes.size()-1)));
	}
	
}
