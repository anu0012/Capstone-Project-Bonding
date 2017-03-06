package com.example.anuragsharma.bonding;


/**
 * Created by anuragsharma on 19/2/17.
 */

public class FileDetails {
    private String mName;
    private String mBranch;
    private String mSize;

    public FileDetails(String name, String branch) {
        mName = name;
        mBranch = branch;
        //mSize = size;
    }

    public String getFileName() {
        return mName;
    }

    public String getFileBranch() {
        return mBranch;
    }

    public String getSize() {
        return mSize;
    }
}
