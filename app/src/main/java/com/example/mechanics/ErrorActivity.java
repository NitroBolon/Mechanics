package com.example.mechanics;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class ErrorActivity extends AppCompatActivity {
    private ErrorViewModel errorViewModel;
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode, data);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ErrorAdapter adapter = new ErrorAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        errorViewModel = ViewModelProviders.of(this).get(ErrorViewModel.class);
        errorViewModel.findAll().observe(this, new Observer<List<Error>> ()
        {
            @Override
            public void onChanged(List<Error> errors) {
                adapter.setErrors(errors);
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
    private class ErrorHolder extends RecyclerView.ViewHolder{

        private TextView errorCodeTextView;
        private TextView errorDescriptionTextView;
        Error error;
        public ErrorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.error_list_item, parent, false));

            errorCodeTextView = itemView.findViewById(R.id.code);
            errorDescriptionTextView = itemView.findViewById(R.id.description);

        }
        public void bind(Error error)
        {
            this.error = error;
            errorCodeTextView.setText(error.getCode());
            errorDescriptionTextView.setText(error.getDescription());
        }

    }
    private class ErrorAdapter extends RecyclerView.Adapter<ErrorHolder>
    {
        private List<Error> errors;
        @NonNull
        @Override
        public ErrorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ErrorHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ErrorHolder holder, int position) {
            if(errors != null)
            {
                Error error = errors.get(position);
                holder.bind(error);
            }
            else
            {
                Log.d("ErrorActivity", "No error codes");
            }
        }

        @Override
        public int getItemCount() {
            if(errors != null)
            {
                return errors.size();
            }
            else
            {
                return 0;
            }
        }
        void setErrors(List<Error>errors)
        {
            this.errors = errors;
            notifyDataSetChanged();
        }
    }
}
