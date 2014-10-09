package com.apps.knowledgeRepo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.dataModel.Answer;
import com.apps.knowledgeRepo.utils.CourseUtil;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAnswerModeActivity extends Activity{
	public final static String EXTRA_MESSAGE = "com.apps.knowledagerepo.MESSAGE";
	public final static char CHOICE_A= 'A';
	
	private int questionNumber = -1;
    private Exam exam = null;
    private List<Integer> inCorrectList=null;
    private int inCorrectListIndex=0;
    private boolean viewInCorrectMode = false;
    
    private String examId;
    private String courseId;
    private String moduleId;
    
    private List<RadioButton> choiceList; 
    
    public void initilizeExam() {
    	
    	//final WebView questionText = (WebView) findViewById(R.id.questionPractice);
        final RadioButton choiceA = (RadioButton) findViewById(R.id.choiceAPractice);
        final RadioButton choiceB = (RadioButton) findViewById(R.id.choiceBPractice);
        final RadioButton choiceC = (RadioButton) findViewById(R.id.choiceCPractice);
        final RadioButton choiceD = (RadioButton) findViewById(R.id.choiceDPractice);
        choiceList = new ArrayList<RadioButton>();
        choiceList.add(choiceA);
        choiceList.add(choiceB);
        choiceList.add(choiceC);
        choiceList.add(choiceD);

        TextView titleView = (TextView) findViewById(R.id.practiceModeExamName);
        titleView.setText(exam.getName());
        
 		addListenerOnJumpToButton();
 		
        addListenerOnPrevAndNextButton();
        //parseExam();

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
        	examId = extras.getString("examId");
        	courseId = extras.getString("courseId");
        	moduleId = extras.getString("moduleId");
        	
        	exam = CourseUtil.initilizeExam(courseId, moduleId, examId, getApplicationContext());
    	    //exam = (Exam) extras.getSerializable("exam");
    	    
        }
        if(exam==null) throw new RuntimeException("Exam is null");

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
			if(questionNumber >= exam.getQuestions().size()-1) {
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
    /*
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
    }*/
    
    private void refreshPage() {
    	
        final TextView qnumText = (TextView) findViewById(R.id.singleChoicePracticeNumber);
        qnumText.setText((questionNumber+1)+"/"+exam.getQuestions().size() );
        
        final EditText goToNumber = (EditText) findViewById(R.id.jumpToTextPractice);
        goToNumber.setText("");
    	setQuestionText(questionNumber);
    	setAnswerText(questionNumber);
    	List<Answer> answerList= exam.getQuestions().get(questionNumber).getAnswers();
    	
    	
    	//TODO make sure there's at least one correct answer
    	for(Answer answer : answerList ) {
    		if(answer.getScore() ==1) {
    			String a = ""+(char)(answer.getAnswerNumber()+'A'-1);
    			setRadioButtonChecked(a);
    			break;
    		}
    	}
    	//String answer = ;
        
        
        
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
    	List<Answer> answerList = exam.getQuestions().get(questionNumber).getAnswers();
    	String answerNum=" ";
    	for(Answer answer : answerList ) {
    		if(answer.getScore() ==1) {
    			answerNum = ""+(char)(answer.getAnswerNumber()+'A'-1);
       			break;
    		}
    	}
    	answerText.setText(Html.fromHtml("Correct Answer is "+answerNum+"<br>"+exam.getQuestions().get(questionNumber).getExplanation() ));
    	//answerText.setTextColor(0xff);
    }
    private void setQuestionText(int questionNumber) {
    	final WebView questionText = (WebView) findViewById(R.id.questionPractice);
        
        questionText.loadData((questionNumber+1)+". "+ exam.getQuestions().get(questionNumber).getText().trim(),"text/html","utf-8");
        //questionText.setText(Html.fromHtml( (questionNumber+1)+". "+ exam.getQuestions().get(questionNumber).getText()));
        for(int i=0; i< choiceList.size(); ++i) {
        	//Log.d("Choice","size of choiceList  is "+choiceList.size());
        	choiceList.get(i).setVisibility(View.INVISIBLE); 
        }
        for(int i=0; i< exam.getQuestions().get(questionNumber).getAnswers().size(); ++i) {
        	Log.d("Choice","size of choiceList  is "+choiceList.size());
        	char c = (char) ('A'+i);
        	choiceList.get(i).setText(Html.fromHtml(c+". "+exam.getQuestions().get(questionNumber).getAnswers().get(i).getAnswerText() )); 
        	choiceList.get(i).setVisibility(View.VISIBLE); 
        }
    }
    
    private void addListenerOnJumpToButton() {
 	   final Button buttonJump = (Button) findViewById(R.id.jumpToButtonPractice);
        buttonJump.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               final EditText goToNumber = (EditText) findViewById(R.id.jumpToTextPractice);
     		   int upperBound = exam.getQuestions().size();  

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
