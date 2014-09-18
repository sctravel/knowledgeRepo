package com.apps.knowledgeRepo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.exams.SingleChoiceExam;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAnswerModeActivity extends Activity{
	public final static String EXTRA_MESSAGE = "com.apps.knowledagerepo.MESSAGE";
	public final static char CHOICE_A= 'A';
	
	private int questionNumber = -1;
    private SingleChoiceExam exam = null;
    private List<Integer> inCorrectList=null;
    private int inCorrectListIndex=0;
    private boolean viewInCorrectMode = false;
    
    public void initilizeExam() {
    	
 		addListenerOnJumpToButton();
 		
        addListenerOnPrevAndNextButton();
        parseExam();

	    questionNumber=0;
	    refreshPage();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	inCorrectList= extras.getIntegerArrayList("inCorrectList");
        	if(inCorrectList!=null && !inCorrectList.isEmpty()) {
        		viewInCorrectMode = true;
        	}
        }
        
        setContentView(R.layout.practice_mode);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // If your minSdkVersion is 11 or higher, instead use:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
           getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initilizeExam();
    }
    private void addListenerOnPrevAndNextButton() {
       	final Button buttonPrev = (Button) findViewById(R.id.previousButtonPractice);
           buttonPrev.setOnClickListener(new View.OnClickListener() {
        	   @Override
               public void onClick(View v) {
               	   prevQuestion();
                   refreshPage();
               }
           });
           final Button buttonNext = (Button) findViewById(R.id.nextButtonPractice);
           buttonNext.setOnClickListener(new View.OnClickListener() {
        	   @Override
               public void onClick(View v) {
               	   nextQuestion();
               	   refreshPage();    
               }
           }); 
    }
    public void nextQuestion() {
    	if(viewInCorrectMode) {
    		if(inCorrectListIndex < inCorrectList.size()-1) {
	    		++inCorrectListIndex;
	    		questionNumber = inCorrectList.get(inCorrectListIndex);
    		} else {
   			   Toast.makeText(getApplicationContext(), "This is the last one. ", Toast.LENGTH_LONG).show();

    		}
    	} else {
    		//The last Question
			if(questionNumber >= exam.getCount()-1) {
				return;
			}
		    ++questionNumber;		        	
    	}
    }
    
    //Get the previous questionNumber
    //Depends on the mode (isReviewMode)
    public void prevQuestion() {
    	if(viewInCorrectMode) {
    		if(inCorrectListIndex > 0) {
	    		--inCorrectListIndex;
	    		questionNumber = inCorrectList.get(inCorrectListIndex);
    		} else {
  			   Toast.makeText(getApplicationContext(), "This is the first one. ", Toast.LENGTH_LONG).show();
    		}
    	} else {
	    	if(questionNumber<=0) {
				return;
			}
			--questionNumber;
    	}
    	
    }
    
    private void parseExam() {
    	exam = new SingleChoiceExam();
    	String questionString = "none";
 		String answerString = "none";
    	try {
      	  //make it configuable  here
         	 InputStream question = getAssets().open("exams/exam1.txt");
         	 InputStream answer = getAssets().open("exams/exp1.txt");

  		    questionString = exam.readFromFile(question);
  		    answerString = exam.readFromFile(answer);

         } catch (FileNotFoundException e) {
         	  System.err.println("File not found ");
         } catch (IOException e) {
           	System.err.println("Error pasing file a");
         }
         
  	   exam.parseExam(questionString,answerString);
    }
    
    private void refreshPage() {
    	
        final TextView qnumText = (TextView) findViewById(R.id.singleChoicePracticeNumber);
        qnumText.setText((questionNumber+1)+"/"+exam.getCount());
        
        final EditText goToNumber = (EditText) findViewById(R.id.jumpToTextPractice);
        goToNumber.setText("");
    	setQuestionText(questionNumber);
    	setAnswerText(questionNumber);
    	String answer = exam.getAnswerList().get(questionNumber).getAnswer();
        setRadioButtonChecked(answer);
        
        
    }
    private void setRadioButtonChecked(String answer) {
    	   
    	RadioGroup radioGroup = (RadioGroup) findViewById(R.id.singleChoicePractice);
    	radioGroup.setEnabled(true);

    	radioGroup.clearCheck();
    	if(answer==null || answer.isEmpty()) return;
    	
    	int checkedButtonId=0;
    	switch (answer) {
    		case "A" :  checkedButtonId=R.id.choiceAPractice ;break;
    		case "B" :  checkedButtonId=R.id.choiceBPractice ;break;
    		case "C" :  checkedButtonId=R.id.choiceCPractice ;break;
    		case "D" :  checkedButtonId=R.id.choiceDPractice ;break;
    		default  :  checkedButtonId=0;
    	}
    	if(checkedButtonId !=0 ) {
    		radioGroup.check(checkedButtonId);
    		//radioGroup.
    	}
    	radioGroup.setEnabled(false);
    }
    
    private void setAnswerText( int  questionNumber) {
    	final TextView answerText = (TextView ) findViewById(R.id.checkAnswer);
    	answerText.setText("Correct Answer is "+exam.getAnswerList().get(questionNumber).getAnswer()+"\n"+exam.getAnswerList().get(questionNumber).getExplaination());
    	//answerText.setTextColor(0xff);
    }
    private void setQuestionText(int questionNumber) {
    	final TextView questionText = (TextView) findViewById(R.id.questionPractice);
        final TextView choiceA = (TextView) findViewById(R.id.choiceAPractice);
        final TextView choiceB = (TextView) findViewById(R.id.choiceBPractice);
        final TextView choiceC = (TextView) findViewById(R.id.choiceCPractice);
        final TextView choiceD = (TextView) findViewById(R.id.choiceDPractice);
        questionText.setText(Html.fromHtml(exam.getQuestionList().get(questionNumber).getQuestion()));
        choiceA.setText(Html.fromHtml(exam.getQuestionList().get(questionNumber).getChoices().get(0)));
        choiceB.setText(Html.fromHtml(exam.getQuestionList().get(questionNumber).getChoices().get(1)));
        choiceC.setText(Html.fromHtml(exam.getQuestionList().get(questionNumber).getChoices().get(2)));
        choiceD.setText(Html.fromHtml(exam.getQuestionList().get(questionNumber).getChoices().get(3)));
    }
    
    private void addListenerOnJumpToButton() {
 	   final Button buttonJump = (Button) findViewById(R.id.jumpToButtonPractice);
        buttonJump.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               final EditText goToNumber = (EditText) findViewById(R.id.jumpToTextPractice);
     		   int upperBound = exam.getCount();

         	   try{
         		   int jumpToNumber = Integer.parseInt(goToNumber.getText().toString())-1;
         		   if(jumpToNumber <0 || jumpToNumber >= upperBound) {
         			   Toast.makeText(getApplicationContext(), "Question Number should between: 1 - "+upperBound+". ", Toast.LENGTH_LONG).show();
         			   goToNumber.setText("");
         			   return;
         		   }
 	         	   questionNumber = jumpToNumber;
 	         	   refreshPage();
         	   } catch (Exception e) {
     			   Toast.makeText(getApplicationContext(), "Question Number should between: 1 - "+upperBound+". ", Toast.LENGTH_LONG).show();
         		   //logger..warn("Jump to Error",e);
         		   System.out.println("Jump to Error");
         		   e.printStackTrace();
         		   
         	   }
            }
        });
    }
}
