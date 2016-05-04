import java.io.*;
import java.io.FileNotFoundException;
import java.util.*;



class parser 
{
	public static String input_token;
	public static String[] tokens,temp;
	public static int counter=0,j;
	public static void main(String[] args) 
	{
		// write code
		String fileName = "C:\\Users\\Teja\\Desktop\\teja.txt";
        String line = "";
		String token = "";
		char c;
        try {
        		BufferedReader br = new BufferedReader(new FileReader(fileName));
        		ArrayList<String> list=new ArrayList<String>();  //creating new generic arraylist  
        		while((line = br.readLine())!=null){
        			temp =  line.split(" ");
        			for(j=0;j<temp.length;j++)
        			{
        				list.add(temp[j]);	
        			}
        		}
        		br.close();
				//System.out.println("ArrayList:"+list);
				//converting array list to object array 
				Object obj[]=list.toArray();
				
				//converting object array to string array
				tokens= Arrays.copyOf(obj, obj.length, String[].class);
				
				// for(String s:tokens)
				// System.out.println(s);

				for(int i=0;i < tokens.length; i++)
				{
					token = tokens[i];
					if(token.length() == 1)
					{
						c =  tokens[i].charAt(0);
						if (Character.isDigit(c))
						{
							tokens[i] = "number";
						}
						else if (Character.isLetter(c))
						{
							tokens[i] = "id";
						}
					}
					else if (token.length() > 1)
					{
						if (token.equalsIgnoreCase("read"))
						{
							tokens[i] = "read";
						}
						else if (token.equalsIgnoreCase("write"))
						{
							tokens[i] = "write";
						}
						else if (token == "$$")
						{
							tokens[i] = "$$";
						}
					}
					//System.out.println(tokens[i]);
				}
				input_token = tokens[0];
				program(input_token);    
	
	        }
	        catch(FileNotFoundException ex) {
	            System.out.println("Unable to open file '" + fileName + "'");                
	        }
	        catch(IOException ex) { 
	           ex.printStackTrace();
	        }
	}


	public static void program(String input_token)
	{
		if (input_token.equalsIgnoreCase("read")||input_token.equalsIgnoreCase("write")||input_token.equalsIgnoreCase("id")||input_token.equalsIgnoreCase("$$"))
		{
			input_token = stmt_list(input_token);
			input_token = match("$$");
			System.out.println("valid input");
		}
		else
		{
			System.out.println("Tokenizer error : Desired token is (id,read,write,number or $$) \n                  Actual  token is ("+input_token+")");
			System.out.println("Error in procedure \"Invalid token\"");
			System.exit(0);
		}
	}

	public static String stmt_list(String input_token)
	{
		if (input_token.equalsIgnoreCase("read")||input_token.equalsIgnoreCase("write")||input_token.equalsIgnoreCase("id"))
		{
			input_token = stmt(input_token);
			input_token = stmt_list(input_token);
		}
		else if(input_token.equalsIgnoreCase("$$"))
		{
			System.out.println("valid input. Program Ends");
			System.exit(0);
		}
		else
		{
			System.out.println("Tokenizer error : Desired token is (id,read,write or $$) \n                  Actual  token is ("+input_token+")");
			System.out.println("Error in procedure \"Stmt_list\"");
			System.exit(0);
		}
		return input_token;
	}


	public static String stmt(String input_token)
	{
		if (input_token.equalsIgnoreCase("id"))
		{
			input_token = match(input_token);
			input_token = match(":=");
			input_token = expr(input_token);
			
		}
		else if(input_token.equalsIgnoreCase("read"))
		{
			input_token = match("read");
			input_token = match("id");
		}
		else if(input_token.equalsIgnoreCase("write"))
		{
			input_token = match("write");
			input_token = expr(input_token);
		}
		else
		{
			System.out.println("Tokenizer error : Desired token is (id,read or write) \n                  Actual  token is ("+input_token+")");
			System.out.println("Error in procedure \"Stmt\"");
			System.exit(0);
		}
		return input_token;
	}

	public static String expr(String input_token)
	{
		if(input_token.equalsIgnoreCase("id")||input_token.equalsIgnoreCase("number")||input_token.equalsIgnoreCase("("))
		{
			input_token = term(input_token);
			input_token = term_tail(input_token);
		}
		else
		{
			System.out.println("Tokenizer error : Desired token is (id,number or () \n                  Actual  token is ("+input_token+")");
			System.out.println("Error in procedure Expr");
			System.exit(0);
		}
		return input_token;
	}

	public static String term_tail(String input_token)
	{
		if(input_token.equals("+")||input_token.equals("-"))
		{
			input_token = add_op(input_token);
			input_token = term(input_token);
			input_token = term_tail(input_token);
		}
		else if (input_token.equals(")")||input_token.equals("id")||input_token.equals("read")||input_token.equals("write")||input_token.equals("$$"))
		{
		}
		else
		{
			System.out.println("Tokenizer error : Desired token is (+,-,id,read,write,$$) \n                  Actual  token is ("+input_token+")");
			System.out.println("Error in procedure \"term_tail\"");
			System.exit(0);
		}
		return input_token;
	}

	public static String term(String input_token)
	{
		if(input_token.equals("id")||input_token.equals("number")||input_token.equals("("))
		{
			input_token = factor(input_token);
			input_token = factor_tail(input_token);
		}
		else
		{
			System.out.println("Tokenizer error : Desired token is (id,number or ( ) \n                  Actual  token is ("+input_token+")");
			System.out.println("Error in \"procedure Term\"");
			System.exit(0);
		}
		return input_token;
	}

	public static String factor_tail(String input_token)
	{
		if(input_token.equals("*")||input_token.equals("/"))
		{
			input_token = mult_op(input_token);
			input_token = factor(input_token);
			input_token = factor_tail(input_token);
		}
		else if(input_token.equals("+")||input_token.equals("-")||input_token.equals(")")||input_token.equals("id")||input_token.equals("read")||input_token.equals("write")||input_token.equals("$$"))
		{
		}
		else
		{
			System.out.println("Tokenizer error : Desired token is (+,-,id,read or write) \n                  Actual  token is ("+input_token+")"); 
			System.out.println("Error in procedure \"Factor_tail\"");
			System.exit(0);
		}
		return input_token;
	}

	public static String factor(String input_token)
	{
		if(input_token.equals("id"))
			input_token = match("id");
		else if(input_token.equals("number"))
			input_token = match("number");
		else if(input_token.equals("("))
		{
			input_token = match("(");
			expr(input_token);
			input_token = match(")");
		}
		else
		{
			System.out.println("Tokenizer error : Desired token is (id,number or ( ) \n                  Actual  token is ("+input_token+")");
			System.out.println("Error in procedure \"Factor\"");
			System.exit(0);
		}

		return input_token;

	}

	public static String add_op(String input_token)
	{
		if(input_token.equals("+"))
		{
			input_token = match("+");
		}
		else if (input_token.equals("-"))
		{
			input_token = match("-");
		}
		else 
		{
			System.out.println("Tokenizer error : Desired token is (+ or -) \n                  Actual  token is ("+input_token+")");
			System.out.println("Error in procedure \"add_op\"");
			System.exit(0);
		}
		return input_token;
	}

	public static String mult_op(String input_token)
	{
		if(input_token.equals("*"))
		{
			 input_token = match("*");
		}
		else if (input_token.equals("/"))
		{
			input_token = match("/");
		}
		else 
		{
			System.out.println("Tokenizer error : Desired token is (* or /) \n                  Actual  token is ("+input_token+")");
			System.out.println("Error in procedure \"Mult_op\"");
		}
		return input_token;
	}

	public static String match(String expected)
	{
		if (input_token.equalsIgnoreCase(expected))
		{
			if(counter < tokens.length-1)
			{
				counter=counter + 1;
				input_token=tokens[counter];
			}
		}
		else
		{
			System.out.println("Tokenizer error : Desired token is ("+expected+") \n                  Actual  token is ("+input_token+")");
			System.out.println("Error in procedure \"Match\"");
			System.exit(0);
		}
		return input_token;
	}

}