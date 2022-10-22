package edu.cs478.sofialucca.project3_2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class WebsiteFragment  extends Fragment {

    private static final String TAG = "WebsiteFragment";

    private WebView mWebsiteView = null;
    private int mCurrIdx = -1;
    private int mQuoteArrayLen;
    private ListViewModel model;

    public WebsiteFragment() {
        super() ;
        Log.i("QuotesFragment", "I got created!") ;
    }

    int getShownIndex() {
        return mCurrIdx;
    }

    // Show the Quote string at position newIndex
    void showQuoteAtIndex(int newIndex) {
        if (newIndex < 0 || newIndex >= mQuoteArrayLen)
            return;
        mCurrIdx = newIndex;
        mWebsiteView.loadUrl(AttractionsActivity.mWebsitesArray[mCurrIdx]);
    }

    @Override
    public void onAttach(@NonNull Context activity) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
        super.onCreate(savedInstanceState);
    }

    // Called to create the content view for this Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreateView()");

        // Inflate the layout defined in quote_fragment.xml
        // The last parameter is false because the returned view does not need to be attached to the container ViewGroup
        return inflater.inflate(R.layout.website_fragment,container, false);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){

        Log.i(TAG, getClass().getSimpleName() + ":entered onViewCreated()");
        super.onViewCreated(view,savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(ListViewModel.class);

        // retains last quote shown on config change
        model.getSelectedItem().observe(getViewLifecycleOwner(), item -> {
            // UB: 2/19/2022 -- No need to update UI if same item reselected in TitlesFragment

            Log.i("Ugo says", "Entered QuoteFragment observe()") ;
            if (item == mCurrIdx || item < 0 || item >= mQuoteArrayLen)
                return;

            // Otherwise, make sure quotes fragment is visible

            // Update UI
            mCurrIdx = item;
            mWebsiteView.loadUrl(AttractionsActivity.mWebsitesArray[mCurrIdx]);
        });

        mWebsiteView = getActivity().findViewById(R.id.websiteView);
        mQuoteArrayLen = AttractionsActivity.mWebsitesArray.length;
    }

    // Set up some information about the mWebsiteView TextView
    // UB: 2/19/2022 -- This is now deprecated
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		Log.i(TAG, getClass().getSimpleName() + ":entered onActivityCreated()");
//		super.onActivityCreated(savedInstanceState);
//
//		mWebsiteView = (TextView) getActivity().findViewById(R.id.quoteView);
//		mQuoteArrLen = QuoteViewerActivity.mQuoteArray.length;
//	}

    @Override
    public void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }


    @Override
    public void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDetach()");
        super.onDetach();
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroyView()");
        super.onDestroyView();
    }

}