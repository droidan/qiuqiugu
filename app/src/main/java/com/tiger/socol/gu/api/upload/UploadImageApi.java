package com.tiger.socol.gu.api.upload;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class UploadImageApi extends ObjectRequest<UploadImageEntity> {

    public ArrayList<File> fields;

    @Override
    public String api() {
        return UploadApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(UploadApi.modules.IMAGE);
    }

    @Override
    public void parament(Map<String, String> ps) {

    }

    @Override
    public void files(Map<String, File> fs) {
        for (int i = 0; i < fields.size(); i++) {
            fs.put("field" + i, fields.get(i));
        }
    }

}
