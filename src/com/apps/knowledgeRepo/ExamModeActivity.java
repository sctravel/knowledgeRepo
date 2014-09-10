package com.apps.knowledgeRepo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.exams.SingleChoiceExam;
import com.apps.knowledgeRepo.om.SingleChoiceAnswer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ExamModeActivity extends Activity{

	private static final Logger logger = Logger.getLogger("ExamModeActivity");
	private static final String TITLE_REVIEW_MODE = "Single Choice Exam - Review Mode";
	private static final String TITLE = "Single Choice Exam";

	public final static char CHOICE_A= 'A';
	private ShareActionProvider mShareActionProvider;

	//This is the current question number user is working on.
	private int questionNumber = -1;
    SingleChoiceExam exam = null;
    
    //store the answer of the question which user already finished 
    private Map<Integer, String> scoreMap = new HashMap<Integer, String>(); 
    
    //A list of questions numbers that are marked for review
    private List<Integer> reviewList = new ArrayList<Integer>();
    private boolean isReviewMode = false;
    private int storedQuestionNumber=0;
    
    public void initilizeExam() {
 		addListenerOnJumpToButton();
 		addListenerOnMarkForReviewCheckBox();
        addListenerOnPrevAndNextButton();
        parseExam();
	    questionNumber=0;
	    refreshPage();
    }
  //Review Mode can only see the questions marked for review

    public boolean getReviewMode() {
    	return this.isReviewMode;
    }
    public void setReviewMode(boolean isReviewMode) {
    	this.isReviewMode=isReviewMode;
    }
    
    public void nextQuestion() {
    	if(!isReviewMode) {
    		//The last Question
        	if(questionNumber >= exam.getCount()-1) {
        		//TODO need to go to the summary page instead
        		return;
        	}
    		++questionNumber;
    	} else {
    		if(reviewList.listIterator().hasNext()) {
    		   questionNumber = reviewList.iterator().next();
    		}
    	}
    	
    }
    public void prevQuestion() {
    	if(!isReviewMode) {
    		if(questionNumber<=0) {
    			return;
    		}
    		--questionNumber;
    	} else {
    		if(reviewList.listIterator().hasPrevious()) {
    			questionNumber = reviewList.listIterator().previous();
    		}
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
        final EditText goToNumber = (EditText) findViewById(R.id.jumpToTextExam);

        goToNumber.setText("");
    	setQuestionText(questionNumber);
    	disableButtons();

    	//Restore the answer, clear if no answer
        String answerStored = scoreMap.get(questionNumber);
        setRadioButtonChecked(answerStored);
        
        //Clear check status
        CheckBox mark = (CheckBox) findViewById(R.id.markForReviewExam);
		mark.setChecked(false);
    	//Restore the marked status
    	if(reviewList.contains(questionNumber)) {
    		mark.setChecked(true);
    	}
        
    }
    
    private void disableButtons() {
        final Button buttonPrev = (Button) findViewById(R.id.previousExam);
        final Button buttonNext = (Button) findViewById(R.id.nextExam);

        if(!isReviewMode) {
	    	int next = questionNumber;
	    	String answerStored = scoreMap.get(next);
	    	if( answerStored == null ) {
	    		buttonNext.setEnabled(false);
	        } else {
	        	buttonNext.setEnabled(true);
	        } 
	    	int prev = questionNumber-1;
	    	if( prev >= 0 ) {
	    		buttonPrev.setEnabled(true);
	    	} else {
	    		buttonPrev.setEnabled(false);
	    	}
        } else {
        	if(reviewList.listIterator().hasNext()) {
        		buttonNext.setEnabled(true);
        	} else {
        		buttonNext.setEnabled(false);
        	}
        	
        	if(reviewList.listIterator().hasPrevious()) {
        		buttonPrev.setEnabled(true);
        	} else {
        		buttonPrev.setEnabled(true);
        	}
        }
    	
    }
    
    private void setQuestionText(int questionNumber) {
    	final TextView questionText = (TextView) findViewById(R.id.questionExam);
        final TextView choiceA = (TextView) findViewById(R.id.choiceAExam);
        final TextView choiceB = (TextView) findViewById(R.id.choiceBExam);
        final TextView choiceC = (TextView) findViewById(R.id.choiceCExam);
        final TextView choiceD = (TextView) findViewById(R.id.choiceDExam);
        questionText.setText(exam.getQuestionList().get(questionNumber).getQuestion());
        choiceA.setText(exam.getQuestionList().get(questionNumber).getChoices().get(0));
        choiceB.setText(exam.getQuestionList().get(questionNumber).getChoices().get(1));
        choiceC.setText(exam.getQuestionList().get(questionNumber).getChoices().get(2));
        choiceD.setText(exam.getQuestionList().get(questionNumber).getChoices().get(3));
    }
    public  void onRadioButtonClicked(View view) {
    	
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.singleChoiceExam);
    	String value = getCheckedAnswer();
    	//Store user answer
        scoreMap.put(questionNumber, value);

        nextQuestion();
        refreshPage();
    }

    private void setRadioButtonChecked(String answer) {
   
    	RadioGroup radioGroup = (RadioGroup) findViewById(R.id.singleChoiceExam);
    	radioGroup.clearCheck();
    	if(answer==null || answer.isEmpty()) return;
    	
    	int checkedButtonId=0;
    	switch (answer) {
    		case "A" :  checkedButtonId=R.id.choiceAExam ;break;
    		case "B" :  checkedButtonId=R.id.choiceBExam ;break;
    		case "C" :  checkedButtonId=R.id.choiceCExam ;break;
    		case "D" :  checkedButtonId=R.id.choiceDExam ;break;
    		default  :  checkedButtonId=0;
    	}
    	if(checkedButtonId !=0 ) {
    		radioGroup.check(checkedButtonId);
    	}
    }
    
    private String getCheckedAnswer() {
    	RadioGroup radioGroup = (RadioGroup) findViewById(R.id.singleChoiceExam);
    	int id = radioGroup.getCheckedRadioButtonId();
    	int chosen=0;
    	switch (id) {
    		case  R.id.choiceAExam : chosen=1; break; 
	    	case  R.id.choiceBExam : chosen=2; break; 
	    	case  R.id.choiceCExam : chosen=3; break; 
	    	case  R.id.choiceDExam : chosen=4; break; 
	    	default : chosen=0; 
    	}
    	if (chosen==0) return null;
    	return (char)(CHOICE_A+chosen-1)+"";
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_mode);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // If your minSdkVersion is 11 or higher, instead use:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
           getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initilizeExam();
    }
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exam_mode_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_enter_review_mode:
                enterReviewMode();
                return true;
            case R.id.action_leave_review_mode:
                leaveReviewMode();
                return true;
            case R.id.action_grade:
                grade();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /** Defines a default (dummy) share intent to initialize the action provider.
     * However, as soon as the actual content to be used in the intent
     * is known or changes, you must update the share intent by again calling
     * mShareActionProvider.setShareIntent()
     */
   private Intent getDefaultIntent() {
       Intent intent = new Intent(Intent.ACTION_SEND);
       intent.setType("image/*");
       return intent;
   }
   
   @Override
   public void onResume() {
       super.onResume();  // Always call the superclass method first
   } 
   @Override
   public void onPause() {
       super.onPause();  // Always call the superclass method first
   }
   
   private void openSearch(){
    	
   }
   
   private void openSettings(){
	   
   }
   
   private void leaveReviewMode() {
	   if(isReviewMode) {
		   final  TextView title = (TextView) findViewById(R.id.singleChoiceExamTitle);
		   title.setText(TITLE);
		   final Button buttonJump = (Button) findViewById(R.id.jumpToButtonExam);
		   buttonJump.setEnabled(true);
		   Toast.makeText(getApplicationContext(), "Leaving review mode.", Toast.LENGTH_LONG).show();
		   questionNumber = storedQuestionNumber;
		   isReviewMode = false;
		   refreshPage();
	   }
   }
   
   private void enterReviewMode() {
	   if(!isReviewMode) {
		   
	   		if(reviewList.isEmpty()) {
	   		   Toast.makeText(getApplicationContext(), "Your review list is empty", Toast.LENGTH_LONG).show();
	   		} else {
	   			final  TextView title = (TextView) findViewById(R.id.singleChoiceExamTitle);
	 		   title.setText(TITLE_REVIEW_MODE);
	 		   final Button buttonJump = (Button) findViewById(R.id.jumpToButtonExam);
	 		   buttonJump.setEnabled(false);
	   		    Toast.makeText(getApplicationContext(), "Entering review mode.", Toast.LENGTH_LONG).show();
	   			storedQuestionNumber = questionNumber;
	   			questionNumber = reviewList.get(0);
	   			isReviewMode = true;
	   			refreshPage();
	   		}
	   }
   }
   
   //Mark a question for review. 
   //User can review all the questions marked at any time.
   private void markForReview() {
	   if(!reviewList.contains(questionNumber)) {
		   reviewList.add(questionNumber);
	   }
	   //Toast.makeText(getApplicationContext(), "Marked the question for review", Toast.LENGTH_LONG).show();
   }
   
   private void removeFromMarkedSet() {
	   int num= reviewList.indexOf(questionNumber);
	   if(num >= 0) {
		   reviewList.remove(num);
	   }
	   //Toast.makeText(getApplicationContext(), "Remove the question from reviewList", Toast.LENGTH_LONG).show();
   }
   
   private void addListenerOnJumpToButton() {
	   final Button buttonJump = (Button) findViewById(R.id.jumpToButtonExam);
       buttonJump.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
              final EditText goToNumber = (EditText) findViewById(R.id.jumpToTextExam);
    		   int upperBound = scoreMap.size();

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
   private void addListenerOnPrevAndNextButton() {
   	final Button buttonPrev = (Button) findViewById(R.id.previousExam);
       buttonPrev.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
           	   prevQuestion();
               refreshPage();
           }
       });
       final Button buttonNext = (Button) findViewById(R.id.nextExam);
       buttonNext.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
           	   nextQuestion();
           	   refreshPage();    
           }
       }); 
   }
   private void addListenerOnMarkForReviewCheckBox() {
	   CheckBox markForReview = (CheckBox) findViewById(R.id.markForReviewExam); 
	   markForReview.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					markForReview();
				} else {
					removeFromMarkedSet();
				}
			}
			});
   }

   //Grade the exam based on the user answer, and show the score of the user
   //TODO: save the grade result(user answer), and upload to web server.
   private void grade(){
	   List<SingleChoiceAnswer> answerList = exam.getAnswerList();
	   int count=0;
	   List<Integer> correctList = new ArrayList<Integer>();
	   for(SingleChoiceAnswer answer : answerList) {
		   ++count;
		   System.out.println("For question: "+count+", you choose:"+scoreMap.get(count)+"; and correct answer is "+answer.getAnswer());
		   if(answer.getAnswer().equalsIgnoreCase(scoreMap.get(count))) {
			   correctList.add(count);
		   }
	   }
	   Toast.makeText(getApplicationContext(), "Your score is "+correctList.size()+" out of "+answerList.size(), Toast.LENGTH_LONG).show();
   }
}
