package com.tiger.socol.gu.api.upload;

import com.tiger.socol.gu.network.Module;

public class UploadApi {

    public static final String name = "upload";

    public static final UploadApi.Modules modules = new UploadApi().new Modules();

    public class Modules {
        public final Module IMAGE = new Module("images", 1);

    }

}
