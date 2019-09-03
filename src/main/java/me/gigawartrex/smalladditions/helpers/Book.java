package me.gigawartrex.smalladditions.helpers;

import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.*;
import java.util.ArrayList;

public class Book extends ItemStack {
 
    //private ArrayList<String> lore = new ArrayList<String>();
    private BookMeta meta;
 
    public Book(String fileName, String author, String title){
        super(Material.WRITTEN_BOOK);
        this.setAmount(1);
        String path = "./plugins/" + Constants.name + "/BookTextFiles/"+fileName+".txt";
        meta = (BookMeta) this.getItemMeta();
        meta.setAuthor(author);
        meta.setTitle(title);
        meta.setPages(fileToPages(new File(path)));
        setItemMeta(meta);
    }
    
    private ArrayList<String> fileToPages(File file){ //Method to convert text to book pages which are separated after word ending
    	
    	ArrayList<String> list = new ArrayList<>();
		ArrayList<String> testList = new ArrayList<>();
    	String fullString = "";
    	
    	try(BufferedReader br = new BufferedReader(new FileReader(file))) {

			//testList = new ArrayList<>();
			StringBuilder stringB = new StringBuilder();
			stringB.append("Test Zeile 1");
			stringB.append("\n");
			stringB.append("Test Zeile 2");
			testList.add(stringB.toString());
    		
    		//list = new ArrayList<>();
    	    String line = br.readLine();
    	    
    	    while (line != null) {
    	    	fullString = fullString + line;
    	        line = br.readLine();
    	    }
    	    
    	    int pointer = 0; //points at upper end char of substring
    	    int newpointer = 0; //points at lower end char of substring
    	    boolean noSpace; //for while loop
    	    
    	    while(true) {
    	    	noSpace = true;
    	    	newpointer = pointer;
    	    	pointer += 256;
    	    	if(pointer >= fullString.length()) {
    	    		list.add(fullString.substring(newpointer));
    	    		break;
    	    	}
    	    	while(noSpace) {
        	    	if(fullString.charAt(pointer-1) != ' ') {
            	   		pointer--;
            	   	}else if(fullString.charAt(pointer-1) == '-' || fullString.charAt(pointer-1) == ' '){
            	   		list.add(fullString.substring(newpointer, pointer));
            	   		noSpace = false;
            	   	}
        	    }
    	    }
    	    
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return list;
    }
    
    public BookMeta getMeta() {
    	return this.meta;
    }
    
}