package com.example.jason.route_application.features.commentInput;

import com.example.jason.route_application.interactors.CommentInputInteractor;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Jason on 19-Feb-18.
 */

@Module
public abstract class CommentInputModule {

    @Binds
    public abstract MvpCommentInput.View provideView(CommentInputActivity view);

    @Binds
    public abstract MvpCommentInput.Presenter providePresenter(CommentInputPresenter presenter);

    @Binds
    public abstract MvpCommentInput.Interactor provideInteractor(CommentInputInteractor interactor);

}
