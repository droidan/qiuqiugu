package com.tiger.socol.gu.network;

public abstract class NullRequest extends Request {

    public void request(OnRequestListeren listeren) {
        Agent.getInstance().requestNull(this, listeren);
    }

    public interface OnRequestListeren extends OnRequestErrorListeren {
        void onSuccess();
    }
}
