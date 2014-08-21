package com.apps.knowledgeRepo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.exams.SingleChoiceExam;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PracticeModeActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.apps.knowledagerepo.MESSAGE";
	public final static char CHOICE_A= 'A';
	private ShareActionProvider mShareActionProvider;

	private int questionNumber = -1;
    SingleChoiceExam exam = null;
    //private Map<Long, Long> scoreMap = new HashMap<Long, Long>(); //store the answer of the question which user already finished 
    
    public void initilizePractice() {
    	final TextView answerText = (TextView) findViewById(R.id.checkAnswer);
        final Button buttonShowAnswer = (Button) findViewById(R.id.showAnswer);
        buttonShowAnswer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	int chosen = 0;
            	int id = getCheckedSingleChoiceId();
            	switch (id) {
            	    case  R.id.choiceA : chosen=1; break; 
            	    case  R.id.choiceB : chosen=2; break; 
            	    case  R.id.choiceC : chosen=3; break; 
            	    case  R.id.choiceD : chosen=4; break; 
            	    default : chosen=0; 
            	}
            
            	System.out.println("chosen=="+chosen+";  You've chosen --- "+(char)(CHOICE_A+chosen-1));
            	System.out.println("answer is --"+exam.getAnswerList().get(questionNumber).getAnswer().charAt(0));
            	if(chosen==0) {
            		answerText.setText("Please select an answer.");        
            		answerText.setTextColor(Color.RED);
            		System.out.println("Please select an answer.");
            	} else if(  (char)(CHOICE_A+chosen-1) == exam.getAnswerList().get(questionNumber).getAnswer().charAt(0) ) {
            		System.out.println("Congraturations! You are correct with answer - "+(char)(CHOICE_A+chosen-1));
            		answerText.setText("Congraturations! You are correct with answer - "+(char)(CHOICE_A+chosen-1));
            		answerText.setTextColor(Color.GREEN);
            		/*//Using Customer Toast
            		Toast toast=new Toast(getApplicationContext());
            		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            		toast.setDuration(Toast.LENGTH_LONG);
            		toast.setView(getLayoutInflater().inflate(R.layout.custom_toast, null));
            		toast.show();*/
            		Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG).show();
            	} else {
            		System.out.println("Sorry! You've chosen - "+(char)(CHOICE_A+chosen-1)+", but the correct answer is - "+exam.getAnswerList().get(questionNumber).getAnswer().charAt(0));
            		answerText.setText("Sorry! You've chosen - "+(char)(CHOICE_A+chosen-1)+", but the correct answer is - "+exam.getAnswerList().get(questionNumber).getAnswer().charAt(0));
            		answerText.setTextColor(Color.RED);
            		
            		
            		Toast.makeText(getApplicationContext(), "Sorry! Answer is "+exam.getAnswerList().get(questionNumber).getAnswer().charAt(0), Toast.LENGTH_LONG).show();

            	}
            }
        });
    }
    
    public void initilizeExam() {
    	exam = new SingleChoiceExam();
 		String questionString = "none";
 		String answerString = "none";
 		
        final TextView questionText = (TextView) findViewById(R.id.question);
    	final TextView answerText = (TextView) findViewById(R.id.checkAnswer);
        final TextView choiceA = (TextView) findViewById(R.id.choiceA);
        final TextView choiceB = (TextView) findViewById(R.id.choiceB);
        final TextView choiceC = (TextView) findViewById(R.id.choiceC);
        final TextView choiceD = (TextView) findViewById(R.id.choiceD);
        
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.singleChoice);

        final Button buttonPrev = (Button) findViewById(R.id.previous);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(questionNumber<=0) return;
                --questionNumber;
                questionText.setText(exam.getQuestionList().get(questionNumber).getQuestion());
                choiceA.setText(exam.getQuestionList().get(questionNumber).getChoices().get(0));
                choiceB.setText(exam.getQuestionList().get(questionNumber).getChoices().get(1));
                choiceC.setText(exam.getQuestionList().get(questionNumber).getChoices().get(2));
                choiceD.setText(exam.getQuestionList().get(questionNumber).getChoices().get(3));
                answerText.setText("");
                radioGroup.clearCheck();
            }
        });
        final Button buttonNext = (Button) findViewById(R.id.next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(questionNumber >= exam.getCount()-1) return;
                ++questionNumber;
                questionText.setText(exam.getQuestionList().get(questionNumber).getQuestion());
                choiceA.setText(exam.getQuestionList().get(questionNumber).getChoices().get(0));
                choiceB.setText(exam.getQuestionList().get(questionNumber).getChoices().get(1));
                choiceC.setText(exam.getQuestionList().get(questionNumber).getChoices().get(2));
                choiceD.setText(exam.getQuestionList().get(questionNumber).getChoices().get(3));
                answerText.setText("");
                radioGroup.clearCheck();
                // Perform action on click
            }
        });  
        
        try {
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
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_mode);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // If your minSdkVersion is 11 or higher, instead use:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
           getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initilizeExam();
        initilizePractice();
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
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	EditText editText = (EditText) findViewById(R.id.title);
    	String message = editText.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
    }
}
