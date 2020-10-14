package csv.data.operations;
import csv.data.exceptions.BabyNameNotFoundException;
import csv.data.exceptions.FileWithYearNotFoundException;
import csv.data.exceptions.MatchingRecordNotFoundException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;
//import com.sun.tools.javac.util.Pair;

public class CSVOperations {
	private Baby baby;
	
	/*All baby file data is stored in the varaiable - 'babyMap' where prime key is the year.
	 * the value for this key is another hashmap with baby names set as key 
	 * and the value is baby object, which carries all the baby data relevant to the name.
	 */
	private LinkedHashMap<Integer, LinkedHashMap<String, Baby>> babyMap;
	
	CSVOperations(){
		//allocates memory for babyMap when an instance of CSVOperators is created
		babyMap = new LinkedHashMap<Integer, LinkedHashMap<String, Baby>>();
	}
	
	/*Method that takes input as year and reads CSV file from desktop location and stores file data into
	 * babyMap Hashmap with year as key
	 */
	public LinkedHashMap<Integer, LinkedHashMap<String, Baby>> readFile(int year) {
		String csvFile = "/Users/vinisha/Documents/Vinisha/Aplomb Training/Java Assignment 2/yob" + year + "short.csv";
		String line;
		int girlRank = 1;
		int boyRank = 1;
		
		try(BufferedReader br = new BufferedReader(new FileReader(csvFile))){
			LinkedHashMap<String, Baby> babyMapValue = new LinkedHashMap<>();
			while((line = br.readLine()) != null) {
				String[] babyData = line.split(",");
				
				if(babyData[1].charAt(0) == 'F') {
					baby = new Baby(babyData[1].charAt(0), Integer.parseInt(babyData[2]), girlRank);
					girlRank++;
				}
				else if(babyData[1].charAt(0) == 'M') {
					baby = new Baby(babyData[1].charAt(0), Integer.parseInt(babyData[2]), boyRank);
					boyRank++;
				}
				
				/*Linked Hashmap babyMapValue that stores baby name as key and baby object as value
				 * baby object contains total births by name, gender and Rank
				 * Rank - is stored with the assumption that csv files contain data sorted in the following order:
				 * 1 - Gender, 2 - total births, 3 - Names
				 */
				babyMapValue.put(babyData[0], baby);
			}
			//Stores year as key and Linked Hashmap - babyMapValue as value to the year key.
			babyMap.put(year, babyMapValue);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return babyMap;
	}
	
	//returns total number of babies born in a given year
	public int totalBirths(int year) {
		return babyMap.get(year).size();
	}
	
	//Returns unique girl names for a given year
	public int uniqueGirlNames (int year) throws FileWithYearNotFoundException{
		if(!babyMap.containsKey(year))
			throw new FileWithYearNotFoundException();
		
		int girlCount = 0;
		for(Map.Entry<String, Baby> entry : babyMap.get(year).entrySet()) {
			if(entry.getValue().getGender() == 'F')
				girlCount++;
			/*Breaking the loop when we encounter male babies, because file contains babies sorted by gender
			 * So the 1st occurance of male baby will be followed by only male babies till the end of record
			 */
			if(entry.getValue().getGender() == 'M')
				break;
		}
		return girlCount;
	}
	
	//Returns unique boy names for a given year
	public int uniqueBoyNames(int year) throws FileWithYearNotFoundException {
		if(!babyMap.containsKey(year))
			throw new FileWithYearNotFoundException();
		
		int boyCount = 0;
		for(Map.Entry<String, Baby> entry : babyMap.get(year).entrySet()) {
			if(entry.getValue().getGender() == 'M')
				boyCount++;	
		}
		return boyCount;
	}
	
	//method to return rank based on year, baby name and gender
	public int getRank(int year, String name, char gender){
		if(babyMap.containsKey(year) && babyMap.get(year).containsKey(name) && (babyMap.get(year).get(name).getGender() == gender))
			return babyMap.get(year).get(name).getRank();
		return -1;
	}
	
	//returns name of the baby based on year, gender and rank
	public String getName(int year, int rank, char gender) {
		for(Map.Entry<String, Baby> entry : babyMap.get(year).entrySet()) {
			if(entry.getValue().getGender() == gender && entry.getValue().getRank() == rank)
				return entry.getKey();
		}
//	 Stream<Object> babyName = babyMap.get(year).entrySet().stream().filter(entry -> gender == entry.getValue().getGender()
//			 && rank == entry.getValue().getRank()).map(Map.Entry::getKey);
	 return null;
	}
	
	/*takes in year1 and name1 of baby in a given year and then returns the baby name based
	 * on the rank, gender and year2 for the same baby if born in year2
	 */
	public void whatIsNameInYear(String name1, char gender, int year1, int year2) throws MatchingRecordNotFoundException {
		if(!(babyMap.containsKey(year1) && babyMap.get(year1).containsKey(name1)))
			throw new MatchingRecordNotFoundException();
		int rank1 = babyMap.get(year1).get(name1).getRank();
		String name2 = getName(year2, rank1, gender);
		if(name2 == null || name2.equals(""))
			throw new MatchingRecordNotFoundException();
		else
			System.out.println(name1 + "born in "+year1+" will be "+ name2+" if born in "+year2);
			
	}
	
	//Returns the year with highest rank for a given name and gender
	public int yearOfHighestRank(String name, char gender) {
		int highestRank = Integer.MAX_VALUE, highestRankYear = 0;
		for(int year : babyMap.keySet()) {
			if(babyMap.get(year).containsKey(name) && (babyMap.get(year).get(name).getGender() == gender) && (babyMap.get(year).get(name).getRank() < highestRank)) {
				highestRank = babyMap.get(year).get(name).getRank();
				highestRankYear = year;
			}
		}
		return highestRankYear;
	}
	
	//returns average rank for the given baby name and gender. Currently iterating through 3 files
	public double getAverageRank(String name, char gender) {
		int totalRank = 0;
		for(int year : babyMap.keySet()) {
			if(babyMap.get(year).containsKey(name) && (babyMap.get(year).get(name).getGender() == gender))
				totalRank += babyMap.get(year).get(name).getRank();
			else return -1.0;
		}
		return (totalRank*1.0)/babyMap.size();
	}
	
	/*takes in year, baby name and gender and
	 * returns the total number of babies ranked before the given baby
	 */
	public int getTotalBirthsRankedHigher(int year, String name, char gender) {
		//checks if baby name is present in the given year file 
		if(!babyMap.get(year).containsKey(name))
			return -1;
		int higherBirths = 0;
		for(Map.Entry<String, Baby> entry : babyMap.get(year).entrySet()) {
			//checks if the gender matches
			if(entry.getValue().getGender() == gender) {
				//if name also matches then ends the loop
				if(entry.getKey().equals(name))
					break;
				higherBirths += entry.getValue().getTotBabies();
			}
		}
		return higherBirths;
	}
}
