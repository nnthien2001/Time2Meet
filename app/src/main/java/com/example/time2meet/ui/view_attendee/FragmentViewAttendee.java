package com.example.time2meet.ui.view_attendee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> usernames = new ArrayList<>();
    private AttendeeRecyclerViewAdapter adapter;
    private LayoutInflater inflater;
    private PopupWindow inviteNewAttendeePopup;
    private View inviteNewAttendeePopupView;
    private PopupWindow removeNewAttendeeConfirmationPopup;
    private View removeNewAttendeeConfirmationView;
    private String attendeeToRemove = null;

    public FragmentViewAttendee() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_attendee, container, false);
        this.inflater = inflater;

        initRecyclerView(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);
        userViewModel = new UserViewModel();

        this.view = view;
        try {
            this.host = meetingViewModel.getHost();
        } catch (Exception e) {
            host = new User();
            host.setUserID(1);
        }

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

    }

    private void initRecyclerView(View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.attendee_recycler_view);

        getData();
        adapter = new AttendeeRecyclerViewAdapter(getContext(), names, usernames);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //This is the code to provide a sectioned list
        List<AttendeeSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<AttendeeSectionedRecyclerViewAdapter.Section>();

        //Sections
        sections.add(new AttendeeSectionedRecyclerViewAdapter.Section(0,"host"));
        if(names.size()>1) {
            sections.add(new AttendeeSectionedRecyclerViewAdapter.Section(1, "attendee"));
        }

        //Add your adapter to the sectionAdapter
        AttendeeSectionedRecyclerViewAdapter.Section[] dummy = new AttendeeSectionedRecyclerViewAdapter.Section[sections.size()];
        AttendeeSectionedRecyclerViewAdapter mSectionedAdapter = new
                AttendeeSectionedRecyclerViewAdapter(getContext(),R.layout.section,R.id.section_text,adapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        //Apply this adapter to the RecyclerView
        if(isHostOfThisMeeting()) {
            new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);
        }
        recyclerView.setAdapter(mSectionedAdapter);
    }

    private void getData() {
        try {
            ArrayList<User> attendees = meetingViewModel.getAttendees();
            attendees.remove(this.host);
            names.add(this.host.getName());
            usernames.add(this.host.getUsername());
            for(User attendee : attendees) {
                names.add(attendee.getName());
                usernames.add(attendee.getUsername());
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "There is a network error", Toast.LENGTH_SHORT).show();
        }

    }

    private void initAppBar() {
        TextView tv_appbar = (TextView) view.findViewById(R.id.tv_action_bar_center);
        tv_appbar.setText("Attendance List");
        ImageButton imgBtn_back = (ImageButton) view.findViewById(R.id.btn_action_bar_leftmost);
        imgBtn_back.setImageResource(R.drawable.ic_back);
    }

    private boolean isHostOfThisMeeting() {
        try {
            return this.host.getUserID().equals(userViewModel.getCurrentUser().getUserID());
        } catch (Exception e) {
            Toast.makeText(getContext(), "There is a network error", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();
            // Toast.makeText(getContext(), "This is position" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            int real_position = position - 2;
            String remove_attedee_username = usernames.get(real_position);
            openRemoveAttendeeConfirmation(remove_attedee_username);
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
        meetingViewModel.removeAttendee(getUserIDOfAttendeeToRemove());
        attendeeToRemove = null;
        // TODO: Observe changes in data
    }

    private Integer getUserIDOfAttendeeToRemove() {
        ArrayList<User> users = meetingViewModel.getAttendees();
        for(User user : users) {
            if(user.getUsername().equals(attendeeToRemove)) {
                return user.getUserID();
            }
        }
    }

    private void inviteAttendee() {
        String attendee_username = ((EditText)inviteNewAttendeePopupView.findViewById(R.id.attendee_username))
                .getText().toString();
        meetingViewModel.invite(attendee_username);
        // TODO: Handle error
    }
}