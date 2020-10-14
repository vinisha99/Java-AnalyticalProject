package csv.data.operations;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import csv.data.exceptions.FileWithYearNotFoundException;
import csv.data.exceptions.MatchingRecordNotFoundException;

public class BabyExecution {

	private CSVOperations csvr = new CSVOperations();
	private static BabyExecution bExec;
	private static final int[] years = new int[] { 2012, 2013, 2014 };

	public static void main(String[] args) {
		
		bExec = new BabyExecution();

		try {
			//reading excel data based on years
			for (int year : years)
				bExec.csvr.readFile(year);
			
			
			System.out.println("\nUnique boy names in 2013 are " + bExec.csvr.uniqueBoyNames(years[1]));

			System.out.println("\nYear of Highest rank: " + bExec.csvr.yearOfHighestRank("Noah", 'M'));

			System.out.printf("\nAverage of Rank: %.3f\n", bExec.csvr.getAverageRank("Emma", 'F'));

			System.out.println("\nTotal births ranked higher: " + bExec.csvr.getTotalBirthsRankedHigher(2013, "William", 'M'));

			System.out.println("\nGet Name of baby based on year, rank and gender: " + bExec.csvr.getName(2013, 3, 'M'));

			System.out.println("\nWhats is name of Jacob from year 2014 if born in 2012 ");
			bExec.csvr.whatIsNameInYear("Jacob", 'M', 2014, 2012);
		} catch (MatchingRecordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileWithYearNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
