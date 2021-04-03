package com.bloodFinder.bloodSavior.common;

import com.bloodFinder.bloodSavior.feed.ModelClassFeedFragment;

import java.util.List;

public interface CallBackFirebaseCall {
    void onFeedFragmentCall(List<ModelClassFeedFragment> postsListReturned);
}
