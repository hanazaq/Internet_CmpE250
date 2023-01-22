package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {


	public static void main(String args[]) {
		long dt1 = 0, dt2 = 0;
		Bst_tree<String> binary_tree = new Bst_tree<String>();
		Avl_tree<String> avl_tree = new Avl_tree<String>();
		try {
			// create output_bst.txt
			String name1 = args[1].substring(0, args[1].length() - 4) + "_bst.txt";
			File myObj = new File(name1);
			if (myObj.createNewFile()) {
				binary_tree.name = args[1];
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		try {
			// create output_avl.txt
			String name2 = args[1].substring(0, args[1].length() - 4) + "_avl.txt";
			File myObj = new File(name2);
			if (myObj.createNewFile()) {
				avl_tree.file_name_avl = name2;
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		try {
			// run bst
			File myObj = new File(args[0]);
			Scanner myReader = new Scanner(myObj);
			long begin1 = System.currentTimeMillis();
			if (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				binary_tree.add(data);

			}
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] inputs = data.split(" ");

				if (inputs[0].equals("ADDNODE")) {
					binary_tree.add(inputs[1]);

				} else if (inputs[0].equals("DELETE")) {
					binary_tree.delete(inputs[1]);
				} else if (inputs[0].equals("SEND")) {
					binary_tree.send(inputs[1], inputs[2]);
				}

			}
			long end1 = System.currentTimeMillis();
			dt1 = end1 - begin1;
			myReader.close();
			///////////////////////////////////////////
			// run avl
			File myObj2 = new File(args[0]);
			Scanner myReader2 = new Scanner(myObj2);
			long begin2 = System.currentTimeMillis();
			if (myReader2.hasNextLine()) {
				String data = myReader2.nextLine();
				avl_tree.add(data);

			}
			while (myReader2.hasNextLine()) {
				String data = myReader2.nextLine();
				String[] inputs = data.split(" ");

				if (inputs[0].equals("ADDNODE")) {
					avl_tree.add(inputs[1]);

				} else if (inputs[0].equals("DELETE")) {
					avl_tree.delete(inputs[1]);
				} else if (inputs[0].equals("SEND")) {
					avl_tree.send(inputs[1], inputs[2]);
				}

			}
			long end2 = System.currentTimeMillis();
			dt2 = end2 - begin2;
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

//		try {
//
//			File myObj = new File("time_difference.txt");
//			if (myObj.createNewFile()) {
//				System.out.println("File created: " + myObj.getName());
//
//			} else {
//				System.out.println("File already exists.");
//			}
//		} catch (
//
//		IOException e) {
//			System.out.println("An error occurred.");
//			e.printStackTrace();
//		}
		try {
			FileWriter myWriter = new FileWriter((java.lang.String) "time_difference.txt", true);

			myWriter.write(Long.toString(dt2) + "\n");
			myWriter.write(Long.toString(dt1));
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
//		System.out.println(dt1);System.out.println(dt2);
	}
}
