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
import com.apps.knowledgeRepo.dataModel.Course;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.db.DBTool;
import com.apps.knowledgeRepo.exams.SingleChoiceExam;
import com.apps.knowledgeRepo.om.SingleChoiceAnswer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView; 
import android.widget.Toast;

public class ExamModeActivity extends Activity{

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("ExamModeActivity");
	private static final String TITLE_REVIEW_MODE = "Only marked questions are shown";
	private static final String TITLE = "";

	public final static char CHOICE_A= 'A';
	@SuppressWarnings("unused")
	private  ShareActionProvider mShareActionProvider;

	//This is the current question number user is working on.
	private int questionNumber = 0;
	private int reviewQuestionNumberIndex = 0;
    SingleChoiceExam exam = null;
    
    //store the answer of the question which user already finished 
    @SuppressLint("UseSparseArrays")
	private Map<Integer, String> scoreMap = new HashMap<Integer, String>(); 
    
    //A list of questions numbers that are marked for review
    private List<Integer> reviewList = new ArrayList<Integer>();
    private boolean isReviewMode = false;
    private int storedQuestionNumber=0;
    private List<Integer> inCorrectList = new ArrayList<Integer>();
    
    private long startTime;
    private long pauseStartTime=0;
    private long totalPauseTime=0;
    
    public void initilizeExam() {
    	
 		addListenerOnJumpToButton();
 		addListenerOnPauseButton();
 		addListenerOnGradeButton();
        addListenerOnPrevAndNextButton();
        addListenerOnReviewMarkedButton();
        parseExam();
        startTime = System.currentTimeMillis();
        totalPauseTime=0;
        pauseStartTime=0;
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
    
    //Store the marked status, called before refresh to a new page
    private void checkMarkedStatus() {
    	CheckBox mark = (CheckBox) findViewById(R.id.markForReviewExam);
    	boolean isChecked=mark.isChecked();
    	if (isChecked) {
			markForReview();
		} else {
			removeFromMarkedSet();
		}
    }
    
    //Get the next questionNumber
    //Depends on the mode (isReviewMode)
    public void nextQuestion() {
    	if(!isReviewMode) {
    		//The last Question
        	if(questionNumber >= exam.getCount()-1) {
        		//TODO need to go to the summary page instead
        		return;
        	}
    		++questionNumber;
    	} else {
    		if(reviewQuestionNumberIndex >= reviewList.size()-1 ){
     		   return;
     		}
 		    ++reviewQuestionNumberIndex;
 		    questionNumber = reviewList.get(reviewQuestionNumberIndex);
    		
    	}
    }
    
    //Get the previous questionNumber
    //Depends on the mode (isReviewMode)
    public void prevQuestion() {
    
    	if(!isReviewMode) {
    		if(questionNumber<=0) {
    			return;
    		}
    		--questionNumber;
    	} else {
    		if(reviewQuestionNumberIndex <=0 ){
      		   return;
      		}
    		--reviewQuestionNumberIndex;
 		    questionNumber = reviewList.get(reviewQuestionNumberIndex);

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
    
    private long getRemainTimeInMillis() {
    	System.out.println("Total pause time is: "+totalPauseTime);
    	return exam.getExamTimeInMinutes()*1000*60 - (System.currentTimeMillis() - startTime - totalPauseTime) ;
    }
    private void refreshPage() {
    	
    	if(isTimeOut()) {
    		Toast.makeText(getApplicationContext(), "You've timed out. Start grading!", Toast.LENGTH_LONG).show();
    		grade();
    		return;
    	}
    	
        final TextView qnumText = (TextView) findViewById(R.id.singleChoiceExamNumber);
        qnumText.setText((questionNumber+1)+"/"+exam.getCount());
        
        final EditText goToNumber = (EditText) findViewById(R.id.jumpToTextExam);
        goToNumber.setText("");
    	setQuestionText(questionNumber);
    	disableButtons();

    	//Restore the answer, clear if no answer
        String answerStored = scoreMap.get(questionNumber);
        setRadioButtonChecked(answerStored);
        
        long remainTimeInMinutes = getRemainTimeInMillis()/1000/60;
        
        final TextView timeText = (TextView) findViewById(R.id.remainTime);
        timeText.setText(""+remainTimeInMinutes+" minutes remaining");
        //Clear check status
        CheckBox mark = (CheckBox) findViewById(R.id.markForReviewExam);
		mark.setChecked(false);
    	//Restore the marked status
    	if(reviewList.contains(questionNumber)) {
    		mark.setChecked(true);
    	}
        
    }
    
    private void disableButtons() {
        final Button buttonPrev = (Button) findViewById(R.id.previousButtonExam);
        final Button buttonNext = (Button) findViewById(R.id.nextButtonExam);

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
        	if(reviewQuestionNumberIndex < reviewList.size()-1) {
        		buttonNext.setEnabled(true);
        	} else {
        		buttonNext.setEnabled(false);
        	}
        	
        	if(reviewQuestionNumberIndex>0) {
        		buttonPrev.setEnabled(true);
        	} else {
        		buttonPrev.setEnabled(false);
        	}
        }
    	
    }
    
    private void setQuestionText(int questionNumber) {
    	final TextView questionText = (TextView) findViewById(R.id.questionExam);
        final TextView choiceA = (TextView) findViewById(R.id.choiceAExam);
        final TextView choiceB = (TextView) findViewById(R.id.choiceBExam);
        final TextView choiceC = (TextView) findViewById(R.id.choiceCExam);
        final TextView choiceD = (TextView) findViewById(R.id.choiceDExam);
        questionText.setText(Html.fromHtml(exam.getQuestionList().get(questionNumber).getQuestion()));
        choiceA.setText(Html.fromHtml(exam.getQuestionList().get(questionNumber).getChoices().get(0)));
        choiceB.setText(Html.fromHtml(exam.getQuestionList().get(questionNumber).getChoices().get(1)));
        choiceC.setText(Html.fromHtml(exam.getQuestionList().get(questionNumber).getChoices().get(2)));
        choiceD.setText(Html.fromHtml(exam.getQuestionList().get(questionNumber).getChoices().get(3)));
    }
    public  void onRadioButtonClicked(View view) {
    	
    	String value = getCheckedAnswer();
    	//Store user answer
        scoreMap.put(questionNumber, value);
        //need course id, exam id, attempt,  
        Course course = new Course();
        
    	checkMarkedStatus();
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
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
	        Exam exam = (Exam) extras.getSerializable("exam");
	        if(exam!=null) {
	        	Log.d("Exam Mode","exam not null!! exam---"+exam.getName());
	        }
        }
        initilizeExam();
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
   @SuppressWarnings("unused")
   private Intent getDefaultIntent() {
       Intent intent = new Intent(Intent.ACTION_SEND);
       intent.setType("image/*");
       return intent;
   }
   
   @Override
   public void onResume() {
       super.onResume();  // Always call the superclass method first
       resume();
   } 
   @Override
   public void onPause() {
       super.onPause();  // Always call the superclass method first
       pause();
   }
   
   public void pause() {
	   System.out.println("Exam paused;");
	   pauseStartTime=System.currentTimeMillis();
   }
   
   public boolean isTimeOut() {
	   if( getRemainTimeInMillis() <0  ) {
		   return true;
	   } else {
		   return false;
	   }
	   
   }
   
   public void resume() {
	   System.out.println("Exam resumed;");
	   if(pauseStartTime>0)
		   totalPauseTime += System.currentTimeMillis()-pauseStartTime;
	   //TODO resume the screen
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
	   if(num>=0) {
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
        		   checkMarkedStatus();
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
   
   private void addListenerOnPauseButton() {
		final Button button = (Button) findViewById(R.id.pauseButtonExam);
		button.setOnClickListener(new View.OnClickListener() {
			 
			  @Override
			  public void onClick(View arg0) {
	 
				// custom dialog
				final Dialog dialog = new Dialog(ExamModeActivity.this);
				dialog.setContentView(R.layout.pause_dialog);
				dialog.setTitle("Exam Paused");
				dialog.setCancelable(false);
				// set the custom dialog components - text, image and button
				//TextView text = (TextView) dialog.findViewById(R.id.text);
				//text.setText("Android custom dialog example!");
				ImageView image = (ImageView) dialog.findViewById(R.id.pauseExamImage);
				image.setImageResource(R.drawable.ic_launcher);
	 
				Button dialogButton = (Button) dialog.findViewById(R.id.resumeExamButton);
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						resume();
					}
				});
				pause();
				dialog.show();
			  }
			});

   }
   
   private void addListenerOnPrevAndNextButton() {
   	final Button buttonPrev = (Button) findViewById(R.id.previousButtonExam);
       buttonPrev.setOnClickListener(new View.OnClickListener() {
    	   @Override
           public void onClick(View v) {
           	   checkMarkedStatus();
           	   prevQuestion();
               refreshPage();
           }
       });
       final Button buttonNext = (Button) findViewById(R.id.nextButtonExam);
       buttonNext.setOnClickListener(new View.OnClickListener() {
    	   @Override
           public void onClick(View v) {
           	   checkMarkedStatus();
           	   nextQuestion();
           	   refreshPage();    
           }
       }); 
   }
   
   private void addListenerOnReviewMarkedButton() {
	   final Button button = (Button) findViewById(R.id.reviewButtonExam);
	   button.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View arg0) {
			   if(isReviewMode) {
				   leaveReviewMode();
				   button.setText("Review Marked");

			   } else {
				   enterReviewMode();
				   button.setText("Back to Exam");
			   }
		   }
	   });
   }
   private void addListenerOnGradeButton() {
		final Button button = (Button) findViewById(R.id.gradeButtonExam);
		button.setOnClickListener(new View.OnClickListener() {
			 
			  @Override
			  public void onClick(View arg0) {
				  grade();
			  }
			});

  }
   
   private void showResultDialog(String results) {
	// custom dialog
			final Dialog dialog = new Dialog(ExamModeActivity.this);
			dialog.setContentView(R.layout.grade_dialog);
			dialog.setTitle("Exam Finished");
			dialog.setCancelable(false);

			//String results = grade();
			// set the custom dialog components - text, image and button
			TextView text = (TextView) dialog.findViewById(R.id.gradeTextExamEnd);
			text.setText(results);
			ImageView image = (ImageView) dialog.findViewById(R.id.imageExamEnd);
			image.setImageResource(R.drawable.ic_launcher);

			//Review All Button
			Button reviewAllButton = (Button) dialog.findViewById(R.id.reviewAllButton);
			// if button is clicked, close the custom dialog
			reviewAllButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ExamModeActivity.this, ViewAnswerModeActivity.class);	
					//need to pass exam information to ViewAnswerModeActivity
				    //intent.putExtra(EXTRA_MESSAGE, message);
				    startActivity(intent);
					System.out.println("reviewAllButton!");
				}
			});
			//Review Incorrect Button
			Button reviewIncorrectButton = (Button) dialog.findViewById(R.id.reviewInCorrectButton);
			// if button is clicked, close the custom dialog
			reviewIncorrectButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ExamModeActivity.this, ViewAnswerModeActivity.class);	
					//need to pass exam information to ViewAnswerModeActivity
				    intent.putIntegerArrayListExtra("inCorrectList", (ArrayList<Integer>) inCorrectList);
				    startActivity(intent);
				    System.out.println("reviewIncorrectButton!");

				}
			});
			//Retake Exam Button
			Button retakeExamButton = (Button) dialog.findViewById(R.id.retakeExamButton);
			// if button is clicked, close the custom dialog
			retakeExamButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//dialog.dismiss();
					Intent intent = new Intent(ExamModeActivity.this, ExamModeActivity.class);				       
				    //intent.putExtra(EXTRA_MESSAGE, message);
				    startActivity(intent);
					System.out.println("retakeExamButton!");

				}
			});
			
			//Return to Main Menu Button
			Button returnToMainMenuButton = (Button) dialog.findViewById(R.id.returnToMainMenuButton);
			// if button is clicked, close the custom dialog
			returnToMainMenuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//dialog.dismiss();
					Intent intent = new Intent(ExamModeActivity.this, ModeSelectionActivity.class);				       
				    //intent.putExtra(EXTRA_MESSAGE, message);
				    startActivity(intent);
					System.out.println("returnToMainMenuButton!");

				}
			});
			
			
			dialog.show();
   }
   //Grade the exam based on the user answer, and show the score of the user
   //TODO: save the grade result(user answer), and upload to web server.
   private boolean grade() {
	   StringBuilder sb = new StringBuilder();
	   List<SingleChoiceAnswer> answerList = exam.getAnswerList();
	   int count=0;
	   boolean isPassed = false;
	   
	   List<Integer> correctList = new ArrayList<Integer>();
	   for(SingleChoiceAnswer answer : answerList) {
		   if(answer.getAnswer().equalsIgnoreCase(scoreMap.get(count))) {
			   correctList.add(count);
		   } else {
			   inCorrectList.add(count);
		   }
		   ++count;
	   }
	   if(correctList.size() > exam.getPassingScore()) {
		   isPassed = true;
		   sb.append("Congratulations! You have passed the exam. \n");
	   } else {
		   sb.append("Sorry, you didn't pass the exam. \n");
	   }
	   
	   sb.append("Your score is "+correctList.size()+" out of "+answerList.size()+", and the passing score is "+exam.getPassingScore()+". ");
	   
	   showResultDialog(sb.toString());
		
	   return isPassed;
	   //Toast.makeText(getApplicationContext(), "Your score is "+correctList.size()+" out of "+answerList.size(), Toast.LENGTH_LONG).show();
   }
}
