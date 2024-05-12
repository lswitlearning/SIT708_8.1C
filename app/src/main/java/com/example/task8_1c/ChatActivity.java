package com.example.task8_1c;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private EditText inputEditText;
    private ImageButton sendButton;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private Retrofit retrofit;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        inputEditText = findViewById(R.id.inputEditText);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, this);

        // Get username passed from previous activity
        username = getIntent().getStringExtra("username");

        // Set up RecyclerView
        chatRecyclerView.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);

        // Initialize Retrofit for API communication
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.MINUTES).build())
                .build();

        // Set onClickListener for sendButton
        sendButton.setOnClickListener(view -> sendMessage());

        // Send initial message from robot to greet user
        sendRobotMessage("Hello! " + username + ", Welcome to the chat! How can I assist you today? Is there something specific you would like to talk about or ask?");
    }

    // Method to send user message
    private void sendMessage() {
        String messageText = inputEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            messageList.add(new Message(messageText, true));
            messageAdapter.notifyDataSetChanged();
            requestRobotResponse(messageText);
            inputEditText.getText().clear();

            chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
        }
    }

    // Method to add robot's message to the list and notify adapter
    private void sendRobotMessage(String message) {
        messageList.add(new Message(message, false));
        messageAdapter.notifyDataSetChanged();

        chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
    }

    // Method to request response from server based on user message
    private void requestRobotResponse(String message) {
        RobotApiService service = retrofit.create(RobotApiService.class);
        Call<ApiResponse> call = service.getRobotResponse(new MessageRequest(message));
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        String robotResponse = apiResponse.getMessage();
                        sendRobotMessage(robotResponse);
                    }
                } else {
                    // Handle unsuccessful response
                    showError("Unsuccessful response. Please try again later.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // Handle failure
                showError("Failed to communicate with the server. Please check your internet connection and try again.");
            }
            private void showError(String message) {
                // Display error message to the user (e.g., using Toast)
                Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Retrofit interface for API communication
    interface RobotApiService {
        @POST("chat")
        Call<ApiResponse> getRobotResponse(@Body MessageRequest message);
    }
}
