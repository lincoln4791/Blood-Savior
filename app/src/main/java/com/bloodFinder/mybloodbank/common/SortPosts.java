package com.bloodFinder.mybloodbank.common;

import com.bloodFinder.mybloodbank.mainActivity.chats.invite.ModelClassInvite;
import com.bloodFinder.mybloodbank.mainActivity.feed.ModelClassFeedFragment;
import com.bloodFinder.mybloodbank.mainActivity.requests.AcceptedFragment.ModelClassAcceptedFragment;
import com.bloodFinder.mybloodbank.mainActivity.requests.MyRequests.ModelClassMyRequests;

import java.util.Comparator;

public class SortPosts implements Comparator<ModelClassFeedFragment> {
    @Override
    public int compare(ModelClassFeedFragment o1, ModelClassFeedFragment o2) {

        return Integer.parseInt(o1.getPost_order()) - Integer.parseInt(o2.getPost_order());
    }

    public static class SortAcceptedPosts implements Comparator<ModelClassAcceptedFragment> {

        @Override
        public int compare(ModelClassAcceptedFragment o1, ModelClassAcceptedFragment o2) {
            return Integer.parseInt(o1.getPost_order()) - Integer.parseInt(o2.getPost_order());
        }
    }

    public static class SortMyPosts implements Comparator<ModelClassMyRequests> {


        @Override
        public int compare(ModelClassMyRequests o1, ModelClassMyRequests o2) {
            return Integer.parseInt(o1.getPost_order()) - Integer.parseInt(o2.getPost_order());
        }
    }

    public static class SortInviteList implements Comparator<ModelClassInvite> {


        @Override
        public int compare(ModelClassInvite o1, ModelClassInvite o2) {
            return o1.getUserName().compareTo(o2.getUserName());
        }
    }

}
