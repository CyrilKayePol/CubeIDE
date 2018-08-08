package cube.semantics.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cube.semantics.Type;
import cube.semantics.Variable;
import cube.semantics.blocks.Block;
import cube.semantics.blocks.ElsifBlock;
import cube.semantics.blocks.IfBlock;
import cube.semantics.blocks.MainBlock;

public class Print {
	
	/* ================= Used in Semantics Checker =====================*/
	
	public static void printMainBlockVariables(MainBlock block) {
		ArrayList<Variable> main_var = block.getSeenVariables();
		
		System.out.println(" ==== Main Variables =====");
		for(int i = 0; i < main_var.size(); i++) {
			System.out.print("\t ==== Name: " + main_var.get(i).getName() + " Value: ");
			Object val = main_var.get(i).getValue();
			
			if(val == null) {
				System.out.println("Null");
			}
			else {
				System.out.println(val.toString() + " Type: "+ main_var.get(i).getType());
			}
		}
	}
	
	public static void readFile(HashMap<Integer, String> line_hash) {
		int counter = 1;

            try {
            	 File file = new File("src/Cube.cube");
                 
                 if(file.isFile()) {
                     BufferedReader br = new BufferedReader(new FileReader(file));

                     String line;
                     while ((line = br.readLine()) != null) {
                    	 line = line.trim();
                    	 
                    	 line_hash.put(new Integer(counter), line);
                    	 counter++;
                     }
                     br.close();
                 } else {
                     System.out.println("Error: File does not exist!");
                 }
            }catch( IOException ex ) {
            	System.out.println("At catch clause of readFile method" + ex.getMessage());
            }
	}
	 /* ===================================================================================*/
	
	/* ============================ Used in Main Block ====================================*/
	public static void printMainBlock(ArrayList<Block> sub_blocks) {
		System.out.println(" ==== Main Blocks ==== ");
		for(int i = 0; i < sub_blocks.size(); i++) {
			Block b = sub_blocks.get(i);
			
			System.out.print("\t == Type: "+ b.getType() + " Start: "+ b.getStartline()+
					" End: "+ b.getEndline() + " MotherBlock: [ Type: "); 
			if(b.getMotherBlock() != null) {
				System.out.println(b.getMotherBlock().getType()+
						" Start: "+b.getMotherBlock().getStartline() + " End: "+
						b.getMotherBlock().getEndline() + "]");
			}
			else {
				System.out.println("Main is my mother block ]");
			}
		}
	}
	
	public static void printElsifs(ArrayList<Block>  sub_blocks) {
		System.out.println("===== ELSIFS ======");
		for(int i = 0; i < sub_blocks.size(); i++) {
			Block b = sub_blocks.get(i);
			
			if(b.getType() == Type.IF) {
				System.out.println("\tIF: [ Start: "+ b.getStartline() + " End: "+
						b.getEndline() + " ]");
				ArrayList<ElsifBlock> blocks = ((IfBlock) b).getElsifs();
				
				for(int j = 0; j < blocks.size(); j++) {
					System.out.println("\t\t==== Elsif: [Start: "+ blocks.get(j).getStartline()  +
							" End: "+ blocks.get(j).getEndline()+" ]");
				}
				
				if(((IfBlock) b).getElse() != null){
					System.out.println("\t\t==== Else: [Start: "+ ((IfBlock) b).getElse().getStartline() +
							" End: "+ ((IfBlock) b).getElse().getEndline() +" ]");
				}
			}
		}
	}
	
	public static void printBlockInformation(Block b) {
		System.out.println("\tBlock: [Type: "+ b.getType() + " Start: "+
				b.getStartline() + " End: "+b.getEndline()+" ]");
				for(int k = 0; k < b.getSubBlocks().size(); k++) {
					Block sblock = b.getSubBlocks().get(k);
					System.out.println("\t\tSub-block: [ Type: "+ sblock.getType() + 
							" Start: "+ sblock.getStartline() + " End: "+
							sblock.getEndline() + " ]");
				}
	}
	 /* ===================================================================================*/
	
}
