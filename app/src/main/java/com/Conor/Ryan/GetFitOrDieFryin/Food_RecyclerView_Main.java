package com.Conor.Ryan.GetFitOrDieFryin;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import org.json.JSONException;

import java.lang.ref.WeakReference;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;


public class Food_RecyclerView_Main extends Fragment {

    private static final String ARG_MOVIE = "R.id.mdf_main_replace";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    FoodDataJson foodData;
    private RecyclerView mRecyclerView;
    private Food_MyRecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public Food_RecyclerView_Main() {
        // Constructor
    }

    public static Food_RecyclerView_Main newInstance() {
        Food_RecyclerView_Main fragment = new Food_RecyclerView_Main();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, "R.id.rcmain");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        foodData = new FoodDataJson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_recyclerview_activity, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerViewAdapter = new Food_MyRecyclerViewAdapter(getActivity(), foodData.foodList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        // Animation
        mRecyclerView.setItemAnimator(new ScaleInBottomAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(100);
        mRecyclerView.getItemAnimator().setRemoveDuration(1000);
        mRecyclerView.getItemAnimator().setMoveDuration(100);
        mRecyclerView.getItemAnimator().setChangeDuration(100);
        ScaleInBottomAnimator animator = new ScaleInBottomAnimator();
        mRecyclerView.setItemAnimator(animator);
        // Adapter Animation
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mRecyclerViewAdapter));
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(mRecyclerViewAdapter);
        alphaAdapter.setDuration(500);
        mRecyclerView.setAdapter(alphaAdapter);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_actionview, menu);
        super.onCreateOptionsMenu(menu, inflater);

        final SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (search != null) {
            search.setIconifiedByDefault(true);
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    String food;
                    food = query;
                    food = food.replace(" ", "");
                    String f_url = "https://api.nutritionix.com/v1_1/search/" + food + "?results=0%3A20&cal_min=0&cal_max=50000&fields=item_name%2Cbrand_name%2Citem_id%2Cbrand_id%2Citem_description%2Cnf_protein%2Cnf_calories%2Cnf_total_carbohydrate%2Cnf_total_fat&appId=42e8cbe9&appKey=a4e373fe0f10ab1de40cffbffb9db544";
                    MyDownloadJsonAsyncTask downloadJson = new MyDownloadJsonAsyncTask(mRecyclerViewAdapter);
                    Log.d("ConorRyan", f_url);
                    downloadJson.execute(f_url);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    // Load Async Food Data from Nutrionix.com
    private class MyDownloadJsonAsyncTask extends AsyncTask<String, Void, FoodDataJson> {
        private final WeakReference<Food_MyRecyclerViewAdapter> adapterReference;

        public MyDownloadJsonAsyncTask(Food_MyRecyclerViewAdapter adapter) {
            adapterReference = new WeakReference<Food_MyRecyclerViewAdapter>(adapter);
        }

        @Override
        protected FoodDataJson doInBackground(String... urls) {
            FoodDataJson threadMovieData = new FoodDataJson();
            for (String url : urls) {
                try {
                    threadMovieData.downloadFoodDataJson(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return threadMovieData;
        }

        @Override
        protected void onPostExecute(FoodDataJson threadMovieData) {
            foodData.foodList.clear();
            for (int i = 0; i < threadMovieData.getSize(); i++)
                foodData.foodList.clear();
            for (int k = 0; k < threadMovieData.getSize(); k++)
                foodData.foodList.add(threadMovieData.foodList.get(k));
            if (adapterReference != null) {
                final Food_MyRecyclerViewAdapter adapter = adapterReference.get();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

}
