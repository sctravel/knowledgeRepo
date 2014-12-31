package com.apps.knowledgeRepo;

import java.util.ArrayList;
import java.util.List;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.activityHelper.OnSwipeTouchListener;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.dataModel.ExamAnswer;
import com.apps.knowledgeRepo.utils.CourseUtil;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAnswerModeActivity extends Activity{
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
    //private int answerValue=0;

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

        setTitle(exam.getName());
 		addListenerOnJumpToButton();
 		
        addListenerOnPrevAndNextButton();
        addListernerOnGuesture();

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
        if(exam==null || exam.getQuestions().isEmpty()) {
        	Toast.makeText(getApplicationContext(), "This exam is Empty. ", Toast.LENGTH_LONG).show();
			finish();
			return;
        }

        setContentView(R.layout.practice_mode);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // If your minSdkVersion is 11 or higher, instead use:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
           //getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initilizeExam();
    }
    
    /*
    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        // Do your calculations
        return super.dispatchTouchEvent(ev);
    }*/
    
    private void addListernerOnGuesture() {
    	final View rootView = (View) findViewById(R.id.practiceModeScrollView);
    	final WebView webView = (WebView) findViewById(R.id.questionPractice);

    	OnSwipeTouchListener listener = new OnSwipeTouchListener(getApplicationContext()){
    		@Override
    	    public void onSwipeLeft() {
    			final Button buttonPrev = (Button) findViewById(R.id.previousButtonPractice);
    			if(buttonPrev.isEnabled()) {
    				buttonPrev.performClick();
    			} else {
    	    		Toast.makeText(getApplicationContext(), "Button Prev is disabled.", Toast.LENGTH_LONG).show();
    			}
    	    }
    		@Override
    	    public void onSwipeRight() {
    			final Button buttonNext = (Button) findViewById(R.id.nextButtonPractice);
    			if(buttonNext.isEnabled()) {
    				buttonNext.performClick();
    			} else {
    	    		Toast.makeText(getApplicationContext(), "Button Next is disabled.", Toast.LENGTH_LONG).show();
    			}
    	    }
    	};
    	
    	rootView.setOnTouchListener(listener);
    	webView.setOnTouchListener(listener);
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
  
    
    private void refreshPage() {
    	
        final TextView qnumText = (TextView) findViewById(R.id.singleChoicePracticeNumber);
        qnumText.setText((questionNumber+1)+"/"+exam.getQuestions().size() );
        
        final EditText goToNumber = (EditText) findViewById(R.id.jumpToTextPractice);
        goToNumber.setText("");
    	setQuestionText(questionNumber);
    	setAnswerText(questionNumber);
    	List<ExamAnswer> answerList= exam.getQuestions().get(questionNumber).getAnswers();
    	
    	
    	//TODO make sure there's at least one correct answer
    	for(ExamAnswer answer : answerList ) {
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
    	List<ExamAnswer> answerList = exam.getQuestions().get(questionNumber).getAnswers();
    	String answerNum=" ";
    	//int answerNo=0;
    	for(ExamAnswer answer : answerList ) {
    		if(answer.getScore() ==1) {
    			answerNum = ""+(char)(answer.getAnswerNumber()+'A'-1);
       			break;
    		}
    	}
    	
        
    	//choiceList.get(answerNo-1).setBackgroundColor(Color.RED);;
        answerText.setText(Html.fromHtml("<b><font color=\"red\">Correct Answer is "+answerNum+"</font><b> <br>"+exam.getQuestions().get(questionNumber).getExplanation() ));
    	 //answerText.setTextColor(0xff);
    }
    private void setQuestionText(int questionNumber) {
    	final WebView questionText = (WebView) findViewById(R.id.questionPractice);
        
        questionText.loadData((questionNumber+1)+". "+ exam.getQuestions().get(questionNumber).getText().trim(),"text/html","utf-8");
        questionText.setBackgroundColor(Color.TRANSPARENT);

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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exam_mode_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners
        
       // MenuItem shareItem = menu.findItem(R.id.action_share);
       // mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
       // mShareActionProvider.setShareIntent(getDefaultIntent());
        
     // When using the support library, the setOnActionExpandListener() method is
        // static and accepts the MenuItem object as an argument
        MenuItemCompat.setOnActionExpandListener(searchItem, new OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    
    //TODO: Need to remove unnecessary menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_back_to_main_menu:
                backToMainMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void backToMainMenu() {
    	//Intent intent = new Intent(ExamModeActivity.this, ModeSelectionActivity.class);				       
	    //startActivity(intent);
    	this.finish();
		System.out.println("returnToMainMenuButton!");
    }
}
