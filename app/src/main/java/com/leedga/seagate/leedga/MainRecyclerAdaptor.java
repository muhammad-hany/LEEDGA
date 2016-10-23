package com.leedga.seagate.leedga;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Muhammad Workstation on 07/10/2016.
 */

public class MainRecyclerAdaptor extends RecyclerView.Adapter<MainRecyclerAdaptor.ViewHolder> {


    private int[] cardsIds;
    private Context context;
    private OnMyItemClick listener;

    public MainRecyclerAdaptor(Context context, OnMyItemClick listener) {
        cardsIds = new int[]{R.layout.main_card_test, R.layout.main_card_test_setting, R.layout
                .main_card_history, R.layout.main_card_day_question, R.layout.main_card_countdown};

        this.context = context;
    }

    @Override
    public MainRecyclerAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(cardsIds[viewType], parent, false);
        return getProperHandler(v, viewType);

    }

    private ViewHolder getProperHandler(View v, int position) {
        ViewHolder holder = null;
        switch (position) {
            case 0:
                holder = new TestHolder(v);
                break;
            case 1:
                holder = new TestSettingHolder(v);
                break;
            case 2:
                holder = new HistoryHolder(v);
                break;
            case 3:
                holder = new DayQuestionHolder(v);
                break;
            case 4:
                holder = new CountDownHolder(v);
                break;


        }
        return holder;
    }

    @Override
    public void onBindViewHolder(MainRecyclerAdaptor.ViewHolder viewHolder, int position) {

    }

    private View getCard(int position) {


        return null;
    }

    @Override
    public int getItemCount() {
        return cardsIds.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }


    public interface OnMyItemClick {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    class CountDownHolder extends ViewHolder {

        public CountDownHolder(View itemView) {
            super(itemView);
        }
    }

    class TestHolder extends ViewHolder {
        Button testAgain;
        CardView cardView;

        public TestHolder(View view) {
            super(view);
            testAgain = (Button) view.findViewById(R.id.button);
            testAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).checkThereIsUnfinishedTest();
                }
            });

            cardView = (CardView) view.findViewById(R.id.testCard);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).checkThereIsUnfinishedTest();
                }
            });
        }

    }

    class DayQuestionHolder extends ViewHolder {
        TextView textView;

        public DayQuestionHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(((MainActivity) context).getRandomQuestion().getQuestionBody());

        }

    }

    class HistoryHolder extends ViewHolder {
        CardView cardView;
        Button button;
        ListView listView;
        private ArrayList<String> stringPref;
        private List<Test> testPref;

        public HistoryHolder(View view) {
            super(view);
            stringPref = new ArrayList<>();
            testPref = new ArrayList<>();
            cardView = (CardView) view.findViewById(R.id.testCard);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, HistoryActivity.class);
                    context.startActivity(i);
                }
            });

            listView = (ListView) view.findViewById(R.id.lisView);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout
                    .simple_list_item_1, android.R.id.text1, gettingTestFromPref());

            listView.setAdapter(adapter);
            getListViewSize(listView);

            button = (Button) view.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, HistoryActivity.class);
                    context.startActivity(i);
                }
            });


        }

        public List<String> gettingTestFromPref() {
            SharedPreferences prefs = context.getSharedPreferences(ResultActivity.TESTS_PREFS,
                    MODE_PRIVATE);
            Map<String, ?> tests = prefs.getAll();
            for (Map.Entry<String, ?> test : tests.entrySet()) {
                stringPref.add(test.getValue().toString());
            }
            Gson gson = new Gson();
            if (stringPref.size() != 0) {
                for (String entry : stringPref) {
                    Test test = gson.fromJson(entry, Test.class);
                    testPref.add(test);
                }
            }

            Collections.sort(testPref, new Comparator<Test>() {
                @Override
                public int compare(Test lhs, Test rhs) {
                    return rhs.getSavingDate().compareTo(lhs.getSavingDate());
                }
            });

            List<String> dates = new ArrayList<>();
            DateFormat format = new SimpleDateFormat("MMM dd, yyyy h:mm a");
            for (Test test : testPref) {
                String date = format.format(test.getSavingDate());
                dates.add(date);
            }
            List<String> subitems = dates.subList(0, Math.min(2, dates.size()));

            return subitems;
        }


        public void getListViewSize(ListView myListView) {
            ListAdapter myListAdapter = myListView.getAdapter();

            if (myListAdapter == null) {
                //do nothing return null
                return;
            }
            //set listAdapter in loop for getting final size
            int totalHeight = 0;
            for (int size = 0; size < myListAdapter.getCount(); size++) {
                View listItem = myListAdapter.getView(size, null, myListView);
                listItem.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }
            //setting listview item in adapter
            ViewGroup.LayoutParams params = myListView.getLayoutParams();
            params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
            myListView.setLayoutParams(params);
        }


    }

    class TestSettingHolder extends ViewHolder {
        private final View.OnClickListener itemClickListener;
        LinearLayout linearLayout;
        ConstraintLayout item1, item2, item3, item4;
        ArrayList<Integer> viewItemsIDs;
        List<String> testSetting;


        public TestSettingHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.l1);
            testSetting = new ArrayList<>();
            testSetting.add("Answer Setting");
            testSetting.add("Question Type");
            testSetting.add("Number of Questions");
            testSetting.add("Domains of Test");
            viewItemsIDs = new ArrayList<>();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);


            item1 = (ConstraintLayout) view.findViewById(R.id.item1);
            item2 = (ConstraintLayout) view.findViewById(R.id.item2);
            item3 = (ConstraintLayout) view.findViewById(R.id.item3);
            item4 = (ConstraintLayout) view.findViewById(R.id.item4);
            ViewGroup[] viewGroups = new ViewGroup[]{item1, item2, item3, item4};

            SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.DEFAULT_TEST_PREF_KEY, MODE_PRIVATE);
            String json = sharedPreferences.getString(MainActivity.DEFAULT_TEST_KEY, null);
            Gson gson = new Gson();
            Test test = gson.fromJson(json, Test.class);

            int i = 0;
            for (ViewGroup view1 : viewGroups) {
                TextView textView1 = (TextView) view1.findViewById(R.id.text1);
                textView1.setText(testSetting.get(i));
                TextView textView2 = (TextView) view1.findViewById(R.id.text2);
                textView2.setVisibility(View.VISIBLE);
                switch (i) {
                    case 0:
                        if (test.getAnswerShow() == TestTypeFragment.ANSWER_AFTER_ALL) {
                            textView2.setText("Show answer after test");
                        } else if (test.getAnswerShow() == TestTypeFragment.ANSWER_AFTER_EVERY) {
                            textView2.setText("Show answer when yours is wrong");
                        } else {
                            textView2.setText("Show answer after every question");
                        }
                        break;
                    case 1:
                        textView2.setVisibility(View.GONE);
                        break;
                    case 2:

                        textView2.setText(String.valueOf(test.getNumberOfQuestions()));
                        break;
                    case 3:
                        textView2.setVisibility(View.GONE);

                        break;
                }

                i++;
            }

            itemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.item1) {
                        launchDialog();
                    } else if (v.getId() == R.id.item2) {

                    } else if (v.getId() == R.id.item3) {

                    } else if (v.getId() == R.id.item4) {

                    }
                }
            };
            item1.setOnClickListener(itemClickListener);

        }

        private void launchDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Test");
            builder.setTitle("Title");
            AlertDialog dialog = builder.create();
            dialog.show();
        }


    }


}
