package com.leedga.seagate.leedga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class ReportActivity extends AppCompatActivity {

    EditText name, subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Report");
        name = (EditText) findViewById(R.id.name);
        subject = (EditText) findViewById(R.id.subject);


    }

    private void sendReport() {
        if (subject.getText().toString().trim().equalsIgnoreCase("")) {
            subject.setError("this field can not be blank");
        }
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        String user;
        if (name.getText().toString().trim().equals("")) {
            user = "user";
        } else {
            user = name.getText().toString();
        }
        i.putExtra(Intent.EXTRA_SUBJECT, user + " has reported question in LEED GA");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"osama@khayata.com"});
        i.putExtra(Intent.EXTRA_TEXT, makeEmailBody());
        startActivity(Intent.createChooser(i, "Send Email...."));
    }

    private String makeEmailBody() {

        Question question = (Question) getIntent().getSerializableExtra(REF.QUESTION_KEY);
        String body = name.getText().toString() + " has reported the following question : \n\n " + String.valueOf(question.getId()) + ": " + question.getQuestionBody();
        String answer;
        if (question.getKey().equals(REF.SINGLE_CHOICE_KEY)) {
            answer = "\n\n a) " + question.getChoice1() + "\n b) " + question.getChoice2() + "\n c) " +
                    "" + question.getChoice3() + "\n d) " + question.getChoice4();
        } else if (question.getKey().equals(REF.MULTI_CHOICE_KEY)) {
            answer = "\n\n a) " + question.getChoice1() + "\n b) " + question.getChoice2() + "\n c) " +
                    "" + question.getChoice3() + "\n d) " + question.getChoice4() + "\n e)" + question
                    .getChoice5() + "\n f) " + question.getChoice6();
        } else {
            answer = "\n\n a) true\n b) false";
        }
        answer = answer + "\n\n user comment :" + subject.getText().toString();
        return body + answer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send:
                if (!subject.getText().toString().trim().equalsIgnoreCase("")) {
                    sendReport();
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

   /* private void sendBackgroundMail() {
        BackgroundMail.newBuilder(this)
                .withUsername("mohammedeletreby@gmail.com")
                .withPassword("mohammedhany123")
                .withBody(makeEmailBody())
                .withMailto("mohammedeletreby@gmail.com")
                .withSubject(name.getText().toString()+" has reported question in LEED GA")
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ReportActivity.this,"Report sent successfully",Toast
                                .LENGTH_LONG).show();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        Log.i("REPORT","sending failed");
                    }
                }).send();


    }*/
}
