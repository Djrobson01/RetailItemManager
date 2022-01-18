/**
 * 
 * @author Dominic Robson
 * 
 * This program uses the RetailItem class to create an ArrayList of items that will be saved to a file.
 * 
 * Uses a menu system to allow users to do nine different things. These include: 
 * 		print all items in the ArrayList, print a specific item, remove units on hand from a specific item, 
 * 		add units on hand to a specific item, change a retail item's price, change a retail item's description,
 * 		create a new retail item, delete an existing retail item, and save the ArrayList to a file and exit.
 * 
 * This program also creates a backup file to ensure that all the data is not lost. 
 */

import java.io.*;
import java.util.*;

public class RetailItemManager {
    /**
	 * Scanner userScnr Class variable used throughout the program to get user input.
	 */
    static Scanner userScnr = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
	
	
		//creates new file using RetailItemDatabase.txt
		File itemFile = new File("RetailItemDatabase.txt");

		//creates an ArrayList
		ArrayList<RetailItem> itemList = new ArrayList<RetailItem>();
		
		//variables
		int choice = 0; //used for menuChoice and executeMenuSelection
		
		//checks to ensure file exists
		//if it does not exist, program will exit without doing anything
		if(!itemFile.exists()) {
			//error message if file does not exist
			System.out.println("The file " + itemFile + " cannot be found.");
		}
		else {
			//try block to catch any file errors
			try {
				//creates a Scanner for the file
				Scanner fileScnr = new Scanner(itemFile);
				
				//creates ArrayList from items in file
				//may throw IOException
				createArrayList(itemList, itemFile, fileScnr); 
				
				//backs up the most recently saved version of the file
				//done in case something goes wrong with saving changes
				//may throw IOException
				backupFile(itemList);

				do {
					displayMenu();
					choice = menuChoice(userScnr);
					executeMenuSelection(itemList, choice); //may throw IOException
				} while(choice != 9);
			}
			catch(IOException fileError) {
				//error message if executeMenuSelection(), createArrayList(), or backupFile() throw an IOException
				System.out.println("There was an error when handling the file.");
			}
		}
	}
	
	/**
	 * Creates an ArrayList that is used throughout the program.
	 * 
	 * @param itemList an ArrayList used to hold RetailItem objects.
	 * @param itemFile a File used to take data and create RetailItem objects.
	 * @param fileScnr Scanner used to read data from the File.
	 * 
	 * @throws IOException May throw IOException when creating a Scanner for itemFile
	 * 
	 */	
	public static void createArrayList(ArrayList<RetailItem> itemList, File itemFile, Scanner fileScnr) throws IOException {
		//creates new items to add to itemList
		//while there is still another line to read
		while(fileScnr.hasNextLine()) {
			//gets next line in the file
			String dataLine = fileScnr.nextLine();
			
			//creates Scanner for the line
			Scanner lineScnr = new Scanner(dataLine);
			
			//creates new retailItem
			RetailItem item = new RetailItem();
			
			try {
				//may throw IllegalArgumentException
				//reads data in line to create a new RetailItem
				item.readData(lineScnr);
				
				//adds new item to itemList if exception isn't thrown
				itemList.add(item);
			}
			catch(IllegalArgumentException badData) {
				//error message specified in RetailItem.java
				System.out.println(badData);
				
				//message informing user record was not added to list
				System.out.println("Deleted record input.\n");
			}
		}
		
		//closes Scanner
		fileScnr.close();
	}
	
	/**
	 * Displays the menu for the user to choose from.
	 */
	public static void displayMenu() {
		System.out.println("MENU: ");
		System.out.println("1. Display All Retail Items.");
		System.out.println("2. Display a Specific Retail Item.");
		System.out.println("3. Remove Units from an Item.");
		System.out.println("4. Add Units to an Item.");
		System.out.println("5. Change the Price of a Retail Item.");
		System.out.println("6. Change the Description of a Retail Item.");
		System.out.println("7. Create a New Retail Item.");
		System.out.println("8. Delete an Existing Retail Item.");
		System.out.println("9. Save and Exit.");
		System.out.println();
	}

	/**
	 * Gets the user's menu choice and returns it to main.
	 * 
	 * @param userScnr Scanner used to get input from the user.
	 * 
	 * @return an integer, 1-9, representing the user's choice.
	 */
	public static int menuChoice(Scanner userScnr) {
		//variables
		int choice = 0;
		int counter = 0;
		String userInput;
		
		//prompts user for a menu number
		System.out.println("Please enter a number (1-9), no decimals, corresponding to what you would like to do.");
		
		while(counter < 5) {
			//gets user input
			userInput = userScnr.next();
			
			//clears Scanner in case user put in anything else
			userScnr.nextLine();
			
			//creates scanner for user input
			//done to handle bad data
			Scanner inputScnr = new Scanner(userInput);
			
			if(inputScnr.hasNextInt()) {
				choice = inputScnr.nextInt();
				
				if(choice < 1 || choice > 9) {
					//error message for user
					System.out.println("\"" + choice + "\" is not a valid choice.");
					System.out.println("You have " + (5 - counter) + " more tries to enter a number 1 through 9 before the program exits itself and current changes will be saved.");
					System.out.println("Please input another number.");
				
				}
				else {
					//closes Scanner
					inputScnr.close();

					return choice;
				}
			}
			else {
				//error message for user
				System.out.println("\"" + userInput + "\" is not a valid choice.");
				System.out.println("You have " + (5 - counter) + " more tries to enter a number 1 through 9 before the program exits itself and current changes will be saved.");
			}
			
			++counter;
			
			//closes Scanner
			inputScnr.close();
		}	
		
		//sets choice to 9 to save and exit the program
		choice = 9;
		
		return choice;	
	}
	
	/**
	 * Uses the user's selection to call a specific method that will execute the user's choice.
	 * 
	 * @param itemList ArrayList holding RetailItem objects.
	 * @param menuSelection integer representing the user's choice.
	 * 
	 * @throws IOException May throw IOException when executing the exit() method
	 */
	public static void executeMenuSelection(ArrayList<RetailItem> itemList, int menuSelection) throws IOException {
		if(menuSelection == 1) {
			displayAllRetailItems(itemList);
		}
		else if(menuSelection == 2) {
			displaySpecificItem(itemList, userScnr);
		}
		else if(menuSelection == 3) {
			removeItemUnits(itemList, userScnr);
		}
		else if(menuSelection == 4) {
			addItemUnits(itemList, userScnr);
		}
		else if(menuSelection == 5) {
			changeItemPrice(itemList, userScnr);
		}
		else if(menuSelection == 6) {
			changeItemDescription(itemList, userScnr);
		}
		else if(menuSelection == 7) {
			addNewRetailItem(itemList, userScnr);
		}
		else if(menuSelection == 8) {
			deleteRetailItem(itemList, userScnr);
		}
		else if(menuSelection == 9) {
			exit(itemList);
		}
	}
	
	/**
	 * Displays all items in itemList.
	 * 
	 * @param itemList ArrayList holding RetailItem objects.
	 */
	public static void displayAllRetailItems(ArrayList<RetailItem> itemList) {
		System.out.println("Displaying all retail items.\n");
		
		//iterates through itemList and prints out the info for each item
		for(int i = 0; i < itemList.size(); ++i) {
			itemList.get(i).printInfo();
		}
	}
	
	/**
	 * Displays information for a user-specified item.
	 * 
	 * @param itemList ArrayList holding RetailItem objects.
	 * @param userScnr Scanner used to get input from the user.
	 */
	public static void displaySpecificItem(ArrayList<RetailItem> itemList, Scanner userScnr) {
		//variables
		int indexFound;
		String userInput;
		String choice;
		
		do {
			//prompts user for item
			System.out.println("What item would you like to display? Remember, no spaces.");
			
			userInput = userScnr.next();
			
			//clears Scanner in case user put anything else in.
			userScnr.nextLine();
			
			indexFound = findItem(itemList, userInput);
			
			if(indexFound >= 0) {
				itemList.get(indexFound).printInfo();
			}
			else {
				//lets user know the item was not found
				System.out.println("Item \"" + userInput + "\" was not found. Make sure there are no spaces.");
			}

			//asks user if they want to display another item
			System.out.println("Press \"y\" to display another item. Any other key will return you to the menu.");
			
			choice = userScnr.next();
			
			//clears Scanner in case user put anything else in
			userScnr.nextLine();
		} while(choice.equalsIgnoreCase("y"));
		
		System.out.println("User no longer wants to display a specific item. Returning to the menu.\n");
	}

	/**
	 * Removes a user-specified number of units from a user-specified item.
	 * 
	 * @param itemList ArrayList holding RetailItem objects.
	 * @param userScnr Scanner used to get input from the user.
	 */
	public static void removeItemUnits(ArrayList<RetailItem> itemList, Scanner userScnr) {
		//variables
		boolean done = false;
		int indexFound;
		int previousUnits;
		int unitsToRemove;
		int counter = 0;
		String userInput;
		
		//prompts user for an item
		System.out.println("What item would you like to remove units from. Remember, no spaces.");
		
		userInput = userScnr.next();
		
		//clears Scanner in case user put in anything else
		userScnr.nextLine();
		
		indexFound = findItem(itemList, userInput);
		
		if(indexFound >= 0) {
			
			previousUnits = itemList.get(indexFound).getUnitsOnHand();
			
			//informs user how many units the item has
			System.out.println("Item found. \"" + itemList.get(indexFound).getDescription() + "\" has " + previousUnits + " units on hand.");
			
			while(counter <= 5 && !done) {
				//prompts user for number of units to remove.
				System.out.println("How many items would you like to remove? You can remove up to " + (previousUnits - 1) + " units.");
				
				userInput = userScnr.next();
				
				//clears Scanner in case user input something else;
				userScnr.nextLine();
				
				//creates Scanner for user input
				//done for bad input handling
				Scanner inputScnr = new Scanner(userInput);
						
				if(inputScnr.hasNext()) {
					if(inputScnr.hasNextInt()) {
						unitsToRemove = inputScnr.nextInt();
						
						if(!(unitsToRemove <= 0) && !(unitsToRemove > itemList.get(indexFound).getUnitsOnHand())) {
							//lets user know program is removing the units
							System.out.println("Removing " + unitsToRemove + " units from item \"" 
							    + itemList.get(indexFound).getDescription() + "\".");
							
							//changes units on hand
							itemList.get(indexFound).setUnitsOnHand(previousUnits - unitsToRemove);
					
							//tells user program has removed the units and how many units are left
							System.out.println("Item \"" 
                                + itemList.get(indexFound).getDescription() 
                                + "\" now has " + itemList.get(indexFound).getUnitsOnHand() 
                                + " units on hand.\n");
							
							//sets to true to get out of loop
							done = true;
						}
						else {
							//error message for bad input
							System.out.println("The number of units to remove needs to be a whole number"
                                + " greater than zero without decimals. Please try again.");
							//informs user how many tries are left
							System.out.println("You have " + (5 - counter) + " more tries.");
						}
						
					}
					else {
						//error message for bad input
						System.out.println("The number of units to remove needs to be a whole number"
                            + " greater than zero without decimals. Please try again.");
						//informs user how many tries are left
						System.out.println("You have " + (5 - counter) + " more tries.");
					}	
				}
				
				//closes Scanner
				inputScnr.close();
				
				++counter;
			}
			
			//message displays if user has run out of tries
			if(!done) {
				System.out.println("You have failed to input a valid amount of units to remove. Returning to the menu.");
			}
			
		}
		else {
			itemNotFoundMessage(userInput);
		}
	}
	
	/**
	 * Adds a user-specified number of units to a user-specified item.
	 * 
	 * @param itemList ArrayList holding RetailItem objects.
	 * @param userScnr Scanner used to get input from the user.
	 */
	public static void addItemUnits(ArrayList<RetailItem> itemList, Scanner userScnr) {
		//variables
		boolean done = false;
		int indexFound;
		int unitsToAdd;
		int previousUnits;
		int counter = 0;
		String userInput;
		
		//prompts user for item
		System.out.println("What item would you like to add units to. Remember, no spaces.");
		
		userInput = userScnr.next();
		
		//clears Scanner in case user input something else
		userScnr.nextLine();
		
		indexFound = findItem(itemList, userInput);
		
		if(indexFound >= 0) {
			previousUnits = itemList.get(indexFound).getUnitsOnHand();
			
			//informs user how many units on hand there are for the given item
			System.out.println("Item \"" + itemList.get(indexFound).getDescription() + "\" has " + itemList.get(indexFound).getUnitsOnHand() + " units.");
			
			while(counter <= 5 && !done) {
				//prompts user for items to add
				System.out.println("How many units would you like to add?");
				
				userInput = userScnr.next();
				
				//clears Scanner in case user input something else
				userScnr.nextLine();
				
				Scanner inputScnr = new Scanner(userInput);
				
				if(inputScnr.hasNext()) {
					if(inputScnr.hasNextInt()) {
						unitsToAdd = inputScnr.nextInt();
						
						if(unitsToAdd > 0) {
							//informs user the program is adding the units
							System.out.println("Adding " + unitsToAdd + " units to item \"" 
                                + itemList.get(indexFound).getDescription() + "\".");
							
							//adds the units
							itemList.get(indexFound).setUnitsOnHand(previousUnits + unitsToAdd);
						
							//informs user the units have been added and how many are left
							System.out.println("Item \"" + itemList.get(indexFound).getDescription() 
                                + "\" now has " + itemList.get(indexFound).getUnitsOnHand() 
                                + " units.\n");
							
							done = true;
						}
						else {
							//error message for bad input
							System.out.println("The number of units to add needs to be a whole number"
                                + " greater than zero without decimals. Please try again.");
							//message letting user know how many tries are left
							System.out.println("You have " + (5 - counter) + " more tries.");
						}
					}
					else {
						//error message for bad input
						System.out.println("The number of units to add needs to be a whole number" 
                            + " greater than zero without decimals. Please try again.");
						//message letting user know how many tries are left
						System.out.println("You have " + (5 - counter) + " more tries.");
					}
				}
				
				//closes Scanner
				inputScnr.close();
				
				++counter;
			}
			
			//message displays if user has run out of tries
			if(!done) {
				System.out.println("You have failed to input a valid amount of units to add. Returning to the menu.\n");
			}
				
		}
		else {
			itemNotFoundMessage(userInput);
		}
	}
	
	/**
	 * Changes the price of a user-specified item to a user-specified amount. 
	 * 
	 * @param itemList ArrayList holding RetailItem objects.
	 * @param userScnr Scanner used to get input from the user.
	 */
	public static void changeItemPrice(ArrayList<RetailItem> itemList, Scanner userScnr) {
		//variables
		boolean done = false;
		double newPrice;
		int indexFound;
		int counter = 0;
		String userInput;
		
		//prompts user for an item
		System.out.println("What item would you like to change the price for? Remember, no spaces.");
		
		userInput = userScnr.next();
		
		//clears Scanner in case user put anything else in
		userScnr.nextLine();
		
		indexFound = findItem(itemList, userInput);
		
		if(indexFound >= 0) {
			//tells user the current price of the item found
			System.out.println("Item \"" + itemList.get(indexFound).getDescription() + "\""
								+ " has a current price of $" + itemList.get(indexFound).getPrice() + ".");

				while(counter <= 5 && !done) {
	
				//prompts user for new price
				System.out.println("What would you like to change the price to? Number only.");
				
				userInput = userScnr.next();
				
				//clears Scanner in case user input something else
				userScnr.nextLine();
				
				//creates Scanner for user input
				//done for bad data handling
				Scanner inputScnr = new Scanner(userInput);
				
				if(inputScnr.hasNext()) {
					if(inputScnr.hasNextDouble()) {
						newPrice = inputScnr.nextDouble();
						
						if(newPrice > 0) {
							//informs user the program is changing the price to a new one
							System.out.printf("Changing the price for item \"%s\" from $%.2f to $%.2f.\n", itemList.get(indexFound).getDescription(), itemList.get(indexFound).getPrice(), newPrice);
							
							//changes the price
							itemList.get(indexFound).setPrice(newPrice);
							
							//informs user the price has been changed
							System.out.println("Price has been changed.\n");
							
							done = true;
						}
						else {
							//error message for user if they input a bad value
							System.out.println("The new price needs to be greater than zero with two decimals (only the first two decimals will be stored).\n"
								+ "If no decimals are input, the program shall assume" 
                                + " the decimals are \".00\". Please try again.\n");
							
							//informs user how many tries they have left
							System.out.println("You have " + (5 - counter) + " more tries.\n");
						}
					}
					else {
						//error message for user if they input a bad value
						System.out.println("The new price needs to be greater than zero with two decimals (only the first two decimals will be stored).\n"
								+ "If no decimals are input, the program shall assume" 
                                + " the decimals are \".00\". Please try again.\n");
						
						//informs user how many tries they have left
						System.out.println("You have " + (5 - counter) + " more tries.\n");
					}
				}
				
				//closes Scanner
				inputScnr.close();
				
				++counter;
			}
			
			//message only displays if user runs out of tries
			if(!done) {
				System.out.println("You have failed to input a valid price. Returning to the menu.");
			}
			
		}
		else {
			itemNotFoundMessage(userInput);
		}

	}

	/**
	 * Changes the description of a user-specified item to a user-specified description.
	 * 
	 * @param itemList ArrayList holding RetailItem objects.
	 * @param userScnr Scanner used to get input from the user.
	 */
	public static void changeItemDescription(ArrayList<RetailItem> itemList, Scanner userScnr) {
		//variables
		int indexFound;
		String userInput;
		String newDescription;
		String choice;
		
		//prompts user for an item
		System.out.println("What item would you like to change the description for? Remember, no spaces.");
		
		//no need to protect against an empty string
		//will not continue until at least one character is inputed
		userInput = userScnr.next();
		
		//clears Scanner in case user put in anything else
		userScnr.nextLine();
		
		indexFound = findItem(itemList, userInput);
		
		if(indexFound >= 0) {
			
			do {
				System.out.println("Current description for the item is \"" + itemList.get(indexFound).getDescription() + "\". What would you like to change it to?");
				
				newDescription = userScnr.next();
				
				//clears Scanner in case user put in anything else
				userScnr.nextLine();
				
				//confirmation message for the new description
				System.out.println("Is the new desired description for the current item \"" +itemList.get(indexFound).getDescription() + "\" "
									+ "\"" + newDescription + "\"? If the description isn't correct, remember to not use spaces.");
				
				//tells user to press Y to set new description
				System.out.println("If new description is correct, please press \"Y\" to continue. Any other key will repeat this process.");

				choice = userScnr.next();
				
				//clears Scanner in case user put in anything else
				userScnr.nextLine();
			} while(!choice.equalsIgnoreCase("Y"));
			
			//informs user the program is changing the item description
			System.out.println("Changing item description from \"" + itemList.get(indexFound).getDescription() + "\" to \"" + newDescription + "\".");
			
			//changes item description
			itemList.get(indexFound).setDescription(newDescription);
			
			//informs user the description has been changed.
			System.out.println("Done");
			
		}
		else {
			itemNotFoundMessage(userInput);
		}

	}

	/**
	 * Creates a new RetailItem with user-specified values for the description, unitsOnHand, and price.
	 * 
	 * @param itemList ArrayList holding RetailItem objects.
	 * @param userScnr Scanner used to get input from the user.
	 */
	public static void addNewRetailItem(ArrayList<RetailItem> itemList, Scanner userScnr) {
		//variables
		boolean done = false;
		double userPrice = 0.00;
		int userUnitsOnHand = 0;
		int indexFound;
		int counter = 0;
		String choice;
		String userDescription = null;
		String userInput;
		
		while(counter <= 5 && !done) {
			
			do {
				//prompts user for description for the new item
				System.out.println("What is the description of the new item? Remember, no spaces.");
				
				userDescription = userScnr.next();
				
				//clears Scanner in case user inputs more than one input
				userScnr.nextLine();
				
				//confirms the description is correct
				System.out.println("Is the description \"" + userDescription + "\" correct?");
				System.out.println("Press \"y\" to continue. Anything else and this process will repeat.");
				
				choice = userScnr.next();
				
				//clears Scanner in case user inputs more than one input
				userScnr.nextLine();
				
			} while(!choice.equalsIgnoreCase("y"));
			
			//used to see if item already exists
			indexFound = findItem(itemList, userDescription);
			
			if(indexFound >= 0) {
				//informs user that the item already exists
				System.out.println("There is already an item with the description \"" + userDescription + "\".");
				
				//informs user how many tries they have left
				System.out.println("You have " + (5 - counter) + " more tries.");
				
				++counter;
			}
			else {
				do {
					while(!done) {
						//prompts user for units on hand
						System.out.println("How many units do you have on hand? Remember, positive, whole numbers greater than zero only.");
						
						userInput = userScnr.next();
						
						//clears Scanner in case user put in anything else
						userScnr.nextLine();
						
						//makes Scanner for user input
						//done for bad input handling
						Scanner inputScnr = new Scanner(userInput);
						
						if(inputScnr.hasNext()) {
							if(inputScnr.hasNextInt()) {
								userUnitsOnHand = inputScnr.nextInt();
								
								if(userUnitsOnHand > 0) {
									//confirms the amount is correct
									System.out.println("Is the amount \"" + userUnitsOnHand + "\" correct?");
									System.out.println("Press \"y\" to continue. Anything else and this process will repeat.");
									
									choice = userScnr.next();
									
									//clears Scanner in case user put in anything else
									userScnr.nextLine();
									
									//set to true to get out of while loop if user is done
									done = true;
								}
								else {
									//error message for negative number
									System.out.println("Must be larger than zero! Please try again.\n");
								}
							}
							else {
								//error message for bad input
								System.out.println("Your input for the units on hand is invalid. Please try again.\n");
							}
						}
						
						//closes Scanner
						inputScnr.close();
					}
					
					//resets flag in case user is not done.
					done = false;
					
				} while(!choice.equalsIgnoreCase("y"));
				
				do {
					while(!done) {
						//prompts user for item price
						System.out.println("What is the price of the new item? Remember, positive numbers with two decimal places.");
						
						userInput = userScnr.next(); 
						
						//clears Scanner in case user put anything else
						userScnr.nextLine();
						
						//creates Scanner for user input
						//done for bad data handling
						Scanner inputScnr = new Scanner(userInput);
						
						if(inputScnr.hasNextDouble()) {
							userPrice = inputScnr.nextDouble();
										
							if(userPrice > 0) {
								//confirms price is correct
								System.out.printf("Is the price \"$%.2f\" correct?\n", userPrice);
								System.out.println("Press \"Y\" to continue. Anything else and this process will repeat.");
								
								choice = userScnr.next();
								
								//clears Scanner in case user input anything else
								userScnr.nextLine();
								
								//set to true to get out of while loop in case user is done
								done = true;
							}
							else {
								//error message for negative price
								System.out.println("The price of the new item needs to be greater than 0. Please try again.\n");
							}
						}
						else {
							//error message for bad input
							System.out.println("The price needs to be a positive number with two decimals (only the first two will be used). Please try again.\n");
						}
						
						//closes Scanner
						inputScnr.close();
					}
					
					//resets flag in case user is not done
					done = false;
					
				} while(!choice.equalsIgnoreCase("y"));
				
				//sets to true to get out of the encompassing while loop 
				//put here to make sure it isn't flagged true if item already exists
				done = true;
				
			} //else curly brace
	
		} //while curly brace
		
		if(counter <= 5) {
			//confirms new item creation with specified values	
			System.out.printf("Press \"y\" to create an new item with the description of "
							+ "\"%s\", units of %d, and a price of $%.2f. ", userDescription, userUnitsOnHand, userPrice);
			System.out.println("Any other key will not create the item.");
				
			choice = userScnr.next();
			
			//clears Scanner in case user input something else
			userScnr.nextLine();
			
			if(choice.equalsIgnoreCase("y")) {
				//creates new item with user-specified values
				RetailItem newItem = new RetailItem(userDescription, userUnitsOnHand, userPrice);
				
				//informs user the item has been created
				System.out.println("Item has been created.");
			
				//adds item to the ArrayList
				itemList.add(newItem);
			}
			else {
				//informs user the item has not been created and the data is deleted
				System.out.println("User has input " + choice + " deleting item contents and returning to the menu.");
			}
		}
		else {
			//informs user the have run out of tries
			System.out.println("You have failed to give a description that differs from existing items. Returning to the menu.\n");
		}
	}

	/**
	 * Deletes a user-specified RetailItem from itemList if it exists in the list.
	 *  
	 * @param itemList ArrayList holding RetailItem objects.
	 * @param userScnr Scanner used to get input from the user.
	 */
	public static void deleteRetailItem(ArrayList<RetailItem> itemList, Scanner userScnr) {
		//variables
		int indexFound;
		String itemToDelete;
		String choice;
		
		//displays all retail items 
		//this inform user what items are available to delete
		displayAllRetailItems(itemList);
		
		//prompts user to choose an item to delete
		System.out.println("\nWhat item would you like to try and delete? Remember, no spaces.");
		
		itemToDelete = userScnr.next();
		
		//clears Scanner in case user input something else
		userScnr.nextLine();
		
		indexFound = findItem(itemList, itemToDelete);
		
		if(indexFound >= 0) {
			//confirms user's desire to delete the item
			System.out.println("Are you sure you want to delete the item \"" + itemList.get(indexFound).getDescription() + "\"?");
			System.out.println("Press \"y\" to delete the item. Any other key will return to the menu.");
			
			choice = userScnr.next();
			
			//clears Scanner in case user input something else
			userScnr.nextLine();
			
			if(choice.equalsIgnoreCase("y")) {
				//informs user the item is being deleted
				System.out.println("Deleting item...\n");
				
				//removes item form the itemList
				itemList.remove(indexFound);
				
				//informs user the item has been deleted
				System.out.println("Item has been deleted. Displaying remaining retail items.\n");
			
				//displays the remaining retail items.
				displayAllRetailItems(itemList);
			}
			else {
				//if user does not press Y, this informs user the item has not been deleted
				System.out.println("You have decided not to delete the item \"" + itemToDelete + "\". Returning to the menu.");
			}
		}
		else {
			itemNotFoundMessage(itemToDelete);
		}	
	}
	
	/**
	 * Exits the program and saves the current state of itemList to a .txt file.
	 * 
	 * @param itemList ArrayList hold RetailItem objects.
	 * 
	 * @throws IOException May throw IOException if FileWriter cannot open the new file.
	 */
	public static void exit(ArrayList<RetailItem> itemList) throws IOException {
		//variables
		int i;
		
		//creates FileWriter to write to the new file
		FileWriter fileWriter = new FileWriter("RetailItemDatabase.txt");
		
		//creates PrintWriter to write to the file
		PrintWriter printWriter = new PrintWriter(fileWriter);
		
		//informs user they have decided to quit
		System.out.println("You have decided to quit. Saving the file.");
		
		//writes current items in itemList to the file and saves it
		for(i = 0; i < itemList.size(); ++i) {
			itemList.get(i).writeData(printWriter);
		}
		
		//informs user the file is saved and it is exiting
		System.out.println("File has been saved. Exiting the program.");
		
		//closes PrintWriter
		printWriter.close();
	}

	/**
	 * Saves the previously saved version of the file to prevent the complete loss of data 
	 * 		if there is an error while handling the file.
	 * 
	 * @param itemList ArrayList holding RetailItem objects.
	 * 
	 * @throws IOException May throw IOException if FileWriter cannot open the new file.
	 */
	public static void backupFile(ArrayList<RetailItem> itemList) throws IOException {
		//variables
		int i;
		
		//creates FileWriter to write to the new file
		FileWriter fileWriter = new FileWriter("RetailItemDatabaseBackup.txt");
		
		//creates PrintWriter to write to the file
		PrintWriter printWriter = new PrintWriter(fileWriter);
		
		//writes current items in itemList to the file and saves it
		for(i = 0; i < itemList.size(); ++i) {
			itemList.get(i).writeData(printWriter);
		}
		
		//closes PrintWriter
		printWriter.close();
	}
	
	/**
	 * Prints out an error message if an item is not found.
	 * 
	 * @param userInput String representing the item a user wants to find.
	 */
	public static void itemNotFoundMessage(String userInput) {
		//error message
		System.out.println("The item \"" + userInput + "\" was not found. Returning to the menu.\n");
	}
	
	/**
	 * Iterates through itemList to try and find an item by the description.
	 * 
	 * @param itemList ArrayList holding RetailItem objects.
	 * @param userInput String representing the item a user wants to find.
	 * 
	 * @return an integer representing the index an item was found at.
	 * 			Returns -1 if item was not found.
	 */
	public static int findItem(ArrayList<RetailItem> itemList, String userInput) {
		//variables
		int i;
		
		//iterates through itemList trying to find the item
		for(i = 0; i < itemList.size(); ++i) {
			if(itemList.get(i).getDescription().equalsIgnoreCase(userInput)) {
				return i;
			}
		}
		
		//default return
		return -1;
	} 
}