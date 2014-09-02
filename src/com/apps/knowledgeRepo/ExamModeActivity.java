package com.apps.knowledgeRepo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ExamModeActivity extends Activity{

	public final static char CHOICE_A= 'A';
	private ShareActionProvider mShareActionProvider;

	private int questionNumber = -1;
    SingleChoiceExam exam = null;
    
    private Map<Integer, String> scoreMap = new HashMap<Integer, String>(); //store the answer of the question which user already finished 

    public void initilizeExam() {
    	exam = new SingleChoiceExam();
 		String questionString = "none";
 		String answerString = "none";
 		
        final TextView questionText = (TextView) findViewById(R.id.questionExam);
        final TextView choiceA = (TextView) findViewById(R.id.choiceAExam);
        final TextView choiceB = (TextView) findViewById(R.id.choiceBExam);
        final TextView choiceC = (TextView) findViewById(R.id.choiceCExam);
        final TextView choiceD = (TextView) findViewById(R.id.choiceDExam);
        
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.singleChoiceExam);

        final Button buttonPrev = (Button) findViewById(R.id.previousExam);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(questionNumber<=0) return;
                --questionNumber;
                questionText.setText(exam.getQuestionList().get(questionNumber).getQuestion());
                choiceA.setText(exam.getQuestionList().get(questionNumber).getChoices().get(0));
                choiceB.setText(exam.getQuestionList().get(questionNumber).getChoices().get(1));
                choiceC.setText(exam.getQuestionList().get(questionNumber).getChoices().get(2));
                choiceD.setText(exam.getQuestionList().get(questionNumber).getChoices().get(3));
                radioGroup.clearCheck();
            }
        });
        final Button buttonNext = (Button) findViewById(R.id.nextExam);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String value = getCheckedAnswer();
            	if(value==null) {
            		Toast.makeText(getApplicationContext(), "Please choose an answer!", Toast.LENGTH_LONG).show();
            		return;
            	}
            	//Store user answer
                scoreMap.put(questionNumber, value);

            	if(questionNumber >= exam.getCount()-1) return;
            	
                ++questionNumber;
                questionText.setText(exam.getQuestionList().get(questionNumber).getQuestion());
                choiceA.setText(exam.getQuestionList().get(questionNumber).getChoices().get(0));
                choiceB.setText(exam.getQuestionList().get(questionNumber).getChoices().get(1));
                choiceC.setText(exam.getQuestionList().get(questionNumber).getChoices().get(2));
                choiceD.setText(exam.getQuestionList().get(questionNumber).getChoices().get(3));
                radioGroup.clearCheck();
                
                // Perform action on click
            }
        });  
        
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
		
	   ++questionNumber;
	   questionText.setText(exam.getQuestionList().get(questionNumber).getQuestion());
       choiceA.setText(exam.getQuestionList().get(questionNumber).getChoices().get(0));
       choiceB.setText(exam.getQuestionList().get(questionNumber).getChoices().get(1));
       choiceC.setText(exam.getQuestionList().get(questionNumber).getChoices().get(2));
       choiceD.setText(exam.getQuestionList().get(questionNumber).getChoices().get(3));
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
        inflater.inflate(R.menu.main, menu);
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
            case R.id.action_grade:
                grade();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private int getCheckedSingleChoiceId() {
    	RadioGroup radioGroup = (RadioGroup) findViewById(R.id.singleChoice);
    	int d = radioGroup.getCheckedRadioButtonId();
    	
    	return d;
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
