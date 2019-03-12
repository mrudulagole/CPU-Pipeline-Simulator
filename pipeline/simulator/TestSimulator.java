package cao.pipeline.simulator;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class TestSimulator {
	static CodeMemory code_memory;
	static DataMemory data_memory;
	
	static Pipeline pipeline=new Pipeline();
	static String file="";
	public static void main(String[] args)throws IOException {
		String input="";
		DataInputStream d=new DataInputStream(System.in);
		file=args[0];
		while(true) {
			System.out.println("List of commands");
			System.out.println("1. initialize");
			System.out.println("2. simulate n");
			System.out.println("3. display");
			System.out.println("4. exit");
			input=d.readLine();
			if(input.equalsIgnoreCase("initialize")) {
				pipeline=new Pipeline();
				pipeline.initializePipeline();
				pipeline.writeFiletoCodeMemory();
				System.out.println("Pipeline initialized...");
			//	pipeline.displayCodeMem();
			}
			else if(input.contains("simulate")) {
				String split[]=input.split(" ");
				if(split[0].equalsIgnoreCase("simulate") && Utils.isValidInteger(split[1])) {
					pipeline.simulate(Integer.parseInt(split[1]));
				}
				else
					System.out.println("Invalid input");
				
				
				
				
			}
			else if(input.equalsIgnoreCase("display")) {
				pipeline.display();
				
			}
			else if(input.equalsIgnoreCase("exit")) {
				System.exit(0);
				
			}
			else {	
				System.out.println("Invalid command");
			}
			
			
			
		}
		
		
		
	}
	
	

}
