// Kou Vang
// Final Assignment
// ICS 240

// FILE: AnimalGuess.java
// This animal-guessing program illustrates the use of the binary tree node class.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

/******************************************************************************
* The <CODE>AnimalGuess</CODE> Java application illustrates the use of
* the binary tree node class is a small animal-guessing game.
*
* <p><dt><b>Java Source Code for this class:</b><dd>
*   <A HREF="../applications/Animals.java">
*   http://www.cs.colorado.edu/~main/applications/Animals.java
*   </A>
*
* @author Michael Main 
*   <A HREF="mailto:main@colorado.edu"> (main@colorado.edu) </A>
*
* @version
*   Jul 22, 2005
*   
*   To populate the knowledge tree, the very first line will be the subject title.
*   Every line afterwards will begin with an integer (1 to signify an internal (question)
*   node or (0) to signifiy an external (guess or leaf) node. This is followed by a single space
*   and the remainder of the line will be the question or guess. The tree is stored in preorder
*   traversal.
******************************************************************************/
public class AnimalGuess
{
   private static Scanner stdin = new Scanner(System.in);
   public static BufferedReader br = null;
   public static String line;
   private String subject;		
   private Node root;			
   
   

   /** Constructor 
    *
    *  @param file the file to read the game tree from
    */
   
	public AnimalGuess(BufferedReader file) throws IOException
	{
	    subject = file.readLine();
	    root = readTree(file);
	}

	/** Save the (possibly modified) game tree to a file
	 *
	 *  @param file the file to save the game tree to
	 */
	 
	public void saveTree(PrintWriter file) throws IOException
	{
	    file.println(subject);
	    writeTree(file, root);
	}

	public void playGame() throws IOException
	{
	    Node current = root;
	    
	    if (!askYesNo(subject))
	    	return;
	    
	    while (current.isQuestion())
	    {
	        if (askYesNo(current.getQuestion()))
	            current = current.getYesBranch();
	        else
	            current = current.getNoBranch();
	    }

	    if (!askYesNo(current.getGuess()))
	    {
	    	// Wrong guess - find out what user was thinking of
	    	// and get a new question for future use.

			String userAnswer, userQuestion;
		
			System.out.print("I give up, what were you thinking of? "); 
			userAnswer = consoleIn.readLine();
			System.out.println("Please enter a yes/no question that would distinguish a(n) "
		    	+ userAnswer + " from a(n) " + current.getGuess() + ".");
			userQuestion = consoleIn.readLine();
		
			// Extend the tree appropriately
		
			if (askYesNo("For a(n) " + userAnswer + " the answer would be"))
		    	current.convertToQuestion(userQuestion, 
		    		new Node(current.getGuess()), new Node(userAnswer));
			else
		    	current.convertToQuestion(userQuestion,
		    		new Node(userAnswer), new Node(current.getGuess()));
	    }
	    else
	    {
	    	System.out.println("Yay I win! I knew it all along.");
	    }
	}
   
   /**
   * The main method prints instructions and repeatedly plays the 
   * animal-guessing game. As the game is played, the taxonomy tree
   * grows by learning new animals. The <CODE>String</CODE> argument
   * (<CODE>args</CODE>) is not used in this implementation.
   **/
   public static void main (String[ ] args) throws IOException
   {
	    // Access file containing initial knowledge base
	   
	    System.out.print("Please enter the file name to be read (e.g. tree_1.txt): ");
	    
	    String filename;
	    filename = consoleIn.readLine();
	    
	    System.out.println("Think of an animal and I will try to guess it!");
	    
	    BufferedReader knowledgeIn = 
	    	new BufferedReader(new FileReader("C://ics240//" + filename));
	    
	    // Create the game
	    
	    AnimalGuess theGame = new AnimalGuess(knowledgeIn);
	    knowledgeIn.close();

	    // Play the game as often as the user wants
	    
	    do
	    {
	        theGame.playGame();
	    }
	    while (askYesNo("Shall we play again"));

	    // Offer opportunity to save the knowledge base to a file

	    System.out.print("Please enter a name for the file to be saved as (e.g. tree_2.txt): ");
	    filename = consoleIn.readLine();
	    
	    if (filename.length() > 0)
	    {
	        PrintWriter knowledgeOut = new PrintWriter(new FileWriter("C://ics240//" + filename));
	        theGame.saveTree(knowledgeOut);
	        knowledgeOut.close();
	    }
	    
	    System.exit(0);
	}
   
   /* Private methods - auxiliary to public methods above */
   
	/** Read a tree stored in preorder in a file
	 *
	 *	@param file the file to read from
	 *	@return root of resultant tree
	 */
	 
   private static Node readTree(BufferedReader file) throws IOException
	{
	    // Read the information for this node
	    
	    boolean isQuestion = ((char) file.read() == '1');
	    file.skip(1);		// Skip over single blank space
	    String contents = file.readLine();
	    
	    // Construct the node, reading subtrees recursively if needed
	    
	    if (isQuestion)
	    {
	        Node ifNo = readTree(file);
	        Node ifYes = readTree(file);
	        return new Node(contents, ifNo, ifYes);
	    }
	    else
	    	return new Node(contents);
	}
   
   
   /** Write a tree to a file in preorder
	 *
	 *	@param file the file to write to
	 *	@param root the root of the tree
	 */
	 
	private static void writeTree(PrintWriter file, Node root) throws IOException
	{
	    file.print(root.isQuestion() ? 1 : 0);
	    file.print(" ");
	    if (root.isQuestion())
	    {
			file.println(root.getQuestion());
	        writeTree(file, root.getNoBranch());
	        writeTree(file, root.getYesBranch());
	    }
	    else
	    	file.println(root.getGuess());
	}
	
	/** Ask the user a yes-no question
	 *
	 *	@param question the question to ask
	 *	@returntrue if user answers yes, false if no
	 *
	 *  (reprompt the user if the answer is not recognizable.)
	 */
	 
	private static boolean askYesNo(String question) throws IOException
	{
	    String answer;
	    do
	    {
			// Ask the user the question, read answer, convert to all caps
			
			System.out.print(question + "? ");
			answer = consoleIn.readLine();
			
			// Check to see if answer was yes or no.  If so, return appropriate
			// value - else ask again.
			
			if (answer.equalsIgnoreCase("YES".substring(0, answer.length())))
			    return true;
			else if (answer.equalsIgnoreCase("NO".substring(0, answer.length())))
			    return false;
			else
			    System.out.println("Please answer yes or no");
	    }
	    while (true);
	}

	/** The game tree is composed of two kinds of nodes - question
	 *	(internal) nodes and guess (leaf) nodes.  The content of
	 *	a question node is the question to ask; of a guess node,
	 *	the answer to propose.  A guess node can be turned into a
	 * 	question node when a guess fails
	 */
	 
	private static class Node
	{
		/** Constructor for a question node
		 *
		 *	@param question the question to ask
		 *	@param ifNo the subtree to go into if user answers no
		 *	@param ifYes the subtree to go into if user answers yes
		 */
		 
		Node(String question, Node ifNo, Node ifYes)
		{
			isQuestion = true;
			contents = question;
			this.lchild = ifNo;
			this.rchild = ifYes;
		}		

		/** Constructor for a guess node
		 *
		 *	@param guess the guess to try
		 */
		
		Node(String guess)
		{ 
			isQuestion = false;
			contents = guess;
			lchild = null;
			rchild = null;
		}
		
		/** Accessor for whether a node represents a question or a guess
		 *
		 *	@return true if a question, false if a guess
		 */
		 
		boolean isQuestion()
		{ 
			return isQuestion; 
		}
		
		/** Accessor for question stored in a node.
		 *  Precondition: the node represents a question
		 *
		 *	@return the question stored
		 */
	
		String getQuestion()
		{
			return contents;
		}
		
		/** Accessor for "no" branch from a question node.
		 *  Precondition: the node represents a question
		 *
		 *	@return root of the "no" branch
		 */
		
		Node getNoBranch()
		{
			return lchild;
		}
		
		/** Accessor for "yes" branch from a question node.
		 *  Precondition: the node represents a question
		 *
		 *	@return root of the "yes" branch
		 */
		
		Node getYesBranch()
		{
			return rchild; 
		}
		
		/** Accessor for guess stored in a node.
		 *  Precondition: the node represents a guess
		 *
		 *	@return the guess stored
		 */
	
		String getGuess()
		{ 
			return contents;
		}
		
		/** Convert a guess node to a question node
		 *	Precondition: the node currently represents a guess
		 *
		 *	@param question the question to ask
		 *	@param ifNo the subtree to go into if user answers no
		 *	@param ifYes the subtree to go into if user answers yes
		 */

		void convertToQuestion(String question, Node ifNo, Node ifYes)
		{
	 	    isQuestion = true;
		    contents = question;
		    lchild = ifNo;
		    rchild = ifYes;
		}
		
		/* Instance variables of a Node */
		
	    private boolean isQuestion;	// True for question, false for guess
	    private String contents;	// Question or quess as the case may be
	    private Node lchild, rchild;// "No" and "Yes" branches for a question
	 }
	 
	 /* Wrap System.in in a BufferedReader object so we can use readLine(),
	  * etc. on it.
	  */
	
	private static BufferedReader consoleIn = 
		new BufferedReader(new InputStreamReader(System.in));

}

