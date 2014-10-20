package edu.gatech.scs.pwd.hardening;
import java.math.*;
import java.io.File;
import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.nio.file.*;
import au.com.bytecode.opencsv.CSVReader;

public class LoginAttempt{

	
	BigInteger[] featureValuesReceived = new BigInteger[16];
	private String fileDir = ""; 
	public String pwd;
	BigInteger mean;
	String[] array1 =new String[100];
	BigInteger[] thresholdValues = new BigInteger[16];
	
	public BigInteger[] loginAttempt() throws NumberFormatException, IOException{
		
		//Generating a string that denotes the path to the directory of loginfeaturevalues.txt file. 
		fileDir = new String("loginAndThresholdFeatureValues"
                + File.separator);
		
		
		
		//Using an external library to parse through the CSV file of login features that we pretend we received from the client.
	
			CSVReader reader = new CSVReader(new FileReader(fileDir + "loginfeaturevalues.txt"));
			
			
			//To  temporarily read the lines from the file
		    String [] nextLine;
		    
		    
		    //Iterating through the lines
		    while ((nextLine = reader.readNext()) != null) {
		        // nextLine[] is an array of values from the line
		        	    	
		    	//Extracting password and featurevalues from the loginfeaturevalues.txt file and storing them in an integer array.
		    	
		    	pwd = nextLine[15]; //Password is the last value in the loginfeaturevalues csv file
		    	System.out.println(pwd);
		    	
		    	for(int i=0; i<15; i++){
		    
		    			featureValuesReceived[i+1] = BigInteger.valueOf(Integer.parseInt(nextLine[i]));
		    			System.out.println(featureValuesReceived[i+1]);
		    	}
		    }
			
		    reader.close();
				
		return featureValuesReceived;
		
		
	}
	
	
	
	//For reading threshold values and storing in an array of integers
	public BigInteger[] fetchThresholdValues() throws NumberFormatException, IOException{
		
		//Generating a string that denotes the path to the directory of thresholdvalues.txt file. 
		fileDir = new String("loginAndThresholdFeatureValues"
                + File.separator);
		
		
			//Using an external library to parse through the CSV file of threshold values
			CSVReader reader = new CSVReader(new FileReader(fileDir + "threshold.txt"));
						
			
			//To  temporarily read the lines from the file
		    String [] nextLine;
		    
		    //Iterating through all the lines in the file
		    while ((nextLine = reader.readNext()) != null) {
		        // nextLine[] is an array of values from the line
		   		    	
		    	
		    	//Extracting thresholdvalues from the thresholdvalues.txt file and storing them in an integer array.
		    	for(int i=0; i<15; i++){
		    		
		    			thresholdValues[i+1] = BigInteger.valueOf(Integer.parseInt(nextLine[i]));
		    			System.out.println("\n"+thresholdValues[i+1]);
		    	}
		    }
			
			
		    reader.close();
		
		return thresholdValues;
	}
	
	
	
	
	//Calculate mean of feature values by fetching them from the history file
	public BigInteger calculateMean(int feature){
		mean = BigInteger.valueOf(0);
		try
		{
		File file = new File("Dec_History_File.txt");
		BufferedReader File = new BufferedReader(new FileReader(file));
		String[] array =new String[100];

		int count=0;
		String line= null;
		while((line=File.readLine())!= null)
		{
			if(count<8)
			{
			array = line.split(" ");
			//System.out.println(array[feature-1]);
			array1[count+1]=array[feature-1];
			count++;
			}
			else break;
		}

		for(int i=1;i<=8;i++)
		{
			BigInteger no= BigInteger.valueOf(Integer.parseInt(array1[i]));
			System.out.println(no);
			mean=mean.add(no);
		}
		mean=mean.divide(BigInteger.valueOf(8));
		}
		catch(FileNotFoundException ex){
				ex.printStackTrace();}
				catch (IOException ex){
				ex.printStackTrace();
				}
		System.out.println("Mean for feature "+feature+" is "+mean);
		return mean;
		
	}
	
	
	//Calculate median of feature values by fetching them from the history file
	public BigInteger calculateDev(int feature){
		/*try{
		File File = new File("Dec_History_File.txt");
		List<String> lines = Files.readAllLines(File.toPath());
		for(String line : lines)
		{
			String[] array = line.split(" ");
			//System.out.println(array[feature-1]);
		}

		}
		catch(FileNotFoundException ex){
				ex.printStackTrace();}
				catch (IOException ex){
				ex.printStackTrace();
				}*/
		return BigInteger.valueOf(1);
	}
	
	

	
	//To calculate the y co-ordinates
	public BigDecimal calculateHpwdnew(BigInteger[] y,BigInteger[] x, BigInteger q){
		
		/*BigDecimal hpwd=new BigDecimal(0);
		System.out.println("Q is"+q);
		for(int i =0;i<15;i++)
		{
			hpwd=hpwd.add((new BigDecimal(y[i]).multiply(calculateLagrangeMultiplier(x[i],i,x)).remainder(new BigDecimal(q))));
		}
		return hpwd;*/
		BigDecimal hpwd =  new BigDecimal(0);
		for(int i=1;i<=15;i++)
		{
			BigDecimal mul =  new BigDecimal(1);
			for(int j=1;j<=15;j++)
			{
				if(j!=i)
				{
					//System.out.println(mul);
					//System.out.println("j and i are "+j+" and "+i);
					//System.out.println("Value of x[j] and x[i] is "+x[j]+" and "+x[i]);
					BigDecimal div_part= new BigDecimal(x[j]).subtract(new BigDecimal(x[i]));
					BigDecimal lambda=new BigDecimal(x[j]).divide(div_part,5,RoundingMode.HALF_UP);
					mul=mul.multiply(lambda);	

				}
			}
			hpwd=hpwd.add(mul.multiply(new BigDecimal(y[i])).remainder(new BigDecimal(q)));
			//System.out.println("Hardened Password after round "+i+" is "+ hpwd);	
		}
		return hpwd;
	}

	public BigInteger calculateHpwdnew1(BigInteger[] y,BigInteger[] x, BigInteger q){
		

		BigInteger hpwd =  BigInteger.valueOf(0);
		for(int i=1;i<=15;i++)
		{
			BigInteger mul =  BigInteger.valueOf(1);
			for(int j=1;j<=15;j++)
			{
				if(j!=i)
				{
					//System.out.println("j and i are "+j+" and "+i);
					//System.out.println("Value of x[j] and x[i] is "+x[j]+" and "+x[i]);
					BigInteger div_part= x[j].subtract(x[i]);
					mul=mul.multiply(x[j].divide(div_part));	
				}
			}
				//System.out.println(mul);
				hpwd=hpwd.add(mul.multiply(y[i]).mod(q));	
		}
		return hpwd;
	}
	
	public void History_File_Update(int line_no,BigInteger[] featureValuesReceived)
	{

		try {
			    File inFile = new File("Dec_History_File.txt");

			    //Construct the new file that will later be renamed to the original filename.
			    File tempFile = new File("His.tmp");

			    BufferedReader br = new BufferedReader(new FileReader("Dec_History_File.txt"));
			    PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			    String line = null;

			    //Read from the original file and write to the new
			    //unless content matches data to be removed.
			    while ((line = br.readLine()) != null) {
				if(line_no>0)
				{
					pw.print(line+"\n");
					pw.flush();
					line_no--;
				}
				else if(line_no==0)
				{
					line= "";
					for(int i=1;i<16;i++)
					{
						System.out.println(featureValuesReceived[i]);
						line+=featureValuesReceived[i]+" ";
					}
					pw.print(line+"\n");
					pw.flush();
					line_no--;
				}
				else
				{
					pw.print(line+"\n");
					pw.flush();
				}
			    }
			    pw.close();
			    br.close();

			    //Delete the original file
			    if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			    }

			    //Rename the new file to the filename the original file had.
			    if (!tempFile.renameTo(inFile)) System.out.println("Could not rename file");
			}
				catch(FileNotFoundException ex){
				ex.printStackTrace();}
				catch (IOException ex){
				ex.printStackTrace();
				}
			

        }
		/*
		try{
		if(line_no<8)
		{
			FileOutputStream File = new FileOutputStream("Dec_History_File.temp");
			BufferedReader reader= new BufferedReader(new FileReader("Dec_History_File.txt"));
			String line;
			while(reader.readLine()!=null)
			{
			
				while(line_no!=0)
				{
					System.out.println(line_no);
					line=reader.readLine();
					line_no--;
					System.out.println(line);
					File.write(line.getBytes());
				}
				if(line_no==0)
				{
				for(int i=1;i<16;i++)
				{
					System.out.println(featureValuesReceived[i]);
					File.write(featureValuesReceived[i].toByteArray());

				}
					reader.readLine();
					line_no=-1;
				}
				line=reader.readLine();
				File.write(line.getBytes());


			}
				File.close();
				reader.close();
				new File("Dec_History_File.temp").renameTo(new File("Dec_history_text1.txt"));
		}
		}
		catch(Exception e){
			e.printStackTrace();}*/


	//Calculating lagrange multiplier
	/* public BigDecimal calculateLagrangeMultiplier(BigInteger xi,int i, BigInteger[] x){
		
		BigDecimal LagrangeMultiplier=new BigDecimal(1);
		for(int j=1;j<=15;j++)
		{	
			if(j==i)
			{
				//Do nothing
				System.out.println("Catching j=i");
			}
			else
			{
			System.out.println("Value of x[j] and x[i] is "+x[j]+" and "+x[i]);
			System.out.println("Subtraction of x[j] and x[i] is "+x[j].subtract(x[i]));
			BigDecimal div_part= new BigDecimal(x[j]).subtract(new BigDecimal(x[i]));
			LagrangeMultiplier=LagrangeMultiplier.multiply(new BigDecimal(x[j]).divide(div_part,3,RoundingMode.HALF_UP));
			}
		}
		//System.out.println("LagrangeMultiplier for i = "+i+" is "+LagrangeMultiplier+".");
		return LagrangeMultiplier;
		
		
	}*/
	
	
}
