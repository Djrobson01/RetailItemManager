/**
 * This class creates a retail item with a specific description, number of units, and price.
 * It allows these items to be created from a database or to be created by the user.
 * It also allows the user to write the retail item(s) to a new file to create their own database.
 */


import java.io.PrintWriter;
import java.util.*;

public class RetailItem {
	
	/**
	 * String description RetailItem object's description.
	 * int unitsOnHand Remaining units of a RetailItem object.
	 * double price cost of a RetailItem object.
	 */
	private String description; 
	private int unitsOnHand; 
	private double price;
	
	/**
	 * default constructor initializing description, unitOnHand, and price.
	 */
	public RetailItem() {
		description = "";
		unitsOnHand = 0;
		price = 0.00; 
	}
	
	/**
	 * Creates a new RetailItem object using the three user-specified parameters.
	 * 
	 * @param userDescription String representing the description a user wants for the new RetailItem object.
	 * @param userUnitsOnHand integer representing the unitsOnHand a user wants for the new RetailItem object.
	 * @param userPrice double representing the price a user wants for the new RetailItem object.
	 * 
	 * @throws IllegalArgumentException if userDescription is an empty String, userUnitsOnHand is not an integer, or if userPrice is not a double or an integer.
	 */
	public RetailItem(String userDescription, int userUnitsOnHand, double userPrice) throws IllegalArgumentException {
		//used mutator methods to protect data
		setDescription(userDescription);
		setUnitsOnHand(userUnitsOnHand);
		setPrice(userPrice);
	}
	
	//mutators
	/**
	 * Takes a user-specified String and sets it as the description for a RetailItem object if it is not an empty String.
	 * 
	 * @param userDescription String representing the description a user wants to set for a RetailItem object.
	 * 
	 * @throws IllegalArgumentException if userDesctiption is an empty String
	 */
	public void setDescription(String userDescription) throws IllegalArgumentException {
		//checks to make sure description isn't empty
		if(userDescription.equals("")) {
			throw new IllegalArgumentException("Description cannot be empty.");
		}
		else {
			description = userDescription;
		}
			
	}
	
	/**
	 * Takes a user-specified integer and sets it as the unitsOnHand for a RetailItem object if it is greater than zero.
	 * 
	 * @param userUnitsOnHand integer representing the unitsOnHand a user wants to set for a RetailItem object.
	 * 
	 * @throws IllegalArgumentException if userUnitsOnHand is not an integer.
	 */
	public void setUnitsOnHand(int userUnitsOnHand) throws IllegalArgumentException {
		//ensures units on hand is not less than 0
		if(userUnitsOnHand <= 0) {
			throw new IllegalArgumentException("Units on hand cannot be less than or equal to 0.");
		}
		else {
			unitsOnHand = userUnitsOnHand;
		}
	}
	
	/**
	 * Takes a user-specified double and sets it as the price of a RetailItem if it is greater than zero.
	 * 
	 * @param userPrice double representing the price a user wants to set for a RetailItem object.
	 * 
	 * @throws IllegalArgumentException if userPrice is not a double or an integer
	 */
	public void setPrice(double userPrice) throws IllegalArgumentException {
		//ensures price is not less than 0
		if(userPrice <= 0) {
			throw new IllegalArgumentException("Price cannot be less than or equal to 0.");
		}
		else {
			price = userPrice;
		}
	}
	
	//accessors
	/**
	 * Returns a String representing the description of a RetailItem object.
	 * 
	 * @return a String representing the description of a RetailItem object.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns an integer representing the unitsOnHand for a RetailItem object.
	 * 
	 * @return an integer representing the unitsOnHand for a RetailItem object.
	 */
	public int getUnitsOnHand() {
		return unitsOnHand;
	}
	
	/**
	 * Returns a double representing the price of an RetailItem object.
	 * 
	 * @return a double representing the price of an RetailItem object.
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * Takes lineScnr to set the description, unitsOnHand, and price for 
	 * 		a RetailItem object instance from a file.
	 * 
	 * @param lineScnr Scanner passed representing a line of data in a file.
	 * 
	 * @throws IllegalArgumentException if description is empty, unitsOnHand is negative or not an integer, or if price is negative or not a double or an integer.
	 */
	public void readData(Scanner lineScnr) throws IllegalArgumentException {
		
		//checks to make sure there is something to read
		if(lineScnr.hasNext()) {
			setDescription(lineScnr.next());
			
			//checks to see if there is an int to read
			if(lineScnr.hasNextInt()) {
				setUnitsOnHand(lineScnr.nextInt());
				
				//checks if there is a double to read
				if(lineScnr.hasNextDouble()) {
					setPrice(lineScnr.nextDouble());
				}
				else {
					//error message thrown if the price is incorrect (not an int or double or a negative)
					throw new IllegalArgumentException("The price for \"" + description + "\" was not found or formatted incorrectly.");
				}
			}
			else {
				//error message thrown if unitsOnHand is incorrect (not an int or negative)
				throw new IllegalArgumentException("The units on hand for \"" + description + "\" was not found or formatted incorrectly.");
			}
		}
		else {
			//error message if a line is blank
			throw new IllegalArgumentException("Blank line. Deleting the line.");
		}

	} 
	
	/**
	 * Writes data to a new file. 
	 *  
	 * @param printWriter passed to allow a specific file to be written to.
	 */
	public void writeData(PrintWriter printWriter) {
		//formatted print
		printWriter.printf("%s %d %.2f \n", description, unitsOnHand, price);
	}
	
	/**
	 * Prints out a String representing an item's description, 
	 * 		an integer representing an item's unitsOnHand, 
	 * 		and a double representing an item's price.
	 */
	public void printInfo() {
		System.out.println("Description: " + description);
		System.out.println("Units On Hand: " + unitsOnHand);
		System.out.printf("Price: $%.2f\n\n", price);
	}	
	
	
	/**
	 * Returns a String representing the a RetailItems object's description, unitsOnHand, and price.
	 * 
	 * @return A String representing the a RetailItems object's description, unitsOnHand, and price.
	 */
	@Override
	public String toString() {
		return description + " " + unitsOnHand + " " + price;
	}
	
}