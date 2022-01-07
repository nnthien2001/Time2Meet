package com.example.time2meet.ui.view_attendee;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time2meet.R;
import com.example.time2meet.data.Meeting;
import com.example.time2meet.data.MeetingViewModel;
import com.example.time2meet.data.User;
import com.example.time2meet.data.UserRepository;
import com.example.time2meet.data.UserViewModel;
import com.example.time2meet.databinding.MeetingItemBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewAttendee extends Fragment implements View.OnClickListener{

    private NavController navController;
    private MeetingViewModel meetingViewModel;
    private UserViewModel userViewModel;
    private View view;
    private User host;
    private FloatingActionButton fab;
    private AttendeeRecyclerViewAdapter adapter;
    private LayoutInflater inflater;
    private PopupWindow inviteNewAttendeePopup;
    private View inviteNewAttendeePopupView;
    private PopupWindow removeNewAttendeeConfirmationPopup;
    private View removeNewAttendeeConfirmationView;
    private String attendeeToRemove = null;
    private ImageButton back_button;
    private static final String TAG = "FragmentViewAttendee";

    public FragmentViewAttendee() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);
        userViewModel = new ViewModelProvider(navController.getBackStackEntry(R.id.nav_graph)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_attendee, container, false);

        this.inflater = inflater;
        this.host = meetingViewModel.getHost();

        assert this.host != null;

        initRecyclerView(rootView);
        setupObserver();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        initAppBar();
        fab = view.findViewById(R.id.fab_invite);
        if(isHostOfThisMeeting()) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(this);
        }
    }

    private void openInviteNewAttendee() {
        View layout = inflater.inflate(R.layout.popup_invite_attendee, null);
        PopupWindow popupWindow = new PopupWindow(layout, (int) (300 * this.getContext().getResources().getDisplayMetrics().density),
                (int) (200 * this.getContext().getResources().getDisplayMetrics().density),
                true);
        inviteNewAttendeePopup = popupWindow;
        inviteNewAttendeePopupView = popupWindow.getContentView();
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        inviteNewAttendeePopupView.findViewById(R.id.btn_invite).setOnClickListener(this);
        inviteNewAttendeePopupView.findViewById(R.id.btn_cancel_invite).setOnClickListener(this);
    }

    private void initRecyclerView(View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.attendee_recycler_view);
        getData();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AttendeeSectionedRecyclerViewAdapter mSectionedAdapter = new
                AttendeeSectionedRecyclerViewAdapter(getContext(),R.layout.section,R.id.section_text,adapter);
        mSectionedAdapter.setSections();

        if(isHostOfThisMeeting()) {
            new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);
        }
        recyclerView.setAdapter(mSectionedAdapter);
    }

    private void getData() {
        ArrayList<User> attendees = new ArrayList<>();
        attendees.add(this.host);
        ArrayList<User> dummy = meetingViewModel.getAttendees();

        for(User user : dummy) {
            if(!user.getUsername().equals(this.host.getUsername())) {
                attendees.add(user);
            }
        }

        if(adapter == null) {
            adapter = new AttendeeRecyclerViewAdapter(getContext(), attendees, navController);
        }
        else {
            adapter.setUsers(attendees);
        }

    }

    private void initAppBar() {
        TextView tv_appbar = (TextView) view.findViewById(R.id.tv_action_bar_center);
        tv_appbar.setText("Attendance List");
        back_button =getView().findViewById(R.id.btn_action_bar_leftmost);
        back_button.setImageResource(R.drawable.ic_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigateUp();
            }
        });
    }

    private boolean isHostOfThisMeeting() {
        assert this.host != null;
        return this.host.getUserID().equals(userViewModel.getCurrentUser().getUserID());
    }

    // Swipe and remove operation
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }


        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();
            int real_position = position - 2;
            String remove_attedee_username = adapter.getUsers().get(real_position).getUsername();
            openRemoveAttendeeConfirmation(remove_attedee_username);
            getData();
        }

        @Override
        public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof AttendeeSectionedRecyclerViewAdapter.SectionViewHolder) return 0;
            if (viewHolder.getBindingAdapterPosition() <= 2) return 0;
            return super.getSwipeDirs(recyclerView, viewHolder);
        }
    };

    private void openRemoveAttendeeConfirmation(String remove_attedee_username) {
        attendeeToRemove = remove_attedee_username;
        View layout = inflater.inflate(R.layout.popup_remove_attendee_confirmation, null);
        PopupWindow popupWindow = new PopupWindow(layout, (int) (300 * this.getContext().getResources().getDisplayMetrics().density),
                (int) (200 * this.getContext().getResources().getDisplayMetrics().density),
                true);
        removeNewAttendeeConfirmationPopup = popupWindow;
        removeNewAttendeeConfirmationView = popupWindow.getContentView();
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        removeNewAttendeeConfirmationView.findViewById(R.id.btn_remove_attendee).setOnClickListener(this);
        removeNewAttendeeConfirmationView.findViewById(R.id.btn_cancel_remove_attendee).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_invite:
                openInviteNewAttendee();
                break;
            case R.id.btn_cancel_invite:
                inviteNewAttendeePopup.dismiss();
                break;
            case R.id.btn_invite:
                inviteAttendee();
                break;
            case R.id.btn_cancel_remove_attendee:
                removeNewAttendeeConfirmationPopup.dismiss();
                break;
            case R.id.btn_remove_attendee:
                removeAttendee();
                break;
        }
    }



    private void removeAttendee() {
        assert attendeeToRemove != null;
        assert !attendeeToRemove.equals(this.host.getUsername());
        meetingViewModel.removeAttendee(getUserIDOfAttendeeToRemove());
        attendeeToRemove = null;
        removeNewAttendeeConfirmationPopup.dismiss();
        getData();
    }

    private Integer getUserIDOfAttendeeToRemove() {
        ArrayList<User> users = meetingViewModel.getAttendees();
        for(User user : users) {
            if(user.getUsername().equals(attendeeToRemove)) {
                return user.getUserID();
            }
        }
        return 0;
    }

    private void inviteAttendee() {
        String attendee_username = ((EditText)inviteNewAttendeePopupView.findViewById(R.id.attendee_username))
                .getText().toString();
        Integer request_state = meetingViewModel.invite(attendee_username);
        Log.d(TAG, "The request state is" + Integer.toString(request_state));
        if(UserRepository.getInstance().REQUEST_ERROR.equals(request_state)) {
            Log.d(TAG, "Request failed");
            Toast.makeText(getContext(), "This user does not exits", Toast.LENGTH_SHORT).show();
        }
        inviteNewAttendeePopup.dismiss();
        getData();
        Log.d(TAG, "The size of the list" + Integer.toString(meetingViewModel.getAttendees().size()));
        // Toast.makeText(getContext(), "The size of the list" + Integer.toString(meetingViewModel.getAttendees().size()), Toast.LENGTH_SHORT).show();
    }

    // set up observer
    public void setupObserver() {
        final Observer<Meeting> meetingObserver = new Observer<Meeting>() {
            @Override
            public void onChanged(Meeting newMeeting) {
                getData();
                Log.d(TAG, "The size of the list at change" + Integer.toString(meetingViewModel.getAttendees().size()));
            }
        };
        meetingViewModel.getMeetingLiveDate().observe(getViewLifecycleOwner(), meetingObserver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        meetingViewModel.getMeetingLiveDate().removeObservers(getViewLifecycleOwner());
    }

}