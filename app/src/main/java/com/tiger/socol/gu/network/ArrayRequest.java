package com.tiger.socol.gu.network;

import java.util.List;

public abstract class ArrayRequest<T> extends Request {

    public void request(OnRequestListeren<T> listeren) {
        Agent.getInstance().requestArray(this, listeren);
    }

    public interface OnRequestListeren<T> extends OnRequestErrorListeren {
        void onSuccess(List<T> value);
    }

}
