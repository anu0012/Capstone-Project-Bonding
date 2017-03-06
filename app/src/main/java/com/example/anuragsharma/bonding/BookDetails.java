package com.example.anuragsharma.bonding;

/**
 * Created by anuragsharma on 18/2/17.
 */

public class BookDetails {
    private String mbookName;
    private String mQuantity;
    private String mbookOwner;
    private String mhostelName;
    private String mroomNo;
    private String mContactNo;
    private String mbranch;

    public BookDetails(String bookName, String Quantity, String bookOwner,
                       String hostelName, String roomNo, String ContactNo, String branch) {
        mbookName = bookName;
        mQuantity = Quantity;
        mbookOwner = bookOwner;
        mhostelName = hostelName;
        mContactNo = ContactNo;
        mbranch = branch;
        mroomNo = roomNo;
    }

    public String getBookName() {
        return mbookName;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public String getOwnerName() {
        return mbookOwner;
    }

    public String getBranch() {
        return mbranch;
    }

    public String getContact() {
        return mContactNo;
    }

    public String getHostelName() {
        return mhostelName;
    }

    public String getRoomNo() {
        return mroomNo;
    }
}
