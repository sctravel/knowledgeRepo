package com.apps.knowledgeRepo.exams;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.apps.knowledgeRepo.om.SingleChoiceQuestion;
import com.apps.knowledgeRepo.om.SingleChoiceAnswer;




public class SingleChoiceExam {
	
	
	
	private List<SingleChoiceQuestion> questionList;
	private List<SingleChoiceAnswer> answerList;
	private int count;

	public SingleChoiceExam() {
		initilize();
	}
	
	public void initilize() {
		questionList = new ArrayList<SingleChoiceQuestion>();
		answerList = new ArrayList<SingleChoiceAnswer>();
		count = 0;
	}
	
	
	
	public List<SingleChoiceQuestion> getQuestionList() {
		return questionList;
	}
	public List<SingleChoiceAnswer> getAnswerList() {
		return answerList;
	}
	
	public int getCount() {
		return count;
	}

	public String readFromFile( InputStream inputStream ) throws IOException {
	    	//InputStream inputStream = null;
	    	BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

	    	try {
	    		br = new BufferedReader(new InputStreamReader(inputStream));
	        	System.err.println("Start bufferedreader ");
	    		String line;
	    		while ((line = br.readLine()) != null) {
	    			sb.append(line + "\n");
	    		}
	    		System.out.println("\nDone!");
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	} finally {
	    		if (inputStream != null) {
	    			try {
	    				inputStream.close();
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    		if (br != null) {
	    			try {
	    				br.close();
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    	}
	    	
	    	return sb.toString();
		}
	/*private String readFromFile( String file ) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        System.out.println(line);
	        stringBuilder.append( ls );
	    }

	    return stringBuilder.toString();
	}*/
	
	
	//This is for parsing single choice exam with 4 choices
	public void parseExam(final String questionFile, final String answerFile) {
		initilize();
		
		try {
					

			String exam = questionFile;			
			String exp = answerFile;
			
			final char          choiceA = 'A';

			String              sQuestionText, sExplanation;
			String              sQwork, sEwork;
			int                 index;
			String              sCorrectAnswer;
			int                 i = 0;
			int                 questionNumber = 0;
			int                 ContQuestionNumber = 0;
			boolean             quitloop = false;
			
			//Loop through and parse all the questions and answers one by one
			while(!quitloop) {
				
				SingleChoiceQuestion scq = new SingleChoiceQuestion();
				SingleChoiceAnswer sca = new SingleChoiceAnswer();

				
				questionNumber ++;
				String  delimit = questionNumber + ".";					
				index = exam.indexOf(delimit);
				if(index == -1){
					System.out.println("Error: Unable to find questionNumber:" + questionNumber);
					break;
				}									
				exam  = exam.substring(index + delimit.length(), exam.length());
				index = exam.indexOf((questionNumber + 1) + ".");					
				if(index == -1) {
					System.out.println("End of questions! quiz = " + i + " and questionNumber = " + questionNumber);
					index = exam.length();
					quitloop = true;
				}
				
				//Extract the question text
				sQwork = exam.substring(0, index).replace('\r', ' ').replace('\n', ' ').replace('\t',' ').trim();
				index  = sQwork.indexOf("A.");
				if(index == -1){
					System.out.println("Unable to find the Answer choice A for question " + questionNumber);
					return;
				}
				
				sQuestionText = delimit+" "+sQwork.substring(0, index);
				scq.setQuestion(sQuestionText); //Set question text
				
				sQwork = sQwork.substring(index, sQwork.length()); //Choices
				
				
				
				
				//Extract Answers
				index = exp.indexOf(delimit);
				if(index == -1) {
					System.out.println("Wrong in explanation! quiz = " + i + " and questionNumber = " + questionNumber);
					System.out.println("exp:" + exp.substring(0, 100));
					System.out.println("delimit:" + delimit);						
					return;
				}										

				exp   = exp.substring(index + delimit.length(), exp.length());
				index = exp.indexOf((questionNumber + 1) + ".");
				if(index == -1) {
					index = exp.length();
					if(index == -1){
						System.out.println("Error: Unable to find questionNumber:" + questionNumber);
						break;							
					}
				}
				sEwork = exp.substring(0, index).replace('\r', ' ').replace('\n', ' ').replace('\t',' ').trim();
				index  = sEwork.indexOf("(");					
				sEwork = sEwork.substring(index +1, sEwork.length());
				index  = sEwork.indexOf(")");
				if(index == -1){
						System.out.println("Error: Unable to find ) in the explanantion file. ");
						break;							
				}					
				sCorrectAnswer = sEwork.substring(0, index).toUpperCase();
				sExplanation = sEwork.substring(index +1, sEwork.length());
				if(questionNumber < ContQuestionNumber)
					continue;
				//Extract the four choices A. B. C. D.
				int choicenumber = 0;
				boolean endloopchoice = false;
				
				
				while(!endloopchoice) {						
					choicenumber++;		
					//System.err.println("-----------------------------------ChoiceNumber"+choicenumber);
					delimit = ((char)(choiceA + choicenumber -1)) + ".";						
					index = sQwork.indexOf(delimit);
					sQwork = sQwork.substring(index + 2, sQwork.length());
					
					index = sQwork.indexOf(((char)(choiceA + choicenumber)) + ".");
					if(index == -1) {
						index = sQwork.length();
						endloopchoice = true;
						if(delimit.charAt(0) != (choiceA + 3)) {
							System.out.println("Warning: The last choice is '" + delimit + "' in  quiz = " + i + " and questionNumber = " + questionNumber);
						}
					}
					String Choice = delimit+" "+sQwork.substring(0, index);
					scq.getChoices().add(Choice);
				}
				sca.setAnswer(sCorrectAnswer);
				sca.setExplaination(sExplanation);
				questionList.add(scq);
				answerList.add(sca);
			}			
			count = questionList.size();

			System.out.println("Final count is"+count);
			
		} catch (Exception e) { 
			System.err.println("Error in  quiz = " + questionFile + " and questionNumber = " + answerFile);
			e.printStackTrace();
		}
		
	}
	
	
}
