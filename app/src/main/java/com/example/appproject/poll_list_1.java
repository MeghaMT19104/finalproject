package com.example.appproject;
import java.util.ArrayList;
import java.util.List;

public class poll_list_1 {
    private static poll_list_1 sPollList;
    private List<Polls_1> mPolls;
    private poll_list_1() {
        mPolls=new ArrayList<>();
        for(int i=0;i<100;i++){
            Polls_1 poll=new Polls_1(i,"Destination "+i,"price "+i,"time "+i,"date "+i);
            mPolls.add(poll);
        }
    }

    public static poll_list_1 get(){
        if(sPollList==null){
            sPollList=new poll_list_1();
        }
        return sPollList;
    }

    public List<Polls_1> getPolls() {
        return mPolls;
    }
}
