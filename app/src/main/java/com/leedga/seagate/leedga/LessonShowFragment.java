package com.leedga.seagate.leedga;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static com.leedga.seagate.leedga.REF.FRAGMENT_TYPE_KEY;
import static com.leedga.seagate.leedga.REF.LESSONS_KEY;
import static com.leedga.seagate.leedga.REF.POSITION_KEY;
import static com.leedga.seagate.leedga.REF.REFERENCES_KEY;
import static com.leedga.seagate.leedga.REF.TERMS_KEY;
import static com.leedga.seagate.leedga.REF.chapterNames;
import static com.leedga.seagate.leedga.REF.links;
import static com.leedga.seagate.leedga.REF.references;


public class LessonShowFragment extends Fragment {
    int position;
    String url;
    private int fragmentType;

    public LessonShowFragment() {
    }

    public static LessonShowFragment newInstance(int position, int fragmentType) {
        LessonShowFragment fragment = new LessonShowFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION_KEY, position);
        args.putInt(FRAGMENT_TYPE_KEY, fragmentType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentType = getArguments().getInt(FRAGMENT_TYPE_KEY);
        if (fragmentType == LESSONS_KEY) {
            position = getArguments().getInt(POSITION_KEY);
            ((LessonsActivity) getActivity()).getSupportActionBar().setTitle(chapterNames[position]);
            ((LessonsActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            switch (position) {
                case 0:
                    url = "ch1.html";
                    break;
                case 1:
                    url = "ch2.html";
                    break;
                case 2:
                    url = "ch3.html";
                    break;
                case 3:
                    url = "ch4.html";
                    break;
                case 4:
                    url = "ch5.html";
                    break;
                case 5:
                    url = "ch6.html";
                    break;
                case 6:
                    url = "ch7.html";
                    break;
                case 7:
                    url = "ch8.html";
                    break;
                case 8:
                    url = "ch9.html";
                    break;
                case 9:
                    url = "ch10.html";
                    break;
                case 10:
                    url = "ch11.html";
                    break;
                case 11:
                    url = "ch12.html";
                    break;
            }
            url = "file:///android_asset/" + url;
        } else if (fragmentType == TERMS_KEY) {
            ((LessonsActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            url = "file:///android_asset/terms.html";
            ((LessonsActivity) getActivity()).getSupportActionBar().setTitle(REF.KEY_TERMS);
        } else {
            url = "file:///android_asset/references.html";
            ((LessonsActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((LessonsActivity) getActivity()).getSupportActionBar().setTitle(REF.REFERENCE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_show, container, false);
        WebView webView = (WebView) view.findViewById(R.id.web);
        ListView listView = (ListView) view.findViewById(R.id.list);
        if (fragmentType == REFERENCES_KEY) {
            listView.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout
                    .simple_list_item_1, references);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(links[position]));
                    startActivity(intent);
                }
            });

        } else {
            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setIndeterminate(false);
            dialog.setMessage("Please wait");
            dialog.show();
            listView.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    dialog.dismiss();
                }
            });
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
